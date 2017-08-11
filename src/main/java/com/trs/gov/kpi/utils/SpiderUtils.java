package com.trs.gov.kpi.utils;

import com.trs.gov.kpi.constant.*;
import com.trs.gov.kpi.dao.WebPageMapper;
import com.trs.gov.kpi.entity.PageDepth;
import com.trs.gov.kpi.entity.PageSpace;
import com.trs.gov.kpi.entity.ReplySpeed;
import com.trs.gov.kpi.entity.UrlLength;
import com.trs.gov.kpi.entity.dao.QueryFilter;
import com.trs.gov.kpi.entity.dao.Table;
import com.trs.gov.kpi.entity.exception.RemoteException;
import com.trs.gov.kpi.entity.responsedata.LinkAvailabilityResponse;
import com.trs.gov.kpi.scheduler.CKMScheduler;
import com.trs.gov.kpi.service.LinkAvailabilityService;
import com.trs.gov.kpi.service.WebPageService;
import com.trs.gov.kpi.service.outer.ChnlDocumentServiceHelper;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.*;
import us.codecraft.webmagic.downloader.Downloader;
import us.codecraft.webmagic.downloader.HttpClientDownloader;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.utils.UrlUtils;

import javax.annotation.Resource;
import java.io.File;
import java.lang.ref.SoftReference;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by wangxuan on 2017/5/10.
 */
@Slf4j
@Component
@Scope("prototype")
public class SpiderUtils {

    private static final String LOC_TAG = "||||||";

    private static final String DOUBLE_QUOTS = "&quot;";

    private static final String SRC_ATTR = "[src]";

    @Value("${issue.location.dir}")
    private String locationDir;

    @Resource
    private WebPageService webPageService;

    @Resource
    private WebPageMapper webPageMapper;

    @Resource
    private LinkAvailabilityService linkAvailabilityService;

    // 过大页面阀值
    private static final int THRESHOLD_MAX_PAGE_SIZE = 5 * 1024 * 1024;

    //栏目URL地址层级阀值
    private static final int THRESHOLD_MAX_URL_LENGHT = 6;

    //页面深度阀值
    private static final int THRESHOLD_MAX_PAGE_DEPTH = 8;

    //响应速度阈值
    private static final int THRESHOLD_MAX_REPLY_SPEED = 10 * 1000;

    private Map<String, Set<String>> pageParentMap = new ConcurrentHashMap<>();

    // 缓存页面正文内容，如果内存紧张的情况下，可以释放
    private Map<String, SoftReference<String>> pageContentMap = new ConcurrentHashMap<>();

    private Set<String> unavailableUrls = Collections.synchronizedSet(new HashSet<String>());

    private Site site = Site.me().setRetryTimes(3).setSleepTime(10).setTimeOut(15000);

    private String baseUrl;

    private Integer siteId;

    private String baseHost;

    //失效链接计数
    @Getter
    int count = 0;

    private PageProcessor kpiProcessor = new PageProcessor() {

        @Override
        public void process(Page page) {

            // 当页面已经完全跳转到外部去了，就不再处理了。
            String curHost = UrlUtils.getHost(page.getUrl().get());
            if (!StringUtils.equals(curHost, baseHost)) {
                return;
            }

            EnumUrlType urlType = WebPageUtil.getUrlType(page.getUrl().get());
            if (urlType != EnumUrlType.HTML) {
                return;
            }

            //存放页面内容
            pageContentMap.put(page.getUrl().get(), new SoftReference(page.getRawText()));

            // 获取页面内的所有连接
            List<String> targetUrls = page.getHtml().links().all();
            final Elements medias = page.getHtml().getDocument().select(SRC_ATTR);
            Elements imports = page.getHtml().getDocument().select("link[href]");
            for (Element importElem : imports) {
                targetUrls.add(UrlUtils.canonicalizeUrl(importElem.attr("href"), page.getUrl().get()));
            }
            for (Element mediaElem : medias) {
                targetUrls.add(UrlUtils.canonicalizeUrl(mediaElem.attr("src"), page.getUrl().get()));
            }

            synchronized (pageParentMap) {
                for (String targetUrl : targetUrls) {
                    if (!pageParentMap.containsKey(targetUrl)) {
                        pageParentMap.put(targetUrl.intern(), Collections.synchronizedSet(new HashSet<String>()));
                    }
                    Set<String> parentUrlSet = pageParentMap.get(targetUrl);
                    if (!targetUrl.equals(page.getUrl().get().intern())) {

                        boolean isEqual = false;
                        if (targetUrl.startsWith(page.getUrl().get())) {
                            String remainStr = targetUrl.substring(page.getUrl().get().length());
                            if (remainStr.equals("/") || remainStr.equals("#") || remainStr.endsWith("/#")) {
                                isEqual = true;
                            }
                        }
                        if (page.getUrl().get().startsWith(targetUrl)) {
                            String remainStr = page.getUrl().get().substring(targetUrl.length());
                            if (remainStr.equals("/") || remainStr.equals("#") || remainStr.endsWith("/#")) {
                                isEqual = true;
                            }
                        }

                        if (!isEqual) {
                            parentUrlSet.add(page.getUrl().get().intern());
                        }
                    }
                }
            }
            page.addTargetRequests(targetUrls);
        }

        @Override
        public Site getSite() {

            return site;
        }
    };

    private Downloader homepageCheckDownloader = new HttpClientDownloader() {

        ThreadLocal<Boolean> isUrlAvailable = new ThreadLocal<>();

        @Override
        public Page download(Request request, Task task) {

            isUrlAvailable.set(false);
            super.download(request, task);
            if (!isUrlAvailable.get()) {
                unavailableUrls.add(request.getUrl().intern());
            }
            return null;
        }

        @Override
        public void onSuccess(Request request) {

            isUrlAvailable.set(true);
        }
    };

    private Downloader recordUnavailableUrlDownloader = new HttpClientDownloader() {

        ThreadLocal<Boolean> isUrlAvailable = new ThreadLocal<>();

        @Override
        public Page download(Request request, Task task) {

            isUrlAvailable.set(false);
            Date startDate = new Date();
            Page result = super.download(request, task);
            Date endDate = new Date();
            long useTime = endDate.getTime() - startDate.getTime();

            Integer chnlId = null;
            try {
                chnlId = ChnlDocumentServiceHelper.getChnlIdByUrl("", request.getUrl().intern(), siteId);
            } catch (RemoteException e) {
                log.error("", e);
                LogUtil.addErrorLog(OperationType.REMOTE, ErrorType.REMOTE_FAILED, "url=" + request.getUrl().intern() + ", siteId=" + siteId, e);
            }

            if (!isUrlAvailable.get()) {
                String unavailableUrl = request.getUrl().intern();
                Set<String> parents = pageParentMap.get(request.getUrl().intern());
                if (parents == null) {
                    insertInvalidLink(new ImmutablePair<>(unavailableUrl, unavailableUrl), new Date(), "", (Integer) request.getExtras().get("statusCode"));
                } else {
                    for (String parentUrl : parents) {
                        final SoftReference<String> parentContentSoftReference = pageContentMap.get(parentUrl);
                        insertInvalidLink(new ImmutablePair<>(parentUrl, unavailableUrl), new Date(), parentContentSoftReference.get(), (Integer) request.getExtras().get("statusCode"));
                    }
                }
            } else {
                EnumUrlType urlType = WebPageUtil.getUrlType(request.getUrl());
                if (urlType != EnumUrlType.HTML) {
                    return result;
                }

                if (useTime > THRESHOLD_MAX_REPLY_SPEED) {
                    updateOrInsertSpeed(new ReplySpeed(Types.AnalysisType.REPLY_SPEED.value,
                            chnlId,
                            request.getUrl().intern(),
                            useTime,
                            Long.valueOf(result.getRawText().getBytes().length),
                            new Date()));
                }

                if (result.getRawText().getBytes().length >= THRESHOLD_MAX_PAGE_SIZE) {
                    updateOrInsertSpace(new PageSpace(0,
                            request.getUrl().intern(),
                            useTime,
                            Long.valueOf(result.getRawText().getBytes().length),
                            new Date()));
                }

                String[] urlSize = request.getUrl().split("/");
                if ((urlSize.length - 3) >= THRESHOLD_MAX_URL_LENGHT) {
                    updateOrInsertLength(new UrlLength(Types.AnalysisType.TOO_LONG_URL.value,
                            chnlId,
                            request.getUrl().intern(),
                            Long.valueOf(request.getUrl().length()),
                            Long.valueOf(result.getRawText().getBytes().length),
                            new Date()));
                }

                int deepSize = calcDeep(request.getUrl(), 100, 1);
                if (deepSize > THRESHOLD_MAX_PAGE_DEPTH) {
                    updateOrInsertDepth(new PageDepth(Types.AnalysisType.OVER_DEEP_PAGE.value,
                            chnlId,
                            request.getUrl().intern(),
                            deepSize,
                            Long.valueOf(result.getRawText().getBytes().length),
                            new Date()));
                }
            }

            return result;
        }

        @Override
        public void onSuccess(Request request) {

            isUrlAvailable.set(true);
        }

        private void insertInvalidLink(Pair<String, String> unavailableUrlAndParentUrl, Date checkTime, String parentContent, Integer statusCode) {

            try {

                LinkAvailabilityResponse linkAvailabilityResponse = new LinkAvailabilityResponse();
                linkAvailabilityResponse.setInvalidLink(unavailableUrlAndParentUrl.getValue());
                linkAvailabilityResponse.setCheckTime(checkTime);
                linkAvailabilityResponse.setSiteId(siteId);
                linkAvailabilityResponse.setIssueTypeId(getTypeByLink(unavailableUrlAndParentUrl.getValue()).value);
                final String relativeDir = CKMScheduler.getRelativeDir(siteId, Types.IssueType.LINK_AVAILABLE_ISSUE.value, linkAvailabilityResponse.getIssueTypeId(),
                        linkAvailabilityResponse.getInvalidLink());
                String absoluteDir = locationDir + File.separator + relativeDir;
                linkAvailabilityResponse.setSnapshot("gov/kpi/loc/" + relativeDir.replace(File.separator, "/") + "/index.html");
                if (!linkAvailabilityService.existLinkAvailability(siteId, unavailableUrlAndParentUrl.getValue())) {

                    CKMScheduler.createDir(absoluteDir);

                    // 网页定位
                    String pageLocContent = generatePageLocHtmlText(unavailableUrlAndParentUrl, parentContent, statusCode);
                    if (pageLocContent == null) {
                        log.warn(unavailableUrlAndParentUrl.getValue() + " create location file failed ... ");
                        return;
                    }
                    CKMScheduler.createPagePosHtml(absoluteDir, pageLocContent);

                    // 源码定位
                    String srcLocContent = generateSourceLocHtmlText(unavailableUrlAndParentUrl, parentContent, statusCode);
                    if (srcLocContent == null) {
                        log.warn(unavailableUrlAndParentUrl.getValue() + " create location file failed ... ");
                        return;
                    }
                    CKMScheduler.createSrcPosHtml(absoluteDir, srcLocContent);

                    // 创建头部导航页面
                    CKMScheduler.createContHtml(absoluteDir, unavailableUrlAndParentUrl.getValue(), unavailableUrlAndParentUrl.getKey());

                    // 创建首页
                    CKMScheduler.createIndexHtml(absoluteDir);

                    linkAvailabilityService.insertLinkAvailability(linkAvailabilityResponse);
                    count++;
                }
            } catch (Exception e) {
                log.error("", e);
                LogUtil.addErrorLog(OperationType.REQUEST, ErrorType.REQUEST_FAILED, "插入失效链接，url=" + unavailableUrlAndParentUrl.getValue(), e);
            }

        }

        private String generateSourceLocHtmlText(Pair<String, String> unavailableUrlAndParentUrl, String parentUrlContent, Integer pageStatusCode) {

            if (StringUtil.isEmpty(parentUrlContent)) {
                return "<html><body><h1>快照内容已不存在！</h1></body></html>";
            }

            // 将html标签转义
            String processSource = processSourceLocation(unavailableUrlAndParentUrl, parentUrlContent);
            String result = CKMScheduler.generateBasSourceLoc(processSource);

            int start = 0;
            int end = 0;
            int index = result.indexOf(DOUBLE_QUOTS + LOC_TAG, start);
            if (index >= 0) {

                end = result.indexOf(LOC_TAG, index + DOUBLE_QUOTS.length() + LOC_TAG.length());
                if (end >= 0) {
                    String sourceLinkText = result.substring(index + DOUBLE_QUOTS.length() + LOC_TAG.length(), end);
                    String msgStr = getMsgStr(pageStatusCode, unavailableUrlAndParentUrl.getValue());
                    String errorinfo = "<font trserrid=\"anchor\" msg=\"" + msgStr + "\" msgtitle=\"定位\" style=\"border:2px red solid;color:red;\">" + sourceLinkText +
                            "</font>";
                    result = result.substring(0, index + DOUBLE_QUOTS.length()) + errorinfo + result.substring(end + LOC_TAG.length());
                }
            } else {
                return null;
            }
            index = result.indexOf(DOUBLE_QUOTS + LOC_TAG, index + DOUBLE_QUOTS.length());
            while (index > 0) {
                end = result.indexOf(LOC_TAG, index + DOUBLE_QUOTS.length() + LOC_TAG.length());
                if (end >= 0) {
                    String sourceLinkText = result.substring(index + "&quot;".length() + LOC_TAG.length(), end);
                    String msgStr = getMsgStr(pageStatusCode, unavailableUrlAndParentUrl.getValue());
                    String errorinfo = "<font msg=\"" + msgStr + "\" style=\"border:2px red solid;color:red;\">" + sourceLinkText + "</font>";
                    result = result.substring(0, index + DOUBLE_QUOTS.length()) + errorinfo + result.substring(end + LOC_TAG.length());
                }
                index = result.indexOf(DOUBLE_QUOTS + LOC_TAG, index + DOUBLE_QUOTS.length());
            }

            return result;
        }

        private String getMsgStr(Integer pageStatusCode, String value) {
            return "状态：" + pageStatusCode + "  [<font color=red>" + getDisplayErrorWord(value) +
                    "</font>]<br>地址：<br><a target=_blank style='color:#0000FF;font-size:12px' href='" + value + "'>" + value + "</a>";
        }

        private String generatePageLocHtmlText(Pair<String, String> unavailableUrlAndParentUrl, String parentUrlContent, Integer pageStatusCode) {

            String result = CKMScheduler.generateBasePageLoc(unavailableUrlAndParentUrl.getKey(), parentUrlContent);

            // 解析源码，找到断链标签，加上标记信息。
            final Document parseDoc = Jsoup.parse(result, unavailableUrlAndParentUrl.getKey());
            Elements links = parseDoc.select("[href]");
            Elements media = parseDoc.select(SRC_ATTR);

            // 处理a标签
            for (int i = 0; i < links.size(); i++) {
                Element link = links.get(i);
                String relHref = link.attr("href");
                String absHref = link.attr("abs:href");
                if (StringUtil.isEmpty(absHref)) {
                    absHref = relHref;
                }
                addAttr(link, absHref, unavailableUrlAndParentUrl.getValue(), pageStatusCode);
            }
            // 处理img标签
            for (int i = 0; i < media.size(); i++) {
                Element link = media.get(i);
                String relHref = link.attr("src");
                String absHref = link.attr("abs:src");
                if (StringUtil.isEmpty(absHref)) {
                    absHref = relHref;
                }
                addAttr(link, absHref, unavailableUrlAndParentUrl.getValue(), pageStatusCode);
            }

            result = parseDoc.html();

            return result;
        }

        private String getDisplayErrorWord(String url) {
            return "无效链接：" + url;
        }

        private String processSourceLocation(Pair<String, String> unavailableUrlAndParentUrl, String parentUrlContent) {

            String result = parentUrlContent.intern();
            // 解析源码，找到断链标签，加上标记信息。
            final Document parseDoc = Jsoup.parse(result, unavailableUrlAndParentUrl.getKey());
            Elements links = parseDoc.select("[href]");
            Elements media = parseDoc.select(SRC_ATTR);

            // 处理a标签
            for (int i = 0; i < links.size(); i++) {
                Element link = links.get(i);
                String relHref = link.attr("href");
                String absHref = link.attr("abs:href");
                if (StringUtil.isEmpty(absHref)) {
                    absHref = relHref;
                }
                if (absHref.equals(unavailableUrlAndParentUrl.getValue())) {
                    link.attr("href", LOC_TAG + relHref + LOC_TAG);
                }
            }
            // 处理img标签
            for (int i = 0; i < media.size(); i++) {
                Element link = media.get(i);
                String relHref = link.attr("src");
                String absHref = link.attr("abs:src");
                if (StringUtil.isEmpty(absHref)) {
                    absHref = relHref;
                }
                if (absHref.equals(unavailableUrlAndParentUrl.getValue())) {
                    link.attr("src", LOC_TAG + relHref + LOC_TAG);
                }
            }

            result = parseDoc.html();
            return result;
        }

        private void addAttr(Element link, String absHref, String value, Integer pageStatusCode) {
            if (absHref.equals(value)) {
                link.attr("trserrid", "anchor");
                String msgStr = "状态：" + pageStatusCode + "  [<font color=red>" + getDisplayErrorWord(value) +
                        "</font>]<br>地址：<br><a target=_blank style='color:#0000FF;font-size:12px' href='" + value + "'>" + value + "</a>";
                link.attr("msg", msgStr);
                link.attr("msgtitle", "定位");
                link.attr("style", "border:2px red solid;color:red;");
            }
        }

        private Types.LinkAvailableIssueType getTypeByLink(String url) {

            String suffix = url.substring(url.lastIndexOf('.') + 1);
            for (String imageSuffix : imageSuffixs) {

                if (StringUtils.equalsIgnoreCase(suffix, imageSuffix)) {

                    return Types.LinkAvailableIssueType.INVALID_IMAGE;
                }
            }

            for (String fileSuffix : fileSuffixs) {

                if (StringUtils.equalsIgnoreCase(suffix, fileSuffix)) {

                    return Types.LinkAvailableIssueType.INVALID_FILE;
                }
            }

            return Types.LinkAvailableIssueType.INVALID_LINK;
        }

        private void updateOrInsertSpeed(ReplySpeed replySpeedTo) {

            QueryFilter queryFilter = new QueryFilter(Table.WEB_PAGE);
            queryFilter.addCond(WebpageTableField.SITE_ID, siteId);
            queryFilter.addCond(WebpageTableField.PAGE_LINK, replySpeedTo.getPageLink());
            queryFilter.addCond(WebpageTableField.CHNL_ID, replySpeedTo.getChnlId());
            queryFilter.addCond(WebpageTableField.TYPE_ID, Types.AnalysisType.REPLY_SPEED.value);

            List<ReplySpeed> pageSpaceList = webPageMapper.selectReplySpeed(queryFilter);
            if (pageSpaceList.isEmpty()) {
                replySpeedTo.setSiteId(siteId);
                webPageService.insertReplyspeed(replySpeedTo);
            } else {
                webPageMapper.updateReplySpeed(replySpeedTo);
            }
        }

        private void updateOrInsertSpace(PageSpace pageSpaceTo) {
            QueryFilter queryFilter = new QueryFilter(Table.WEB_PAGE);
            queryFilter.addCond(WebpageTableField.SITE_ID, siteId);
            queryFilter.addCond(WebpageTableField.PAGE_LINK, pageSpaceTo.getPageLink());
            queryFilter.addCond(WebpageTableField.CHNL_ID, pageSpaceTo.getChnlId());
            queryFilter.addCond(WebpageTableField.TYPE_ID, Types.AnalysisType.OVERSIZE_PAGE.value);

            List<PageSpace> pageSpaceList = webPageMapper.selectPageSpace(queryFilter);

            if (pageSpaceList.isEmpty()) {
                pageSpaceTo.setSiteId(siteId);
                webPageService.insertPageSpace(pageSpaceTo);
            } else {
                webPageMapper.updatePageSpace(pageSpaceTo);
            }
        }

        private void updateOrInsertLength(UrlLength urlLenghtTo) {
            QueryFilter queryFilter = new QueryFilter(Table.WEB_PAGE);
            queryFilter.addCond(WebpageTableField.SITE_ID, siteId);
            queryFilter.addCond(WebpageTableField.PAGE_LINK, urlLenghtTo.getPageLink());
            queryFilter.addCond(WebpageTableField.CHNL_ID, urlLenghtTo.getChnlId());
            queryFilter.addCond(WebpageTableField.TYPE_ID, Types.AnalysisType.TOO_LONG_URL.value);

            List<UrlLength> urlLenghtList = webPageMapper.selectUrlLength(queryFilter);

            if (urlLenghtList.isEmpty()) {
                urlLenghtTo.setSiteId(siteId);
                webPageService.insertUrlLength(urlLenghtTo);
            } else {
                webPageMapper.updateUrlLength(urlLenghtTo);
            }
        }

        private void updateOrInsertDepth(PageDepth pageDepthTo) {
            QueryFilter queryFilter = new QueryFilter(Table.WEB_PAGE);
            queryFilter.addCond(WebpageTableField.SITE_ID, siteId);
            queryFilter.addCond(WebpageTableField.PAGE_LINK, pageDepthTo.getPageLink());
            queryFilter.addCond(WebpageTableField.CHNL_ID, pageDepthTo.getChnlId());
            queryFilter.addCond(WebpageTableField.TYPE_ID, Types.AnalysisType.OVER_DEEP_PAGE.value);

            List<PageDepth> pageDepthList = webPageMapper.selectPageDepth(queryFilter);

            if (pageDepthList.isEmpty()) {
                pageDepthTo.setSiteId(siteId);
                webPageService.insertPageDepth(pageDepthTo);
            } else {
                webPageMapper.updatePageDepth(pageDepthTo);
            }
        }
    };


    private String[] imageSuffixs = new String[]{"bmp", "jpg", "jpeg", "png", "gif"};

    private String[] fileSuffixs = new String[]{"zip", "doc", "xls", "xlsx", "docx", "rar"};

    private synchronized void init(String baseUrl, Integer siteId) {
        this.siteId = siteId;
        this.baseUrl = baseUrl;
        this.baseHost = UrlUtils.getHost(this.baseUrl);
        pageParentMap = new ConcurrentHashMap<>();
        unavailableUrls = Collections.synchronizedSet(new HashSet<String>());
    }


    /**
     * 检索链接/图片/附件是否可用
     *
     * @param threadNum 并发线程数
     * @param baseUrl   网页入口地址
     * @return
     */
    public synchronized void linkCheck(int threadNum, Integer siteId, String baseUrl) {

        log.info("linkCheck started!");
        init(baseUrl, siteId);
        Spider.create(kpiProcessor).setDownloader(recordUnavailableUrlDownloader).addUrl(baseUrl).thread(threadNum).run();
        log.info("linkCheck completed!");
    }

    private int calcDeep(String url, int minDeep, int deep) {

        Set<String> parentUrls = pageParentMap.get(url);
        if (CollectionUtils.isNotEmpty(parentUrls)) {
            return this.deepSize(parentUrls, minDeep, deep);
        } else {
            return minDeep < deep ? minDeep : deep;
        }
    }

    private int deepSize(Set<String> parentUrls, int minDeep, int deeps) {

        // 规避页面循环引用导致的无限递归问题
        if (deeps > 100) {
            return minDeep < deeps ? minDeep : deeps;
        }

        if (deeps > minDeep) {
            return minDeep;
        }

        int newMinDeep = minDeep;
        synchronized (pageParentMap) {
            for (String parentUrl : parentUrls) {
                if (parentUrl.equals(baseUrl)) {
                    return deeps + 1 < newMinDeep ? deeps + 1 : newMinDeep;
                } else {
                    int newDeep = calcDeep(parentUrl, newMinDeep, deeps + 1);
                    newMinDeep = newMinDeep > newDeep ? newDeep : newMinDeep;
                }
            }
        }

        return newMinDeep;
    }


    /**
     * 检索首页是否可用
     *
     * @param homePageUrl
     * @return
     */
    public synchronized List<String> homePageCheck(Integer siteId, String homePageUrl) {

        log.info("homePageCheck started!");
        init(homePageUrl, siteId);
        homepageCheckDownloader.download(new Request(homePageUrl), new Task() {
            @Override
            public String getUUID() {

                return UUID.randomUUID().toString();
            }

            @Override
            public Site getSite() {

                return site;
            }
        });
        log.info("homePageCheck completed!");
        return new LinkedList<>(unavailableUrls);
    }
}

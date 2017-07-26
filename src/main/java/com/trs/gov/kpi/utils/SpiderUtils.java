package com.trs.gov.kpi.utils;

import com.trs.gov.kpi.constant.Types;
import com.trs.gov.kpi.entity.PageDepth;
import com.trs.gov.kpi.entity.PageSpace;
import com.trs.gov.kpi.entity.ReplySpeed;
import com.trs.gov.kpi.entity.UrlLength;
import com.trs.gov.kpi.entity.exception.RemoteException;
import com.trs.gov.kpi.entity.outerapi.Channel;
import com.trs.gov.kpi.entity.responsedata.LinkAvailabilityResponse;
import com.trs.gov.kpi.scheduler.CKMScheduler;
import com.trs.gov.kpi.service.LinkAvailabilityService;
import com.trs.gov.kpi.service.outer.SiteApiService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringEscapeUtils;
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

    @Value("${issue.location.dir}")
    private String locationDir;

    @Resource
    SiteApiService siteApiService;

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

    private Map<String, String> pageContentMap = new ConcurrentHashMap<>();

    private Set<String> unavailableUrls = Collections.synchronizedSet(new HashSet<String>());

    //响应速度
    private Set<ReplySpeed> replySpeeds = Collections.synchronizedSet(new HashSet<ReplySpeed>());

    //过大页面
    private Set<PageSpace> biggerPage = Collections.synchronizedSet(new HashSet<PageSpace>());

    //过长URL页面
    private Set<UrlLength> biggerUrlPage = Collections.synchronizedSet(new HashSet<UrlLength>());

    //过深页面
    private Set<PageDepth> pageDepths = Collections.synchronizedSet(new HashSet<PageDepth>());

    private Site site = Site.me().setRetryTimes(3).setSleepTime(10).setTimeOut(15000);

    private String baseUrl;

    private Integer siteId;

    private PageProcessor kpiProcessor = new PageProcessor() {

        @Override
        public void process(Page page) {
            //存放页面内容
            pageContentMap.put(page.getUrl().get(), page.getRawText());

            //去掉外站链接
            List<String> targetUrls = page.getHtml().links().all();
            Iterator<String> targetUrlIter = targetUrls.iterator();
            String baseHost = UrlUtils.getHost(page.getUrl().get());
            while (targetUrlIter.hasNext()) {

                String targetHost = UrlUtils.getHost(targetUrlIter.next());
                if (!StringUtils.equals(baseHost, targetHost)) {

                    targetUrlIter.remove();
                }
            }

            //相对/绝对路径的处理问题
            List<String> imgUrls = page.getHtml().$("img", "src").all();
            for (String imgUrl : imgUrls) {

                targetUrls.add(UrlUtils.canonicalizeUrl(imgUrl, page.getUrl().get()));
            }
            targetUrls.addAll(page.getHtml().$("img", "src").all());

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
                Channel channel = siteApiService.findChannelByUrl("", request.getUrl().intern(), siteId);
                if (channel != null) {
                    chnlId = channel.getChannelId();
                }
            } catch (RemoteException e) {
                log.error("");
            }

            if (!isUrlAvailable.get()) {
                String unavailableUrl = request.getUrl().intern();
                Set<String> parents = pageParentMap.get(request.getUrl().intern());
                for (String parentUrl : parents) {
                    String parentContent = pageContentMap.get(parentUrl);
                    insertInvalidLink(new ImmutablePair<>(parentUrl, unavailableUrl), new Date(), parentContent, (Integer) request.getExtras().get("statusCode"));
                }
            } else {

                if (useTime > THRESHOLD_MAX_REPLY_SPEED) {
                    replySpeeds.add(new ReplySpeed(Types.AnalysisType.REPLY_SPEED.value,
                            chnlId,
                            request.getUrl().intern(),
                            useTime,
                            Long.valueOf(result.getRawText().getBytes().length),
                            new Date()));
                }

                if (result.getRawText().getBytes().length >= THRESHOLD_MAX_PAGE_SIZE) {
                    biggerPage.add(new PageSpace(0,
                            request.getUrl().intern(),
                            useTime,
                            Long.valueOf(result.getRawText().getBytes().length),
                            new Date()));
                }

                String[] urlSize = request.getUrl().split("/");
                if ((urlSize.length - 3) >= THRESHOLD_MAX_URL_LENGHT) {
                    biggerUrlPage.add(new UrlLength(Types.AnalysisType.TOO_LONG_URL.value,
                            chnlId,
                            request.getUrl().intern(),
                            Long.valueOf(request.getUrl().length()),
                            Long.valueOf(result.getRawText().getBytes().length),
                            new Date()));
                }

                int deepSize = calcDeep(request.getUrl(), 100, 1);
                if (deepSize > THRESHOLD_MAX_PAGE_DEPTH) {
                    pageDepths.add(new PageDepth(Types.AnalysisType.OVER_DEEP_PAGE.value,
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
                linkAvailabilityResponse.setSnapshot(relativeDir + File.separator + "index.html");

                if (!linkAvailabilityService.existLinkAvailability(siteId, unavailableUrlAndParentUrl.getValue())) {
                    String absoluteDir = locationDir + File.separator + relativeDir;
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
                }
            } catch (Exception e) {
                log.error("", e);
            }
        }

        private String generateSourceLocHtmlText(Pair<String, String> unavailableUrlAndParentUrl, String parentUrlContent, Integer pageStatusCode) {
            // 将html标签转义
            String processSource = processSourceLocation(unavailableUrlAndParentUrl, parentUrlContent);
            String sourceEscape = StringEscapeUtils.escapeHtml4(processSource);
            StringBuffer sb = new StringBuffer();
            sb.append("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">");
            sb.append(CKMScheduler.LINE_SP);
            sb.append("<html xmlns=\"http://www.w3.org/1999/xhtml\">");
            sb.append(CKMScheduler.LINE_SP);
            sb.append("	<head>");
            sb.append(CKMScheduler.LINE_SP);
            sb.append("		<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"/>");
            sb.append(CKMScheduler.LINE_SP);
            sb.append("		<title>源码定位</title>");
            sb.append(CKMScheduler.LINE_SP);
            sb.append("		<link href=\"http://gov.trs.cn/jsp/cis4/css/SyntaxHighlighter.css\" rel=\"stylesheet\" type=\"text/css\">");
            sb.append(CKMScheduler.LINE_SP);
            sb.append("	</head>");
            sb.append(CKMScheduler.LINE_SP);
            sb.append("	<body> ");
            sb.append(CKMScheduler.LINE_SP);
            sb.append("		<div class=\"sh_code\">");
            sb.append(CKMScheduler.LINE_SP);
            sb.append("			<ol start=\"1\">");
            sb.append(CKMScheduler.LINE_SP);
            sourceEscape = sourceEscape.replaceAll("\r", "");
            sourceEscape = sourceEscape.replaceAll("\n", CKMScheduler.LINE_SP);
            sourceEscape = sourceEscape.replaceAll(" ", "&nbsp;");
            sourceEscape = sourceEscape.replaceAll("\t", "&nbsp;&nbsp;&nbsp;&nbsp;");
            String[] sourceArr = sourceEscape.split(CKMScheduler.LINE_SP);
            for (int i = 0; i < sourceArr.length; i++) {
                String line = sourceArr[i];
                if (i % 2 == 0) {
                    sb.append("				<li>" + line + "</li>");
                } else {
                    sb.append("				<li class=\"alt\">" + line + "</li>");
                }
                sb.append(CKMScheduler.LINE_SP);
            }
            sb.append("			</ol>");
            sb.append(CKMScheduler.LINE_SP);
            sb.append("		</div>");
            sb.append(CKMScheduler.LINE_SP);
            sb.append("	</body>");
            sb.append(CKMScheduler.LINE_SP);
            sb.append("</html>");
            sb.append(CKMScheduler.LINE_SP);

            // 在源码中增加定位用的脚本定义
            sb.append(CKMScheduler.LINE_SP);
            sb.append("<link href=\"http://gov.trs.cn/jsp/cis4/css/jquery.qtip.min.css\" rel=\"stylesheet\" type=\"text/css\">");
            sb.append(CKMScheduler.LINE_SP);
            sb.append("<script type=\"text/javascript\" src=\"http://gov.trs.cn/jsp/cis4/js/jquery.js\"></script>");
            sb.append(CKMScheduler.LINE_SP);
            sb.append("<script type=\"text/javascript\" src=\"http://gov.trs.cn/jsp/cis4/js/jquery.qtip.min.js\"></script>");
            sb.append(CKMScheduler.LINE_SP);
            sb.append("<script type=\"text/javascript\" src=\"http://gov.trs.cn/jsp/cis4/js/trsposition.js\"></script>");

            String result = sb.toString();

            int start = 0;
            int end = 0;
            int index = result.indexOf(DOUBLE_QUOTS + LOC_TAG, start);
            if (index >= 0) {

                end = result.indexOf(LOC_TAG, index + DOUBLE_QUOTS.length() + LOC_TAG.length());
                if (end >= 0) {
                    String sourceLinkText = result.substring(index + DOUBLE_QUOTS.length() + LOC_TAG.length(), end);
                    String msgStr = "状态：" + pageStatusCode + "  [<font color=red>" + getDisplayErrorWord(unavailableUrlAndParentUrl.getValue()) +
                            "</font>]<br>地址：<br><a target=_blank style='color:#0000FF;font-size:12px' href='" + unavailableUrlAndParentUrl.getValue() + "'>" + unavailableUrlAndParentUrl
                            .getValue() +
                            "</a>";
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
                    String msgStr = "状态：" + pageStatusCode + "  [<font color=red>" + getDisplayErrorWord(unavailableUrlAndParentUrl.getValue()) +
                            "</font>]<br>地址：<br><a target=_blank style='color:#0000FF;font-size:12px' href='" + unavailableUrlAndParentUrl.getValue() + "'>" + unavailableUrlAndParentUrl
                            .getValue() + "</a>";
                    String errorinfo = "<font msg=\"" + msgStr + "\" style=\"border:2px red solid;color:red;\">" + sourceLinkText + "</font>";
                    result = result.substring(0, index + DOUBLE_QUOTS.length()) + errorinfo + result.substring(end + LOC_TAG.length());
                }
                index = result.indexOf(DOUBLE_QUOTS + LOC_TAG, index + DOUBLE_QUOTS.length());
            }

            return result;
        }

        private String generatePageLocHtmlText(Pair<String, String> unavailableUrlAndParentUrl, String parentUrlContent, Integer pageStatusCode) {
            StringBuffer sb = new StringBuffer();
            // 给网站增加base标签
            sb.append("<base href=\"" + unavailableUrlAndParentUrl.getKey() + "\" />");
            sb.append(CKMScheduler.LINE_SP);
            sb.append(parentUrlContent.intern());
            // 在源码中增加定位用的脚本定义
            sb.append(CKMScheduler.LINE_SP);
            sb.append("<link href=\"http://gov.trs.cn/jsp/cis4/css/jquery.qtip.min.css\" rel=\"stylesheet\" type=\"text/css\">");
            sb.append(CKMScheduler.LINE_SP);
            sb.append("<script type=\"text/javascript\" src=\"http://gov.trs.cn/jsp/cis4/js/jquery.js\"></script>");
            sb.append(CKMScheduler.LINE_SP);
            sb.append("<script type=\"text/javascript\" src=\"http://gov.trs.cn/jsp/cis4/js/jquery.qtip.min.js\"></script>");
            sb.append(CKMScheduler.LINE_SP);
            sb.append("<script type=\"text/javascript\" src=\"http://gov.trs.cn/jsp/cis4/js/trsposition.js\"></script>");
            // 解析源码，找到断链标签，加上标记信息。
            String result = sb.toString();

            final Document parseDoc = Jsoup.parse(result, unavailableUrlAndParentUrl.getKey());
            Elements links = parseDoc.select("[href]");
            Elements media = parseDoc.select("[src]");

            // 处理a标签
            for (int i = 0; i < links.size(); i++) {
                Element link = links.get(i);
                String relHref = link.attr("href");
                String absHref = link.attr("abs:href");
                if (StringUtil.isEmpty(absHref)) {
                    absHref = relHref;
                }
                if (absHref.equals(unavailableUrlAndParentUrl.getValue())) {
                    link.attr("trserrid", "anchor");
                    String msgStr = "状态：" + pageStatusCode + "  [<font color=red>" + getDisplayErrorWord(unavailableUrlAndParentUrl.getValue()) +
                            "</font>]<br>地址：<br><a target=_blank style='color:#0000FF;font-size:12px' href='" + unavailableUrlAndParentUrl.getValue() + "'>" + unavailableUrlAndParentUrl
                            .getValue() + "</a>";
                    link.attr("msg", msgStr);
                    link.attr("msgtitle", "定位");
                    link.attr("style", "border:2px red solid;color:red;");
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
                    link.attr("trserrid", "anchor");
                    String msgStr = "状态：" + pageStatusCode + "  [<font color=red>" + getDisplayErrorWord(unavailableUrlAndParentUrl.getValue()) +
                            "</font>]<br>地址：<br><a target=_blank style='color:#0000FF;font-size:12px' href='" + unavailableUrlAndParentUrl.getValue() + "'>" + unavailableUrlAndParentUrl
                            .getValue() + "</a>";
                    link.attr("msg", msgStr);
                    link.attr("msgtitle", "定位");
                    link.attr("style", "border:2px red solid;color:red;");
                }
            }

            result = parseDoc.html();

            return result;
        }

        private String getDisplayErrorWord(String url) {
            return "无效链接：" + url;
        }

        private String processSourceLocation(Pair<String, String> unavailableUrlAndParentUrl, String parentUrlContent) {
            StringBuffer sb = new StringBuffer();
            sb.append(parentUrlContent.intern());
            // 解析源码，找到断链标签，加上标记信息。
            String result = sb.toString();

            final Document parseDoc = Jsoup.parse(result, unavailableUrlAndParentUrl.getKey());
            Elements links = parseDoc.select("[href]");
            Elements media = parseDoc.select("[src]");

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
    };

    private String[] imageSuffixs = new String[]{"bmp", "jpg", "jpeg", "png", "gif"};

    private String[] fileSuffixs = new String[]{"zip", "doc", "xls", "xlsx", "docx", "rar"};

    private synchronized void init(String baseUrl, Integer siteId) {
        this.siteId = siteId;
        this.baseUrl = baseUrl;
        pageParentMap = new HashMap<>();
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
     * @param homePageUrls
     * @return
     */
    public synchronized List<String> homePageCheck(String... homePageUrls) {

        log.info("homePageCheck started!");
        init(baseUrl, siteId);
        for (String homePageUrl : homePageUrls) {

            recordUnavailableUrlDownloader.download(new Request(homePageUrl), new Task() {
                @Override
                public String getUUID() {

                    return UUID.randomUUID().toString();
                }

                @Override
                public Site getSite() {

                    return site;
                }
            });
        }
        log.info("homePageCheck completed!");
        return new LinkedList<>(unavailableUrls);
    }

    /**
     * 过大页面
     *
     * @return
     */
    public Set<PageSpace> biggerPageSpace() {
        return this.biggerPage;
    }

    /**
     * 过长URL页面
     *
     * @return
     */
    public Set<UrlLength> getBiggerUrlPage() {
        return this.biggerUrlPage;
    }

    /**
     * 响应熟读
     *
     * @return
     */
    public Set<ReplySpeed> getReplySpeeds() {
        return this.replySpeeds;
    }

    /**
     * 过深页面
     *
     * @return
     */
    public Set<PageDepth> getPageDepths() {
        return this.pageDepths;
    }

    @Data
    public static class CheckResult {

        private String baseUrl;

        private String unavailableUrl;

        private String parentUrl;
    }
}

package com.trs.gov.kpi.utils;

import com.trs.gov.kpi.constant.EnumUrlType;
import com.trs.gov.kpi.constant.Types;
import com.trs.gov.kpi.entity.PageDepth;
import com.trs.gov.kpi.entity.PageSpace;
import com.trs.gov.kpi.entity.ReplySpeed;
import com.trs.gov.kpi.entity.UrlLength;
import com.trs.gov.kpi.entity.msg.InvalidLinkMsg;
import com.trs.gov.kpi.entity.msg.PageInfoMsg;
import com.trs.gov.kpi.entity.wangkang.SiteManagement;
import com.trs.gov.kpi.msgqueue.CommonMQ;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.*;
import us.codecraft.webmagic.downloader.Downloader;
import us.codecraft.webmagic.downloader.HttpClientDownloader;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.utils.UrlUtils;

import java.io.IOException;
import java.net.URLConnection;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 下载页面检测
 *
 * Created by wangxuan on 2017/5/10.
 */
@Slf4j
@Component
@Scope("prototype")
public class PageSpider {

    @Autowired
    private CommonMQ commonMQ;

    private SiteManagement siteManagement;

//    // 过大页面阀值
//    private static final int THRESHOLD_MAX_PAGE_SIZE = 5 * 1024 * 1024;
//
//    //栏目URL地址层级阀值
//    private static final int THRESHOLD_MAX_URL_LENGHT = 6;
//
//    //页面深度阀值
//    private static final int THRESHOLD_MAX_PAGE_DEPTH = 8;
//
//    //响应速度阈值
//    private static final int THRESHOLD_MAX_REPLY_SPEED = 10 * 1000;

    private Map<String, Set<String>> pageParentMap = new ConcurrentHashMap<>();

    // 页面内容
    private Map<String, String> pageContent = new ConcurrentHashMap<>();

    private Set<String> unavailableUrls = Collections.synchronizedSet(new HashSet<String>());

    //响应速度
    private Set<ReplySpeed> replySpeeds = Collections.synchronizedSet(new HashSet<ReplySpeed>());

    //过大页面
    private Set<PageSpace> biggerPage = Collections.synchronizedSet(new HashSet<PageSpace>());

    //过长URL页面
    private Set<UrlLength> biggerUrlPage = Collections.synchronizedSet(new HashSet<UrlLength>());

    //过深页面
    private Set<PageDepth> pageDepths = Collections.synchronizedSet(new HashSet<PageDepth>());

    //链接数
    private Map<Types.WkLinkIssueType, Integer> linkCountMap =  new ConcurrentHashMap<>();

    private Site site = Site.me().setRetryTimes(3).setSleepTime(10).setTimeOut(15000);

    private String baseUrl;

    private int checkId;

    private int count = 0;

    private String homepageHost;

    private PageProcessor kpiProcessor = new PageProcessor() {

        @Override
        public void process(Page page) {

            //去掉外站链接
            List<String> targetUrls = page.getHtml().links().all();

            // 当页面已经完全跳转到外部去了，就不再处理了。
            String baseHost = UrlUtils.getHost(page.getUrl().get());
            if (!StringUtils.equals(baseHost, homepageHost)) {
                return;
            }

            final Elements medias = page.getHtml().getDocument().select("[src]");
            Elements imports = page.getHtml().getDocument().select("link[href]");

            for (Element importElem : imports) {
//                log.info(importElem.attr("rel") + ":" + UrlUtils.canonicalizeUrl(importElem.attr("href"), page.getUrl().get()) );
                targetUrls.add(UrlUtils.canonicalizeUrl(importElem.attr("href"), page.getUrl().get()));
            }

            for (Element mediaElem : medias) {
//                log.info(mediaElem.attr("type") + ":" + UrlUtils.canonicalizeUrl( mediaElem.attr("src"), page.getUrl().get()) );
                targetUrls.add(UrlUtils.canonicalizeUrl( mediaElem.attr("src"), page.getUrl().get()));
            }

            //相对/绝对路径的处理问题
            List<String> imgUrls = page.getHtml().$("img", "src").all();
            for (String imgUrl : imgUrls) {
                targetUrls.add(UrlUtils.canonicalizeUrl(imgUrl, page.getUrl().get()));
            }
//            targetUrls.addAll(page.getHtml().$("img", "src").all());
            targetUrls = fixUrl(targetUrls);

            for (String targetUrl : targetUrls) {
                if (!pageParentMap.containsKey(targetUrl)) {
                    synchronized (pageParentMap) {
                        if (!pageParentMap.containsKey(targetUrl)) {
                            pageParentMap.put(targetUrl.intern(), Collections.synchronizedSet(new HashSet<String>()));
                        }
                    }
                }
                Set<String> parentUrlSet = pageParentMap.get(targetUrl);
                if (!targetUrl.equals(page.getUrl().get().intern())) {

                    boolean isEqual = false;
                    if (targetUrl.startsWith(page.getUrl().get())) {
                        String remainStr = targetUrl.substring(page.getUrl().get().length());
                        if (remainStr.equals("/")  || remainStr.equals("#") || remainStr.endsWith("/#")) {
                            isEqual = true;
                        }
                    }
                    if (page.getUrl().get().startsWith(targetUrl)) {
                        String remainStr = page.getUrl().get().substring(targetUrl.length());
                        if (remainStr.equals("/") ||  remainStr.equals("#") ||  remainStr.endsWith("/#")) {
                            isEqual = true;
                        }
                    }

                    if (!isEqual) {
                        parentUrlSet.add(page.getUrl().get().intern());
                    }
                }
            }

            for (String targetUrl: targetUrls) {
                if(!StringUtils.isBlank(targetUrl) && !targetUrl.equals("#") && !targetUrl.startsWith("javascript:")) {
                    targetUrl = UrlUtils.canonicalizeUrl(targetUrl, page.getUrl().toString());
                    final Request request = new Request(targetUrl);
                    request.putExtra("parentUrl", page.getUrl());
                    page.addTargetRequest(request);
                }
            }
        }

        @Override
        public Site getSite() {

            return site;
        }

        private List<String> fixUrl(List<String> urls) {
            List<String> result = new ArrayList<>();
            for (String url : urls) {
                if (url.endsWith("#")) {
                    url = url.substring(0, url.length() -1);
                }
                result.add(url);
            }
            return result;
        }
    };

    private Downloader recordUnavailableUrlDownloader = new HttpClientDownloader() {

        ThreadLocal<Boolean> isUrlAvailable = new ThreadLocal<>();
        ThreadLocal<String> contentType = new ThreadLocal<>();
        ThreadLocal<String> contentDisposition = new ThreadLocal<>();

        @Override
        public Page download(Request request, Task task) {

            isUrlAvailable.set(false);
            Date startDate = new Date();

            EnumUrlType urlType = null;
            Object parentUrl = request.getExtra("parentUrl");
            if (parentUrl == null) {
                parentUrl = request.getUrl().intern();
            }

            try {
                Page result = super.download(request, task);
                Date endDate = new Date();
                long useTime = endDate.getTime() - startDate.getTime();

                if (!isUrlAvailable.get()) {
                    final String contentTypeName = URLConnection.guessContentTypeFromName(request.getUrl());
                    urlType = WebPageUtil.getUrlTypeByContentType(contentTypeName, null);
                    handleInvalidLink(request, parentUrl);
                    return null;
                } else {

                    urlType = WebPageUtil.getUrlTypeByContentType(contentType.get(), contentDisposition.get());
                    final String contentTypeName = URLConnection.guessContentTypeFromName(request.getUrl());
                    EnumUrlType guessUrlType;
                    if (StringUtil.isEmpty(contentTypeName)) {
                        guessUrlType = WebPageUtil.getUrlType(request.getUrl());
                    } else {
                        guessUrlType = WebPageUtil.getUrlTypeByContentType(contentTypeName, null);
                    }
                    if (guessUrlType == EnumUrlType.RES && urlType != guessUrlType) {
                        urlType = guessUrlType;
                        // 断链重定向到情况
                        log.warn("redirect url = " + request.getUrl().intern() + ", content type=" + contentType.get());
                        unavailableUrls.add(request.getUrl().intern());
                        InvalidLinkMsg invalidLinkMsg = new InvalidLinkMsg();
                        invalidLinkMsg.setCheckId(checkId);
                        invalidLinkMsg.setUrlType(urlType);
                        invalidLinkMsg.setParentUrl(parentUrl.toString());
                        setParentContent(request, parentUrl, invalidLinkMsg);
                        invalidLinkMsg.setSiteId(siteManagement.getSiteId());
                        invalidLinkMsg.setUrl(request.getUrl().intern());
                        invalidLinkMsg.setErrorCode(302);
                        commonMQ.publishMsg(invalidLinkMsg);
                        result = null;
                    } else {
                        synchronized (pageContent) {
                            pageContent.put(request.getUrl().intern(), result.getRawText());
                        }

                        String baseHost = UrlUtils.getHost(request.getUrl());
                        // 只处理本域名下的网页
                        if (urlType == EnumUrlType.HTML && StringUtils.equals(baseHost, homepageHost)) {
                            PageInfoMsg pageInfoMsg = new PageInfoMsg();
                            pageInfoMsg.setSiteId(siteManagement.getSiteId());
                            pageInfoMsg.setParentUrl(parentUrl.toString());
                            pageInfoMsg.setSpeed(useTime);
                            pageInfoMsg.setUrl(request.getUrl());
                            pageInfoMsg.setCheckId(checkId);
                            pageInfoMsg.setContent(result.getRawText());
                            count++;
                            commonMQ.publishMsg(pageInfoMsg);
                        }
                    }

                    return result;
                }

            } catch (Exception e) {
                final String contentTypeName = URLConnection.guessContentTypeFromName(request.getUrl());
                urlType = WebPageUtil.getUrlTypeByContentType(contentTypeName, null);
                handleInvalidLink(request, parentUrl);
                return null;
            } finally {
                if (urlType != null) {
                    // 链接类型计数
                    Types.WkLinkIssueType linkType = WebPageUtil.toWkLinkType(urlType);
                    synchronized (linkCountMap) {
                        int count = linkCountMap.get(linkType);
                        if (count == 0) {
                            linkCountMap.put(linkType, 1);
                        } else {
                            linkCountMap.put(linkType, count+1);
                        }
                    }
                }
            }
        }

        private void setParentContent(Request request, Object parentUrl, InvalidLinkMsg invalidLinkMsg) {
            synchronized (pageContent) {
                String parentContent = pageContent.get(parentUrl.toString());
                if (parentContent == null) {
                    if (siteManagement.getSiteIndexUrl().equals(request.getUrl())) {
                        parentContent = "<html><body><h1>首页不可用！</h1></body></html>";
                    } else {
                        parentContent = "<html><body><h1>父页面不存在！</h1></body></html>";
                    }
                }
                invalidLinkMsg.setParentContent(parentContent);
            }
        }

        private void handleInvalidLink(Request request, Object parentUrl) {
            final String contentTypeName = URLConnection.guessContentTypeFromName(request.getUrl());
            final EnumUrlType urlType = WebPageUtil.getUrlTypeByContentType(contentTypeName, null);

            unavailableUrls.add(request.getUrl().intern());
            InvalidLinkMsg invalidLinkMsg = new InvalidLinkMsg();
            invalidLinkMsg.setCheckId(checkId);
            invalidLinkMsg.setUrlType(urlType);
            setParentContent(request, parentUrl, invalidLinkMsg);
            invalidLinkMsg.setParentUrl(parentUrl.toString());
            invalidLinkMsg.setSiteId(siteManagement.getSiteId());
            invalidLinkMsg.setUrl(request.getUrl().intern());
            invalidLinkMsg.setErrorCode(Integer.valueOf(request.getExtra("statusCode").toString()));
            commonMQ.publishMsg(invalidLinkMsg);
        }

        @Override
        protected Page handleResponse(Request request, String charset, HttpResponse httpResponse, Task task) throws IOException {
            contentType.set(httpResponse.getEntity().getContentType().getValue());
            final Header[] headers = httpResponse.getHeaders("Content-Disposition");
            if (headers != null && headers.length >= 1) {
                contentType.set(headers[0].getValue());
            }
            return super.handleResponse(request, charset, httpResponse, task);
        }

        @Override
        public void onSuccess(Request request) {

            isUrlAvailable.set(true);
        }
    };

    private synchronized void init(String baseUrl) {
        this.baseUrl = baseUrl;
        pageParentMap = new HashMap<>();
        unavailableUrls = Collections.synchronizedSet(new HashSet<String>());
        homepageHost = UrlUtils.getHost(baseUrl);
    }

    /**
     * 检索链接/图片/附件是否可用
     *
     * @param threadNum 并发线程数
     * @param baseUrl   网页入口地址
     * @return
     */
    public synchronized List<Pair<String, String>> fetchAllPages(int threadNum, String baseUrl) {

        log.info("linkCheck started!");
        init(baseUrl);
        if (StringUtils.isBlank(baseUrl)) {

            log.info("linkCheck completed, no URL has been checked!");
            return Collections.emptyList();
        }

        Spider.create(kpiProcessor).setDownloader(recordUnavailableUrlDownloader).addUrl(baseUrl).thread(threadNum).run();

        List<Pair<String, String>> unavailableUrlAndParentUrls = new LinkedList<>();
        for (String unavailableUrl : unavailableUrls) {
            Set<String> parentUrls = pageParentMap.get(unavailableUrl);
            if (CollectionUtils.isNotEmpty(parentUrls)) {
                for (String parentUrl : parentUrls) {
                    unavailableUrlAndParentUrls.add(new ImmutablePair<String, String>(parentUrl, unavailableUrl));
                }
            }
        }

        log.info("linkCheck completed!");
        return unavailableUrlAndParentUrls;
    }

    private int calcDeep(String url, int minDeep, int deep) {

        Set<String> parentUrls = pageParentMap.get(url);
        if (CollectionUtils.isNotEmpty(parentUrls)) {
            return this.deepSize(parentUrls, minDeep, deep);
        } else {
            return minDeep < deep ? minDeep : deep;
        }
    }

    private int deepSize (Set<String> parentUrls, int minDeep, int deeps){

        // 规避页面循环引用导致的无限递归问题
        if (deeps > 100) {
            return minDeep < deeps ? minDeep : deeps;
        }

        if (deeps > minDeep) {
            return minDeep;
        }

        int newMinDeep = minDeep;
        for (String parentUrl : parentUrls) {
            if (parentUrl.equals(baseUrl)){
                return deeps + 1 < newMinDeep ? deeps + 1 : newMinDeep;
            }else{
                int newDeep = calcDeep(parentUrl, newMinDeep, deeps + 1);
                newMinDeep = newMinDeep >  newDeep ? newDeep : newMinDeep;
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
        init(baseUrl);
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


    /**
     * 获取统计计数
     * @return
     */
    public Map<Types.WkLinkIssueType, Integer> getLinkCountMap() {
        return this.linkCountMap;
    }


    public void setSite(SiteManagement site) {
        this.siteManagement = site;
    }

    public void setCheckId(int checkId) {
        this.checkId = checkId;
    }

    @Data
    public static class CheckResult {

        private String baseUrl;

        private String unavailableUrl;

        private String parentUrl;
    }
}

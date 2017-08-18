package com.trs.gov.kpi.utils;

import com.trs.gov.kpi.constant.EnumUrlType;
import com.trs.gov.kpi.scheduler.CKMScheduler;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.*;
import us.codecraft.webmagic.downloader.Downloader;
import us.codecraft.webmagic.downloader.HttpClientDownloader;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.utils.UrlUtils;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
@Scope("prototype")
public class PageCKMSpiderUtil {

    private Map<String, Set<String>> pageParentMap = new ConcurrentHashMap<>();

    private Site site = Site.me().setRetryTimes(3).setSleepTime(10).setTimeOut(15000);

    @Setter
    @Getter
    private String baseUrl;

    private Integer siteId;

    private CKMScheduler ckmScheduler;

    private PageProcessor kpiProcessor = new PageProcessor() {

        @Override
        public void process(Page page) {

            // 获取所有 <a href> 的连接
            List<String> targetUrls = page.getHtml().links().all();

            // 去掉外站链接
            Iterator<String> targetUrlIter = targetUrls.iterator();
            String baseHost = UrlUtils.getHost(page.getUrl().get());
            while (targetUrlIter.hasNext()) {
                String targetHost = UrlUtils.getHost(targetUrlIter.next());
                if (!StringUtils.equals(baseHost, targetHost)) {
                    targetUrlIter.remove();
                }
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

    private Downloader recordPageDownloader = new HttpClientDownloader() {

        ThreadLocal<Boolean> isUrlAvailable = new ThreadLocal<>();

        @Override
        public Page download(Request request, Task task) {

            EnumUrlType urlType = WebPageUtil.getUrlType(request.getUrl());
            if (urlType == EnumUrlType.HTML) {
                isUrlAvailable.set(false);
                Page result = super.download(request, task);
                if (isUrlAvailable.get()) {
//                    checkTime = new Date();
//                    linkContentStatsService.insertLinkContent(siteId, Types.IssueType.INFO_ERROR_ISSUE.value,request.getUrl().intern(),
//                            result.getRawText().intern(),
//                            checkTime);
                    ckmScheduler.insert(new CKMPage(request.getUrl().intern(), result.getRawText()));
                    return result;
                } else {
                    return null;
                }
            } else {
                return null;
            }
        }

        @Override
        public void onSuccess(Request request) {

            isUrlAvailable.set(true);
        }
    };


    private synchronized void init(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    /**
     * 页面url和页面信息的获取
     *
     * @param threadNum 并发线程数
     * @param baseUrl   网页入口地址
     * @return
     */
    public synchronized void fetchPages(int threadNum, String baseUrl, CKMScheduler ckmScheduler, Integer siteId) {

        log.info("fetch ckm pages started!");
        this.siteId = siteId;
        this.ckmScheduler = ckmScheduler;
        init(baseUrl);
        if (StringUtils.isBlank(baseUrl)) {

            log.info("fetch ckm pages completed, no URL has been checked!");
            return;
        }

        Spider.create(kpiProcessor).setDownloader(recordPageDownloader).addUrl(baseUrl).thread(threadNum).run();
        log.info("fetch ckm pages completed!");
    }

    public String getParentUrl(String url) {
        synchronized (pageParentMap) {
            Set<String> parents = pageParentMap.get(url);
            if (parents != null && !parents.isEmpty()) {
                return parents.iterator().next();
            }
            return null;
        }
    }


    @Data
    public static class CKMPage {

        private String url;

        private String content;

        public CKMPage(String url, String content) {
            this.url = url;
            this.content = content;
        }

    }

}

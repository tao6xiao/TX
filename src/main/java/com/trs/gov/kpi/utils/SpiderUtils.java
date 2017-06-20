package com.trs.gov.kpi.utils;

import com.trs.gov.kpi.constant.Types;
import com.trs.gov.kpi.entity.PageDepth;
import com.trs.gov.kpi.entity.PageSpace;
import com.trs.gov.kpi.entity.ReplySpeed;
import com.trs.gov.kpi.entity.UrlLength;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.*;
import us.codecraft.webmagic.downloader.Downloader;
import us.codecraft.webmagic.downloader.HttpClientDownloader;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.utils.UrlUtils;

import java.util.*;

/**
 * Created by wangxuan on 2017/5/10.
 */
@Slf4j
@Component
@Scope("prototype")
public class SpiderUtils {

    // 过大页面阀值
    private static final int THRESHOLD_MAX_PAGE_SIZE = 5 * 1024 * 1024;

    //栏目URL地址层级阀值
    private static final int THRESHOLD_MAX_URL_LENGHT = 6;

    //页面深度阀值
    private static final int THRESHOLD_MAX_PAGE_DEPTH = 8;

    private HashMap<String, Set<String>> pageParentMap = new HashMap<>();

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

    private PageProcessor kpiProcessor = new PageProcessor() {

        @Override
        public void process(Page page) {

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

            if (!isUrlAvailable.get()) {
                unavailableUrls.add(request.getUrl().intern());
            } else {
                replySpeeds.add(new ReplySpeed(Types.AnalysisType.REPLY_SPEED.value,
                        0,
                        request.getUrl().intern(),
                        useTime,
                        Long.valueOf(result.getRawText().getBytes().length),
                        new Date()));

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
                            0,
                            request.getUrl().intern(),
                            Long.valueOf(request.getUrl().length()),
                            Long.valueOf(result.getRawText().getBytes().length),
                            new Date()));
                }

                int deepSize = calcDeep(request.getUrl(), 100, 1);
                if (deepSize > THRESHOLD_MAX_PAGE_DEPTH) {
                    pageDepths.add(new PageDepth(Types.AnalysisType.OVER_DEEP_PAGE.value,
                            0,
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
    };


    private synchronized void init(String baseUrl) {
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
    public synchronized List<Pair<String, String>> linkCheck(int threadNum, String baseUrl) {


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

    @Data
    public static class CheckResult {

        private String baseUrl;

        private String unavailableUrl;

        private String parentUrl;
    }
}

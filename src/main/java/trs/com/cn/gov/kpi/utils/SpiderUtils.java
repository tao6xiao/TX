package trs.com.cn.gov.kpi.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
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
public class SpiderUtils {

    private static HashMap<String, Set<String>> pageParentMap = new HashMap<>();

    private static Set<String> unavailableUrls = Collections.synchronizedSet(new HashSet<String>());

    private static Site site = Site.me().setRetryTimes(3).setSleepTime(1000).setTimeOut(15000);

    private static PageProcessor kpiProcessor = new PageProcessor() {

        @Override
        public void process(Page page) {

            //去掉外站链接
            List<String> targetUrls = page.getHtml().links().all();
            Iterator<String> targetUrlIter = targetUrls.iterator();
            String baseHost = UrlUtils.getHost(page.getUrl().get());
            while(targetUrlIter.hasNext()) {

                String targetHost = UrlUtils.getHost(targetUrlIter.next());
                if(!StringUtils.equals(baseHost, targetHost)) {

                    targetUrlIter.remove();
                }
            }

            //相对/绝对路径的处理问题
            List<String> imgUrls = page.getHtml().$("img", "src").all();
            for(String imgUrl: imgUrls) {

                targetUrls.add(UrlUtils.canonicalizeUrl(imgUrl, page.getUrl().get()));
            }
            targetUrls.addAll(page.getHtml().$("img", "src").all());

            for(String targetUrl: targetUrls) {

                if(!pageParentMap.containsKey(targetUrl)) {

                    synchronized (pageParentMap) {

                        if(!pageParentMap.containsKey(targetUrl)) {

                            pageParentMap.put(targetUrl.intern(), Collections.synchronizedSet(new HashSet<String>()));
                        }
                    }
                }
                Set<String> parentUrlSet = pageParentMap.get(targetUrl);
                parentUrlSet.add(page.getUrl().get().intern());
            }

            page.addTargetRequests(targetUrls);
        }

        @Override
        public Site getSite() {

            return site;
        }
    };

    private static Downloader recordUnavailableUrlDownloader = new HttpClientDownloader() {

        ThreadLocal<Boolean> isUrlUnavailable = new ThreadLocal<>();

        @Override
        public Page download(Request request, Task task) {

            isUrlUnavailable.set(true);
            Page result = super.download(request, task);
            if(isUrlUnavailable.get()) {

                unavailableUrls.add(request.getUrl().intern());
            }
            return result;
        }

        @Override
        public void onSuccess(Request request) {

            isUrlUnavailable.set(false);
        }
    };

    private synchronized static void init() {

        pageParentMap = new HashMap<>();
        unavailableUrls = Collections.synchronizedSet(new HashSet<String>());
    }

    /**
     * 检索链接/图片/附件是否可用
     * @param threadNum 并发线程数
     * @param baseUrls 网页入口地址
     * @return
     */
    public synchronized static List<Pair<String, String>> linkCheck(int threadNum, String... baseUrls) {

        log.info("linkCheck started!");
        init();
        if(ArrayUtils.isEmpty(baseUrls)) {

            log.info("linkCheck completed, no URL has been checked!");
            return Collections.EMPTY_LIST;
        }

        Spider.create(kpiProcessor).setDownloader(recordUnavailableUrlDownloader).addUrl(baseUrls).thread(threadNum).run();
        List<Pair<String, String>> unavailableUrlAndParentUrls = new LinkedList<>();
        for(String unavailableUrl: unavailableUrls) {

            Set<String> parentUrls = pageParentMap.get(unavailableUrl);

            if(CollectionUtils.isNotEmpty(parentUrls)) {

                for(String parentUrl: parentUrls) {

                    unavailableUrlAndParentUrls.add(new ImmutablePair<String, String>(parentUrl, unavailableUrl));
                }
            }
        }

        log.info("linkCheck completed!");
        return unavailableUrlAndParentUrls;
    }

    /**
     * 检索首页是否可用
     * @param homePageUrls
     * @return
     */
    public synchronized static List<String> homePageCheck(String... homePageUrls) {

        log.info("homePageCheck started!");
        init();
        for(String homePageUrl: homePageUrls) {

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
}

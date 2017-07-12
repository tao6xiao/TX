package com.trs.gov.kpi.utils;

import com.trs.gov.kpi.constant.Types;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.downloader.Downloader;
import us.codecraft.webmagic.downloader.HttpClientDownloader;

import java.util.UUID;

/**
 * Created by he.lang on 2017/7/12.
 */
@Slf4j
@Component
@Scope("prototype")
public class ServiceLinkSpiderUtil {

    //service link
    private Types.ServiceLinkIssueType type = Types.ServiceLinkIssueType.INVALID;

    private Site site = Site.me().setRetryTimes(3).setSleepTime(10).setTimeOut(15000);

    @Setter
    @Getter
    private String baseUrl;

    private Downloader recordServiceLinkDownloader = new HttpClientDownloader() {

        ThreadLocal<Boolean> isUrlAvailable = new ThreadLocal<>();

        @Override
        public Page download(Request request, Task task) {
            Page result = super.download(request, task);
            if (result.getStatusCode() != 200) {
                type = Types.ServiceLinkIssueType.INVALID_LINK;
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
    }

    /**
     * 页面url和页面信息的获取
     *
     * @param baseUrl   网页入口地址
     * @return
     */
    public synchronized Types.ServiceLinkIssueType linkCheck(String baseUrl) {


        log.info("service link get started!");
        init(baseUrl);
        if (StringUtils.isBlank(baseUrl)) {

            log.info("fetch ckm pages completed, no URL has been checked!");
            return type;
        }

        recordServiceLinkDownloader.download(new Request(baseUrl), new Task() {
            @Override
            public String getUUID() {
                return UUID.randomUUID().toString();
            }

            @Override
            public Site getSite() {
                return site;
            }
        });
        log.info("service link get completed!");
        return type;
    }


}

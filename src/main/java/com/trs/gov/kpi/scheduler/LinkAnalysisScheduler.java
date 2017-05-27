package com.trs.gov.kpi.scheduler;

import com.trs.gov.kpi.constant.Types;
import com.trs.gov.kpi.entity.responsedata.LinkAvailabilityResponse;
import com.trs.gov.kpi.service.LinkAvailabilityService;
import com.trs.gov.kpi.utils.SpiderUtils;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.ScheduledExecutorService;

/**
 * Created by wangxuan on 2017/5/10.
 * 定时任务，抓取网站链接是否可用
 */
@Slf4j
@Component
@Scope("prototype")
public class LinkAnalysisScheduler extends AbstractScheduler{

    private static Map<String, ScheduledExecutorService> taskAndExecutorMap =
            Collections.synchronizedMap(new HashMap<String, ScheduledExecutorService>());

    @Resource
    LinkAvailabilityService linkAvailabilityService;

    @Resource
    SpiderUtils spider;

    @Setter @Getter
    private Integer siteId;

    @Setter @Getter
    private String baseUrl;

    @Getter
    private final Runnable task = new Runnable() {

        @Override
        public void run() {

            log.info("LinkAnalysisScheduler " + String.valueOf(siteId) + " start...");
            try {

                List<Pair<String, String>> unavailableUrlAndParentUrls = spider.linkCheck(5, baseUrl);
                Date checkTime = new Date();
                for(Pair<String, String> unavailableUrlAndParentUrl: unavailableUrlAndParentUrls) {
                    LinkAvailabilityResponse linkAvailabilityResponse = new LinkAvailabilityResponse();
                    linkAvailabilityResponse.setInvalidLink(unavailableUrlAndParentUrl.getKey());
                    linkAvailabilityResponse.setSnapshot(unavailableUrlAndParentUrl.getValue());
                    linkAvailabilityResponse.setCheckTime(checkTime);
                    linkAvailabilityResponse.setSiteId(siteId);
                    linkAvailabilityResponse.setIssueTypeId(getTypeByLink(unavailableUrlAndParentUrl.getKey()).value);
                    linkAvailabilityService.insertLinkAvailability(linkAvailabilityResponse);
                }
            } catch (Exception e) {
                log.error("check link:{}, siteId:{} availability error!", baseUrl, siteId, e);
            } finally {
                log.info("LinkAnalysisScheduler " + String.valueOf(siteId) + " end...");
            }
        }
    };

    private String[] imageSuffixs = new String[]{"bmp", "jpg", "jpeg", "png", "gif"};

    private String[] fileSuffixs = new String[]{"zip", "doc", "xls", "xlsx", "docx", "rar"};

    private Types.LinkAvailableIssueType getTypeByLink(String url) {

        String suffix = url.substring(url.lastIndexOf(".") + 1);
        for(String imageSuffix: imageSuffixs) {

            if(StringUtils.equalsIgnoreCase(suffix, imageSuffix)) {

                return Types.LinkAvailableIssueType.INVALID_IMAGE;
            }
        }

        for(String fileSuffix: fileSuffixs) {

            if(StringUtils.equalsIgnoreCase(suffix, fileSuffix)) {

                return Types.LinkAvailableIssueType.INVALID_FILE;
            }
        }

        return Types.LinkAvailableIssueType.INVALID_LINK;
    }
}

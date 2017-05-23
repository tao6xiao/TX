package com.trs.gov.kpi.scheduler;

import com.trs.gov.kpi.constant.LinkType;
import com.trs.gov.kpi.entity.Issue;
import com.trs.gov.kpi.entity.LinkAvailability;
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
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ScheduledExecutorService;
import java.util.regex.Pattern;

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

            log.info("check start...");
            try {

                List<Pair<String, String>> unavailableUrlAndParentUrls = spider.linkCheck(5, baseUrl);
                Date checkTime = new Date();
                for(Pair<String, String> unavailableUrlAndParentUrl: unavailableUrlAndParentUrls) {

                    LinkAvailability linkAvailability = new LinkAvailability();
                    linkAvailability.setInvalidLink(unavailableUrlAndParentUrl.getKey());
                    linkAvailability.setSnapshot(unavailableUrlAndParentUrl.getValue());
                    linkAvailability.setCheckTime(checkTime);
                    linkAvailability.setSiteId(siteId);
                    linkAvailability.setIssueTypeName(getTypeByLink(unavailableUrlAndParentUrl.getKey()).getName());
                    linkAvailabilityService.insertLinkAvailability(linkAvailability);
                }
            } catch (Exception e) {

                log.error("check link:{}, siteId:{} availability error!", baseUrl, siteId, e);
            }
        }
    };

    private String[] imageSuffixs = new String[]{"bmp", "jpg", "jpeg", "png", "gif"};

    private String[] fileSuffixs = new String[]{"zip", "doc", "xls", "xlsx", "docx", "rar"};

    private LinkType getTypeByLink(String url) {

        String suffix = url.substring(url.lastIndexOf(".") + 1);
        for(String imageSuffix: imageSuffixs) {

            if(StringUtils.equalsIgnoreCase(suffix, imageSuffix)) {

                return LinkType.IMAGE;
            }
        }

        for(String fileSuffix: fileSuffixs) {

            if(StringUtils.equalsIgnoreCase(suffix, fileSuffix)) {

                return LinkType.FILE;
            }
        }

        return LinkType.PAGE;
    }
}

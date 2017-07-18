package com.trs.gov.kpi.scheduler;

import com.trs.gov.kpi.constant.Types;
import com.trs.gov.kpi.entity.PageDepth;
import com.trs.gov.kpi.entity.PageSpace;
import com.trs.gov.kpi.entity.ReplySpeed;
import com.trs.gov.kpi.entity.UrlLength;
import com.trs.gov.kpi.entity.responsedata.LinkAvailabilityResponse;
import com.trs.gov.kpi.service.LinkAvailabilityService;
import com.trs.gov.kpi.service.WebPageService;
import com.trs.gov.kpi.utils.SpiderUtils;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * Created by wangxuan on 2017/5/10.
 * 定时任务，抓取网站链接是否可用
 */
@Slf4j
@Component
@Scope("prototype")
public class LinkAnalysisScheduler implements SchedulerTask {
    @Resource
    LinkAvailabilityService linkAvailabilityService;

    @Resource
    SpiderUtils spider;

    @Resource
    WebPageService webPageService;

    @Setter
    @Getter
    private Integer siteId;

    @Setter
    @Getter
    private String baseUrl;

    @Setter
    @Getter
    private Boolean isTimeNode;

    @Override
    public void run() {

        log.info("LinkAnalysisScheduler " + siteId + " start...");
        try {

            List<Pair<String, String>> unavailableUrlAndParentUrls = spider.linkCheck(5, baseUrl);
            Date checkTime = new Date();
            for (Pair<String, String> unavailableUrlAndParentUrl : unavailableUrlAndParentUrls) {
                LinkAvailabilityResponse linkAvailabilityResponse = new LinkAvailabilityResponse();
                linkAvailabilityResponse.setInvalidLink(unavailableUrlAndParentUrl.getKey());
                linkAvailabilityResponse.setSnapshot(unavailableUrlAndParentUrl.getValue());
                linkAvailabilityResponse.setCheckTime(checkTime);
                linkAvailabilityResponse.setSiteId(siteId);
                linkAvailabilityResponse.setIssueTypeId(getTypeByLink(unavailableUrlAndParentUrl.getKey()).value);

                if (!linkAvailabilityService.existLinkAvailability(siteId, unavailableUrlAndParentUrl.getKey())) {
                    linkAvailabilityService.insertLinkAvailability(linkAvailabilityResponse);
                }
            }
            //获取响应速度基本信息，信息入库
            Set<ReplySpeed> replySpeedSet = spider.getReplySpeeds();
            for (ReplySpeed replySpeed : replySpeedSet) {
                replySpeed.setSiteId(siteId);
                webPageService.insertReplyspeed(replySpeed);
            }

            //获取过大页面信息；信息入库
            Set<PageSpace> biggerPageSpace = spider.biggerPageSpace();
            for (PageSpace pageSpaceto : biggerPageSpace) {
                pageSpaceto.setSiteId(siteId);
                webPageService.insertPageSpace(pageSpaceto);
            }

            //获取过长URL页面信息；信息入库
            Set<UrlLength> biggerUerLenght = spider.getBiggerUrlPage();
            for (UrlLength urlLenght : biggerUerLenght) {
                urlLenght.setSiteId(siteId);
                webPageService.insertUrlLength(urlLenght);
            }

            //获取过深页面信息；信息入库
            Set<PageDepth> pageDepthSet = spider.getPageDepths();
            for (PageDepth pageDepth : pageDepthSet) {
                pageDepth.setSiteId(siteId);
                webPageService.insertPageDepth(pageDepth);
            }

        } catch (Exception e) {
            log.error("check link:{}, siteId:{} availability error!", baseUrl, siteId, e);
        } finally {
            log.info("LinkAnalysisScheduler " + siteId + " end...");
        }
    }

    private String[] imageSuffixs = new String[]{"bmp", "jpg", "jpeg", "png", "gif"};

    private String[] fileSuffixs = new String[]{"zip", "doc", "xls", "xlsx", "docx", "rar"};

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
}

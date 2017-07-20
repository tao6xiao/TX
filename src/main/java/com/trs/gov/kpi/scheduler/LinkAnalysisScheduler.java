package com.trs.gov.kpi.scheduler;

import com.trs.gov.kpi.constant.Types;
import com.trs.gov.kpi.constant.WebpageTableField;
import com.trs.gov.kpi.dao.WebPageMapper;
import com.trs.gov.kpi.entity.PageDepth;
import com.trs.gov.kpi.entity.PageSpace;
import com.trs.gov.kpi.entity.ReplySpeed;
import com.trs.gov.kpi.entity.UrlLength;
import com.trs.gov.kpi.entity.dao.QueryFilter;
import com.trs.gov.kpi.entity.dao.Table;
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

    @Resource
    WebPageMapper webPageMapper;

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

            List<Pair<String, String>> unavailableUrlAndParentUrls = spider.linkCheck(3, siteId, baseUrl);
            Date checkTime = new Date();
            for (Pair<String, String> unavailableUrlAndParentUrl : unavailableUrlAndParentUrls) {
                LinkAvailabilityResponse linkAvailabilityResponse = new LinkAvailabilityResponse();
                linkAvailabilityResponse.setInvalidLink(unavailableUrlAndParentUrl.getValue());
                linkAvailabilityResponse.setSnapshot(unavailableUrlAndParentUrl.getKey());
                linkAvailabilityResponse.setCheckTime(checkTime);
                linkAvailabilityResponse.setSiteId(siteId);
                linkAvailabilityResponse.setIssueTypeId(getTypeByLink(unavailableUrlAndParentUrl.getValue()).value);

                if (!linkAvailabilityService.existLinkAvailability(siteId, unavailableUrlAndParentUrl.getValue())) {
                    linkAvailabilityService.insertLinkAvailability(linkAvailabilityResponse);
                }
            }

            //获取响应速度基本信息，信息入库并去除重复数据和更新数据库信息
            Set<ReplySpeed> replySpeedSet = spider.getReplySpeeds();
            for (ReplySpeed replySpeedTo : replySpeedSet) {

                QueryFilter queryFilter = new QueryFilter(Table.WEB_PAGE);
                queryFilter.addCond(WebpageTableField.SITE_ID, siteId);
                queryFilter.addCond(WebpageTableField.PAGE_LINK, replySpeedTo.getPageLink());
                queryFilter.addCond(WebpageTableField.CHNL_ID, replySpeedTo.getChnlId());
                queryFilter.addCond(WebpageTableField.TYPE_ID, Types.AnalysisType.REPLY_SPEED.value);

                List<ReplySpeed> pageSpaceList = webPageMapper.selectReplySpeed(queryFilter);
                updateOrInsertSpeed(pageSpaceList, replySpeedTo);
            }

            //获取过大页面信息；信息入库并去除重复数据和更新数据库信息
            Set<PageSpace> biggerPageSpace = spider.biggerPageSpace();
            for (PageSpace pageSpaceTo : biggerPageSpace) {
                QueryFilter queryFilter = new QueryFilter(Table.WEB_PAGE);
                queryFilter.addCond(WebpageTableField.SITE_ID, siteId);
                queryFilter.addCond(WebpageTableField.PAGE_LINK, pageSpaceTo.getPageLink());
                queryFilter.addCond(WebpageTableField.CHNL_ID, pageSpaceTo.getChnlId());
                queryFilter.addCond(WebpageTableField.TYPE_ID, Types.AnalysisType.OVERSIZE_PAGE.value);

                List<PageSpace> pageSpaceList = webPageMapper.selectPageSpace(queryFilter);
                updateOrInsertSpace(pageSpaceList, pageSpaceTo);
            }

            //获取过长URL页面信息；信息入库并去除重复数据和更新数据库信息
            Set<UrlLength> biggerUerLenght = spider.getBiggerUrlPage();
            for (UrlLength urlLenghtTo : biggerUerLenght) {

                QueryFilter queryFilter = new QueryFilter(Table.WEB_PAGE);
                queryFilter.addCond(WebpageTableField.SITE_ID, siteId);
                queryFilter.addCond(WebpageTableField.PAGE_LINK, urlLenghtTo.getPageLink());
                queryFilter.addCond(WebpageTableField.CHNL_ID, urlLenghtTo.getChnlId());
                queryFilter.addCond(WebpageTableField.TYPE_ID, Types.AnalysisType.TOO_LONG_URL.value);

                List<UrlLength> urlLenghtList = webPageMapper.selectUrlLength(queryFilter);
                updateOrInsertLength(urlLenghtList, urlLenghtTo);
            }

            //获取过深页面信息；信息入库并去除重复数据和更新数据库信息
            Set<PageDepth> pageDepthSet = spider.getPageDepths();
            for (PageDepth pageDepthTo : pageDepthSet) {
                QueryFilter queryFilter = new QueryFilter(Table.WEB_PAGE);
                queryFilter.addCond(WebpageTableField.SITE_ID, siteId);
                queryFilter.addCond(WebpageTableField.PAGE_LINK, pageDepthTo.getPageLink());
                queryFilter.addCond(WebpageTableField.CHNL_ID, pageDepthTo.getChnlId());
                queryFilter.addCond(WebpageTableField.TYPE_ID, Types.AnalysisType.OVER_DEEP_PAGE.value);

                List<PageDepth> pageDepthList = webPageMapper.selectPageDepth(queryFilter);
                updateOrInsertDepth(pageDepthList, pageDepthTo);
            }

        } catch (Exception e) {
            log.error("check link:{}, siteId:{} availability error!", baseUrl, siteId, e);
        } finally {
            log.info("LinkAnalysisScheduler " + siteId + " end...");
        }
    }

    private void updateOrInsertSpeed(List<ReplySpeed> pageSpaceList, ReplySpeed replySpeedTo) {
        if (pageSpaceList.isEmpty()) {
            replySpeedTo.setSiteId(siteId);
            webPageService.insertReplyspeed(replySpeedTo);
        } else {
            webPageMapper.updateReplySpeed(replySpeedTo);
        }
    }

    private void updateOrInsertSpace(List<PageSpace> pageSpaceList, PageSpace pageSpaceTo) {
        if (pageSpaceList.isEmpty()) {
            pageSpaceTo.setSiteId(siteId);
            webPageService.insertPageSpace(pageSpaceTo);
        } else {
            webPageMapper.updatePageSpace(pageSpaceTo);
        }
    }

    private void updateOrInsertLength(List<UrlLength> urlLenghtList, UrlLength urlLenghtTo) {
        if (urlLenghtList.isEmpty()) {
            urlLenghtTo.setSiteId(siteId);
            webPageService.insertUrlLength(urlLenghtTo);
        } else {
            webPageMapper.updateUrlLength(urlLenghtTo);
        }
    }

    private void updateOrInsertDepth(List<PageDepth> pageDepthList, PageDepth pageDepthTo) {
        if (pageDepthList.isEmpty()) {
            pageDepthTo.setSiteId(siteId);
            webPageService.insertPageDepth(pageDepthTo);
        } else {
            webPageMapper.updatePageDepth(pageDepthTo);
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

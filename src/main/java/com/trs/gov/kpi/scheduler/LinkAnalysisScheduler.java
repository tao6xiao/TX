package com.trs.gov.kpi.scheduler;

import com.trs.gov.kpi.constant.Types;
import com.trs.gov.kpi.constant.WebpageTableField;
import com.trs.gov.kpi.dao.WebPageMapper;
import com.trs.gov.kpi.entity.PageDepth;
import com.trs.gov.kpi.entity.PageSpace;
import com.trs.gov.kpi.entity.ReplySpeed;
import com.trs.gov.kpi.entity.UrlLength;
import com.trs.gov.kpi.entity.dao.QueryFilter;
import com.trs.gov.kpi.entity.requestdata.PageDataRequestParam;
import com.trs.gov.kpi.entity.responsedata.LinkAvailabilityResponse;
import com.trs.gov.kpi.entity.wangkang.SiteManagement;
import com.trs.gov.kpi.msgqueue.CommonMQ;
import com.trs.gov.kpi.service.LinkAvailabilityService;
import com.trs.gov.kpi.service.WebPageService;
import com.trs.gov.kpi.service.helper.QueryFilterHelper;
import com.trs.gov.kpi.service.wangkang.WkIdService;
import com.trs.gov.kpi.utils.PageSpider;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
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
    private LinkAvailabilityService linkAvailabilityService;

    @Resource
    private WkIdService wkIdService;

    @Resource
    PageSpider pageSpider;

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

    @Getter
    @Setter
    private SiteManagement site;

    private int checkId;

    @Override
    public void run() {

        log.info("LinkAnalysisScheduler " + siteId + " start...");
        try {
            checkId = wkIdService.getNewCheckId();
            pageSpider.setSite(site);
            pageSpider.setCheckId(checkId);

            List<Pair<String, String>> unavailableUrlAndParentUrls = pageSpider.fetchAllPages(5, site.getSiteIndexUrl());
//            Date checkTime = new Date();
//            for (Pair<String, String> unavailableUrlAndParentUrl : unavailableUrlAndParentUrls) {
//                LinkAvailabilityResponse linkAvailabilityResponse = new LinkAvailabilityResponse();
//                linkAvailabilityResponse.setInvalidLink(unavailableUrlAndParentUrl.getKey());
//                linkAvailabilityResponse.setSnapshot(unavailableUrlAndParentUrl.getValue());
//                linkAvailabilityResponse.setCheckTime(checkTime);
//                linkAvailabilityResponse.setSiteId(siteId);
//                linkAvailabilityResponse.setIssueTypeId(getTypeByLink(unavailableUrlAndParentUrl.getKey()).value);
//                linkAvailabilityService.insertLinkAvailability(linkAvailabilityResponse);
//            }
//            //获取响应速度基本信息，信息入库并去除重复数据和更新数据库信息
//            Set<ReplySpeed> replySpeedSet = pageSpider.getReplySpeeds();
//            for (ReplySpeed replySpeedTo : replySpeedSet) {
//
//                PageDataRequestParam param = new PageDataRequestParam();
//                param.setSiteId(siteId);
//                QueryFilter queryFilter = QueryFilterHelper.toFilter(param);
//                queryFilter.addCond(WebpageTableField.REPLY_SPEED, replySpeedTo.getSpeed());
//                queryFilter.addCond(WebpageTableField.PAGE_LINK, replySpeedTo.getPageLink());
//                queryFilter.addCond(WebpageTableField.CHNL_NAME, replySpeedTo.getChnlName());
//                queryFilter.addCond(WebpageTableField.TYPE_ID, Types.AnalysisType.REPLY_SPEED.value);
//
//                List<ReplySpeed> pageSpaceList = webPageMapper.selectReplySpeed(queryFilter);
//                if(pageSpaceList.size() == 0){
//                    replySpeedTo.setSiteId(siteId);
//                    webPageService.insertReplyspeed(replySpeedTo);
//                }else{
//                    webPageMapper.updateReplySpeed(replySpeedTo);
//                }
//            }
//            PageDataRequestParam param = new PageDataRequestParam();
//            param.setSiteId(siteId);
//            QueryFilter queryFilter = QueryFilterHelper.toFilter(param);
//            queryFilter.addCond(WebpageTableField.TYPE_ID, Types.AnalysisType.REPLY_SPEED.value);
//
//            List<ReplySpeed> getListByReplySpeed = webPageMapper.selectReplySpeed(queryFilter);
//            if(getListByReplySpeed.size() > replySpeedSet.size()){
//                for (ReplySpeed replySpeed : getListByReplySpeed){
//                    boolean getRepeat = true;
//                    for(ReplySpeed replySpeedTo : replySpeedSet){
//                        if (replySpeed.equals(replySpeedTo)){
//                            getRepeat = false;
//                        }
//                    }
//                    if (getRepeat){
//                        webPageMapper.daleteRepeatPageSpace(replySpeed.getId());
//                    }
//                }
//
//            }
//
//            //获取过大页面信息；信息入库并去除重复数据和更新数据库信息
//            Set<PageSpace> biggerPageSpace = pageSpider.biggerPageSpace();
//            for (PageSpace pageSpaceTo : biggerPageSpace) {
//
//                param = new PageDataRequestParam();
//                param.setSiteId(siteId);
//                queryFilter = QueryFilterHelper.toFilter(param);
//                queryFilter.addCond(WebpageTableField.PAGE_SPACE, pageSpaceTo.getSpace());
//                queryFilter.addCond(WebpageTableField.PAGE_LINK, pageSpaceTo.getPageLink());
//                queryFilter.addCond(WebpageTableField.CHNL_NAME, pageSpaceTo.getChnlName());
//                queryFilter.addCond(WebpageTableField.TYPE_ID, Types.AnalysisType.OVERSIZE_PAGE.value);
//
//                List<PageSpace> pageSpaceList = webPageMapper.selectPageSpace(queryFilter);
//                if(pageSpaceList.size() == 0){
//                    pageSpaceTo.setSiteId(siteId);
//                    webPageService.insertPageSpace(pageSpaceTo);
//                }else{
//                    webPageMapper.updatePageSpace(pageSpaceTo);
//                }
//            }
//            param = new PageDataRequestParam();
//            param.setSiteId(siteId);
//            queryFilter = QueryFilterHelper.toFilter(param);
//            queryFilter.addCond(WebpageTableField.TYPE_ID, Types.AnalysisType.OVERSIZE_PAGE.value);
//
//            List<PageSpace> getListByPageSpace = webPageMapper.selectPageSpace(queryFilter);
//            if(getListByPageSpace.size() > biggerPageSpace.size()){
//                for (PageSpace pageSpace : getListByPageSpace){
//                    boolean getRepeat = true;
//                    for(PageSpace pageSpaceto : biggerPageSpace){
//                        if (pageSpace.equals(pageSpaceto)){
//                            getRepeat = false;
//                        }
//                    }
//                    if (getRepeat){
//                        webPageMapper.daleteRepeatPageSpace(pageSpace.getId());
//                    }
//                }
//
//            }
//
//            //获取过长URL页面信息；信息入库并去除重复数据和更新数据库信息
//            Set<UrlLength> biggerUerLenght = pageSpider.getBiggerUrlPage();
//            for (UrlLength urlLenghtTo : biggerUerLenght) {
//
//                param = new PageDataRequestParam();
//                param.setSiteId(siteId);
//                queryFilter = QueryFilterHelper.toFilter(param);
//                queryFilter.addCond(WebpageTableField.URL_LENGTH, urlLenghtTo.getLength());
//                queryFilter.addCond(WebpageTableField.PAGE_LINK, urlLenghtTo.getPageLink());
//                queryFilter.addCond(WebpageTableField.CHNL_NAME, urlLenghtTo.getChnlName());
//                queryFilter.addCond(WebpageTableField.TYPE_ID, Types.AnalysisType.TOO_LONG_URL.value);
//
//                List<PageSpace> urlLenghtList = webPageMapper.selectPageSpace(queryFilter);
//                if(urlLenghtList.size() == 0){
//                    urlLenghtTo.setSiteId(siteId);
//                    webPageService.insertUrlLength(urlLenghtTo);
//                }else {
//                    webPageMapper.updateUrlLength(urlLenghtTo);
//                }
//            }
//            param = new PageDataRequestParam();
//            param.setSiteId(siteId);
//            queryFilter = QueryFilterHelper.toFilter(param);
//            queryFilter.addCond(WebpageTableField.TYPE_ID, Types.AnalysisType.TOO_LONG_URL.value);
//
//            List<UrlLength> getListByUrlLenght = webPageMapper.selectUrlLength(queryFilter);
//            if(getListByUrlLenght.size() > biggerUerLenght.size()){
//                for (UrlLength urlLength : getListByUrlLenght){
//                    boolean getRepeat = true;
//                    for(UrlLength urlLenghtTo : biggerUerLenght){
//                        if (urlLength.equals(urlLenghtTo)){
//                            getRepeat = false;
//                        }
//                    }
//                    if (getRepeat){
//                        webPageMapper.daleteRepeatPageSpace(urlLength.getId());
//                    }
//                }
//
//            }
//
//            //获取过深页面信息；信息入库并去除重复数据和更新数据库信息
//            Set<PageDepth> pageDepthSet = pageSpider.getPageDepths();
//            for (PageDepth pageDepthTo : pageDepthSet) {
//                param = new PageDataRequestParam();
//                param.setSiteId(siteId);
//                queryFilter = QueryFilterHelper.toFilter(param);
//                queryFilter.addCond(WebpageTableField.URL_LENGTH, pageDepthTo.getDepth());
//                queryFilter.addCond(WebpageTableField.PAGE_LINK, pageDepthTo.getPageLink());
//                queryFilter.addCond(WebpageTableField.CHNL_NAME, pageDepthTo.getChnlName());
//                queryFilter.addCond(WebpageTableField.TYPE_ID, Types.AnalysisType.TOO_LONG_URL.value);
//
//                List<PageSpace> urlLenghtList = webPageMapper.selectPageSpace(queryFilter);
//                if(urlLenghtList.size() == 0){
//                    pageDepthTo.setSiteId(siteId);
//                    webPageService.insertPageDepth(pageDepthTo);
//                }else {
//                    webPageMapper.updatePageDepth(pageDepthTo);
//                }
//            }
//            param = new PageDataRequestParam();
//            param.setSiteId(siteId);
//            queryFilter = QueryFilterHelper.toFilter(param);
//            queryFilter.addCond(WebpageTableField.TYPE_ID, Types.AnalysisType.OVER_DEEP_PAGE.value);
//
//            List<PageDepth> getListByPageDepth = webPageMapper.selectPageDepth(queryFilter);
//            if(getListByPageDepth.size() > pageDepthSet.size()){
//                for (PageDepth pageDepth : getListByPageDepth){
//                    boolean getRepeat = true;
//                    for(PageDepth PageDepthTo : pageDepthSet){
//                        if (pageDepth.equals(PageDepthTo)){
//                            getRepeat = false;
//                        }
//                    }
//                    if (getRepeat){
//                        webPageMapper.daleteRepeatPageSpace(pageDepth.getId());
//                    }
//                }
//            }


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

package com.trs.gov.kpi.service.impl.wangkang;

import com.trs.gov.kpi.constant.Status;
import com.trs.gov.kpi.constant.Types;
import com.trs.gov.kpi.constant.WkIssueTableField;
import com.trs.gov.kpi.dao.*;
import com.trs.gov.kpi.entity.dao.QueryFilter;
import com.trs.gov.kpi.entity.requestdata.PageDataRequestParam;
import com.trs.gov.kpi.entity.responsedata.*;
import com.trs.gov.kpi.entity.wangkang.*;
import com.trs.gov.kpi.service.helper.QueryFilterHelper;
import com.trs.gov.kpi.service.wangkang.WkOneSiteDetailService;
import com.trs.gov.kpi.utils.PageInfoDeal;
import com.trs.gov.kpi.utils.StringUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by li.hao on 2017/7/12.
 */
@Service
class WkOneSiteDetailServiceImpl implements WkOneSiteDetailService {

    @Resource
    WkSiteDetailMapper wkSiteDetailMapper;

    @Resource
    WkIssueCountMapper wkIssueCountMapper;

    @Resource
    WkIssueMapper wkIssueMapper;

    @Resource
    WkLinkTypeMapper wkLinkTypeMapper;

    @Resource
    WkSiteManagementMapper wkSiteManagementMapper;

    @Resource
    private WkCheckTimeMapper wkCheckTimeMapper;

    @Resource
    private CommonMapper commonMapper;

    @Override
    public WkLinkTypeResponse getOneSiteLinkTypeBySiteId(Integer siteId) {
        WkLinkType wkLinkType = wkLinkTypeMapper.getOneSiteLinkTypeBySiteId(siteId);
        WkLinkTypeResponse wkLinkTypeResponse = new WkLinkTypeResponse();
        if (wkLinkType != null){
            wkLinkTypeResponse.setAllLink(wkLinkType.getAllLink());
            wkLinkTypeResponse.setWebLink(wkLinkType.getWebLink());
            wkLinkTypeResponse.setImageLink(wkLinkType.getImageLink());
            wkLinkTypeResponse.setVideoLink(wkLinkType.getVideoLink());
            wkLinkTypeResponse.setEnclosuLink(wkLinkType.getEnclosuLink());
        }
        return wkLinkTypeResponse;
    }

    @Override
    public WkOneSiteScoreResponse getOneSiteScoreBySiteId(Integer siteId) {
        WkScore wkScore = wkSiteDetailMapper.getOneSiteScoreBySiteId(siteId);
        WkOneSiteScoreResponse wkOneSiteScore = new WkOneSiteScoreResponse();
        if(wkScore != null){
            wkOneSiteScore.setTotal(wkScore.getTotal());
            wkOneSiteScore.setContentError(wkScore.getContentError());
            wkOneSiteScore.setInvalidLink(wkScore.getInvalidLink());
            wkOneSiteScore.setOverSpeed(wkScore.getOverSpeed());
            wkOneSiteScore.setUpdateContent(wkScore.getUpdateContent());
        }
        return wkOneSiteScore;
    }
//
//    @Override
//    public List<WkOneSiteScoreResponse> getOneSiteScoreListBySiteId(Integer siteId) {
//        List<WkScore> wkScoreList = wkSiteDetailMapper.getOneSiteScoreListBySiteId(siteId);
//        List<WkOneSiteScoreResponse> wkOneSiteScoreList = new ArrayList<>();
//
//        if(!wkScoreList.isEmpty()){
//            for (WkScore wkScore: wkScoreList) {
//            WkOneSiteScoreResponse wkOneSiteScore = new WkOneSiteScoreResponse();
//                wkOneSiteScore.setCheckTime(wkScore.getCheckTime());
//                wkOneSiteScore.setTotal(wkScore.getTotal());
//                wkOneSiteScore.setContentError(wkScore.getContentError());
//                wkOneSiteScore.setInvalidLink(wkScore.getInvalidLink());
//                wkOneSiteScore.setOverSpeed(wkScore.getOverSpeed());
//                wkOneSiteScore.setUpdateContent(wkScore.getUpdateContent());
//
//                wkOneSiteScoreList.add(wkOneSiteScore);
//            }
//        }
//        return wkOneSiteScoreList;
//    }

    /*---链接可用性---*/
    @Override
    public WkStatsCountResponse getInvalidlinkStatsBySiteId(Integer siteId) {
        Integer typeId = Types.WkSiteCheckType.INVALID_LINK.value;

        WkIssueCount wkIssueCount = wkIssueCountMapper.getlinkAndContentStatsBySiteId(siteId,typeId);
        WkStatsCountResponse wkStatsCountResponse = new WkStatsCountResponse();
        if(wkIssueCount != null){
            wkStatsCountResponse.setUnhandleIssue(wkIssueCount.getUnResolved());
            wkStatsCountResponse.setHandleIssue(wkIssueCount.getIsResolved());
        }
        return wkStatsCountResponse;
    }

    @Override
    public List<WkStatsCountResponse> getInvalidlinkHistoryStatsBySiteId(Integer siteId) {
        Integer typeId = Types.WkSiteCheckType.INVALID_LINK.value;

        List<WkIssueCount> wkIssueCountList = wkIssueCountMapper.getlinkAndContentHistoryStatsBySiteId(siteId, typeId);
        List<WkStatsCountResponse> wkStatsCountResponseList = new ArrayList<>();

        if(!wkIssueCountList.isEmpty()){
            for (WkIssueCount wkIssueCount : wkIssueCountList) {
                WkStatsCountResponse wkStatsCountResponse = new WkStatsCountResponse();
                wkStatsCountResponse.setHandleIssue(wkIssueCount.getIsResolved());
                wkStatsCountResponse.setUnhandleIssue(wkIssueCount.getUnResolved());
                wkStatsCountResponse.setCheckTime(wkIssueCount.getCheckTime());

                wkStatsCountResponseList.add(wkStatsCountResponse);
            }
        }
        return wkStatsCountResponseList;
    }

    @Override
    public WkLinkIndexPageStatus getSiteIndexpageStatusBySiteId(Integer siteId) {
        Integer isDel = Status.Delete.UN_DELETE.value;
        SiteManagement siteManagement = wkSiteManagementMapper.getSiteIndexpageStatusBySiteId(siteId, isDel);
        WkLinkIndexPageStatus wkLinkIndexPageStatus = new WkLinkIndexPageStatus();
        if(siteManagement != null){
            wkLinkIndexPageStatus.setCheckTime(siteManagement.getCheckTime());
            wkLinkIndexPageStatus.setSiteIndexUrl(siteManagement.getSiteIndexUrl());
        }
        return wkLinkIndexPageStatus;
    }

    @Override
    public ApiPageData getInvalidLinkUnhandledList(PageDataRequestParam param) {
        if(!StringUtil.isEmpty(param.getSearchText())){
            param.setSearchText(StringUtil.escape(param.getSearchText()));
        }

        Integer siteId = param.getSiteId();
        final Integer maxCheckId = wkCheckTimeMapper.getMaxCheckId(siteId);
        if (maxCheckId == null) {
            Pager pager = PageInfoDeal.buildResponsePager(param.getPageIndex(), param.getPageSize(), 0);
            return new ApiPageData(pager, Collections.emptyList());
        }

        QueryFilter queryFilter = QueryFilterHelper.toWkIssueFilter(param, Types.WkSiteCheckType.INVALID_LINK);
        queryFilter.addCond(WkIssueTableField.TYPE_ID, Types.WkSiteCheckType.INVALID_LINK.value);
        queryFilter.addCond(WkIssueTableField.IS_RESOLVED, Status.Resolve.UN_RESOLVED.value);
        queryFilter.addCond(WkIssueTableField.CHECK_ID, maxCheckId);
        int itemCount = commonMapper.count(queryFilter);

        Pager pager = PageInfoDeal.buildResponsePager(param.getPageIndex(), param.getPageSize(), itemCount);
        queryFilter.setPager(pager);
        List<WkIssue> wkIssueList = wkIssueMapper.select(queryFilter);

        return new ApiPageData(pager, toWkLinkIssueResponseByWkIssueList(wkIssueList));
    }

    private List<WkIssueResponse> toWkLinkIssueResponseByWkIssueList(List<WkIssue> wkIssueList){
        List<WkIssueResponse> wkIssueResponseList = new ArrayList<>();

        for (WkIssue wkIssue: wkIssueList) {
            WkIssueResponse wkIssueResponse = new WkIssueResponse();

            wkIssueResponse.setId(wkIssue.getId());
            wkIssueResponse.setChnlName(wkIssue.getChnlName());
            wkIssueResponse.setSubTypeId(wkIssue.getSubTypeId());
            wkIssueResponse.setSubTypeName(Types.WkLinkIssueType.valueOf(wkIssue.getSubTypeId()).getName());
            wkIssueResponse.setUrl(wkIssue.getUrl());
            wkIssueResponse.setParentUrl(wkIssue.getParentUrl());
            wkIssueResponse.setLocationUrl(wkIssue.getLocationUrl());

            wkIssueResponseList.add(wkIssueResponse);
        }
        return wkIssueResponseList;
    }

    /*---内容检测---*/
    @Override
    public WkStatsCountResponse getContentErorStatsBySiteId(Integer siteId) {
        Integer typeId = Types.WkSiteCheckType.CONTENT_ERROR.value;

        WkIssueCount wkIssueCount = wkIssueCountMapper.getlinkAndContentStatsBySiteId(siteId, typeId);
        WkStatsCountResponse wkStatsCountResponse = new WkStatsCountResponse();
        if(wkIssueCount != null){
            wkStatsCountResponse.setUnhandleIssue(wkIssueCount.getUnResolved());
            wkStatsCountResponse.setHandleIssue(wkIssueCount.getIsResolved());
        }
        return wkStatsCountResponse;
    }

    @Override
    public List<WkStatsCountResponse> getContentErorHistoryStatsBySiteId(Integer siteId) {
        Integer typeId = Types.WkSiteCheckType.CONTENT_ERROR.value;

        List<WkIssueCount> wkIssueCountList = wkIssueCountMapper.getlinkAndContentHistoryStatsBySiteId(siteId, typeId);
        List<WkStatsCountResponse> wkStatsCountResponseList = new ArrayList<>();

        if(!wkIssueCountList.isEmpty()){
            for (WkIssueCount wkIssueCount : wkIssueCountList) {
                WkStatsCountResponse wkStatsCountResponse = new WkStatsCountResponse();
                wkStatsCountResponse.setHandleIssue(wkIssueCount.getIsResolved());
                wkStatsCountResponse.setUnhandleIssue(wkIssueCount.getUnResolved());
                wkStatsCountResponse.setCheckTime(wkIssueCount.getCheckTime());

                wkStatsCountResponseList.add(wkStatsCountResponse);
            }
        }
        return wkStatsCountResponseList;
    }

    @Override
    public ApiPageData getContentErrorUnhandledList(PageDataRequestParam param) {
        if(!StringUtil.isEmpty(param.getSearchText())){
            param.setSearchText(StringUtil.escape(param.getSearchText()));
        }

        Integer siteId = param.getSiteId();
        final Integer maxCheckId = wkCheckTimeMapper.getMaxCheckId(siteId);
        if (maxCheckId == null) {
            Pager pager = PageInfoDeal.buildResponsePager(param.getPageIndex(), param.getPageSize(), 0);
            return new ApiPageData(pager, Collections.emptyList());
        }

        QueryFilter queryFilter = QueryFilterHelper.toWkIssueFilter(param, Types.WkSiteCheckType.CONTENT_ERROR);
        queryFilter.addCond(WkIssueTableField.TYPE_ID, Types.WkSiteCheckType.CONTENT_ERROR.value);
        queryFilter.addCond(WkIssueTableField.IS_RESOLVED, Status.Resolve.UN_RESOLVED.value);
        queryFilter.addCond(WkIssueTableField.CHECK_ID, maxCheckId);
        int itemCount = commonMapper.count(queryFilter);

        Pager pager = PageInfoDeal.buildResponsePager(param.getPageIndex(), param.getPageSize(), itemCount);
        queryFilter.setPager(pager);
        List<WkIssue> wkIssueList = wkIssueMapper.select(queryFilter);

        return new ApiPageData(pager, toWkContentIssueResponseByWkIssueList(wkIssueList));
    }

    private List<WkIssueResponse> toWkContentIssueResponseByWkIssueList(List<WkIssue> wkIssueList){
        List<WkIssueResponse> wkIssueResponseList = new ArrayList<>();

        for (WkIssue wkIssue: wkIssueList) {
            WkIssueResponse wkIssueResponse = new WkIssueResponse();

            wkIssueResponse.setId(wkIssue.getId());
            wkIssueResponse.setChnlName(wkIssue.getChnlName());
            wkIssueResponse.setSubTypeId(wkIssue.getSubTypeId());

            // TODO 需要修改一下界面显示的名称和错误信息
            wkIssueResponse.setSubTypeName(Types.InfoErrorIssueType.valueOf(wkIssue.getSubTypeId()).getDisplayName());
            wkIssueResponse.setErrorInfo(wkIssue.getDetailInfo());

            wkIssueResponse.setUrl(wkIssue.getUrl());
            wkIssueResponse.setParentUrl(wkIssue.getParentUrl());
            wkIssueResponse.setLocationUrl(wkIssue.getLocationUrl());

            wkIssueResponseList.add(wkIssueResponse);
        }
        return wkIssueResponseList;
    }

}

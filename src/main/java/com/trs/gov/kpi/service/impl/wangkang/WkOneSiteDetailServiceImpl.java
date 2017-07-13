package com.trs.gov.kpi.service.impl.wangkang;

import com.trs.gov.kpi.constant.IssueTableField;
import com.trs.gov.kpi.constant.Status;
import com.trs.gov.kpi.constant.Types;
import com.trs.gov.kpi.dao.WkIssueMapper;
import com.trs.gov.kpi.dao.WkSiteDetailMapper;
import com.trs.gov.kpi.dao.WkStatsCountMapper;
import com.trs.gov.kpi.entity.dao.QueryFilter;
import com.trs.gov.kpi.entity.requestdata.PageDataRequestParam;
import com.trs.gov.kpi.entity.responsedata.*;
import com.trs.gov.kpi.entity.wangkang.WkIssue;
import com.trs.gov.kpi.entity.wangkang.WkSocre;
import com.trs.gov.kpi.entity.wangkang.WkStatsCount;
import com.trs.gov.kpi.service.helper.QueryFilterHelper;
import com.trs.gov.kpi.service.wangkang.WkOneSiteDetailService;
import com.trs.gov.kpi.utils.PageInfoDeal;
import com.trs.gov.kpi.utils.StringUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by li.hao on 2017/7/12.
 */
@Service
class WkOneSiteDetailServiceImpl implements WkOneSiteDetailService {

    @Resource
    WkSiteDetailMapper wkSiteDetailMapper;

    @Resource
    WkStatsCountMapper wkStatsCountMapper;

    @Resource
    WkIssueMapper wkIssueMapper;

    @Override
    public WkOneSiteScoreResponse getOneSiteScoreBySiteId(Integer siteId) {
        WkSocre wkScore = wkSiteDetailMapper.getOneSiteScoreBySiteId(siteId);
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

    @Override
    public List<WkOneSiteScoreResponse> getOneSiteScoreListBySiteId(Integer siteId) {
        List<WkSocre> wkSocreList = wkSiteDetailMapper.getOneSiteScoreListBySiteId(siteId);
        List<WkOneSiteScoreResponse> wkOneSiteScoreList = new ArrayList<>();

        if(wkSocreList.size() != 0){
            for (WkSocre wkScore:wkSocreList) {
            WkOneSiteScoreResponse wkOneSiteScore = new WkOneSiteScoreResponse();
                wkOneSiteScore.setCheckTime(wkScore.getCheckTime());
                wkOneSiteScore.setTotal(wkScore.getTotal());
                wkOneSiteScore.setContentError(wkScore.getContentError());
                wkOneSiteScore.setInvalidLink(wkScore.getInvalidLink());
                wkOneSiteScore.setOverSpeed(wkScore.getOverSpeed());
                wkOneSiteScore.setUpdateContent(wkScore.getUpdateContent());

                wkOneSiteScoreList.add(wkOneSiteScore);
            }
        }
        return wkOneSiteScoreList;
    }

    /*---链接可用性---*/
    @Override
    public WkStatsCountResponse getInvalidlinkStatsBySiteId(Integer siteId) {

        PageDataRequestParam param = new PageDataRequestParam();
        param.setSiteId(siteId);
        QueryFilter queryFilter = QueryFilterHelper.toFilter(param);
        queryFilter.addCond(IssueTableField.TYPE_ID, Types.WkSiteCheckType.INVALID_LINK.value);

        WkStatsCount wkStatsCount = wkStatsCountMapper.getInvalidlinkStatsBySiteId(queryFilter);
        WkStatsCountResponse wkStatsCountResponse = new WkStatsCountResponse();
        if(wkStatsCount != null){
            wkStatsCountResponse.setUnhandleIssue(wkStatsCount.getUnResolved());
            wkStatsCountResponse.setHandleIssue(wkStatsCount.getIsResolved());
        }
        return wkStatsCountResponse;
    }

    @Override
    public List<WkStatsCountResponse> getInvalidlinkHistoryStatsBySiteId(Integer siteId) {
        PageDataRequestParam param = new PageDataRequestParam();
        param.setSiteId(siteId);
        QueryFilter queryFilter = QueryFilterHelper.toFilter(param);
        queryFilter.addCond(IssueTableField.TYPE_ID, Types.WkSiteCheckType.INVALID_LINK.value);

        List<WkStatsCount> wkStatsCountList = wkStatsCountMapper.getInvalidlinkHistoryStatsBySiteId(queryFilter);
        List<WkStatsCountResponse> wkStatsCountResponseList = new ArrayList<>();

        if(wkStatsCountList.size() != 0){
            for (WkStatsCount wkStatsCount: wkStatsCountList) {
                WkStatsCountResponse wkStatsCountResponse = new WkStatsCountResponse();
                wkStatsCountResponse.setHandleIssue(wkStatsCount.getIsResolved());
                wkStatsCountResponse.setUnhandleIssue(wkStatsCount.getUnResolved());
                wkStatsCountResponse.setCheckTime(wkStatsCount.getCheckTime());

                wkStatsCountResponseList.add(wkStatsCountResponse);
            }
        }
        return wkStatsCountResponseList;
    }

    @Override
    public ApiPageData getInvalidLinkUnhandledList(PageDataRequestParam param) {
        if(!StringUtil.isEmpty(param.getSearchText())){
            param.setSearchText(StringUtil.escape(param.getSearchText()));
        }

        QueryFilter queryFilter = QueryFilterHelper.toFilter(param);
        queryFilter.addCond(IssueTableField.TYPE_ID, Types.WkSiteCheckType.INVALID_LINK.value);
        queryFilter.addCond(IssueTableField.IS_RESOLVED, Status.Resolve.UN_RESOLVED.value);
        int itemCount = wkIssueMapper.getLinkAndContentUnhandledCount(queryFilter);
        Pager pager = PageInfoDeal.buildResponsePager(param.getPageIndex(), param.getPageSize(), itemCount);
        queryFilter.setPager(pager);

        List<WkIssue> wkIssueList = wkIssueMapper.getLinkAndContentUnhandledList(queryFilter);

        return new ApiPageData(pager, getWkLinkIssueResponseByWkIssueList(wkIssueList));
    }

    private List<WkIssueResponse> getWkLinkIssueResponseByWkIssueList(List<WkIssue> wkIssueList){
        List<WkIssueResponse> wkIssueResponseList = new ArrayList<>();

        for (WkIssue wkIssue: wkIssueList) {
            WkIssueResponse wkIssueResponse = new WkIssueResponse();

            wkIssueResponse.setId(wkIssue.getId());
            wkIssueResponse.setChnlName(wkIssue.getChnlName());
            wkIssueResponse.setSubTypeId(wkIssue.getSubTypeId());
            wkIssueResponse.setSubTypeName(Types.WkLinkIssueType.valueOf(wkIssue.getSubTypeId()).name());
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
        PageDataRequestParam param = new PageDataRequestParam();
        param.setSiteId(siteId);
        QueryFilter queryFilter = QueryFilterHelper.toFilter(param);
        queryFilter.addCond(IssueTableField.TYPE_ID, Types.WkSiteCheckType.CONTENT_ERROR.value);

        WkStatsCount wkStatsCount = wkStatsCountMapper.getInvalidlinkStatsBySiteId(queryFilter);
        WkStatsCountResponse wkStatsCountResponse = new WkStatsCountResponse();
        if(wkStatsCount != null){
            wkStatsCountResponse.setUnhandleIssue(wkStatsCount.getUnResolved());
            wkStatsCountResponse.setHandleIssue(wkStatsCount.getIsResolved());
        }
        return wkStatsCountResponse;
    }

    @Override
    public List<WkStatsCountResponse> getContentErorHistoryStatsBySiteId(Integer siteId) {
        PageDataRequestParam param = new PageDataRequestParam();
        param.setSiteId(siteId);
        QueryFilter queryFilter = QueryFilterHelper.toFilter(param);
        queryFilter.addCond(IssueTableField.TYPE_ID, Types.WkSiteCheckType.CONTENT_ERROR.value);

        List<WkStatsCount> wkStatsCountList = wkStatsCountMapper.getInvalidlinkHistoryStatsBySiteId(queryFilter);
        List<WkStatsCountResponse> wkStatsCountResponseList = new ArrayList<>();

        if(wkStatsCountList.size() != 0 ){
            for (WkStatsCount wkStatsCount: wkStatsCountList) {
                WkStatsCountResponse wkStatsCountResponse = new WkStatsCountResponse();
                wkStatsCountResponse.setHandleIssue(wkStatsCount.getIsResolved());
                wkStatsCountResponse.setUnhandleIssue(wkStatsCount.getUnResolved());
                wkStatsCountResponse.setCheckTime(wkStatsCount.getCheckTime());

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

        QueryFilter queryFilter = QueryFilterHelper.toFilter(param);
        queryFilter.addCond(IssueTableField.TYPE_ID, Types.WkSiteCheckType.CONTENT_ERROR.value);
        queryFilter.addCond(IssueTableField.IS_RESOLVED, Status.Resolve.UN_RESOLVED.value);
        int itemCount = wkIssueMapper.getLinkAndContentUnhandledCount(queryFilter);
        Pager pager = PageInfoDeal.buildResponsePager(param.getPageIndex(), param.getPageSize(), itemCount);
        queryFilter.setPager(pager);

        List<WkIssue> wkIssueList = wkIssueMapper.getLinkAndContentUnhandledList(queryFilter);

        return new ApiPageData(pager, getWkContentIssueResponseByWkIssueList(wkIssueList));
    }

    private List<WkIssueResponse> getWkContentIssueResponseByWkIssueList(List<WkIssue> wkIssueList){
        List<WkIssueResponse> wkIssueResponseList = new ArrayList<>();

        for (WkIssue wkIssue: wkIssueList) {
            WkIssueResponse wkIssueResponse = new WkIssueResponse();

            wkIssueResponse.setId(wkIssue.getId());
            wkIssueResponse.setChnlName(wkIssue.getChnlName());
            wkIssueResponse.setSubTypeId(wkIssue.getSubTypeId());
            wkIssueResponse.setSubTypeName(Types.WkLinkIssueType.valueOf(wkIssue.getSubTypeId()).name());
            wkIssueResponse.setErrorInfo(wkIssue.getDetailInfo());
            wkIssueResponse.setUrl(wkIssue.getUrl());
            wkIssueResponse.setParentUrl(wkIssue.getParentUrl());
            wkIssueResponse.setLocationUrl(wkIssue.getLocationUrl());

            wkIssueResponseList.add(wkIssueResponse);
        }
        return wkIssueResponseList;
    }

}

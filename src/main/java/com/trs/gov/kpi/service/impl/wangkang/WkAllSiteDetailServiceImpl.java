package com.trs.gov.kpi.service.impl.wangkang;

import com.trs.gov.kpi.dao.*;
import com.trs.gov.kpi.entity.dao.QueryFilter;
import com.trs.gov.kpi.entity.requestdata.WkAllSiteDetailRequest;
import com.trs.gov.kpi.entity.responsedata.ApiPageData;
import com.trs.gov.kpi.entity.responsedata.Pager;
import com.trs.gov.kpi.entity.responsedata.WkAllSiteScoreResponsed;
import com.trs.gov.kpi.entity.responsedata.WkIndexLinkIssueResponse;
import com.trs.gov.kpi.entity.wangkang.SiteManagement;
import com.trs.gov.kpi.entity.wangkang.WkScore;
import com.trs.gov.kpi.entity.wangkang.WkSiteIndexStats;
import com.trs.gov.kpi.service.helper.QueryFilterHelper;
import com.trs.gov.kpi.service.wangkang.WkAllSiteDetailService;
import com.trs.gov.kpi.service.wangkang.WkSiteManagementService;
import com.trs.gov.kpi.utils.PageInfoDeal;
import com.trs.gov.kpi.utils.StringUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by li.hao on 2017/7/10.
 */
@Service
public class WkAllSiteDetailServiceImpl implements WkAllSiteDetailService {

    @Resource
    WkSiteDetailMapper wkSiteDetailMapper;

    @Resource
    WkSiteManagementService wkSiteManagementService;

    @Resource
    WkIssueMapper wkIssueMapper;

    @Resource
    WkSiteManagementMapper wkSiteManagementMapper;

    @Resource
    private WkAllStatsMapper wkAllStatsMapper;

    @Resource
    private CommonMapper commonMapper;

    @Resource
    private WkSiteIndexStatsMapper wkSiteIndexStatsMapper;

    @Override
    public List<WkAllSiteScoreResponsed> queryAllSiteScore() {
        List<WkScore> wkScoreList = wkSiteDetailMapper.selectAllSiteScore();

        List<WkAllSiteScoreResponsed> wkAllSiteScoreList = new ArrayList<>();
        for (WkScore wkScore : wkScoreList) {
            WkAllSiteScoreResponsed wkAllSiteScore = new WkAllSiteScoreResponsed();
            SiteManagement siteManagement = wkSiteManagementService.getSiteManagementBySiteId(wkScore.getSiteId());

            wkAllSiteScore.setSiteId(wkScore.getSiteId());
            wkAllSiteScore.setSiteName(wkSiteManagementService.getSiteNameBySiteId(wkScore.getSiteId()));
            wkAllSiteScore.setSiteIndexUrl(siteManagement.getSiteIndexUrl());
            wkAllSiteScore.setDeptLatLng(siteManagement.getDeptLatLng());
            wkAllSiteScore.setDeptAddress(siteManagement.getDeptAddress());
            wkAllSiteScore.setAutoCheckType(siteManagement.getAutoCheckType());
            wkAllSiteScore.setCheckTime(siteManagement.getCheckTime());
            wkAllSiteScore.setCheckStatus(siteManagement.getCheckStatus());
            wkAllSiteScore.setTotal(wkScore.getTotal());

            wkAllSiteScoreList.add(wkAllSiteScore);
        }
        return wkAllSiteScoreList;
    }

    @Override
    public ApiPageData queryAllWkSiteAvailable(WkAllSiteDetailRequest wkAllSiteDetail) {
        if (!StringUtil.isEmpty(wkAllSiteDetail.getSearchText())) {
            wkAllSiteDetail.setSearchText(StringUtil.escape(wkAllSiteDetail.getSearchText()));
        }

        QueryFilter filter = QueryFilterHelper.toStatsWkFilter(wkAllSiteDetail);
        int itemCount = commonMapper.count(filter);
        Pager pager = PageInfoDeal.buildResponsePager(wkAllSiteDetail.getPageIndex(), wkAllSiteDetail.getPageSize(), itemCount);
        filter.setPager(pager);

        List<WkSiteIndexStats> wkSiteIndexStatsesList = wkSiteIndexStatsMapper.select(filter);

        return new ApiPageData(pager, getWkIndexLinkIssueResponseByIssue(wkSiteIndexStatsesList));

}

    private List<WkIndexLinkIssueResponse> getWkIndexLinkIssueResponseByIssue(List<WkSiteIndexStats> wkSiteIndexStatsesList){
        List<WkIndexLinkIssueResponse> wkIndexLinkIssueList = new ArrayList<>();

        if(!wkSiteIndexStatsesList.isEmpty()){
            for (WkSiteIndexStats wkSiteIndexStats: wkSiteIndexStatsesList) {
                WkIndexLinkIssueResponse wkIndexLinkIssue = new WkIndexLinkIssueResponse();

                wkIndexLinkIssue.setSiteId(wkSiteIndexStats.getSiteId());
                wkIndexLinkIssue.setSiteName(wkSiteIndexStats.getSiteName());
                wkIndexLinkIssue.setInvalidLinkCount(wkSiteIndexStats.getInvalidLink());
                wkIndexLinkIssue.setContentErrorCount(wkSiteIndexStats.getContentError());
                wkIndexLinkIssue.setOverSpeedCount(wkSiteIndexStats.getOverSpeed());
                wkIndexLinkIssue.setUpdateContentCount(wkSiteIndexStats.getUpdateContent());

                wkIndexLinkIssueList.add(wkIndexLinkIssue);
            }
            return wkIndexLinkIssueList;
        }else{
            return Collections.EMPTY_LIST;
        }
    }
}
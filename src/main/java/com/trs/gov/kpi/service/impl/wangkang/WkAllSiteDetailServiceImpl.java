package com.trs.gov.kpi.service.impl.wangkang;

import com.trs.gov.kpi.constant.Status;
import com.trs.gov.kpi.constant.WkSiteIndexStatsTableField;
import com.trs.gov.kpi.constant.WkSiteTableField;
import com.trs.gov.kpi.dao.*;
import com.trs.gov.kpi.entity.dao.QueryFilter;
import com.trs.gov.kpi.entity.dao.Table;
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
    private WkSiteManagementMapper wkSiteManagementMapper;

    @Resource
    private CommonMapper commonMapper;

    @Resource
    private WkSiteIndexStatsMapper wkSiteIndexStatsMapper;

    @Override
    public List<WkAllSiteScoreResponsed> queryAllSiteScore() {
        List<WkScore> wkScoreList = wkSiteDetailMapper.selectAllSiteScore();

        List<WkAllSiteScoreResponsed> wkAllSiteScoreList = new ArrayList<>();
        if (!wkScoreList.isEmpty()){
            for (WkScore wkScore : wkScoreList) {
                WkAllSiteScoreResponsed wkAllSiteScore = new WkAllSiteScoreResponsed();

                Integer isDel = Status.Delete.UN_DELETE.value;
                SiteManagement siteManagement = wkSiteManagementService.getSiteManagementBySiteId(wkScore.getSiteId(), isDel);

                if(siteManagement != null){
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
            }
            return wkAllSiteScoreList;
        }else{
            return Collections.emptyList();
        }
    }

    @Override
    public ApiPageData queryAllWkSiteAvailable(WkAllSiteDetailRequest wkAllSiteDetail) {
        if (!StringUtil.isEmpty(wkAllSiteDetail.getSearchText())) {
            wkAllSiteDetail.setSearchText(StringUtil.escape(wkAllSiteDetail.getSearchText()));
        }
        QueryFilter filter = QueryFilterHelper.toStatsWkFilter(wkAllSiteDetail);

        QueryFilter unDelFilter = new QueryFilter(Table.WK_SITEMANAGEMENT);
        unDelFilter.addCond(WkSiteTableField.IS_DEL, Status.Delete.UN_DELETE.value);
        List<SiteManagement> siteManagementList = wkSiteManagementMapper.selectAllSiteList(unDelFilter);
        List<Integer> siteIds = new ArrayList<>();
        for (SiteManagement site : siteManagementList) {
            siteIds.add(site.getSiteId());
        }

        filter.addCond(WkSiteIndexStatsTableField.SITE_ID, siteIds);

        int siteIndexStatsCount = commonMapper.count(filter);
        Pager pager = PageInfoDeal.buildResponsePager(wkAllSiteDetail.getPageIndex(), wkAllSiteDetail.getPageSize(), siteIndexStatsCount);
        filter.setPager(pager);

        List<WkSiteIndexStats> wkSiteIndexStatsesListTo = wkSiteIndexStatsMapper.select(filter);

        return new ApiPageData(pager, getWkIndexLinkIssueResponseByIssue(wkSiteIndexStatsesListTo));
    }

    private List<WkIndexLinkIssueResponse> getWkIndexLinkIssueResponseByIssue(List<WkSiteIndexStats> wkSiteIndexStatsesListTo){
        List<WkIndexLinkIssueResponse> wkIndexLinkIssueList = new ArrayList<>();

        if(!wkSiteIndexStatsesListTo.isEmpty()){
            for (WkSiteIndexStats wkSiteIndexStats: wkSiteIndexStatsesListTo) {

                Integer isDel = Status.Delete.UN_DELETE.value;
                SiteManagement siteManagement = wkSiteManagementService.getSiteManagementBySiteId(wkSiteIndexStats.getSiteId(), isDel);

                if (siteManagement != null) {
                    WkIndexLinkIssueResponse wkIndexLinkIssue = new WkIndexLinkIssueResponse();
                    wkIndexLinkIssue.setSiteId(wkSiteIndexStats.getSiteId());
                    wkIndexLinkIssue.setSiteName(wkSiteIndexStats.getSiteName());
                    wkIndexLinkIssue.setInvalidLinkCount(wkSiteIndexStats.getInvalidLink());
                    wkIndexLinkIssue.setContentErrorCount(wkSiteIndexStats.getContentError());
                    wkIndexLinkIssue.setOverSpeedCount(wkSiteIndexStats.getOverSpeed());
                    wkIndexLinkIssue.setUpdateContentCount(wkSiteIndexStats.getUpdateContent());

                    wkIndexLinkIssueList.add(wkIndexLinkIssue);
                }
            }
            return wkIndexLinkIssueList;
        }else{
            return Collections.emptyList();
        }
    }
}
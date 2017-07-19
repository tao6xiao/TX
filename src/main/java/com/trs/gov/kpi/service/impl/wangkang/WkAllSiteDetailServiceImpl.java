package com.trs.gov.kpi.service.impl.wangkang;

import com.trs.gov.kpi.constant.Constants;
import com.trs.gov.kpi.constant.Status;
import com.trs.gov.kpi.constant.WkSiteTableField;
import com.trs.gov.kpi.dao.WkAllStatsMapper;
import com.trs.gov.kpi.dao.WkIssueMapper;
import com.trs.gov.kpi.dao.WkSiteDetailMapper;
import com.trs.gov.kpi.dao.WkSiteManagementMapper;
import com.trs.gov.kpi.entity.dao.QueryFilter;
import com.trs.gov.kpi.entity.dao.Table;
import com.trs.gov.kpi.entity.requestdata.WkAllSiteDetailRequest;
import com.trs.gov.kpi.entity.responsedata.ApiPageData;
import com.trs.gov.kpi.entity.responsedata.Pager;
import com.trs.gov.kpi.entity.responsedata.WkAllSiteScoreResponsed;
import com.trs.gov.kpi.entity.responsedata.WkIndexLinkIssueResponse;
import com.trs.gov.kpi.entity.wangkang.SiteManagement;
import com.trs.gov.kpi.entity.wangkang.WkAllStats;
import com.trs.gov.kpi.entity.wangkang.WkIssue;
import com.trs.gov.kpi.entity.wangkang.WkScore;
import com.trs.gov.kpi.service.wangkang.WkAllSiteDetailService;
import com.trs.gov.kpi.service.wangkang.WkSiteManagementService;
import com.trs.gov.kpi.utils.PageInfoDeal;
import com.trs.gov.kpi.utils.StringUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
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

        QueryFilter filter = new QueryFilter(Table.WK_SITEMANAGEMENT);
        filter.addCond(WkSiteTableField.IS_DEL, Status.Delete.UN_DELETE.value);
        int itemCount = wkSiteManagementMapper.selectAllSiteCount(filter);
        Pager pager = PageInfoDeal.buildResponsePager(wkAllSiteDetail.getPageIndex(), wkAllSiteDetail.getPageSize(), itemCount);
        filter.setPager(pager);

        Integer isResolved = Status.Resolve.UN_RESOLVED.value;
        Integer isDel = Status.Delete.UN_DELETE.value;
        List<WkIssue> wkIssueList = wkIssueMapper.selectIssueSiteList(isResolved, isDel);

        List<WkIndexLinkIssueResponse> wkIndexLinkIssueList = new ArrayList<>();

        for (WkIssue wkIssue : wkIssueList) {
            WkIndexLinkIssueResponse wkIndexLinkIssue = new WkIndexLinkIssueResponse();

            String siteName = wkSiteManagementService.getSiteNameBySiteId(wkIssue.getSiteId());

//            QueryFilter filterTo = QueryFilterHelper.toWkFilter(wkAllSiteDetail);
            QueryFilter filterTo = new QueryFilter(Table.WK_ALL_STATS);
            filterTo.addCond(Constants.DB_FIELD_SITE_ID, wkIssue.getSiteId());
            filterTo.addCond(Constants.DB_FIELD_CHECK_ID, wkIssue.getCheckId());
            WkAllStats wkAllStats = wkAllStatsMapper.selectOnce(filterTo);

            wkIndexLinkIssue.setSiteId(wkIssue.getSiteId());
            wkIndexLinkIssue.setSiteName(siteName);
            wkIndexLinkIssue.setInvalidLinkCount(wkAllStats.getInvalidLink());
            wkIndexLinkIssue.setContentErrorCount(wkAllStats.getErrorInfo());
            wkIndexLinkIssue.setOverSpeedCount(wkAllStats.getAvgSpeed());
            wkIndexLinkIssue.setUpdateContentCount(wkAllStats.getUpdateContent());

            wkIndexLinkIssueList.add(wkIndexLinkIssue);

        }
        return new ApiPageData(pager, wkIndexLinkIssueList);
    }
}

//    private List<WkIndexLinkIssueResponse> getWkIndexLinkIssueResponseByIssue(List<WkIssue> wkIssueList){
//        List<WkIndexLinkIssueResponse> wkIndexLinkIssueList = new ArrayList<>();
//
//        for (WkIssue wkIssue: wkIssueList) {
//            WkIndexLinkIssueResponse wkIndexLinkIssue = new WkIndexLinkIssueResponse();
//
//            String siteName = wkSiteManagementService.getSiteNameBySiteId(wkIssue.getSiteId());
//
//            QueryFilter filter = QueryFilterHelper.toWkFilter(wkAllSiteDetail);
//            filter.addCond(Constants.DB_FIELD_SITE_ID, wkIssue.getSiteId());
//            filter.addCond(Constants.DB_FIELD_CHECK_ID, wkIssue.getCheckId());
//            WkAllStats wkAllStats = wkAllStatsMapper.selectOnce(filter);
//
//            wkIndexLinkIssue.setSiteId(wkIssue.getSiteId());
//            wkIndexLinkIssue.setSiteName(siteName);
//            wkIndexLinkIssue.setInvalidLinkCount(wkAllStats.getInvalidLink());
//            wkIndexLinkIssue.setContentErrorCount(wkAllStats.getErrorInfo());
//            wkIndexLinkIssue.setOverSpeedCount(wkAllStats.getAvgSpeed());
//            wkIndexLinkIssue.setUpdateContentCount(wkAllStats.getUpdateContent());
//
//            wkIndexLinkIssueList.add(wkIndexLinkIssue);
//        }
//        return wkIndexLinkIssueList;
//    }
//}
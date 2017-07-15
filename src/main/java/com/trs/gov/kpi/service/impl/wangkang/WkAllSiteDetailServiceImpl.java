package com.trs.gov.kpi.service.impl.wangkang;

import com.trs.gov.kpi.constant.IssueTableField;
import com.trs.gov.kpi.constant.Status;
import com.trs.gov.kpi.constant.Types;
import com.trs.gov.kpi.dao.WkSiteDetailMapper;
import com.trs.gov.kpi.dao.WkIssueMapper;
import com.trs.gov.kpi.dao.WkRecordMapper;
import com.trs.gov.kpi.dao.WkSiteManagementMapper;
import com.trs.gov.kpi.entity.dao.QueryFilter;
import com.trs.gov.kpi.entity.requestdata.WkAllSiteDetailRequest;
import com.trs.gov.kpi.entity.responsedata.ApiPageData;
import com.trs.gov.kpi.entity.responsedata.Pager;
import com.trs.gov.kpi.entity.responsedata.WkAllSiteScoreResponsed;
import com.trs.gov.kpi.entity.responsedata.WkIndexLinkIssueResponse;
import com.trs.gov.kpi.entity.wangkang.SiteManagement;
import com.trs.gov.kpi.entity.wangkang.WkIssue;
import com.trs.gov.kpi.entity.wangkang.WkScore;
import com.trs.gov.kpi.service.helper.QueryFilterHelper;
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
    WkRecordMapper wkRecordMapper;

    @Resource
    WkSiteManagementMapper wkSiteManagementMapper;

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
        if(!StringUtil.isEmpty(wkAllSiteDetail.getSearchText())){
            wkAllSiteDetail.setSearchText(StringUtil.escape(wkAllSiteDetail.getSearchText()));
        }

        QueryFilter filter = QueryFilterHelper.toWkFilter(wkAllSiteDetail);
        filter.addCond(IssueTableField.IS_DEL, Status.Delete.UN_DELETE.value);
        int itemCount = wkSiteManagementMapper.selectAllSiteCount(filter);
        Pager pager = PageInfoDeal.buildResponsePager(wkAllSiteDetail.getPageIndex(), wkAllSiteDetail.getPageSize(), itemCount);
        filter.setPager(pager);

        Integer isResolved = Status.Resolve.UN_RESOLVED.value;
        Integer isDel = Status.Delete.UN_DELETE.value;
        List<WkIssue> wkIssueList = wkIssueMapper.selectIssueSiteList(isResolved, isDel);

        return new ApiPageData(pager, getWkIndexLinkIssueResponseByIssue(wkIssueList));
    }

    private List<WkIndexLinkIssueResponse> getWkIndexLinkIssueResponseByIssue(List<WkIssue> wkIssueList){
        List<WkIndexLinkIssueResponse> wkIndexLinkIssueList = new ArrayList<>();

        for (WkIssue wkIssue: wkIssueList) {
            WkIndexLinkIssueResponse wkIndexLinkIssue = new WkIndexLinkIssueResponse();

            String siteName = wkSiteManagementService.getSiteNameBySiteId(wkIssue.getSiteId());
            Integer invalidLinkCount = wkIssueMapper.getWkIssueCount(wkIssue.getSiteId(), wkIssue.getCheckId(), Types.WkSiteCheckType.INVALID_LINK.value);
            Integer contentErrorCount = wkIssueMapper.getWkIssueCount(wkIssue.getSiteId(), wkIssue.getCheckId(), Types.WkSiteCheckType.CONTENT_ERROR.value);
            Integer overSpeedCount = wkRecordMapper.getWkRecordAvgSpeed(wkIssue.getSiteId(), wkIssue.getCheckId());
            Integer updateContentCount = wkRecordMapper.getWkRecordUpdateContent(wkIssue.getSiteId(), wkIssue.getCheckId());

            wkIndexLinkIssue.setSiteId(wkIssue.getSiteId());
            wkIndexLinkIssue.setSiteName(siteName);
            wkIndexLinkIssue.setInvalidLinkCount(invalidLinkCount);
            wkIndexLinkIssue.setContentErrorCount(contentErrorCount);
            wkIndexLinkIssue.setOverSpeedCount(overSpeedCount);
            wkIndexLinkIssue.setUpdateContentCount(updateContentCount);

            wkIndexLinkIssueList.add(wkIndexLinkIssue);
        }
        return wkIndexLinkIssueList;
    }
}

package com.trs.gov.kpi.service.impl.wangkang;

import com.trs.gov.kpi.constant.*;
import com.trs.gov.kpi.dao.CommonMapper;
import com.trs.gov.kpi.dao.WkCheckTimeMapper;
import com.trs.gov.kpi.dao.WkSiteManagementMapper;
import com.trs.gov.kpi.entity.dao.DBUpdater;
import com.trs.gov.kpi.entity.dao.QueryFilter;
import com.trs.gov.kpi.entity.dao.Table;
import com.trs.gov.kpi.entity.exception.BizException;
import com.trs.gov.kpi.entity.exception.RemoteException;
import com.trs.gov.kpi.entity.requestdata.WkAllSiteDetailRequest;
import com.trs.gov.kpi.entity.responsedata.ApiPageData;
import com.trs.gov.kpi.entity.responsedata.Pager;
import com.trs.gov.kpi.entity.responsedata.WkSiteManagementResponse;
import com.trs.gov.kpi.entity.wangkang.SiteManagement;
import com.trs.gov.kpi.entity.wangkang.WkScore;
import com.trs.gov.kpi.service.helper.QueryFilterHelper;
import com.trs.gov.kpi.service.wangkang.WkScoreService;
import com.trs.gov.kpi.service.wangkang.WkSiteManagementService;
import com.trs.gov.kpi.utils.DBUtil;
import com.trs.gov.kpi.utils.PageInfoDeal;
import com.trs.gov.kpi.utils.StringUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by li.hao on 2017/7/5.
 */
@Service
public class WkSiteManagementServiceImpl implements WkSiteManagementService {

    @Resource
    private CommonMapper commonMapper;

    @Resource
    private WkSiteManagementMapper wkSiteManagementMapper;

    @Resource
    private WkCheckTimeMapper wkCheckTimeMapper;

    @Resource
    private WkScoreService wkScoreService;

    @Override
    public String  addWkSite(SiteManagement siteManagement) {
       commonMapper.insert(DBUtil.toRow(siteManagement));
        return null;
    }

    @Override
    public SiteManagement getSiteManagementBySiteId(Integer siteId, Integer isDel) {
        return wkSiteManagementMapper.selectSiteManagementByStieId(siteId, isDel);
    }

    @Override
    public Integer getSiteCountBySiteName(String siteName, Integer isDel) {
        return wkSiteManagementMapper.getSiteCountBySiteName(siteName, isDel);
    }

    @Override
    public Integer getSiteCountByUrl(String siteIndexUrl, Integer isDel) {
        return wkSiteManagementMapper.getSiteCountByUrl(siteIndexUrl, isDel);
    }

    @Override
    public ApiPageData queryAllSite(WkAllSiteDetailRequest wkAllSiteDetail) throws RemoteException {

        if(!StringUtil.isEmpty(wkAllSiteDetail.getSearchText())){
            wkAllSiteDetail.setSearchText(StringUtil.escape(wkAllSiteDetail.getSearchText()));
        }
        QueryFilter filter = QueryFilterHelper.toWkFilter(wkAllSiteDetail);
        filter.addCond(IssueTableField.IS_DEL, Status.Delete.UN_DELETE.value);
        int itemCount = wkSiteManagementMapper.selectAllSiteCount(filter);
        Pager pager = PageInfoDeal.buildResponsePager(wkAllSiteDetail.getPageIndex(), wkAllSiteDetail.getPageSize(), itemCount);
        filter.setPager(pager);
        List<SiteManagement> siteManagementList = wkSiteManagementMapper.selectAllSiteList(filter);

        return new ApiPageData(pager, getWkSiteResponseBysiteManagementList(siteManagementList));
    }


    @Override
    public int updateSiteManagement(SiteManagement siteManagement) {

        return wkSiteManagementMapper.updateSiteManagement(siteManagement);
    }

    @Override
    public void deleteSiteBySiteIds(List<Integer> siteIds) {
        wkSiteManagementMapper.deleteSiteBySiteIds(siteIds);
    }

    @Override
    public String getSiteNameBySiteId(Integer siteId) {
        return wkSiteManagementMapper.getSiteNameBySiteId(siteId);
    }

    @Override
    public List<SiteManagement> getAllSites() {
        QueryFilter filter = new QueryFilter(Table.WK_SITEMANAGEMENT);
        filter.addCond(WkSiteTableField.IS_DEL,Status.Delete.UN_DELETE);
        return wkSiteManagementMapper.selectAllSiteList(filter);
    }

    @Override
    public void changeSiteStatus(Integer siteId, Types.WkCheckStatus status) {
        DBUpdater updater = new DBUpdater(Table.WK_SITEMANAGEMENT.getTableName());
        updater.addField("checkStatus", status.value);

        QueryFilter filter = new QueryFilter(Table.WK_SITEMANAGEMENT);
        filter.addCond(Constants.DB_FIELD_SITE_ID, siteId);
        commonMapper.update(updater, filter);
    }

    @Override
    public void updateCheckTime(int siteId, Date date) {
        DBUpdater updater = new DBUpdater(Table.WK_SITEMANAGEMENT.getTableName());
        updater.addField("checkTime", date);

        QueryFilter filter = new QueryFilter(Table.WK_SITEMANAGEMENT);
        filter.addCond(Constants.DB_FIELD_SITE_ID, siteId);
        commonMapper.update(updater, filter);
    }

    @Override
    public void changeTerminateCheckStatus(Integer siteId) throws BizException {
        if(siteId == null){
            throw new BizException(Constants.INVALID_PARAMETER);
        }
        QueryFilter filter = new QueryFilter(Table.WK_SCORE);
        filter.addCond(WkScoreTableField.SITE_ID, siteId);

        if(commonMapper.count(filter) > 0){//该站点之前有进行过算分操作（之前有检查过）
            DBUpdater updater = new DBUpdater(Table.WK_SITEMANAGEMENT.getTableName());
            updater.addField(WkSiteTableField.CHECK_STATUS, Types.WkCheckStatus.DONE_CHECK.value);

            QueryFilter filterTo = new QueryFilter(Table.WK_SITEMANAGEMENT);
            filterTo.addCond(Constants.DB_FIELD_SITE_ID, siteId);
            filterTo.addCond(WkSiteTableField.CHECK_STATUS, Types.WkCheckStatus.CONDUCT_CHECK.value);
            commonMapper.update(updater, filterTo);
        }else{
            DBUpdater updater = new DBUpdater(Table.WK_SITEMANAGEMENT.getTableName());
            updater.addField(WkSiteTableField.CHECK_STATUS, Types.WkCheckStatus.NOT_SUMBIT_CHECK.value);

            QueryFilter filterTo = new QueryFilter(Table.WK_SITEMANAGEMENT);
            filterTo.addCond(Constants.DB_FIELD_SITE_ID, siteId);
            filterTo.addCond(WkSiteTableField.CHECK_STATUS, Types.WkCheckStatus.CONDUCT_CHECK.value);
            commonMapper.update(updater, filterTo);
        }
    }

    private List<WkSiteManagementResponse> getWkSiteResponseBysiteManagementList(List<SiteManagement> siteManagementList){
        List<WkSiteManagementResponse> wkSiteResponseList = new ArrayList<>();

        for (SiteManagement siteManagement: siteManagementList ) {
            WkSiteManagementResponse wkSiteManagementResponse = new WkSiteManagementResponse();
            wkSiteManagementResponse.setSiteId(siteManagement.getSiteId());
            wkSiteManagementResponse.setSiteName(siteManagement.getSiteName());
            wkSiteManagementResponse.setSiteIndexUrl(siteManagement.getSiteIndexUrl());
            wkSiteManagementResponse.setDeptAddress(siteManagement.getDeptAddress());
            wkSiteManagementResponse.setDeptLatLng(siteManagement.getDeptLatLng());
            wkSiteManagementResponse.setAutoCheckType(siteManagement.getAutoCheckType());
            wkSiteManagementResponse.setCheckStatus(siteManagement.getCheckStatus());
            wkSiteManagementResponse.setCheckTime(siteManagement.getCheckTime());

            final Integer maxCheckId = wkCheckTimeMapper.getMaxCheckId(siteManagement.getSiteId());
            if (maxCheckId != null) {
                final WkScore score = wkScoreService.getScore(siteManagement.getSiteId(), maxCheckId);
                if (score != null) {
                    wkSiteManagementResponse.setTotal(score.getTotal());
                }
            }
            wkSiteResponseList.add(wkSiteManagementResponse);
        }
        return wkSiteResponseList;
    }

}

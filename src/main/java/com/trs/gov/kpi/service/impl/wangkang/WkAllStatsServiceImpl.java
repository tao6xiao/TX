package com.trs.gov.kpi.service.impl.wangkang;

import com.trs.gov.kpi.constant.Constants;
import com.trs.gov.kpi.constant.WkAllStatsTableField;
import com.trs.gov.kpi.constant.WkSiteIndexStatsTableField;
import com.trs.gov.kpi.dao.CommonMapper;
import com.trs.gov.kpi.dao.WkAllStatsMapper;
import com.trs.gov.kpi.dao.WkCheckTimeMapper;
import com.trs.gov.kpi.entity.dao.DBUpdater;
import com.trs.gov.kpi.entity.dao.QueryFilter;
import com.trs.gov.kpi.entity.dao.Table;
import com.trs.gov.kpi.entity.responsedata.WkAvgSpeedResponse;
import com.trs.gov.kpi.entity.responsedata.WkUpdateContentResponse;
import com.trs.gov.kpi.entity.wangkang.WkAllStats;
import com.trs.gov.kpi.entity.wangkang.WkCheckTime;
import com.trs.gov.kpi.entity.wangkang.WkSiteIndexStats;
import com.trs.gov.kpi.service.wangkang.WkAllStatsService;
import com.trs.gov.kpi.service.wangkang.WkSiteManagementService;
import com.trs.gov.kpi.utils.DBUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by li.hao on 2017/7/12.
 */
@Service
public class WkAllStatsServiceImpl implements WkAllStatsService {

    @Resource
    WkAllStatsMapper wkAllStatsMapper;

    @Resource
    CommonMapper commonMapper;

    @Resource
    private WkCheckTimeMapper wkCheckTimeMapper;

    @Resource
    private WkSiteManagementService wkSiteManagementService;

    /*---平均访问速度---*/
    @Override
    public List<WkAvgSpeedResponse> getAvgSpeedHistory(Integer siteId, Integer checkId) {

        QueryFilter filter = new QueryFilter(Table.WK_ALL_STATS);
        filter.addCond(Constants.DB_FIELD_SITE_ID, siteId);
        filter.addCond(Constants.DB_FIELD_CHECK_ID,checkId).setRangeEnd(true);
        filter.addSortField("checkId");
        final List<WkAllStats> wkAllStats = wkAllStatsMapper.select(filter);

        List<WkAvgSpeedResponse> wkAvgSpeedRespList = new ArrayList<>();
        if(!wkAllStats.isEmpty()){
            for (WkAllStats wkAllstats: wkAllStats) {
                WkAvgSpeedResponse wkAvgSpeedAndUpdateContent = new WkAvgSpeedResponse();

                final List<WkCheckTime> checkTimes = getWkCheckTime(siteId, wkAllstats.getCheckId());

                if (checkTimes.size() == 1) {
                    wkAvgSpeedAndUpdateContent.setCheckTime(checkTimes.get(0).getBeginTime());
                    wkAvgSpeedAndUpdateContent.setAvgSpeed(wkAllstats.getAvgSpeed());
                    wkAvgSpeedRespList.add(wkAvgSpeedAndUpdateContent);
                }
            }
            return wkAvgSpeedRespList;
        }else{
            return Collections.emptyList();
        }
    }

    private List<WkCheckTime> getWkCheckTime(Integer siteId, Integer checkId) {
        QueryFilter queryFilter  = new QueryFilter(Table.WK_CHECK_TIME);
        queryFilter.addCond(Constants.DB_FIELD_SITE_ID, siteId);
        queryFilter.addCond(Constants.DB_FIELD_CHECK_ID, checkId);
        return wkCheckTimeMapper.select(queryFilter);
    }

    /*---网站更新数---*/
    @Override
    public List<WkUpdateContentResponse> getUpdateContentHistory(Integer siteId, Integer checkId) {
        QueryFilter filter = new QueryFilter(Table.WK_ALL_STATS);
        filter.addCond(Constants.DB_FIELD_SITE_ID, siteId);
        filter.addCond(Constants.DB_FIELD_CHECK_ID, checkId).setRangeEnd(true);
        filter.addSortField("checkId");
        final List<WkAllStats> wkAllStats = wkAllStatsMapper.select(filter);

        List<WkUpdateContentResponse> wkUpdateContentList = new ArrayList<>();

        if(!wkAllStats.isEmpty()){
            for (WkAllStats wkAllstats: wkAllStats) {
                WkUpdateContentResponse wkUpdateContent = new WkUpdateContentResponse();
                final List<WkCheckTime> checkTimes = getWkCheckTime(siteId, wkAllstats.getCheckId());
                if (checkTimes.size() == 1) {
                    wkUpdateContent.setUpdateContent(wkAllstats.getUpdateContent());
                    wkUpdateContent.setCheckTime(checkTimes.get(0).getBeginTime());
                    wkUpdateContentList.add(wkUpdateContent);
                }
            }
            return wkUpdateContentList;
        }else{
            return Collections.emptyList();
        }
    }

    @Override
    public void insertOrUpdateUpdateContentAndSpeed(WkAllStats wkAllStats) {
        final QueryFilter filter = getFilter(wkAllStats);
        if (commonMapper.count(filter) > 0) {
            DBUpdater updater = new DBUpdater(Table.WK_ALL_STATS.getTableName());
            updater.addField(WkAllStatsTableField.AVG_SPEED, wkAllStats.getAvgSpeed());
            updater.addField(WkAllStatsTableField.UPDATE_CONTENT, wkAllStats.getUpdateContent());
            commonMapper.update(updater, filter);
        } else {
            commonMapper.insert(DBUtil.toRow(wkAllStats));
        }
    }

    @Override
    public void insertOrUpdateInvalidLink(WkAllStats wkAllStats) {
        final QueryFilter filter = getFilter(wkAllStats);
        if (commonMapper.count(filter) > 0) {
            DBUpdater updater = new DBUpdater(Table.WK_ALL_STATS.getTableName());
            updater.addField(WkAllStatsTableField.INVALID_LINK, wkAllStats.getInvalidLink());
            commonMapper.update(updater, filter);
        } else {
            commonMapper.insert(DBUtil.toRow(wkAllStats));
        }
    }

    @Override
    public void insertOrUpdateErrorWords(WkAllStats wkAllStats) {
        final QueryFilter filter = getFilter(wkAllStats);
        if (commonMapper.count(filter) > 0) {
            DBUpdater updater = new DBUpdater(Table.WK_ALL_STATS.getTableName());
            updater.addField(WkAllStatsTableField.ERROR_INFO, wkAllStats.getErrorInfo());
            commonMapper.update(updater, filter);
        } else {
            commonMapper.insert(DBUtil.toRow(wkAllStats));
        }
    }

    @Override
    public Integer getLastCheckId(Integer siteId, Integer checkId) {
        return wkCheckTimeMapper.getLastCheckId(siteId, checkId);
    }

    @Override
    public WkAllStats getLastTimeCheckBySiteIdAndCheckId(Integer siteId, Integer checkId) {
        QueryFilter filter = new QueryFilter(Table.WK_ALL_STATS);
        filter.addCond(WkAllStatsTableField.SITE_ID, siteId);
        filter.addCond(WkAllStatsTableField.CHECK_ID, checkId);
        WkAllStats wkAllStats = wkAllStatsMapper.selectOnce(filter);

        String siteName = wkSiteManagementService.getSiteNameBySiteId(siteId);

        QueryFilter filterTo = new QueryFilter(Table.WK_SITE_INDEX_STATS);
        filterTo.addCond(WkSiteIndexStatsTableField.SITE_ID, siteId);
        if (commonMapper.count(filterTo) > 0) {
            DBUpdater updater = new DBUpdater(Table.WK_SITE_INDEX_STATS.getTableName());
            updater.addField(WkSiteIndexStatsTableField.CHECK_ID, wkAllStats.getCheckId());
            updater.addField(WkSiteIndexStatsTableField.SITE_NAME, siteName);
            updater.addField(WkSiteIndexStatsTableField.OVER_SPEED, wkAllStats.getAvgSpeed());
            updater.addField(WkSiteIndexStatsTableField.UPDATE_CONTENT, wkAllStats.getUpdateContent());
            updater.addField(WkSiteIndexStatsTableField.INVALID_LINK, wkAllStats.getInvalidLink());
            updater.addField(WkSiteIndexStatsTableField.CONTENT_ERROR, wkAllStats.getErrorInfo());

            commonMapper.update(updater, filterTo);
        } else {
            WkSiteIndexStats wkSiteIndexStats = new WkSiteIndexStats();
            wkSiteIndexStats.setSiteId(wkAllStats.getSiteId());
            wkSiteIndexStats.setCheckId(wkAllStats.getCheckId());
            wkSiteIndexStats.setSiteName(siteName);
            wkSiteIndexStats.setOverSpeed(wkAllStats.getAvgSpeed());
            wkSiteIndexStats.setUpdateContent(wkAllStats.getUpdateContent());
            wkSiteIndexStats.setContentError(wkAllStats.getErrorInfo());
            wkSiteIndexStats.setInvalidLink(wkAllStats.getInvalidLink());

            commonMapper.insert(DBUtil.toRow(wkSiteIndexStats));
        }
        return null;
    }

    private QueryFilter getFilter(WkAllStats wkAllStats) {
        QueryFilter filter = new QueryFilter(Table.WK_ALL_STATS);
        filter.addCond(WkAllStatsTableField.CHECK_ID, wkAllStats.getCheckId());
        filter.addCond(WkAllStatsTableField.SITE_ID, wkAllStats.getSiteId());
        return filter;
    }


}

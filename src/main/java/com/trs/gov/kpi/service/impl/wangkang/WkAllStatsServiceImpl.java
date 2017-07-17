package com.trs.gov.kpi.service.impl.wangkang;

import com.trs.gov.kpi.constant.Constants;
import com.trs.gov.kpi.constant.WkAllStatsTableField;
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
import com.trs.gov.kpi.service.wangkang.WkAllStatsService;
import com.trs.gov.kpi.utils.DBUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
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
                WkAvgSpeedResponse WkAvgSpeedAndUpdateContent = new WkAvgSpeedResponse();

                final List<WkCheckTime> checkTimes = getWkCheckTime(siteId, wkAllstats.getCheckId());

                if (checkTimes.size() == 1) {
                    WkAvgSpeedAndUpdateContent.setCheckTime(checkTimes.get(0).getBeginTime());
                    WkAvgSpeedAndUpdateContent.setAvgSpeed(wkAllstats.getAvgSpeed());
                    wkAvgSpeedRespList.add(WkAvgSpeedAndUpdateContent);
                }
            }
        }
        return wkAvgSpeedRespList;
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
                WkUpdateContentResponse WkUpdateContent = new WkUpdateContentResponse();
                final List<WkCheckTime> checkTimes = getWkCheckTime(siteId, wkAllstats.getCheckId());
                if (checkTimes.size() == 1) {
                    WkUpdateContent.setUpdateContent(wkAllstats.getUpdateContent());
                    WkUpdateContent.setCheckTime(checkTimes.get(0).getBeginTime());
                    wkUpdateContentList.add(WkUpdateContent);
                }
            }
        }

        return wkUpdateContentList;
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
    public Integer getLastCheckId(Integer siteId, Integer curCheckId) {
        return wkCheckTimeMapper.getLastCheckId(siteId, curCheckId);
    }

    private QueryFilter getFilter(WkAllStats wkAllStats) {
        QueryFilter filter = new QueryFilter(Table.WK_ALL_STATS);
        filter.addCond(WkAllStatsTableField.CHECK_ID, wkAllStats.getCheckId());
        filter.addCond(WkAllStatsTableField.SITE_ID, wkAllStats.getSiteId());
        return filter;
    }


}

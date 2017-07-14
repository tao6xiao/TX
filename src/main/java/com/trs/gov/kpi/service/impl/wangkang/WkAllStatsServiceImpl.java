package com.trs.gov.kpi.service.impl.wangkang;

import com.trs.gov.kpi.constant.WkAllStatsTableField;
import com.trs.gov.kpi.dao.CommonMapper;
import com.trs.gov.kpi.dao.WkAvgSpeedUpdateContentMapper;
import com.trs.gov.kpi.entity.dao.DBUpdater;
import com.trs.gov.kpi.entity.dao.QueryFilter;
import com.trs.gov.kpi.entity.dao.Table;
import com.trs.gov.kpi.entity.responsedata.WkAvgSpeedAndUpdateContentResponse;
import com.trs.gov.kpi.entity.wangkang.WkAvgSpeed;
import com.trs.gov.kpi.entity.wangkang.WkAllStats;
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
    WkAvgSpeedUpdateContentMapper wkAvgSpeedUpdateContentMapper;

    @Resource
    CommonMapper commonMapper;

    /*---平均访问速度---*/
    @Override
    public List<WkAvgSpeedAndUpdateContentResponse> getAvgSpeedHistory() {
        List<WkAvgSpeed> wkAvgSpeedList = wkAvgSpeedUpdateContentMapper.getAvgSpeedHistory();
        List<WkAvgSpeedAndUpdateContentResponse> wkAvgSpeedAndUpdateContentList = new ArrayList<>();

        if(wkAvgSpeedList.size() != 0){
            for (WkAvgSpeed wkAvgSpeed: wkAvgSpeedList) {
                WkAvgSpeedAndUpdateContentResponse WkAvgSpeedAndUpdateContent = new WkAvgSpeedAndUpdateContentResponse();
                WkAvgSpeedAndUpdateContent.setCheckTime(wkAvgSpeed.getCheckTime());
                WkAvgSpeedAndUpdateContent.setAvgSpeed(wkAvgSpeed.getAvgSpeed());

                wkAvgSpeedAndUpdateContentList.add(WkAvgSpeedAndUpdateContent);
            }
        }
        return wkAvgSpeedAndUpdateContentList;
    }

    /*---网站更新数---*/
    @Override
    public List<WkAvgSpeedAndUpdateContentResponse> getUpdateContentHistory() {
        List<WkAllStats> wkAllStatsList = wkAvgSpeedUpdateContentMapper.getUpdateContentHistory();
        List<WkAvgSpeedAndUpdateContentResponse> wkAvgSpeedAndUpdateContentList = new ArrayList<>();

        if(wkAllStatsList.size() != 0){
            for (WkAllStats wkAllStats : wkAllStatsList) {
                WkAvgSpeedAndUpdateContentResponse WkAvgSpeedAndUpdateContent = new WkAvgSpeedAndUpdateContentResponse();
                WkAvgSpeedAndUpdateContent.setUpdateContent(wkAllStats.getUpdateContent());
                wkAvgSpeedAndUpdateContentList.add(WkAvgSpeedAndUpdateContent);
            }
        }
        return wkAvgSpeedAndUpdateContentList;
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

    private QueryFilter getFilter(WkAllStats wkAllStats) {
        QueryFilter filter = new QueryFilter(Table.WK_ALL_STATS);
        filter.addCond(WkAllStatsTableField.CHECK_ID, wkAllStats.getCheckId());
        filter.addCond(WkAllStatsTableField.SITE_ID, wkAllStats.getSiteId());
        return filter;
    }


}

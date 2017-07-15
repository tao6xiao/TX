package com.trs.gov.kpi.service.impl.wangkang;

import com.trs.gov.kpi.constant.WkAllStatsTableField;
import com.trs.gov.kpi.constant.WkScoreTableField;
import com.trs.gov.kpi.dao.CommonMapper;
import com.trs.gov.kpi.entity.dao.DBUpdater;
import com.trs.gov.kpi.entity.dao.QueryFilter;
import com.trs.gov.kpi.entity.dao.Table;
import com.trs.gov.kpi.entity.wangkang.WkScore;
import com.trs.gov.kpi.service.wangkang.WkScoreService;
import com.trs.gov.kpi.utils.DBUtil;

import javax.annotation.Resource;

/**
 * Created by linwei on 2017/7/15.
 */
public class WkScoreServiceImpl implements WkScoreService {

    @Resource
    private CommonMapper commonMapper;

    @Override
    public void insertOrUpdateUpdateContentAndSpeed(WkScore score) {
        final QueryFilter filter = getFilter(score);
        if (commonMapper.count(filter) > 0) {
            DBUpdater updater = new DBUpdater(Table.WK_SCORE.getTableName());
            updater.addField(WkScoreTableField.OVER_SPEED, score.getOverSpeed());
            updater.addField(WkScoreTableField.UPDATE_CONTENT, score.getUpdateContent());
            updater.addField(WkScoreTableField.CHECK_TIME, score.getCheckTime());
            commonMapper.update(updater, filter);
        } else {
            commonMapper.insert(DBUtil.toRow(score));
        }
    }

    @Override
    public void insertOrUpdateErrorWords(WkScore score) {
        final QueryFilter filter = getFilter(score);
        if (commonMapper.count(filter) > 0) {
            DBUpdater updater = new DBUpdater(Table.WK_SCORE.getTableName());
            updater.addField(WkScoreTableField.CONTENT_ERROR, score.getOverSpeed());
            updater.addField(WkScoreTableField.CHECK_TIME, score.getCheckTime());
            commonMapper.update(updater, filter);
        } else {
            commonMapper.insert(DBUtil.toRow(score));
        }
    }

    @Override
    public void insertOrUpdateInvalidLink(WkScore score) {
        final QueryFilter filter = getFilter(score);
        if (commonMapper.count(filter) > 0) {
            DBUpdater updater = new DBUpdater(Table.WK_SCORE.getTableName());
            updater.addField(WkScoreTableField.INVALID_LINK, score.getOverSpeed());
            updater.addField(WkScoreTableField.CHECK_TIME, score.getCheckTime());
            commonMapper.update(updater, filter);
        } else {
            commonMapper.insert(DBUtil.toRow(score));
        }

    }

    private QueryFilter getFilter(WkScore score) {
        QueryFilter filter = new QueryFilter(Table.WK_SCORE);
        filter.addCond(WkAllStatsTableField.CHECK_ID, score.getCheckId());
        filter.addCond(WkAllStatsTableField.SITE_ID, score.getSiteId());
        return filter;
    }
}

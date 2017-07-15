package com.trs.gov.kpi.service.impl.wangkang;

import com.trs.gov.kpi.constant.Constants;
import com.trs.gov.kpi.constant.WkAllStatsTableField;
import com.trs.gov.kpi.constant.WkScoreTableField;
import com.trs.gov.kpi.dao.CommonMapper;
import com.trs.gov.kpi.dao.WkScoreMapper;
import com.trs.gov.kpi.entity.dao.DBUpdater;
import com.trs.gov.kpi.entity.dao.QueryFilter;
import com.trs.gov.kpi.entity.dao.Table;
import com.trs.gov.kpi.entity.wangkang.WkCheckTime;
import com.trs.gov.kpi.entity.wangkang.WkScore;
import com.trs.gov.kpi.service.wangkang.WkScoreService;
import com.trs.gov.kpi.utils.DBUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * Created by linwei on 2017/7/15.
 */
@Service
public class WkScoreServiceImpl implements WkScoreService {

    @Resource
    private CommonMapper commonMapper;

    @Resource
    private WkScoreMapper wkScoreMapper;

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
            updater.addField(WkScoreTableField.CONTENT_ERROR, score.getContentError());
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
            updater.addField(WkScoreTableField.INVALID_LINK, score.getInvalidLink());
            updater.addField(WkScoreTableField.CHECK_TIME, score.getCheckTime());
            commonMapper.update(updater, filter);
        } else {
            commonMapper.insert(DBUtil.toRow(score));
        }
    }

    @Override
    public void calcTotalScore(Integer siteId, Integer checkId) {
        QueryFilter filter = new QueryFilter(Table.WK_SCORE);
        filter.addCond(WkScoreTableField.SITE_ID, siteId);
        filter.addCond(WkScoreTableField.CHECK_ID, checkId);
        final List<WkScore> wkScores = wkScoreMapper.select(filter);

        final WkScore score = wkScores.get(0);
        score.setTotal((int)(score.getInvalidLink() * 0.4 + score.getContentError() * 0.2 + score.getOverSpeed() * 0.2 + score.getUpdateContent() * 0.2));
        score.setCheckTime(new Date());
        DBUpdater updater = new DBUpdater(Table.WK_SCORE.getTableName());
        updater.addField(WkScoreTableField.TOTAL, score.getTotal());
        updater.addField(WkScoreTableField.CHECK_TIME, score.getCheckTime());
        commonMapper.update(updater, filter);
    }

    private QueryFilter getFilter(WkScore score) {
        QueryFilter filter = new QueryFilter(Table.WK_SCORE);
        filter.addCond(WkAllStatsTableField.CHECK_ID, score.getCheckId());
        filter.addCond(WkAllStatsTableField.SITE_ID, score.getSiteId());
        return filter;
    }
}

package com.trs.gov.kpi.service.impl.wangkang;

import com.trs.gov.kpi.constant.WkAllStatsTableField;
import com.trs.gov.kpi.constant.WkScoreTableField;
import com.trs.gov.kpi.dao.CommonMapper;
import com.trs.gov.kpi.dao.WkScoreMapper;
import com.trs.gov.kpi.entity.dao.DBUpdater;
import com.trs.gov.kpi.entity.dao.QueryFilter;
import com.trs.gov.kpi.entity.dao.Table;
import com.trs.gov.kpi.entity.responsedata.WkOneSiteScoreResponse;
import com.trs.gov.kpi.entity.wangkang.WkScore;
import com.trs.gov.kpi.service.wangkang.WkScoreService;
import com.trs.gov.kpi.utils.DBUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
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
    public void calcTotalScore(Integer siteId, Integer checkId, Date date) {
        QueryFilter filter = new QueryFilter(Table.WK_SCORE);
        filter.addCond(WkScoreTableField.SITE_ID, siteId);
        filter.addCond(WkScoreTableField.CHECK_ID, checkId);
        final List<WkScore> wkScores = wkScoreMapper.select(filter);

        final WkScore score = wkScores.get(0);
        //对计算结果小数点后两位做四舍五入处理
        DecimalFormat df = new DecimalFormat("#.00");
        double totalD = score.getInvalidLink() * 0.4 + score.getContentError() * 0.2 + score.getOverSpeed() * 0.2 + score.getUpdateContent() * 0.2;
        double totalS = Double.valueOf(df.format(totalD));
        score.setTotal(totalS);
        score.setCheckTime(date);
        DBUpdater updater = new DBUpdater(Table.WK_SCORE.getTableName());
        updater.addField(WkScoreTableField.TOTAL, score.getTotal());
        updater.addField(WkScoreTableField.CHECK_TIME, score.getCheckTime());
        commonMapper.update(updater, filter);
    }

    private QueryFilter getFilter(WkScore score) {
        QueryFilter filter = new QueryFilter(Table.WK_SCORE);
        filter.addCond(WkScoreTableField.CHECK_ID, score.getCheckId());
        filter.addCond(WkScoreTableField.SITE_ID, score.getSiteId());
        return filter;
    }

    @Override
    public List<WkOneSiteScoreResponse> getListBySiteId(Integer siteId, Integer checkId) {
        QueryFilter filter = new QueryFilter(Table.WK_SCORE);
        filter.addCond(WkAllStatsTableField.SITE_ID, siteId);
        filter.addCond(WkAllStatsTableField.CHECK_ID, checkId).setRangeEnd(true);
        filter.addSortField(WkScoreTableField.CHECK_ID);

        List<WkScore> wkScoreList = wkScoreMapper.select(filter);
        List<WkOneSiteScoreResponse> wkOneSiteScoreList = new ArrayList<>();

        if(!wkScoreList.isEmpty()){
            for (WkScore wkScore: wkScoreList) {
                WkOneSiteScoreResponse wkOneSiteScore = new WkOneSiteScoreResponse();
                wkOneSiteScore.setCheckTime(wkScore.getCheckTime());
                wkOneSiteScore.setTotal(wkScore.getTotal());
                wkOneSiteScore.setContentError(wkScore.getContentError());
                wkOneSiteScore.setInvalidLink(wkScore.getInvalidLink());
                wkOneSiteScore.setOverSpeed(wkScore.getOverSpeed());
                wkOneSiteScore.setUpdateContent(wkScore.getUpdateContent());

                wkOneSiteScoreList.add(wkOneSiteScore);
            }
            return wkOneSiteScoreList;
        }else{
            return Collections.emptyList();
        }
    }

    @Override
    public WkScore getScore(Integer siteId, Integer checkId) {
        QueryFilter filter = new QueryFilter(Table.WK_SCORE);
        filter.addCond(WkAllStatsTableField.SITE_ID, siteId);
        filter.addCond(WkAllStatsTableField.CHECK_ID, checkId);

        List<WkScore> wkScoreList = wkScoreMapper.select(filter);
        if (wkScoreList.isEmpty()) {
            return null;
        } else {
            return wkScoreList.get(0);
        }
    }


}

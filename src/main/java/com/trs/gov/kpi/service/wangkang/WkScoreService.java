package com.trs.gov.kpi.service.wangkang;

import com.trs.gov.kpi.entity.responsedata.WkOneSiteScoreResponse;
import com.trs.gov.kpi.entity.wangkang.WkScore;

import java.util.List;

/**
 * Created by linwei on 2017/7/15.
 */
public interface WkScoreService {

    void insertOrUpdateUpdateContentAndSpeed(WkScore score);

    void insertOrUpdateErrorWords(WkScore score);

    void insertOrUpdateInvalidLink(WkScore score);

    void calcTotalScore(Integer siteId, Integer checkId);

    List<WkOneSiteScoreResponse> getListBySiteId(Integer siteId);
}

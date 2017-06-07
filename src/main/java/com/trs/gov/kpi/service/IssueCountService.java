package com.trs.gov.kpi.service;

import com.trs.gov.kpi.entity.responsedata.Statistics;

import java.util.List;

/**
 * 统计分析->问题统计service
 * Created by he.lang on 2017/6/7.
 */
public interface IssueCountService {

    /**
     * 分类统计问题总数
     * @param siteIds
     * @return
     */
    List<Statistics> countSort(Integer[] siteIds);
}

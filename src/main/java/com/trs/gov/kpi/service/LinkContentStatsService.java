package com.trs.gov.kpi.service;

import com.trs.gov.kpi.entity.LinkContentStats;

/**
 * Created by li.hao on 2017/8/11.
 */
public interface LinkContentStatsService {

    /**
     * 插入链接内容信息
     *
     * @param linkContentStats
     */
    void insertLinkContent(LinkContentStats linkContentStats);
}

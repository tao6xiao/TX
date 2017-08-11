package com.trs.gov.kpi.service.impl;

import com.trs.gov.kpi.dao.CommonMapper;
import com.trs.gov.kpi.dao.LinkContentStatsMapper;
import com.trs.gov.kpi.entity.LinkContentStats;
import com.trs.gov.kpi.service.LinkContentStatsService;
import com.trs.gov.kpi.utils.DBUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by li.hao on 2017/8/11.
 */
@Service
public class LinkContentStatsServiceImpl implements LinkContentStatsService {

    @Resource
    private LinkContentStatsMapper linkContentStatsMapper;

    @Resource
    private CommonMapper commonMapper;

    @Override
    public void insertLinkContent(LinkContentStats linkContentStats) {
        commonMapper.insert(DBUtil.toRow(linkContentStats));
    }
}

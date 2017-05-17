package com.trs.gov.kpi.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.trs.gov.kpi.constant.IssueType;
import com.trs.gov.kpi.dao.IssueMapper;
import com.trs.gov.kpi.service.IssueService;

import javax.annotation.Resource;

/**
 * Created by wangxuan on 2017/5/17.
 */
public class IssueServiceImpl implements IssueService {

    @Resource
    private IssueMapper issueMapper;

    @Override
    public JSONObject getIgnoredIssues(Integer siteId, Integer currPage, Integer pageSize) {


        return null;
    }
}

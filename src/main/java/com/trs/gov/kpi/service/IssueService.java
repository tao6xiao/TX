package com.trs.gov.kpi.service;

import com.alibaba.fastjson.JSONObject;

/**
 * Created by wangxuan on 2017/5/17.
 */
public interface IssueService {

    JSONObject queryIssues(Integer siteId, Integer isResolved, Boolean isDel, Integer currPage, Integer pageSize);
}

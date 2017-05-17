package com.trs.gov.kpi.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.trs.gov.kpi.dao.IssueMapper;
import com.trs.gov.kpi.entity.Issue;
import com.trs.gov.kpi.entity.responsedata.Pager;
import com.trs.gov.kpi.service.IssueService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by wangxuan on 2017/5/17.
 */
@Service
public class IssueServiceImpl implements IssueService {

    @Resource
    private IssueMapper issueMapper;

    @Override
    public JSONObject queryIssues(Integer siteId, Integer isResolved, Boolean isDel, Integer currPage, Integer pageSize) {

        Integer from = (currPage - 1) * pageSize;
        List<Issue> issueList = issueMapper.pageQuery(siteId, isResolved, isDel, from, pageSize);
        Integer total = issueMapper.countIssue(siteId, isResolved, isDel);
        JSONObject result = new JSONObject();

        Pager pager = new Pager();
        pager.setItemCount(total);
        pager.setCurrPage(currPage);
        pager.setPageSize(pageSize);
        pager.setPageCount(total / pageSize + 1);

        result.put("pager", pager);
        result.put("data", JSON.toJSON(issueList));
        return result;
    }
}

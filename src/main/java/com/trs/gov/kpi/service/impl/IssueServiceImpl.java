package com.trs.gov.kpi.service.impl;

import com.trs.gov.kpi.dao.IssueMapper;
import com.trs.gov.kpi.entity.Issue;
import com.trs.gov.kpi.service.IssueService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 综合实时监测   查询待解决、已处理、已忽略的问题和预警
 */
@Service
public class IssueServiceImpl extends OperationServiceImpl implements IssueService {

    @Resource
    private IssueMapper issueMapper;

    @Override
    public int getAllIssueCount(Issue issue) {
        return issueMapper.getAllIssueCount(issue);
    }

    @Override
    public List<Issue> getAllIssueList(Integer currPage, Integer pageSize, Issue issue) {
        return issueMapper.getAllIssueList(currPage, pageSize, issue);
    }
}

package com.trs.gov.kpi.service;

import com.trs.gov.kpi.entity.Issue;

import java.util.List;

/**
 * Created by wangxuan on 2017/5/17.
 */
public interface IssueService extends OperationService {

    int getAllIssueCount(Issue issue);

    List<Issue> getAllIssueList(Integer currPage, Integer pageSize, Issue issue);
}

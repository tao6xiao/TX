package com.trs.gov.kpi.service;

import com.trs.gov.kpi.entity.IssueBase;
import com.trs.gov.kpi.entity.responsedata.IssueIsNotResolvedResponse;

import java.util.List;

/**
 * Created by wangxuan on 2017/5/17.
 */
public interface IssueService extends OperationService {

    int getAllIssueCount(IssueBase issue);

    List<IssueIsNotResolvedResponse> getAllIssueList(Integer currPage, Integer pageSize, IssueBase issue);
}

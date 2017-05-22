package com.trs.gov.kpi.service;

import com.trs.gov.kpi.entity.InfoUpdate;
import com.trs.gov.kpi.entity.IssueBase;
import com.trs.gov.kpi.entity.responsedata.Statistics;

import java.util.List;

/**
 * Created by rw103 on 2017/5/13.
 */
public interface InfoUpdateService extends OperationService {

    int getIssueHistoryCount(IssueBase issueBase);

    List<InfoUpdate> getIssueList(Integer currPage, Integer pageSize, IssueBase issueBase);

    List<Statistics> getIssueCountByType(IssueBase issueBase);

    List<Statistics> getWarningCountByType(IssueBase issueBase);
}

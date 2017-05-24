package com.trs.gov.kpi.service;

import com.trs.gov.kpi.entity.IssueBase;
import com.trs.gov.kpi.entity.responsedata.Statistics;

import java.util.List;

/**
 * Created by ranwei on 2017/5/24.
 */
public interface IntegratedMonitorService {

    List<Statistics> getAllIssueCount(IssueBase issueBase);

    List<Statistics> getUnhandledIssueCount(IssueBase issueBase);

    List<Statistics> getWarningCount(IssueBase issueBase);
}

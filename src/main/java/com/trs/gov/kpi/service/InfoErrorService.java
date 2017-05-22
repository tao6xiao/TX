package com.trs.gov.kpi.service;

import com.trs.gov.kpi.entity.InfoError;
import com.trs.gov.kpi.entity.IssueBase;
import com.trs.gov.kpi.entity.responsedata.HistoryStatistics;
import com.trs.gov.kpi.entity.responsedata.Statistics;

import java.util.List;

/**
 * Created by ranwei on 2017/5/15.
 */
public interface InfoErrorService extends OperationService {

    List<HistoryStatistics> getIssueHistoryCount(IssueBase issueBase);

    List<InfoError> getIssueList(Integer pageIndex, Integer pageSize, IssueBase issueBase);

    List<Statistics> getIssueCountByType(IssueBase issueBase);

}

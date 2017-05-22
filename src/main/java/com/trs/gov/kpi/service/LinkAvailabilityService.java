package com.trs.gov.kpi.service;

import com.trs.gov.kpi.entity.IssueBase;
import com.trs.gov.kpi.entity.LinkAvailability;
import com.trs.gov.kpi.entity.dao.QueryFilter;
import com.trs.gov.kpi.entity.responsedata.Statistics;

import java.util.Date;
import java.util.List;

/**
 * Created by ranwei on 2017/5/11.
 */
public interface LinkAvailabilityService extends OperationService {


    int getIssueHistoryCount(IssueBase issueBase);

    List<LinkAvailability> getIssueList(Integer currPage, Integer pageSize, IssueBase issueBase);

    void insertLinkAvailability(LinkAvailability linkAvailability);

    List<Statistics> getIssueCountByType(IssueBase issueBase);

    int getIndexAvailability(String indexUrl, IssueBase issueBase);

    String getIndexUrl(IssueBase issueBase);

    Date getMonitorTime(String indexUrl, IssueBase issueBase);

    List<LinkAvailability> getUnsolvedIssueList(QueryFilter filter);

    int getUnsolvedIssueCount(QueryFilter filter);

}

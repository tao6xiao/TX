package com.trs.gov.kpi.service;

import com.trs.gov.kpi.entity.IssueBase;
import com.trs.gov.kpi.entity.LinkAvailability;
import com.trs.gov.kpi.entity.responsedata.Statistics;

import java.util.List;

/**
 * Created by rw103 on 2017/5/11.
 */
public interface LinkAvailabilityService extends OperationService {


    int getIssueHistoryCount(IssueBase issueBase);

    List<LinkAvailability> getIssueList(Integer currPage, Integer pageSize, IssueBase issueBase);

    void insertLinkAvailability(LinkAvailability linkAvailability);

    List<Statistics> getIssueCountByType(IssueBase issueBase);

    int getIndexAvailability(IssueBase issueBase);
}

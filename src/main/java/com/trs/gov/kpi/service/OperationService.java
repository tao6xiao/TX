package com.trs.gov.kpi.service;

import com.trs.gov.kpi.entity.IssueBase;

import java.util.List;

/**
 * Created by rw103 on 2017/5/13.
 */
public interface OperationService {

    int getHandledIssueCount(IssueBase issueBase);

    int getUnhandledIssueCount(IssueBase issueBase);

    int getUpdateNotIntimeCount(IssueBase issueBase);

    int getUpdateWarningCount(IssueBase issueBase);

    void handIssueById(int siteId, int id);

    void handIssuesByIds(int siteId, List<Integer> ids);

    void ignoreIssueById(int siteId, int id);

    void ignoreIssuesByIds(int siteId, List<Integer> ids);

    void delIssueByIds(int siteId, List<Integer> ids);
}

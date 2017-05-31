//package com.trs.gov.kpi.service;
//
//import com.trs.gov.kpi.entity.IssueBase;
//import com.trs.gov.kpi.entity.dao.QueryDBField;
//import com.trs.gov.kpi.entity.dao.QueryFilter;
//
//import java.util.Date;
//import java.util.List;
//
///**
// * Created by rw103 on 2017/5/13.
// */
//public interface OperationService {
//
//    int getHandledIssueCount(IssueBase issueBase);
//
//    int getUnhandledIssueCount(IssueBase issueBase);
//
//    int getUpdateWarningCount(IssueBase issueBase);
//
//    void handIssuesByIds(int siteId, List<Integer> ids);
//
//    void ignoreIssuesByIds(int siteId, List<Integer> ids);
//
//    void delIssueByIds(int siteId, List<Integer> ids);
//
//    Date getEarliestIssueTime();
//}

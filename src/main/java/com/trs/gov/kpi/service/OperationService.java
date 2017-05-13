package com.trs.gov.kpi.service;

import java.util.List;

/**
 * Created by rw103 on 2017/5/13.
 */
public interface OperationService {

    void handIssueById(int siteId, int id);

    void handIssuesByIds(int siteId, List<Integer> ids);

    void ignoreIssueById(int siteId, int id);

    void ignoreIssuesByIds(int siteId, List<Integer> ids);

    void delIssueByIds(int siteId, List<Integer> ids);
}

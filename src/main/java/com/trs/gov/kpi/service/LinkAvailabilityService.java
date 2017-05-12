package com.trs.gov.kpi.service;

import com.trs.gov.kpi.entity.LinkAvailability;
import com.trs.gov.kpi.entity.Pager;

import java.util.List;

/**
 * Created by rw103 on 2017/5/11.
 */
public interface LinkAvailabilityService {

    int getHandledIssueCount(int siteId);

    int getUnhandledIssueCount(int siteId);

    int getUnhandledIssueCountByTime(int siteId);

    List<LinkAvailability> getIssueList(int currPage, int pageSize, LinkAvailability linkAvailability);

    void handIssueById(int siteId, int id);

    void handIssuesByIds(int siteId, List<Integer> ids);

    void ignoreIssueById(int siteId, int id);

    void ignoreIssuesByIds(int siteId, List<Integer> ids);

    void delIssueByIds(int siteId, List<Integer> ids);
}

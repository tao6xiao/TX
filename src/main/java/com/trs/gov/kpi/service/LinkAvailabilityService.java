package com.trs.gov.kpi.service;

import com.trs.gov.kpi.entity.dao.QueryFilter;
import com.trs.gov.kpi.entity.requestdata.PageDataRequestParam;
import com.trs.gov.kpi.entity.responsedata.*;

import java.util.Date;
import java.util.List;

/**
 * Created by ranwei on 2017/5/11.
 */
public interface LinkAvailabilityService {


    List<HistoryStatistics> getIssueHistoryCount(PageDataRequestParam param);

    ApiPageData getIssueList(PageDataRequestParam param);

    void insertLinkAvailability(LinkAvailabilityResponse linkAvailabilityResponse);

    boolean getIndexAvailability(PageDataRequestParam param);

    String getIndexUrl(PageDataRequestParam param);

    IndexPage showIndexAvailability(PageDataRequestParam param);

    List<LinkAvailabilityResponse> getUnsolvedIssueList(QueryFilter filter);

    int getUnsolvedIssueCount(QueryFilter filter);

    int getHandledIssueCount(PageDataRequestParam param);

    int getUnhandledIssueCount(PageDataRequestParam param);

    List<Statistics> getIssueCount(PageDataRequestParam param);

    void handIssuesByIds(int siteId, List<Integer> ids);

    void ignoreIssuesByIds(int siteId, List<Integer> ids);

    void delIssueByIds(int siteId, List<Integer> ids);

    Date getEarliestIssueTime();

}

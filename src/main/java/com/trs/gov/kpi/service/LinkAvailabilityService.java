package com.trs.gov.kpi.service;

import com.trs.gov.kpi.entity.dao.QueryFilter;
import com.trs.gov.kpi.entity.requestdata.PageDataRequestParam;
import com.trs.gov.kpi.entity.responsedata.*;

import java.util.List;

/**
 * Created by ranwei on 2017/5/11.
 */
public interface LinkAvailabilityService {


    History getIssueHistoryCount(PageDataRequestParam param);

    ApiPageData getIssueList(PageDataRequestParam param);

    void insertLinkAvailability(LinkAvailabilityResponse linkAvailabilityResponse);

    boolean isIndexAvailable(PageDataRequestParam param);

    String getIndexUrl(PageDataRequestParam param);

    IndexPage showIndexAvailability(PageDataRequestParam param);

    List<LinkAvailabilityResponse> getUnsolvedIssueList(QueryFilter filter);

    int getUnsolvedIssueCount(QueryFilter filter);

    int getHandledIssueCount(PageDataRequestParam param);

    int getUnhandledIssueCount(PageDataRequestParam param);

    List<Statistics> getIssueCount(PageDataRequestParam param);

}

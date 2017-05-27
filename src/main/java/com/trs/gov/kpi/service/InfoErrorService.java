package com.trs.gov.kpi.service;

import com.trs.gov.kpi.entity.responsedata.ApiPageData;
import com.trs.gov.kpi.entity.responsedata.InfoErrorResponse;
import com.trs.gov.kpi.entity.requestdata.PageDataRequestParam;
import com.trs.gov.kpi.entity.responsedata.HistoryStatistics;
import com.trs.gov.kpi.entity.responsedata.Statistics;

import java.util.Date;
import java.util.List;

/**
 * Created by ranwei on 2017/5/15.
 */
public interface InfoErrorService  {

    List<Statistics> getIssueCount(PageDataRequestParam param);

    int getHandledIssueCount(PageDataRequestParam param);

    int getUnhandledIssueCount(PageDataRequestParam param);

    void handIssuesByIds(int siteId, List<Integer> ids);

    void ignoreIssuesByIds(int siteId, List<Integer> ids);

    void delIssueByIds(int siteId, List<Integer> ids);

    Date getEarliestIssueTime();

    List<HistoryStatistics> getIssueHistoryCount(PageDataRequestParam param);

    ApiPageData getIssueList(PageDataRequestParam param);

}

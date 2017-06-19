package com.trs.gov.kpi.service;

import com.trs.gov.kpi.entity.requestdata.PageDataRequestParam;
import com.trs.gov.kpi.entity.responsedata.HistoryStatistics;
import com.trs.gov.kpi.entity.responsedata.Statistics;

import java.util.List;

/**
 * Created by ranwei on 2017/5/24.
 */
public interface IntegratedMonitorService {

    List<Statistics> getAllIssueCount(PageDataRequestParam param);

    List<Statistics> getUnhandledIssueCount(PageDataRequestParam param);

    List<Statistics> getWarningCount(PageDataRequestParam param);

    Double getRecentPerformance(PageDataRequestParam param);

    List<HistoryStatistics> getHistoryPerformance(PageDataRequestParam param);
}

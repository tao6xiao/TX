package com.trs.gov.kpi.service;

import com.trs.gov.kpi.entity.exception.RemoteException;
import com.trs.gov.kpi.entity.requestdata.PageDataRequestParam;
import com.trs.gov.kpi.entity.responsedata.HistoryStatistics;
import com.trs.gov.kpi.entity.responsedata.Statistics;

import java.text.ParseException;
import java.util.List;

/**
 * Created by ranwei on 2017/5/24.
 */
public interface IntegratedMonitorService {

    List<Statistics> getAllIssueCount(PageDataRequestParam param);

    List<Statistics> getUnhandledIssueCount(PageDataRequestParam param);

    List<Statistics> getWarningCount(PageDataRequestParam param);

    Double getPerformance(PageDataRequestParam param) throws ParseException, RemoteException;

    List<HistoryStatistics> getHistoryPerformance(PageDataRequestParam param) throws ParseException;
}

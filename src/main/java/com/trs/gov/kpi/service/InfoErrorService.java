package com.trs.gov.kpi.service;

import com.trs.gov.kpi.entity.exception.RemoteException;
import com.trs.gov.kpi.entity.requestdata.PageDataRequestParam;
import com.trs.gov.kpi.entity.requestdata.WorkOrderRequest;
import com.trs.gov.kpi.entity.responsedata.ApiPageData;
import com.trs.gov.kpi.entity.responsedata.HistoryStatistics;
import com.trs.gov.kpi.entity.responsedata.InfoErrorOrderRes;
import com.trs.gov.kpi.entity.responsedata.Statistics;

import java.util.List;

/**
 * Created by ranwei on 2017/5/15.
 */
public interface InfoErrorService {

    List<Statistics> getIssueCount(PageDataRequestParam param);

    List<HistoryStatistics> getIssueHistoryCount(PageDataRequestParam param);

    ApiPageData getIssueList(PageDataRequestParam param);

    /**
     * 查询信息错误的工单
     *
     * @param request
     * @return
     */
    ApiPageData selectInfoErrorOrder(WorkOrderRequest request) throws RemoteException;

    /**
     * 查询单个信息错误的工单
     *
     * @param request
     * @return
     */
    InfoErrorOrderRes getInfoErrorOrderById(WorkOrderRequest request) throws RemoteException;

}

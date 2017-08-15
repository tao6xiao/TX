package com.trs.gov.kpi.service;

import com.trs.gov.kpi.entity.exception.RemoteException;
import com.trs.gov.kpi.entity.requestdata.PageDataRequestParam;
import com.trs.gov.kpi.entity.requestdata.WorkOrderRequest;
import com.trs.gov.kpi.entity.responsedata.ApiPageData;
import com.trs.gov.kpi.entity.responsedata.HistoryStatisticsResp;
import com.trs.gov.kpi.entity.responsedata.InfoErrorOrderRes;
import com.trs.gov.kpi.entity.responsedata.Statistics;

import java.util.List;

/**
 * 信息错误
 * Created by ranwei on 2017/5/15.
 */
public interface InfoErrorService {

    /**
     * 查询已解决和未解决的问题数量
     *
     * @param param
     * @return
     */
    List<Statistics> getIssueCount(PageDataRequestParam param) throws RemoteException;

    /**
     * 查询每月新增问题数量的历史记录
     *
     * @param param
     * @return
     */
    HistoryStatisticsResp getIssueHistoryCount(PageDataRequestParam param);

    /**
     * 查询未解决问题的列表
     *
     * @param param
     * @return
     */
    ApiPageData getInfoErrorList(PageDataRequestParam param) throws RemoteException;

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

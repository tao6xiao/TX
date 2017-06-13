package com.trs.gov.kpi.service;

import com.trs.gov.kpi.entity.exception.RemoteException;
import com.trs.gov.kpi.entity.requestdata.PageDataRequestParam;
import com.trs.gov.kpi.entity.requestdata.WorkOrderRequest;
import com.trs.gov.kpi.entity.responsedata.ApiPageData;
import com.trs.gov.kpi.entity.responsedata.History;
import com.trs.gov.kpi.entity.responsedata.InfoUpdateOrderRes;
import com.trs.gov.kpi.entity.responsedata.Statistics;

import java.text.ParseException;
import java.util.List;

/**
 * Created by rw103 on 2017/5/13.
 */
public interface InfoUpdateService {

    List<Statistics> getIssueCount(PageDataRequestParam param);

    History getIssueHistoryCount(PageDataRequestParam param);

    /**
     * 获取栏目信息更新不及时问题的统计信息
     *
     * @param param
     * @return
     */
    List<Statistics> getUpdateNotInTimeCountList(PageDataRequestParam param) throws ParseException, RemoteException;

    ApiPageData get(PageDataRequestParam param) throws RemoteException;

    /**
     * 查询信息更新的工单
     *
     * @param request
     * @return
     */
    ApiPageData selectInfoUpdateOrder(WorkOrderRequest request) throws RemoteException;

    /**
     * 查询单个信息错误的工单
     *
     * @param request
     * @return
     */
    InfoUpdateOrderRes getInfoUpdateOrderById(WorkOrderRequest request) throws RemoteException;


}

package com.trs.gov.kpi.service;

import com.trs.gov.kpi.entity.exception.BizException;
import com.trs.gov.kpi.entity.exception.RemoteException;
import com.trs.gov.kpi.entity.requestdata.PageDataRequestParam;
import com.trs.gov.kpi.entity.requestdata.WorkOrderRequest;
import com.trs.gov.kpi.entity.responsedata.*;

import java.util.List;

/**
 * 信息更新
 * Created by ranwei on 2017/5/13.
 */
public interface InfoUpdateService {

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
     * 获取栏目信息更新不及时问题的统计信息
     *
     * @param param
     * @return
     */
    List<Statistics> getUpdateNotInTimeCountList(PageDataRequestParam param) throws BizException, RemoteException;

    /**
     * 查询未解决问题的列表
     *
     * @param param
     * @return
     * @throws RemoteException
     */
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

    /**
     * 获取当前站点下面更新不及时栏目以及空栏目列表
     *
     * @param param
     * @return
     */
    MonthUpdateResponse getNotInTimeCountMonth(PageDataRequestParam param) throws RemoteException;
}

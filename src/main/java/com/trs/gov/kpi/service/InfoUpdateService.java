package com.trs.gov.kpi.service;

import com.trs.gov.kpi.entity.exception.RemoteException;
import com.trs.gov.kpi.entity.requestdata.PageDataRequestParam;
import com.trs.gov.kpi.entity.requestdata.WorkOrderRequest;
import com.trs.gov.kpi.entity.responsedata.ApiPageData;
import com.trs.gov.kpi.entity.responsedata.HistoryStatistics;
import com.trs.gov.kpi.entity.responsedata.Statistics;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

/**
 * Created by rw103 on 2017/5/13.
 */
public interface InfoUpdateService {

    List<Statistics> getIssueCount(PageDataRequestParam param);

    List<HistoryStatistics> getIssueHistoryCount(PageDataRequestParam param);

    void handIssuesByIds(int siteId, List<Integer> ids);

    void ignoreIssuesByIds(int siteId, List<Integer> ids);

    void delIssueByIds(int siteId, List<Integer> ids);

    Date getEarliestIssueTime();

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
     * 修改工单状态
     *
     * @param workOrderStatus
     * @param ids
     */
    void updateOrderByIds(int workOrderStatus, List<Integer> ids);


}

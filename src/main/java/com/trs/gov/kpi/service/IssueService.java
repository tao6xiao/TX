package com.trs.gov.kpi.service;

import com.trs.gov.kpi.entity.requestdata.PageDataRequestParam;
import com.trs.gov.kpi.entity.responsedata.ApiPageData;

import java.util.Date;
import java.util.List;

/**
 * Created by wangxuan on 2017/5/17.
 */
public interface IssueService{

    void handIssuesByIds(int siteId, List<Integer> ids);

    void ignoreIssuesByIds(int siteId, List<Integer> ids);

    void delIssueByIds(int siteId, List<Integer> ids);

    Date getEarliestIssueTime();

    /**
     * 获取待解决问题列表
     * @param param
     * @return
     */
    ApiPageData get( PageDataRequestParam param);

    /**
     * 修改工单状态
     *
     * @param workOrderStatus
     * @param ids
     */
    void updateOrderByIds(int workOrderStatus, List<Integer> ids);
}

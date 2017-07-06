package com.trs.gov.kpi.service;

import com.trs.gov.kpi.entity.exception.RemoteException;
import com.trs.gov.kpi.entity.requestdata.PageDataRequestParam;
import com.trs.gov.kpi.entity.responsedata.ApiPageData;

import java.util.List;

/**
 * Created by wangxuan on 2017/5/17.
 */
public interface IssueService {

    /**
     * 根据id批量处理问题
     *
     * @param siteId
     * @param ids
     */
    void handIssuesByIds(int siteId, List<Integer> ids);

    /**
     * 根据id批量忽略问题
     *
     * @param siteId
     * @param ids
     */
    void ignoreIssuesByIds(int siteId, List<Integer> ids);

    /**
     * 根据id批量删除问题
     *
     * @param siteId
     * @param ids
     */
    void delIssueByIds(int siteId, List<Integer> ids);

    /**
     * 根据id批量设置问题所属部门
     *
     * @param siteId
     * @param ids
     */
    void updateDeptByIds(int siteId, List<Integer> ids, int deptId);

    /**
     * 获取待解决问题列表
     *
     * @param param
     * @return
     */
    ApiPageData get(PageDataRequestParam param) throws RemoteException;

    /**
     * 修改工单状态
     *
     * @param workOrderStatus
     * @param ids
     */
    void updateOrderByIds(int workOrderStatus, List<Integer> ids);
}

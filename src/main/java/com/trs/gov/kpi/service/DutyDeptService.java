package com.trs.gov.kpi.service;

import com.trs.gov.kpi.entity.DutyDept;
import com.trs.gov.kpi.entity.exception.RemoteException;
import com.trs.gov.kpi.entity.requestdata.DutyDeptRequest;
import com.trs.gov.kpi.entity.requestdata.PageDataRequestParam;
import com.trs.gov.kpi.entity.responsedata.ApiPageData;

/**
 * 设置->为栏目设置部门service接口
 * Created by he.lang on 2017/7/5.
 */
public interface DutyDeptService {
    /**
     * 设置部门
     *
     * @param deptRequest
     */
    void add(DutyDeptRequest deptRequest);

    /**
     * 修改栏目和部门关系
     *
     * @param deptRequest
     */
    void update(DutyDeptRequest deptRequest);

    /**
     * 通过栏目id查询对应记录
     * @param chnlId
     * @return
     */
    DutyDept getByChnlId(int chnlId);

    /**
     * 获取分页数据
     * @param param
     * @return
     */
    ApiPageData get(PageDataRequestParam param) throws RemoteException;

    /**
     * 删除对应站点和栏目下设置的部门
     * @param siteId
     * @param chnlIds
     */
    void delete(int siteId, Integer[] chnlIds);
}

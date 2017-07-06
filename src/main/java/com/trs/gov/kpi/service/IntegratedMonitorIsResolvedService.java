package com.trs.gov.kpi.service;


import com.trs.gov.kpi.entity.exception.RemoteException;
import com.trs.gov.kpi.entity.requestdata.PageDataRequestParam;
import com.trs.gov.kpi.entity.responsedata.ApiPageData;

/**
 * 综合监测：已处理service
 * Created by he.lang on 2017/5/19.
 */
public interface IntegratedMonitorIsResolvedService {

    /**
     * 获取分页数据（通用方法：已解决，已忽略）
     *
     * @param param
     * @param isResolved
     * @return
     */
    ApiPageData getPageDataIsResolvedList(PageDataRequestParam param, Boolean isResolved) throws RemoteException;

}

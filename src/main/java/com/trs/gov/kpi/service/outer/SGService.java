package com.trs.gov.kpi.service.outer;

import com.trs.gov.kpi.entity.exception.RemoteException;
import com.trs.gov.kpi.entity.outerapi.sp.SGPageDataRes;
import com.trs.gov.kpi.entity.outerapi.sp.SGStatistics;
import com.trs.gov.kpi.entity.requestdata.PageDataRequestParam;
import com.trs.gov.kpi.entity.responsedata.HistoryStatisticsResp;

/**
 * Created by ranwei on 2017/6/12.
 */
public interface SGService {

    /**
     * 查询服务指南的问题列表
     *
     * @param param
     * @return
     */
    SGPageDataRes getSGList(PageDataRequestParam param) throws RemoteException;

    /**
     * 查询服务实用的统计数量
     *
     * @param param
     * @return
     */
    SGStatistics getSGCount(PageDataRequestParam param) throws RemoteException;

    /**
     * 查询服务实用统计的历史记录
     *
     * @param param
     * @return
     */
    HistoryStatisticsResp getSGHistoryCount(PageDataRequestParam param) throws RemoteException;

    /**
     * 查询当前站点所有的服务链接
     *
     * @param siteId
     * @return
     * @throws RemoteException
     */
    SGPageDataRes getAllService(Integer siteId) throws RemoteException;
}

package com.trs.gov.kpi.service.outer;

import com.trs.gov.kpi.entity.exception.RemoteException;
import com.trs.gov.kpi.entity.outerapi.sp.SGHistoryStatistics;
import com.trs.gov.kpi.entity.outerapi.sp.SGPageDataRes;
import com.trs.gov.kpi.entity.outerapi.sp.SGStatistics;
import com.trs.gov.kpi.entity.requestdata.PageDataRequestParam;

import java.util.List;

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
    List<SGHistoryStatistics> getSGHistoryCount(PageDataRequestParam param) throws RemoteException;

    /**
     * 查询所有的服务链接
     *
     * @param param
     * @return
     * @throws RemoteException
     */
    SGPageDataRes getAllService(PageDataRequestParam param) throws RemoteException;
}

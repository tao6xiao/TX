package com.trs.gov.kpi.service;

import com.trs.gov.kpi.entity.exception.RemoteException;
import com.trs.gov.kpi.entity.outerapi.nbhd.NBHDHistoryRes;
import com.trs.gov.kpi.entity.outerapi.nbhd.NBHDPageDataResult;
import com.trs.gov.kpi.entity.outerapi.nbhd.NBHDRequestParam;
import com.trs.gov.kpi.entity.outerapi.nbhd.NBHDStatisticsRes;

/**
 * Created by ranwei on 2017/6/9.
 */
public interface InteractionService {

    /**
     * 查询问政互动的信件列表
     *
     * @return
     */
    NBHDPageDataResult getGovMsgBoxes(NBHDRequestParam param) throws RemoteException;

    /**
     * 查询问政互动的数量
     *
     * @return
     */
    NBHDStatisticsRes getGovMsgBoxesCount(NBHDRequestParam param) throws RemoteException;

    /**
     * 查询问政互动的数量的历史记录
     *
     * @return
     */
    NBHDHistoryRes getGovMsgHistoryCount(NBHDRequestParam param) throws RemoteException;
}

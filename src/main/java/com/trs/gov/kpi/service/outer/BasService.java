package com.trs.gov.kpi.service.outer;

import com.trs.gov.kpi.entity.exception.BizException;
import com.trs.gov.kpi.entity.exception.RemoteException;
import com.trs.gov.kpi.entity.requestdata.BasRequest;
import com.trs.gov.kpi.entity.responsedata.HistoryStatisticsResp;

import java.text.ParseException;

/**
 * Created by ranwei on 2017/6/13.
 */
public interface BasService {

    /**
     * 获取最近一个月的访问量
     *
     * @param request
     * @return
     */
    Integer getVisits(BasRequest request) throws RemoteException, ParseException;

    /**
     * 获取访问量的历史记录
     *
     * @param request
     * @return
     */
    HistoryStatisticsResp getHistoryVisits(BasRequest request) throws RemoteException, ParseException;

    /**
     * 获取最近一个月的次均停留时间
     *
     * @param request
     * @return
     */
    Integer getStayTime(BasRequest request) throws RemoteException, BizException;

    /**
     * 获取次均停留时间的历史记录
     *
     * @param request
     * @return
     */
    HistoryStatisticsResp getHistoryStayTime(BasRequest request) throws ParseException, RemoteException, BizException;
}

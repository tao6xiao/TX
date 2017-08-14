package com.trs.gov.kpi.service;

import com.trs.gov.kpi.entity.exception.BizException;
import com.trs.gov.kpi.entity.exception.RemoteException;
import com.trs.gov.kpi.entity.requestdata.ReportRequestParam;
import com.trs.gov.kpi.entity.responsedata.ApiPageData;

import java.text.ParseException;

/**
 * Created by ranwei on 2017/6/12.
 */
public interface ReportService {

    /**
     * 查询报表列表
     *
     * @param param
     * @param isTimeNode
     * @return
     * @throws RemoteException
     * @throws ParseException
     */
    ApiPageData selectReportList(ReportRequestParam param, boolean isTimeNode) throws RemoteException, BizException;

    /**
     * 获取导出报表的文件路径
     *
     * @param param
     * @param isTimeNode
     * @return
     * @throws ParseException
     */
    String getReportPath(ReportRequestParam param, boolean isTimeNode) throws BizException;
}

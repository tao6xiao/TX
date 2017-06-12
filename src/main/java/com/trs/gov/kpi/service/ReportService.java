package com.trs.gov.kpi.service;

import com.trs.gov.kpi.entity.exception.RemoteException;
import com.trs.gov.kpi.entity.requestdata.PageDataRequestParam;
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
     * @return
     */
    ApiPageData selectReportList(PageDataRequestParam param, boolean isTimeNode) throws RemoteException, ParseException;
}

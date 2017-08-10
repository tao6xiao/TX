package com.trs.gov.kpi.service;

import com.trs.gov.kpi.entity.exception.BizException;
import com.trs.gov.kpi.entity.exception.RemoteException;
import com.trs.gov.kpi.entity.requestdata.PageDataRequestParam;
import com.trs.gov.kpi.entity.responsedata.ApiPageData;

import java.text.ParseException;

/**
 * 综合实时监测预警提醒service
 * Created by he.lang on 2017/5/18.
 */
public interface IntegratedMonitorWarningService{

    /**
     * 获取预警提醒的分页数据
     * @param param
     * @return
     */
    ApiPageData get(PageDataRequestParam param) throws BizException, RemoteException;
}

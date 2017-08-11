package com.trs.gov.kpi.service;

import com.trs.gov.kpi.entity.MonitorRecord;
import com.trs.gov.kpi.entity.exception.BizException;
import com.trs.gov.kpi.entity.requestdata.PageDataRequestParam;
import com.trs.gov.kpi.entity.responsedata.ApiPageData;

import java.util.Date;

/**
 * Created by li.hao on 2017/8/4.
 */
public interface MonitorRecordService {

    /**
     * 插入检测信息
     *
     * @param monitorRecord
     */
    void insertMonitorRecord(MonitorRecord monitorRecord);


    // TODO REVIEW LINWEI DO_li.hao FIXED 函数名称体现不出来是最近一次检测任务，容易产生误解
    /**
     * 查询最近的监测任务的结束时间
     *
     * @param siteId
     * @param taskId
     * @return
     */
    Date getLastMonitorEndTime(Integer siteId, Integer taskId);

    /**
     * 根据最后一次监测完成时间获取首页可用性的状态
     * @param siteId
     * @param taskId
     * @param endTime
     * @return
     */
    Integer getResultByLastEndTime(Integer siteId, Integer taskId, Date endTime);

    /**
     * 查询日志监测列表
     * @param param
     * @return
     */
    ApiPageData selectMonitorRecordList(PageDataRequestParam param) throws BizException;
}

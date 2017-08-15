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

    /**
     * 查询最近的监测任务的结束时间
     *
     * @param siteId
     * @param taskId
     * @return
     */
    Date getLastMonitorEndTime(Integer siteId, Integer taskId);

    /**
     * 获取最后一次监测完成的结果
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

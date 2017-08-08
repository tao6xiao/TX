package com.trs.gov.kpi.service;

import com.trs.gov.kpi.entity.MonitorRecord;

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
     * 查询最近的监测任务的开始时间
     *
     * @param siteId
     * @param typeId
     * @return
     */
    Date getMonitorStartTime(Integer siteId, Integer typeId);

    /**
     * 查询最近的监测任务的结束时间
     *
     * @param siteId
     * @param typeId
     * @return
     */
    Date getMonitorEndTime(Integer siteId, Integer typeId);
}

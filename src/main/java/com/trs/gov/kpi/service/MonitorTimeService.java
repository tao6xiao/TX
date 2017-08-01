package com.trs.gov.kpi.service;

import com.trs.gov.kpi.entity.MonitorTime;

import java.util.Date;

/**
 * Created by ranwei on 2017/7/31.
 */
public interface MonitorTimeService {

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

    /**
     * 查询监测任务的执行时间
     *
     * @param monitorTime
     */
    void insertMonitorTime(MonitorTime monitorTime);
}

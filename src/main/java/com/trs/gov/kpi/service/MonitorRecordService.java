package com.trs.gov.kpi.service;

import com.trs.gov.kpi.entity.MonitorRecord;

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
}

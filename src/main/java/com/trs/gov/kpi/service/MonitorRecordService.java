package com.trs.gov.kpi.service;

import com.trs.gov.kpi.entity.MonitorRecord;
import com.trs.gov.kpi.entity.dao.QueryFilter;
import com.trs.gov.kpi.entity.exception.BizException;
import com.trs.gov.kpi.entity.requestdata.PageDataRequestParam;
import com.trs.gov.kpi.entity.responsedata.ApiPageData;
import com.trs.gov.kpi.entity.responsedata.MonitorOnceResponse;

import java.util.Date;
import java.util.List;

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
     * 插入手动开始时间
     * @param siteId
     * @param taskId
     * @param typeId
     * @param beginTime
     */
    void insertBeginManualMonitorRecord(Integer siteId, Integer taskId, Integer typeId, Date beginTime);

    /**
     * 获取手动监测最新的开始时间
     * @param filter
     * @return
     */
    Date getLastManualMonitorBeginTime(QueryFilter filter);

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

    /**
     * 获取本次检测记录结果
     * @param siteId
     * @param checkJobValues
     * @return
     */
    List<MonitorOnceResponse> selectMonitorResulrOnce(Integer siteId, List<Integer> checkJobValues);

    /**
     * 获取最新一次的检测记录
     * @param siteId
     * @param taskId
     * @return
     */
    List<MonitorRecord> selectNewestMonitorRecord(Integer siteId, Integer taskId);

    List<MonitorOnceResponse> getMonitorOnceResponse(List<MonitorRecord> monitorRecordList);
}

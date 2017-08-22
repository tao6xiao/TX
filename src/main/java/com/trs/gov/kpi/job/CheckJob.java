package com.trs.gov.kpi.job;

import com.trs.gov.kpi.constant.*;
import com.trs.gov.kpi.dao.CommonMapper;
import com.trs.gov.kpi.entity.MonitorRecord;
import com.trs.gov.kpi.entity.dao.DBUpdater;
import com.trs.gov.kpi.entity.dao.QueryFilter;
import com.trs.gov.kpi.entity.dao.Table;
import com.trs.gov.kpi.scheduler.SchedulerTask;
import com.trs.gov.kpi.service.MonitorRecordService;
import com.trs.gov.kpi.service.outer.SiteApiService;
import com.trs.gov.kpi.utils.LogUtil;
import com.trs.gov.kpi.utils.OuterApiServiceUtil;
import com.trs.gov.kpi.utils.SchedulerUtil;
import com.trs.gov.kpi.utils.SpringContextUtil;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;

import java.util.Date;

/**
 * 用于定时执行检查的job
 * <p>
 * Created by linwei on 2017-05-24.
 */
@Slf4j
public class CheckJob implements Job {

    private MonitorRecordService monitorRecordService = (MonitorRecordService) SpringContextUtil.getBean(MonitorRecordService.class);

    private CommonMapper commonMapper = (CommonMapper) SpringContextUtil.getBean(CommonMapper.class);

    private SiteApiService siteApiService = (SiteApiService) SpringContextUtil.getBean(SiteApiService.class);

    @Override
    public void execute(JobExecutionContext jobExecutionContext) {

        SchedulerTask task = (SchedulerTask) jobExecutionContext.getMergedJobDataMap().get("task");


        log.info(SchedulerUtil.getStartMessage(task.getName(), task.getSiteId()));
        LogUtil.addDebugLog(OperationType.TASK_SCHEDULE, DebugType.MONITOR_START, SchedulerUtil.getStartMessage(task.getName(), task.getSiteId()));
        Date startTime = new Date();
        Date manualMonitorBeginTime = null;
        try {
            OuterApiServiceUtil.checkSite(task.getSiteId(), siteApiService.getSiteById(task.getSiteId(), ""));

            if (task.getMonitorType() == Status.MonitorType.MANUAL_MONITOR.value) {
                manualMonitorBeginTime = toGetManualMonitorBeginTime(task);
            } else {
                //检测开始
                insertBeginMonitorRecord(task, startTime);
            }

            // TODO REVIEW DO_he.lang FIXED 日志记录到这边来
            final LogUtil.PerformanceLogRecorder performanceLogRecorder = new LogUtil.PerformanceLogRecorder(OperationType.TASK_SCHEDULE, task.getName() + "[siteId=" + task.getSiteId() +
                    "]");

            task.run();

            //检测结束
            if(task.getMonitorType() == Status.MonitorType.MANUAL_MONITOR.value){
                insertEndMonitorRecord(task, manualMonitorBeginTime, Status.MonitorStatusType.CHECK_DONE.value);
            }else {
                insertEndMonitorRecord(task, startTime, Status.MonitorStatusType.CHECK_DONE.value);
            }

            performanceLogRecorder.recordAlways();
        } catch (Exception e) {
            //TODO REVIEW  ran.wei DO_he.lang FIXED 日志描述错误 应该为任务调度
            String errorInfo = "任务调度[" + task.getName() + "]运行失败，站点siteId[" + task.getSiteId() + "]";
            log.error(errorInfo, e);
            LogUtil.addErrorLog(OperationType.TASK_SCHEDULE, ErrorType.TASK_SCHEDULE_FAILED, errorInfo, e);
            //检测失败
            insertEndMonitorRecord(task, startTime, Status.MonitorStatusType.CHECK_ERROR.value);
        } finally {
            // 记录结束
            String info = SchedulerUtil.getEndMessage(task.getName(), task.getSiteId());
            log.info(info);
            LogUtil.addDebugLog(OperationType.TASK_SCHEDULE, DebugType.MONITOR_END, info);
        }
    }

    /**
     * 获取手动监测最新一次开始的开始时间
     *
     * @param task
     * @return
     */
    private Date toGetManualMonitorBeginTime(SchedulerTask task) {
        QueryFilter filter = new QueryFilter(Table.MONITOR_RECORD);
        filter.addCond(MonitorRecordTableField.SITE_ID, task.getSiteId());
        filter.addCond(MonitorRecordTableField.TASK_ID, task.getCheckJobType().value);
        filter.addCond(MonitorRecordTableField.TYPE_ID, task.getMonitorType());
        filter.addSortField(MonitorRecordTableField.BEGIN_TIME, false);
        return monitorRecordService.getLastManualMonitorBeginTime(filter);
    }

    //检测开始，插入日志检测基本信息
    private void insertBeginMonitorRecord(SchedulerTask task, Date startTime) {
        MonitorRecord monitorRecord = new MonitorRecord();
        monitorRecord.setSiteId(task.getSiteId());
        monitorRecord.setTypeId(task.getMonitorType());
        monitorRecord.setTaskId(task.getCheckJobType().value);
        monitorRecord.setBeginTime(startTime);
        monitorRecord.setTaskStatus(Status.MonitorStatusType.DOING_CHECK.value);
        monitorRecordService.insertMonitorRecord(monitorRecord);
    }

    private void insertEndMonitorRecord(SchedulerTask task, Date startTime, Integer taskStatus) {
        Date endTime = new Date();
        QueryFilter filter = new QueryFilter(Table.MONITOR_RECORD);
        filter.addCond(MonitorRecordTableField.SITE_ID, task.getSiteId());
        filter.addCond(MonitorRecordTableField.TASK_ID, task.getCheckJobType().value);
        filter.addCond(MonitorRecordTableField.BEGIN_TIME, startTime);

        DBUpdater updater = new DBUpdater(Table.MONITOR_RECORD.getTableName());
        updater.addField(MonitorRecordTableField.RESULT, task.getMonitorResult());
        updater.addField(MonitorRecordTableField.END_TIME, endTime);
        updater.addField(MonitorRecordTableField.TASK_STATUS, taskStatus);
        commonMapper.update(updater, filter);
    }
}

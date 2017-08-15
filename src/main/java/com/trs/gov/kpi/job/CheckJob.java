package com.trs.gov.kpi.job;

import com.trs.gov.kpi.constant.DebugType;
import com.trs.gov.kpi.constant.ErrorType;
import com.trs.gov.kpi.constant.OperationType;
import com.trs.gov.kpi.scheduler.SchedulerTask;
import com.trs.gov.kpi.utils.LogUtil;
import com.trs.gov.kpi.utils.SchedulerUtil;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;

/**
 * 用于定时执行检查的job
 * <p>
 * Created by linwei on 2017-05-24.
 */
@Slf4j
public class CheckJob implements Job {

    @Override
    public void execute(JobExecutionContext jobExecutionContext) {
        SchedulerTask task = (SchedulerTask) jobExecutionContext.getMergedJobDataMap().get("task");
        log.info(SchedulerUtil.getStartMessage(task.getName(), task.getSiteId()));
        LogUtil.addDebugLog(OperationType.TASK_SCHEDULE, DebugType.MONITOR_START, SchedulerUtil.getStartMessage(task.getName(), task.getSiteId()));
        try {
            // TODO REVIEW DO_he.lang FIXED 日志记录到这边来
            final LogUtil.PerformanceLogRecorder performanceLogRecorder = new LogUtil.PerformanceLogRecorder(OperationType.TASK_SCHEDULE, task.getName() + "[siteId=" + task.getSiteId()+ "]");

            task.run();

            performanceLogRecorder.recordAlways();
        } catch (Exception e) {
            //TODO REVIEW  ran.wei DO_he.lang FIXED 日志描述错误 应该为任务调度
            String errorInfo = "任务调度[" + task.getName() + "]运行失败，站点siteId[" + task.getSiteId() + "]";
            log.error(errorInfo, e);
            LogUtil.addErrorLog(OperationType.TASK_SCHEDULE, ErrorType.TASK_SCHEDULE_FAILED, errorInfo, e);
        } finally {
            // 记录结束
            String info = SchedulerUtil.getEndMessage(task.getName(), task.getSiteId());
            log.info(info);
            LogUtil.addDebugLog(OperationType.TASK_SCHEDULE, DebugType.MONITOR_END, info);
        }
    }
}

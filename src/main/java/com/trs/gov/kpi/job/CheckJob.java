package com.trs.gov.kpi.job;

import com.trs.gov.kpi.constant.ErrorType;
import com.trs.gov.kpi.constant.OperationType;
import com.trs.gov.kpi.entity.exception.BizException;
import com.trs.gov.kpi.entity.exception.RemoteException;
import com.trs.gov.kpi.scheduler.SchedulerTask;
import com.trs.gov.kpi.utils.LogUtil;
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
        try {
            // TODO REVIEW 日志记录到这边来

            task.run();
        } catch (Exception e) {
            //TODO REVIEW  ran.wei DO_he.lang FIXED 日志描述错误 应该为任务调度
            log.error("", e);
            LogUtil.addErrorLog(OperationType.TASK_SCHEDULE, ErrorType.TASK_SCHEDULE_FAILED, "任务调度运行失败，站点siteId[" + task.getSiteId() + "]", e);
        } finally {
            // 记录结束

        }
    }
}

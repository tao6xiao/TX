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
            task.run();
        } catch (RemoteException | BizException e) {
            //TODO REVIEW  ran.wei  日志描述错误 应该为任务调度
            log.error("调用外部接口失败", e);
            LogUtil.addErrorLog(OperationType.REMOTE, ErrorType.REMOTE_FAILED, "调用外部接口失败", e);
        }
    }
}

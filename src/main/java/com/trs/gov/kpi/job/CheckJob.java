package com.trs.gov.kpi.job;

import com.trs.gov.kpi.scheduler.SchedulerTask;
import org.quartz.Job;
import org.quartz.JobExecutionContext;

/**
 * 用于定时执行检查的job
 *
 * Created by linwei on 2017-05-24.
 */
public class CheckJob implements Job {

    @Override
    public void execute(JobExecutionContext jobExecutionContext) {
        SchedulerTask task = (SchedulerTask)jobExecutionContext.getMergedJobDataMap().get("task");
        task.run();
    }
}

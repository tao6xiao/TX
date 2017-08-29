package com.trs.gov.kpi.scheduler;

import com.trs.gov.kpi.constant.DebugType;
import com.trs.gov.kpi.constant.OperationType;
import com.trs.gov.kpi.utils.LogUtil;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.quartz.Trigger;
import org.quartz.TriggerListener;

/**
 * Created by he.lang on 2017/8/23.
 */
@Slf4j
public class CustomizeTriggerListener implements TriggerListener {
    @Override
    public String getName() {
        return "CustomizeTriggerListener";
    }

    @Override
    public void triggerFired(Trigger trigger, JobExecutionContext context) {
        log.info("调度任务即将执行，CheckJob:" + trigger.getJobKey().getName());
    }

    @Override
    public boolean vetoJobExecution(Trigger trigger, JobExecutionContext context) {
        return false;
    }

    @Override
    public void triggerMisfired(Trigger trigger) {
        LogUtil.addDebugLog(OperationType.TASK_SCHEDULE, DebugType.MONITOR_WAIT, "调度任务即将执行，CheckJob:" + trigger.getJobKey().getName());

    }

    @Override
    public void triggerComplete(Trigger trigger, JobExecutionContext context, Trigger.CompletedExecutionInstruction triggerInstructionCode) {
        log.info("调度任务执行结束，CheckJob:" + trigger.getJobKey().getName());
    }
}

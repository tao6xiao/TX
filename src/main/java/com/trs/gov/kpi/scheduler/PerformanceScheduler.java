package com.trs.gov.kpi.scheduler;

import com.trs.gov.kpi.constant.DebugType;
import com.trs.gov.kpi.constant.ErrorType;
import com.trs.gov.kpi.constant.OperationType;
import com.trs.gov.kpi.constant.SchedulerRelated;
import com.trs.gov.kpi.dao.PerformanceMapper;
import com.trs.gov.kpi.entity.Performance;
import com.trs.gov.kpi.entity.exception.BizException;
import com.trs.gov.kpi.entity.exception.RemoteException;
import com.trs.gov.kpi.service.impl.PerformanceService;
import com.trs.gov.kpi.utils.LogUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by ranwei on 2017/6/19.
 */
@Slf4j
@Component
@Scope("prototype")
public class PerformanceScheduler implements SchedulerTask {


    @Setter
    @Getter
    private String baseUrl;

    @Setter
    @Getter
    private Integer siteId;

    @Resource
    private PerformanceService performanceService;

    @Resource
    private PerformanceMapper performanceMapper;

    @Setter
    @Getter
    private Boolean isTimeNode;

    @Override
    public void run() {
        log.info(SchedulerRelated.getStartMessage(SchedulerRelated.SchedulerType.PERFORMANCE_SCHEDULER.toString(), siteId));
        LogUtil.addDebugLog(OperationType.TASK_SCHEDULE, DebugType.MONITOR_START, SchedulerRelated.getStartMessage(SchedulerRelated.SchedulerType.PERFORMANCE_SCHEDULER.toString(), siteId));
        try {
            Date startTime = new Date();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            calendar.add(Calendar.HOUR, -1);//数据对应时间往前退一小时，使数据与时间对应
            Double score = performanceService.calPerformanceIndex(siteId);
            Performance performance = new Performance();
            performance.setSiteId(siteId);
            performance.setIndex(score);
            performance.setCheckTime(calendar.getTime());
            performanceMapper.insert(performance);
            Date endTime = new Date();
            LogUtil.addElapseLog(OperationType.TASK_SCHEDULE, SchedulerRelated.SchedulerType.PERFORMANCE_SCHEDULER.toString(), endTime.getTime()-startTime.getTime());
        } catch (BizException | RemoteException e) {
            log.error("", e);
            LogUtil.addErrorLog(OperationType.TASK_SCHEDULE, ErrorType.REQUEST_FAILED, "", e);
        }finally {
            log.info(SchedulerRelated.getEndMessage(SchedulerRelated.SchedulerType.PERFORMANCE_SCHEDULER.toString(), siteId));
            LogUtil.addDebugLog(OperationType.TASK_SCHEDULE, DebugType.MONITOR_END, SchedulerRelated.getEndMessage(SchedulerRelated.SchedulerType.PERFORMANCE_SCHEDULER.toString(), siteId));
        }
    }

}

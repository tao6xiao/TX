package com.trs.gov.kpi.scheduler;

import com.trs.gov.kpi.constant.*;
import com.trs.gov.kpi.dao.PerformanceMapper;
import com.trs.gov.kpi.entity.Performance;
import com.trs.gov.kpi.entity.exception.BizException;
import com.trs.gov.kpi.entity.exception.RemoteException;
import com.trs.gov.kpi.service.impl.PerformanceService;
import com.trs.gov.kpi.utils.LogUtil;
import com.trs.gov.kpi.utils.SchedulerUtil;
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

    //站点监测状态（0：自动监测；1：手动监测）
    @Setter
    private Integer monitorType;

    @Override
    public void run() {
        log.info(SchedulerUtil.getStartMessage(SchedulerType.PERFORMANCE_SCHEDULER.toString(), siteId));
        LogUtil.addDebugLog(OperationType.TASK_SCHEDULE, DebugType.MONITOR_START, SchedulerUtil.getStartMessage(SchedulerType.PERFORMANCE_SCHEDULER.toString(), siteId));
        try {

            final LogUtil.PerformanceLogRecorder performanceLogRecorder = new LogUtil.PerformanceLogRecorder(OperationType.TASK_SCHEDULE, SchedulerType.PERFORMANCE_SCHEDULER + "[siteId=" + siteId + "]");

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            calendar.add(Calendar.HOUR, -1);//数据对应时间往前退一小时，使数据与时间对应
            Double score = performanceService.calPerformanceIndex(siteId);
            Performance performance = new Performance();
            performance.setSiteId(siteId);
            performance.setIndex(score);
            performance.setCheckTime(calendar.getTime());
            performanceMapper.insert(performance);
            performanceLogRecorder.recordAlways();
        } catch (BizException | RemoteException e) {
            String errorDesc = "绩效指数计算失败! [siteId=" + siteId + "]";
            log.error(errorDesc, e);
            LogUtil.addErrorLog(OperationType.TASK_SCHEDULE, ErrorType.REQUEST_FAILED, errorDesc, e);
        } finally {
            log.info(SchedulerUtil.getEndMessage(SchedulerType.PERFORMANCE_SCHEDULER.toString(), siteId));
            LogUtil.addDebugLog(OperationType.TASK_SCHEDULE, DebugType.MONITOR_END, SchedulerUtil.getEndMessage(SchedulerType.PERFORMANCE_SCHEDULER.toString(), siteId));
        }
    }

}

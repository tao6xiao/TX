package com.trs.gov.kpi.scheduler;

import com.trs.gov.kpi.dao.PerformanceMapper;
import com.trs.gov.kpi.entity.Performance;
import com.trs.gov.kpi.entity.exception.RemoteException;
import com.trs.gov.kpi.service.impl.PerformanceService;
import com.trs.gov.kpi.utils.LogUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.text.ParseException;
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
        log.info("PerformanceScheduler " + siteId + " start...");
        try {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            calendar.add(Calendar.HOUR, -1);//数据对应时间往前退一小时，使数据与时间对应
            Double score = performanceService.calPerformanceIndex(siteId);
            Performance performance = new Performance();
            performance.setSiteId(siteId);
            performance.setIndex(score);
            performance.setCheckTime(calendar.getTime());
            performanceMapper.insert(performance);
        } catch (ParseException | RemoteException e) {
            log.error("", e);
            LogUtil.addSystemLog("", e);
        }
        log.info("PerformanceScheduler " + siteId + " end...");
    }

}

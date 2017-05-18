package com.trs.gov.kpi.service.impl;

import com.alibaba.fastjson.JSON;
import com.trs.gov.kpi.constant.FreqUnit;
import com.trs.gov.kpi.constant.FrequencyType;
import com.trs.gov.kpi.dao.MonitorFrequencyMapper;
import com.trs.gov.kpi.entity.MonitorFrequency;
import com.trs.gov.kpi.entity.MonitorSite;
import com.trs.gov.kpi.scheduler.LinkAnalysisScheduler;
import com.trs.gov.kpi.scheduler.SchedulerManager;
import com.trs.gov.kpi.scheduler.SchedulerTask;
import com.trs.gov.kpi.service.MonitorFrequencyService;
import com.trs.gov.kpi.service.MonitorSiteService;
import com.trs.gov.kpi.service.SchedulerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by wangxuan on 2017/5/11.
 */
@Slf4j
@Service
public class SchedulerServiceImpl implements SchedulerService {

    @Resource
    SchedulerTask[] schedulerTasks;

    @Resource
    SchedulerManager schedulerManager;

    @Resource
    MonitorSiteService monitorSiteService;

    @Resource
    MonitorFrequencyMapper monitorFrequencyMapper;

    @Resource
    ApplicationContext applicationContext;

    /**
     * 初始化scheduler
     * TODO:按照数据库配置进行装配
     */
    @PostConstruct
    public void init() {

        try {

            log.info("init scheduler model...");

//            List<MonitorSite> monitorSites = monitorSiteService.getAllMonitorSites();
//            for(MonitorSite monitorSite: monitorSites) {
//
//                if(monitorSite.getSiteId() != null) {
//
//                    List<MonitorFrequency> monitorFrequencies = monitorFrequencyMapper.queryBySiteId(monitorSite.getSiteId());
//                    for(MonitorFrequency monitorFrequency: monitorFrequencies) {
//
//                        try {
//
//                            registerScheduler(
//                                    monitorSite.getIndexUrl(),
//                                    monitorSite.getSiteId(),
//                                    FrequencyType.getFrequencyTypeByTypeId(monitorFrequency.getTypeId()),
//                                    FrequencyType.getFrequencyTypeByTypeId(monitorFrequency.getTypeId()).getFreqUnit(),
//                                    monitorFrequency.getValue().intValue());
//                        } catch (Exception e) {
//
//                            log.error("register scheduler {} error!", JSON.toJSONString(monitorFrequency));
//                        }
//                    }
//                }
//            }

            log.info("init scheduler model completed!");
        } catch (Exception e) {

            log.error("init scheduler model error!", e);
        }
    }

    public void registerScheduler(String baseUrl, Integer siteId, FrequencyType frequencyType, FreqUnit freqUnit, Integer freq) {

        SchedulerTask schedulerTask = applicationContext.getBean(frequencyType.getScheduler());
        schedulerTask.setBaseUrl(baseUrl);
        schedulerTask.setSiteId(siteId);
        addFreqUnitAndFreq(freqUnit, freq, schedulerTask);
        schedulerManager.registerScheduler(schedulerTask);
    }

    private void addFreqUnitAndFreq(FreqUnit freqUnit, Integer freq, SchedulerTask schedulerTask) {

        if(FreqUnit.DAYS_PER_TIME == freqUnit) {

            schedulerTask.setDelayAndTimeUnit(freq.longValue(), TimeUnit.DAYS);
        } else if(FreqUnit.TIMES_PER_DAY == freqUnit) {

            Long totalMinutesPerDay = TimeUnit.DAYS.toMinutes(1);
            schedulerTask.setDelayAndTimeUnit(totalMinutesPerDay / freq, TimeUnit.MINUTES);
        }
    }
}

package com.trs.gov.kpi.service.impl;

import com.trs.gov.kpi.scheduler.SchedulerManager;
import com.trs.gov.kpi.scheduler.SchedulerTask;
import com.trs.gov.kpi.service.SchedulerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

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

    /**
     * 初始化scheduler
     */
    @PostConstruct
    public void init() {

        try {

            log.info("init scheduler model...");
            for(SchedulerTask schedulerTask: schedulerTasks) {

                schedulerManager.registerScheduler(schedulerTask);
                log.info("register scheduler {} completed!", schedulerTask.getName());
            }
            log.info("init scheduler model completed!");
        } catch (Exception e) {

            log.error("init scheduler model error!", e);
        }
    }
}

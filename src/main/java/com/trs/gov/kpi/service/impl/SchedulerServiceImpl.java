package com.trs.gov.kpi.service.impl;

import com.trs.gov.kpi.scheduler.LinkAnalysisScheduler;
import com.trs.gov.kpi.scheduler.SchedulerManager;
import com.trs.gov.kpi.scheduler.SchedulerTask;
import com.trs.gov.kpi.service.SchedulerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

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
     * TODO:按照数据库配置进行装配
     */
    @PostConstruct
    public void init() {

        try {

            log.info("init scheduler model...");

            Map<String, Integer> testUrlMap = new HashMap<>();
            //testUrlMap.put("http://www.wusheng.gov.cn", 1);
            //testUrlMap.put("http://www.scyc.gov.cn", 2);

            for(SchedulerTask schedulerTask: schedulerTasks) {

                if(LinkAnalysisScheduler.class.isInstance(schedulerTask)) {

                    LinkAnalysisScheduler linkAnalysisScheduler = LinkAnalysisScheduler.class.cast(schedulerTask);
                    linkAnalysisScheduler.setBaseUrlAndSiteIdMap(testUrlMap);
                }
                schedulerManager.registerScheduler(schedulerTask);
                log.info("register scheduler {} completed!", schedulerTask.getName());
            }
            log.info("init scheduler model completed!");
        } catch (Exception e) {

            log.error("init scheduler model error!", e);
        }
    }
}

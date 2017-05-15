package com.trs.gov.kpi.scheduler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;

/**
 * Created by wangxuan on 2017/5/11.
 */
@Component("schedulerManager")
@Slf4j
public class SchedulerManager {

    private static final Integer EXECUTOR_THREAD_NUMBER = 1;

    private Map<String, ScheduledExecutorService> scheduledMap = Collections.synchronizedMap(new HashMap<String, ScheduledExecutorService>());

    public void registerScheduler(SchedulerTask schedulerTask) {

        if(schedulerTask.getDelay() == null || schedulerTask.getTimeUnit() == null) {

            log.error("scheduler {} dose not initialed!", schedulerTask.getName());
            return;
        }

        ScheduledExecutorService executorService = scheduledMap.get(schedulerTask.getName());
        if(executorService != null) {

            executorService.shutdown();
        }

        ScheduledExecutorService newExecutorService = new ScheduledThreadPoolExecutor(EXECUTOR_THREAD_NUMBER);
        scheduledMap.put(schedulerTask.getName(), newExecutorService);
        newExecutorService.scheduleAtFixedRate(schedulerTask.getTask(), 0, schedulerTask.getDelay(), schedulerTask.getTimeUnit());
    }
}

package com.trs.gov.kpi.scheduler;

import com.alibaba.fastjson.JSON;
import com.trs.gov.kpi.constant.FrequencyType;
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

    private Map<Map<Integer, FrequencyType>, ScheduledExecutorService> executorServiceMap =
            Collections.synchronizedMap(new HashMap<Map<Integer, FrequencyType>, ScheduledExecutorService>());

    public void registerScheduler(SchedulerTask schedulerTask) {

        if(schedulerTask.getDelay() == null || schedulerTask.getTimeUnit() == null) {

            log.error("scheduler {} dose not initialed!", JSON.toJSONString(schedulerTask));
            return;
        }

        ScheduledExecutorService executorService = executorServiceMap.get(
                Collections.singletonMap(schedulerTask.getSiteId(), FrequencyType.getFrequencyTypeByScheduler(schedulerTask)));
        if(executorService != null) {

            executorService.shutdown();
        }

        ScheduledExecutorService newExecutorService = new ScheduledThreadPoolExecutor(EXECUTOR_THREAD_NUMBER);

        executorServiceMap.put(
                Collections.singletonMap(schedulerTask.getSiteId(), FrequencyType.getFrequencyTypeByScheduler(schedulerTask)),
                newExecutorService);
        newExecutorService.scheduleAtFixedRate(schedulerTask.getTask(), 0, schedulerTask.getDelay(), schedulerTask.getTimeUnit());
    }


}

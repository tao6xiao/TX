package com.trs.gov.kpi.scheduler;

import java.util.concurrent.TimeUnit;

/**
 * Created by wangxuan on 2017/5/11.
 * 所有定时任务的接口
 */
public interface SchedulerTask {

    Long DEFAULT_TIME_DELAY = 1l;

    TimeUnit DEFAULT_TIME_UNIT = TimeUnit.DAYS;

    Long getDelay();

    TimeUnit getTimeUnit();

    Runnable getTask();

    String getBaseUrl();

    Integer getSiteId();

    void setBaseUrl(String baseUrl);

    void setSiteId(Integer siteId);

    void setDelayAndTimeUnit(Long delay, TimeUnit timeUnit);
}

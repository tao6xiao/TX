package com.trs.gov.kpi.scheduler;

import lombok.Getter;
import org.springframework.util.Assert;

import java.util.concurrent.TimeUnit;

/**
 * Created by wangxuan on 2017/5/11.
 */
public abstract class AbstractScheduler implements SchedulerTask {

    @Getter
    private Long delay = DEFAULT_TIME_DELAY;

    @Getter
    private TimeUnit timeUnit = DEFAULT_TIME_UNIT;

    @Override
    public void setDelayAndTimeUnit(Long delay, TimeUnit timeUnit) {

        Assert.isTrue(delay != null && timeUnit != null, "invalid param!");
        this.delay = delay;
        this.timeUnit = timeUnit;
    }
}

package com.trs.gov.kpi.constant;

import com.trs.gov.kpi.scheduler.HomePageCheckScheduler;
import com.trs.gov.kpi.scheduler.LinkAnalysisScheduler;
import com.trs.gov.kpi.scheduler.SchedulerTask;
import lombok.Getter;

/**
 * Created by he.lang on 2017/5/16.
 */
public enum FrequencyType {

    HOMEPAGE_AVAILABILITY(1, "首页可用性", FreqUnit.TIMES_PER_DAY, HomePageCheckScheduler.class),
    TOTAL_BROKEN_LINKS(2, "全站失效链接", FreqUnit.DAYS_PER_TIME, LinkAnalysisScheduler.class),
    WRONG_INFORMATION(3, "信息错误", FreqUnit.DAYS_PER_TIME, SchedulerTask.class);

    @Getter
    private int typeId;//类型id

    @Getter
    private String name;//类型名

    @Getter
    private FreqUnit freqUnit;//粒度

    @Getter
    private Class<? extends SchedulerTask> scheduler;

    private FrequencyType(int typeId, String name,FreqUnit freqUnit, Class scheduler) {
        this.typeId = typeId;
        this.name = name;
        this.freqUnit = freqUnit;
        this.scheduler = scheduler;
    }

    public static FrequencyType getFrequencyTypeByTypeId(int typeId) {
        for (FrequencyType frequencyType : FrequencyType.values()) {
            if (frequencyType.getTypeId() == typeId) {
                return frequencyType;
            }
        }
        return null;
    }

    public static FrequencyType getFrequencyTypeByScheduler(SchedulerTask schedulerTask) {

        for(FrequencyType frequencyType: FrequencyType.values()) {

            if(frequencyType.getScheduler().isInstance(schedulerTask)) {

                return frequencyType;
            }
        }
        return null;
    }
}

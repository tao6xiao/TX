package com.trs.gov.kpi.utils;

/**
 * 调度相关：调度类型等
 * Created by he.lang on 2017/8/4.
 */
public class SchedulerUtil {

    private SchedulerUtil() {
    }
    
    public static String getStartMessage(String schedulerType, Integer siteId) {
        return schedulerType + "[siteId=" + siteId + "] start...";
    }

    public static String getEndMessage(String schedulerType, Integer siteId) {
        return schedulerType + "[siteId=" + siteId + "] end...";
    }
}

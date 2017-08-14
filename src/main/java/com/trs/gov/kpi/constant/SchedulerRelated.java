package com.trs.gov.kpi.constant;

/**
 * 调度相关：调度类型等
 * Created by he.lang on 2017/8/4.
 */
public class SchedulerRelated {

    // TODO REVIEW DONE_he.lang FIXED 修改为枚举类型，解决名字冲突

    public enum SchedulerType {
        CKM_SCHEDULER,
        HOMEPAGE_CHECK_SCHEDULER,
        INFO_UPDATE_CHECK_SCHEDULER,
        LINK_ANALYSIS_SCHEDULER,
        PERFORMANCE_SCHEDULER,
        REPORT_GENERATE_SCHEDULER,
        SERVICE_LINK_SCHEDULER
    }

    private SchedulerRelated() {
    }

    // TODO REVIEW DONE_he.lang FIXED 修改方法名
    public static String getStartMessage(String schedulerType, Integer siteId) {
        return schedulerType + "[siteId=" + siteId + "] start...";
    }

    public static String getEndMessage(String schedulerType, Integer siteId) {
        return schedulerType + "[siteId=" + siteId + "] end...";
    }
}

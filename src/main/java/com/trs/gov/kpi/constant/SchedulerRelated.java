package com.trs.gov.kpi.constant;

/**
 * 调度相关：调度类型等
 * Created by he.lang on 2017/8/4.
 */
public class SchedulerRelated {

    // TODO REVIEW DONE_he.lang FIXED 修改为枚举类型，解决名字冲突
    // TODO REVIEW DO_he.lang FIXED 这个枚举类放这不合适，可以单独成为一个类了，不用放在SchedulerRelated下面

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

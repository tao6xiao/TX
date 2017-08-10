package com.trs.gov.kpi.constant;


/**
 * Created by he.lang on 2017/8/4.
 */
public class SchedulerType {

    // TODO REVIEW DO_he.lang 修改为枚举类型，解决名字冲突

    public static final String CKM_SCHEDULER = "CKMScheduler ";

    public static final String HOMEPAGE_CHECK_SCHEDULER = "CKMScheduler ";

    public static final String INFO_UPDATE_CHECK_SCHEDULER = "InfoUpdateCheckScheduler ";

    public static final String LINK_ANALYSIS_SCHEDULER = "LinkAnalysisScheduler ";

    public static final String PERFORMANCE_SCHEDULER = "PerformanceScheduler ";

    public static final String REPORT_GENERATE_SCHEDULER = "ReportGenerateScheduler ";

    public static final String SERVICE_LINK_SCHEDULER = "ServiceLinkScheduler ";

    private SchedulerType(){}

    // TODO REVIEW DO_he.lang 修改方法名
    public static String startScheduler(String schedulerType, Integer siteId){
        return schedulerType + siteId + " start...";
    }

    public static String endScheduler(String schedulerType, Integer siteId){
        return schedulerType + siteId + " end...";
    }
}

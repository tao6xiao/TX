package com.trs.gov.kpi.constant;


/**
 * Created by he.lang on 2017/8/4.
 */
public class SchedulerType {

    public static final String CKM_SCHEDULER = "CKMScheduler ";

    public static final String HOMEPAGE_CHECK_SCHEDULER = "CKMScheduler ";

    public static final String INFO_UPDATE_CHECK_SCHEDULER = "InfoUpdateCheckScheduler ";

    public static final String LINK_ANALYSIS_SCHEDULER = "LinkAnalysisScheduler ";

    public static final String PERFORMANCE_SCHEDULER = "PerformanceScheduler ";

    public static final String REPORT_GENERATE_SCHEDULER = "ReportGenerateScheduler ";

    public static final String SERVICE_LINK_SCHEDULER = "ServiceLinkScheduler ";

    private SchedulerType(){}

    public static String schedulerStart(String schedulerType, Integer siteId){
        return schedulerType + siteId + " start...";
    }

    public static String schedulerEnd(String schedulerType, Integer siteId){
        return schedulerType + siteId + " end...";
    }
}

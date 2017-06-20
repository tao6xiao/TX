package com.trs.gov.kpi.scheduler;

/**
 * Created by wangxuan on 2017/5/11.
 * 所有定时任务的接口
 */
public interface SchedulerTask {

    void run();

    Integer getSiteId();

    void setBaseUrl(String baseUrl);

    void setSiteId(Integer siteId);

    Boolean getIsTimeNode();

    void setIsTimeNode(Boolean isTimeNode);
}

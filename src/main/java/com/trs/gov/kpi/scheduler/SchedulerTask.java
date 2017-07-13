package com.trs.gov.kpi.scheduler;

import com.trs.gov.kpi.entity.exception.RemoteException;

/**
 * Created by wangxuan on 2017/5/11.
 * 所有定时任务的接口
 */
public interface SchedulerTask extends Runnable {

    void run();

    Integer getSiteId();

    void setBaseUrl(String baseUrl);

    void setSiteId(Integer siteId);

    Boolean getIsTimeNode();

    void setIsTimeNode(Boolean isTimeNode);
}

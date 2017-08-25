package com.trs.gov.kpi.scheduler;

import com.trs.gov.kpi.constant.EnumCheckJobType;
import com.trs.gov.kpi.entity.exception.BizException;
import com.trs.gov.kpi.entity.exception.RemoteException;

/**
 * Created by wangxuan on 2017/5/11.
 * 所有定时任务的接口
 */
public interface SchedulerTask {

    void run() throws RemoteException, BizException;

    Integer getSiteId();

    void setSiteId(Integer siteId);

    Boolean getIsTimeNode();

    void setIsTimeNode(Boolean isTimeNode);

    //站点监测状态（0：自动监测；1：手动监测）
    void setMonitorType(int typeId);

    int getMonitorType();

    String getName();

    EnumCheckJobType getCheckJobType();

    int getMonitorResult();

}

package com.trs.gov.kpi.service;

import com.trs.gov.kpi.constant.EnumCheckJobType;
import com.trs.gov.kpi.entity.exception.BizException;
import com.trs.gov.kpi.entity.exception.RemoteException;

/**
 * Created by wangxuan on 2017/5/11.
 */
public interface SchedulerService {

    /**
     * 添加一个栏目更新监测任务
     *
     * @param siteId
     */
    void addCheckJob(int siteId, EnumCheckJobType checkType) throws BizException;

    /**
     * 移除一个栏目更新监测任务
     *
     * @param siteId
     */
    void removeCheckJob(int siteId, EnumCheckJobType checkType) throws BizException;

    /**
     * 执行一次监测
     *
     * @param siteId
     */
    void doCheckJobOnce(int siteId, EnumCheckJobType checkJobType) throws BizException, RemoteException;

}

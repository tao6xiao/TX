package com.trs.gov.kpi.service;

import com.trs.gov.kpi.constant.EnumCheckJobType;
import com.trs.gov.kpi.constant.FreqUnit;
import com.trs.gov.kpi.constant.FrequencyType;

/**
 * Created by wangxuan on 2017/5/11.
 */
public interface SchedulerService {

    /**
     * 添加一个栏目更新监测任务
     *
     * @param siteId
     */
    void addCheckJob(int siteId, EnumCheckJobType checkType);

    /**
     * 移除一个栏目更新监测任务
     *
     * @param siteId
     */
    void removeCheckJob(int siteId, EnumCheckJobType checkType);

}

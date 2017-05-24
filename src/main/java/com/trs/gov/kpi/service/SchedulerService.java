package com.trs.gov.kpi.service;

import com.trs.gov.kpi.constant.FreqUnit;
import com.trs.gov.kpi.constant.FrequencyType;

/**
 * Created by wangxuan on 2017/5/11.
 */
public interface SchedulerService {

    /**
     * 注册一个新的定时任务或者替换掉原来的定时任务
     * @param baseUrl 检测url
     * @param siteId 站点id
     * @param frequencyType 检测类型
     * @param freqUnit 检测频率类型
     * @param freq 检测频率值
     */
    void registerScheduler(String baseUrl, Integer siteId, FrequencyType frequencyType, FreqUnit freqUnit, Integer freq);


    /**
     * 添加一个首页监测任务
     *
     * @param siteId
     * @param indexUrl
     */
    void addHomePageCheckJob(int siteId, String indexUrl);

    /**
     * 移除一个首页监测任务
     *
     * @param siteId
     * @param indexUrl
     */
    void removeHomePageCheckJob(int siteId, String indexUrl);
}

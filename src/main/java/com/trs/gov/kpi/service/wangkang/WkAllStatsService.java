package com.trs.gov.kpi.service.wangkang;

import com.trs.gov.kpi.entity.exception.BizException;
import com.trs.gov.kpi.entity.responsedata.WkAvgSpeedResponse;
import com.trs.gov.kpi.entity.responsedata.WkUpdateContentResponse;
import com.trs.gov.kpi.entity.wangkang.WkAllStats;

import java.util.List;

/**
 * Created by li.hao on 2017/7/12.
 */
public interface WkAllStatsService {

    /**
     * 访问速度---查询网站平均访问速度历史记录
     *
     * @return
     */
    List<WkAvgSpeedResponse> getAvgSpeedHistory(Integer siteId, Integer checkId);

    /**
     * 网站更新---查询网站每次更新数量的历史记录
     *
     * @return
     */
    List<WkUpdateContentResponse> getUpdateContentHistory(Integer siteId, Integer checkId);

    /**
     * 添加一次检测的网站更新数
     *
     * @param wkAllStats
     */
    void insertOrUpdateUpdateContentAndSpeed(WkAllStats wkAllStats) throws BizException;

    /**
     * 插入或者更新失效链接个数
     *
     * @param wkAllStats
     */
    void insertOrUpdateInvalidLink(WkAllStats wkAllStats);

    /**
     * 插入或者更新错别字，敏感词等个数
     *
     * @param wkAllStats
     */
    void insertOrUpdateErrorWords(WkAllStats wkAllStats);

    /**
     * 获取某个站点上一次的检查编号
     * @param siteId
     * @return
     */
    Integer getLastCheckId(Integer siteId, Integer checkId);

    /**
     * 根据网站名称和检查编号查询最近一次网站指标
     * @param siteId
     * @param checkId
     * @return
     */
    WkAllStats getLastTimeCheckBySiteIdAndCheckId(Integer siteId, Integer checkId);
}

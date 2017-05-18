package com.trs.gov.kpi.service;

/**
 * 综合实时监测预警提醒service
 * Created by he.lang on 2017/5/18.
 */
public interface IntegratedMonitorWarningService {

    /**
     * 处理预警提醒
     *
     * @param siteId
     * @param ids
     * @return
     */
    int dealWithWarningBySiteIdAndId(int siteId, Integer[] ids);

    /**
     * 忽略预警提醒
     *
     * @param siteId
     * @param ids
     * @return
     */
    int ignoreWarningBySiteIdAndId(int siteId, Integer[] ids);

    /**
     * 删除预警提醒
     *
     * @param siteId
     * @param ids
     * @return
     */
    int deleteWarningBySiteIdAndId(int siteId, Integer[] ids);
}

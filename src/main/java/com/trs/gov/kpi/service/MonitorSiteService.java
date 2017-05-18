package com.trs.gov.kpi.service;

import com.trs.gov.kpi.entity.MonitorSite;
import com.trs.gov.kpi.entity.MonitorSiteDeal;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by HLoach on 2017/5/11.
 */
public interface MonitorSiteService {

    /**
     * 通过siteId获取MonitorSite，然后转换为MonitorSiteDeal，用于监测站点设置查询
     * @param siteId
     * @return
     */
    MonitorSiteDeal getMonitorSiteDealBySiteId(Integer siteId);

    /**
     * 通过siteId获取MonitorSite
     * @param siteId
     * @return
     */
    MonitorSite getMonitorSiteBySiteId(Integer siteId);

    /**
     *  添加MonitorSite记录
     * @param monitorSiteDeal
     * @return
     */
    int addMonitorSite(MonitorSiteDeal monitorSiteDeal);

    /**
     * 通过siteId更新MonitorSite对应记录
     * @param monitorSiteDeal
     * @return
     */
    int updateMonitorSiteBySiteId(MonitorSiteDeal monitorSiteDeal);

    /**
     * 获取全部的监控信息
     * @return
     */
    List<MonitorSite> getAllMonitorSites();

}

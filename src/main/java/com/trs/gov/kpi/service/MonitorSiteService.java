package com.trs.gov.kpi.service;

import com.trs.gov.kpi.entity.MonitorSite;
import com.trs.gov.kpi.entity.MonitorSiteDeal;
import org.springframework.stereotype.Service;

/**
 * Created by HLoach on 2017/5/11.
 */
@Service
public interface MonitorSiteService {
    MonitorSiteDeal getMonitorSiteDealBySiteId(Integer siteId);

    MonitorSite getMonitorSiteBySiteId(Integer siteId);

    int addMonitorSite(MonitorSite monitorSite);

    int updateMonitorSiteBySiteId(MonitorSiteDeal monitorSiteDeal);

}

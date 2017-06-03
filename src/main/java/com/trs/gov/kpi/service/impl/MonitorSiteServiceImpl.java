package com.trs.gov.kpi.service.impl;

import com.trs.gov.kpi.dao.MonitorSiteMapper;
import com.trs.gov.kpi.entity.MonitorSite;
import com.trs.gov.kpi.entity.MonitorSiteDeal;
import com.trs.gov.kpi.service.MonitorSiteService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by HLoach on 2017/5/11.
 */
@Service
public class MonitorSiteServiceImpl implements MonitorSiteService {
    @Resource
    MonitorSiteMapper monitorSiteMapper;

    @Override
    public MonitorSiteDeal getMonitorSiteDealBySiteId(Integer siteId) {
        MonitorSite monitorSite = monitorSiteMapper.selectByPrimaryKey(siteId);
        MonitorSiteDeal monitorSiteDeal = null;
        if (monitorSite != null) {
            monitorSiteDeal = getMonitorSiteDealFromMonitorSiteAndSiteIdsArray(monitorSite);
        }
        return monitorSiteDeal;
    }

    @Override
    public MonitorSite getMonitorSiteBySiteId(Integer siteId) {
        return monitorSiteMapper.selectByPrimaryKey(siteId);
    }

    @Override
    public int addMonitorSite(MonitorSiteDeal monitorSiteDeal) {
        MonitorSite monitorSite = getMonitorSiteFromMonitorSiteDealAndSiteIds(monitorSiteDeal);
        return monitorSiteMapper.insert(monitorSite);
    }

    @Override
    public int updateMonitorSiteBySiteId(MonitorSiteDeal monitorSiteDeal) {
        MonitorSite monitorSite = getMonitorSiteFromMonitorSiteDealAndSiteIds(monitorSiteDeal);
        return monitorSiteMapper.updateByPrimaryKey(monitorSite);
    }

    @Override
    public List<MonitorSite> getAllMonitorSites() {
        return monitorSiteMapper.getAllMonitorSites();
    }

    private MonitorSite getMonitorSiteFromMonitorSiteDealAndSiteIds(MonitorSiteDeal monitorSiteDeal) {
        MonitorSite monitorSite = new MonitorSite();
        monitorSite.setSiteId(monitorSiteDeal.getSiteId());
        monitorSite.setDepartmentName(monitorSiteDeal.getDepartmentName());
        monitorSite.setGuarderName(monitorSiteDeal.getGuarderName());
        monitorSite.setGuarderAccount(monitorSiteDeal.getGuarderAccount());
        monitorSite.setGuarderPhone(monitorSiteDeal.getGuarderPhone());
        monitorSite.setIndexUrl(monitorSiteDeal.getIndexUrl());
        return monitorSite;
    }

    private MonitorSiteDeal getMonitorSiteDealFromMonitorSiteAndSiteIdsArray(MonitorSite monitorSite) {
        MonitorSiteDeal monitorSiteDeal = new MonitorSiteDeal();
        monitorSiteDeal.setSiteId(monitorSite.getSiteId());
        monitorSiteDeal.setDepartmentName(monitorSite.getDepartmentName());
        monitorSiteDeal.setGuarderName(monitorSite.getGuarderName());
        monitorSiteDeal.setGuarderAccount(monitorSite.getGuarderAccount());
        monitorSiteDeal.setGuarderPhone(monitorSite.getGuarderPhone());
        monitorSiteDeal.setIndexUrl(monitorSite.getIndexUrl());
        return monitorSiteDeal;
    }

}

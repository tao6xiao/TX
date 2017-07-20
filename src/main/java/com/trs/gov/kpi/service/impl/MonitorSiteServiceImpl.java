package com.trs.gov.kpi.service.impl;

import com.trs.gov.kpi.dao.MonitorSiteMapper;
import com.trs.gov.kpi.entity.MonitorSite;
import com.trs.gov.kpi.entity.MonitorSiteDeal;
import com.trs.gov.kpi.entity.exception.RemoteException;
import com.trs.gov.kpi.service.MonitorSiteService;
import com.trs.gov.kpi.service.outer.UserApiService;
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

    @Resource
    UserApiService userApiService;

    @Override
    public MonitorSiteDeal getMonitorSiteDealBySiteId(Integer siteId) throws RemoteException {
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
        monitorSite.setGuarderId(monitorSiteDeal.getGuarderId());
        monitorSite.setIndexUrl(monitorSiteDeal.getIndexUrl());
        return monitorSite;
    }

    private MonitorSiteDeal getMonitorSiteDealFromMonitorSiteAndSiteIdsArray(MonitorSite monitorSite) throws RemoteException {
        if (monitorSite.getGuarderId() == null) {
            return new MonitorSiteDeal();
        }
        MonitorSiteDeal monitorSiteDeal = new MonitorSiteDeal();
        monitorSiteDeal.setSiteId(monitorSite.getSiteId());
        monitorSiteDeal.setDepartmentName(monitorSite.getDepartmentName());
        monitorSiteDeal.setGuarderId(monitorSite.getGuarderId());
        monitorSiteDeal.setGuarderName(userApiService.findUserById("", monitorSite.getGuarderId()).getTrueName());
        monitorSiteDeal.setGuarderAccount(userApiService.findUserById("", monitorSite.getGuarderId()).getUserName());
        monitorSiteDeal.setGuarderPhone(userApiService.findUserById("", monitorSite.getGuarderId()).getMobile());
        monitorSiteDeal.setIndexUrl(monitorSite.getIndexUrl());
        return monitorSiteDeal;
    }

}

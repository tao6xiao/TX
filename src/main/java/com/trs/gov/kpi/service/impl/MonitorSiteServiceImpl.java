package com.trs.gov.kpi.service.impl;

import com.trs.gov.kpi.dao.MonitorSiteMapper;
import com.trs.gov.kpi.entity.MonitorSite;
import com.trs.gov.kpi.entity.MonitorSiteDeal;
import com.trs.gov.kpi.entity.exception.BizException;
import com.trs.gov.kpi.service.MonitorSiteService;
import com.trs.gov.kpi.utils.DataTypeConversion;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;

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
            String siteIds = monitorSite.getSiteIds();
            Integer[] siteIdsArrayForInteger = DataTypeConversion.stringToIntegerArray(siteIds);
            monitorSiteDeal = getMonitorSiteDealFromMonitorSiteAndSiteIdsArray(monitorSite, siteIdsArrayForInteger);
        }
        return monitorSiteDeal;
    }

    @Override
    public MonitorSite getMonitorSiteBySiteId(Integer siteId) {
        MonitorSite monitorSite = monitorSiteMapper.selectByPrimaryKey(siteId);
        return monitorSite;
    }

    @Override
    public int addMonitorSite(MonitorSiteDeal monitorSiteDeal) {
        Integer[] siteIdsForInteger = monitorSiteDeal.getSiteIds();
        String siteIdsForString = DataTypeConversion.integerArrayToString(siteIdsForInteger);
        int num = 0;
        if (siteIdsForString != null) {
            MonitorSite monitorSite = getMonitorSiteFromMonitorSiteDealAndSiteIds(monitorSiteDeal, siteIdsForString);
            num = monitorSiteMapper.insert(monitorSite);
        }
        return num;
    }

    @Override
    public int updateMonitorSiteBySiteId(MonitorSiteDeal monitorSiteDeal) {
        Integer[] siteIdsForIntegerArray = monitorSiteDeal.getSiteIds();
        String siteIds = DataTypeConversion.integerArrayToString(siteIdsForIntegerArray);
        MonitorSite monitorSite = getMonitorSiteFromMonitorSiteDealAndSiteIds(monitorSiteDeal, siteIds);
        int num = monitorSiteMapper.updateByPrimaryKey(monitorSite);
        return num;
    }

    private MonitorSite getMonitorSiteFromMonitorSiteDealAndSiteIds(MonitorSiteDeal monitorSiteDeal, String siteIds) {
        MonitorSite monitorSite = new MonitorSite();
        monitorSite.setSiteId(monitorSiteDeal.getSiteId());
        monitorSite.setDepartmentName(monitorSiteDeal.getDepartmentName());
        monitorSite.setGuarderName(monitorSiteDeal.getGuarderName());
        monitorSite.setGuarderAccount(monitorSiteDeal.getGuarderAccount());
        monitorSite.setGuarderPhone(monitorSiteDeal.getGuarderPhone());
        monitorSite.setIndexUrl(monitorSiteDeal.getIndexUrl());
        monitorSite.setSiteIds(siteIds);
        return monitorSite;
    }


//    private String[] getSplitSiteIdsArray(MonitorSite monitorSite){
//        String siteIds = monitorSite.getSiteIds();
//        String[] siteIdsArrayForString = siteIds.split(",");
//        return  siteIdsArrayForString;
//    }

    private MonitorSiteDeal getMonitorSiteDealFromMonitorSiteAndSiteIdsArray(MonitorSite monitorSite, Integer[] siteIdsArrayForInteger) {
        MonitorSiteDeal monitorSiteDeal = new MonitorSiteDeal();
        monitorSiteDeal.setSiteId(monitorSite.getSiteId());
        monitorSiteDeal.setDepartmentName(monitorSite.getDepartmentName());
        monitorSiteDeal.setGuarderName(monitorSite.getGuarderName());
        monitorSiteDeal.setGuarderAccount(monitorSite.getGuarderAccount());
        monitorSiteDeal.setGuarderPhone(monitorSite.getGuarderPhone());
        monitorSiteDeal.setIndexUrl(monitorSite.getIndexUrl());
        monitorSiteDeal.setSiteIds(siteIdsArrayForInteger);
        return monitorSiteDeal;
    }

}

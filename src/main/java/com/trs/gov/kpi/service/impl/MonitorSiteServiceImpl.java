package com.trs.gov.kpi.service.impl;

import com.trs.gov.kpi.constant.EnumCheckJobType;
import com.trs.gov.kpi.dao.MonitorSiteMapper;
import com.trs.gov.kpi.entity.MonitorSite;
import com.trs.gov.kpi.entity.MonitorSiteDeal;
import com.trs.gov.kpi.entity.exception.BizException;
import com.trs.gov.kpi.entity.exception.RemoteException;
import com.trs.gov.kpi.service.MonitorSiteService;
import com.trs.gov.kpi.service.SchedulerService;
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

    @Resource
    private SchedulerService schedulerService;

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
    public void addMonitorSite(MonitorSiteDeal monitorSiteDeal) throws BizException {
        MonitorSite monitorSite = getMonitorSiteFromMonitorSiteDealAndSiteIds(monitorSiteDeal);
        Integer siteId = monitorSiteDeal.getSiteId();
        monitorSiteMapper.insert(monitorSite);
        //保存站点信息时，注册报表等调度任务
        // TODO: 2017/8/8 REVIEW FIXED 此处逻辑顺序错误，应该先将数据插入数据库
        schedulerService.removeCheckJob(siteId, EnumCheckJobType.CALCULATE_PERFORMANCE);
        schedulerService.addCheckJob(siteId, EnumCheckJobType.CALCULATE_PERFORMANCE);
        schedulerService.removeCheckJob(siteId, EnumCheckJobType.TIMENODE_REPORT_GENERATE);
        schedulerService.addCheckJob(siteId, EnumCheckJobType.TIMENODE_REPORT_GENERATE);
        schedulerService.removeCheckJob(siteId, EnumCheckJobType.TIMEINTERVAL_REPORT_GENERATE);
        schedulerService.addCheckJob(siteId, EnumCheckJobType.TIMEINTERVAL_REPORT_GENERATE);
        schedulerService.removeCheckJob(siteId, EnumCheckJobType.SERVICE_LINK);
        schedulerService.addCheckJob(siteId, EnumCheckJobType.SERVICE_LINK);
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
        return monitorSiteDeal;
    }

}

package com.trs.gov.kpi.controller;

import com.trs.gov.kpi.constant.Authority;
import com.trs.gov.kpi.constant.Constants;
import com.trs.gov.kpi.constant.EnumCheckJobType;
import com.trs.gov.kpi.constant.OperationType;
import com.trs.gov.kpi.entity.MonitorSite;
import com.trs.gov.kpi.entity.MonitorSiteDeal;
import com.trs.gov.kpi.entity.exception.BizException;
import com.trs.gov.kpi.entity.exception.RemoteException;
import com.trs.gov.kpi.ids.ContextHelper;
import com.trs.gov.kpi.service.MonitorSiteService;
import com.trs.gov.kpi.service.SchedulerService;
import com.trs.gov.kpi.service.outer.AuthorityService;
import com.trs.gov.kpi.service.outer.SiteApiService;
import com.trs.gov.kpi.utils.LogUtil;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * Created by HLoach on 2017/5/11.
 * 监控站点设置Controller
 */
@Slf4j
@RestController
@RequestMapping("/gov/kpi/setting")
public class MonitorSiteController {

    @Resource
    @Setter
    MonitorSiteService monitorSiteService;

    @Resource
    SchedulerService schedulerService;

    @Resource
    private AuthorityService authorityService;

    @Resource
    SiteApiService siteApiService;

    /**
     * 通过siteId查询监测站点的设置参数
     *
     * @param siteId
     * @return
     * @throws BizException
     */
    @RequestMapping(value = "/site", method = RequestMethod.GET)
    @ResponseBody
    public MonitorSiteDeal queryBySiteId(@RequestParam Integer siteId) throws BizException, RemoteException {
        if (!authorityService.hasRight(ContextHelper.getLoginUser().getUserName(), siteId, null, Authority.KPIWEB_MONITORSETUP_SEARCH) && !authorityService.hasRight(ContextHelper
                .getLoginUser().getUserName(), null, null, Authority.KPIWEB_MONITORSETUP_SEARCH)) {
            throw new BizException(Authority.NO_AUTHORITY);
        }
        if (siteId == null) {
            log.error("Invalid parameter: 参数siteId存在null值");
            throw new BizException(Constants.INVALID_PARAMETER);
        }
        MonitorSiteDeal monitorSiteDeal = monitorSiteService.getMonitorSiteDealBySiteId(siteId);
        LogUtil.addOperationLog(OperationType.QUERY, "查询监测站点设置信息", siteApiService.getSiteById(siteId, "").getSiteName());
        return monitorSiteDeal;
    }

    /**
     * 获取参数插入或者修改监测站点设置记录
     *
     * @param monitorSiteDeal
     * @return
     * @throws BizException
     */
    @RequestMapping(value = "/site", method = RequestMethod.POST)
    @ResponseBody
    public Object save(@ModelAttribute MonitorSiteDeal monitorSiteDeal) throws BizException, RemoteException {
        if (!authorityService.hasRight(ContextHelper.getLoginUser().getUserName(), monitorSiteDeal.getSiteId(), null, Authority.KPIWEB_MONITORSETUP_UPDATEADMIN) && !authorityService
                .hasRight(ContextHelper.getLoginUser().getUserName(), null, null, Authority.KPIWEB_MONITORSETUP_UPDATEADMIN)) {
            throw new BizException(Authority.NO_AUTHORITY);
        }
        if (monitorSiteDeal.getSiteId() == null || monitorSiteDeal.getDepartmentName() == null || monitorSiteDeal.getIndexUrl() == null || monitorSiteDeal.getGuarderId() == null) {
            log.error("Invalid parameter: 参数monitorSiteDeal对象中siteId、departmentName、indexUrl、guarderId、四个属性中至少有一个存在null值");
            throw new BizException(Constants.INVALID_PARAMETER);
        }
        if (monitorSiteDeal.getIndexUrl() == null || monitorSiteDeal.getIndexUrl().isEmpty()) {
            log.error("Invalid parameter: 当前站点没有首页");
            throw new BizException("当前站点没有首页，不能设置！");
        }

        int siteId = monitorSiteDeal.getSiteId();
        MonitorSite monitorSite = monitorSiteService.getMonitorSiteBySiteId(siteId);
        if (monitorSite != null) {//检测站点表中存在siteId对应记录，将修改记录
            monitorSiteService.updateMonitorSiteBySiteId(monitorSiteDeal);

            if (monitorSite.getIndexUrl() != null && !monitorSite.getIndexUrl().trim().isEmpty()) {
                schedulerService.removeCheckJob(siteId, EnumCheckJobType.CHECK_HOME_PAGE);
            }
            if (monitorSiteDeal.getIndexUrl() != null && !monitorSiteDeal.getIndexUrl().trim().isEmpty()) {
                schedulerService.addCheckJob(siteId, EnumCheckJobType.CHECK_HOME_PAGE);
            }

            LogUtil.addOperationLog(OperationType.UPDATE, "修改监测站点设置信息", siteApiService.getSiteById(siteId, "").getSiteName());

        } else {//检测站点表中不存在siteId对应记录，将插入记录
            monitorSiteService.addMonitorSite(monitorSiteDeal);

            // 触发监控
            schedulerService.addCheckJob(siteId, EnumCheckJobType.CHECK_HOME_PAGE);
            LogUtil.addOperationLog(OperationType.ADD, "添加监测站点设置信息", siteApiService.getSiteById(siteId, "").getSiteName());

        }
        return null;
    }
}

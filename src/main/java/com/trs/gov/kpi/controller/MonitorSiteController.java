package com.trs.gov.kpi.controller;

import com.trs.gov.kpi.constant.Authority;
import com.trs.gov.kpi.constant.Constants;
import com.trs.gov.kpi.constant.EnumCheckJobType;
import com.trs.gov.kpi.constant.OperationType;
import com.trs.gov.kpi.entity.MonitorSite;
import com.trs.gov.kpi.entity.MonitorSiteDeal;
import com.trs.gov.kpi.entity.exception.BizException;
import com.trs.gov.kpi.entity.exception.RemoteException;
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

    private static final String MONITOR_SITE_DEAL = "monitorSiteDeal";

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
        String logDesc = "查询监测站点设置信息" + LogUtil.paramsToLogString(Constants.DB_FIELD_SITE_ID, siteId);
        return LogUtil.controlleFunctionWrapper(() -> {
            if (siteId == null) {
                log.error("Invalid parameter: 参数siteId存在null值");
                throw new BizException(Constants.INVALID_PARAMETER);
            }
            authorityService.checkRight(Authority.KPIWEB_MONITORSETUP_SEARCH, siteId);
            return monitorSiteService.getMonitorSiteDealBySiteId(siteId);
        }, OperationType.QUERY, logDesc, LogUtil.getSiteNameForLog(siteApiService, siteId));
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
        //TODO REVIEW RANWEI DONE_he.lang FIXED 参数校验逻辑需要修改，缺少错误日志
        // TODO: 2017/8/9  REVIEW DONE_he.lang FIXED 圈复杂度上升
        String logDesc = "设置监测站点设置信息（包括添加和修改）" + LogUtil.paramsToLogString(MONITOR_SITE_DEAL, monitorSiteDeal);
        return LogUtil.controlleFunctionWrapper(() -> {
            checkMonitorDeal(monitorSiteDeal);
            authorityService.checkRight(Authority.KPIWEB_MONITORSETUP_UPDATEADMIN, monitorSiteDeal.getSiteId());
            int siteId = monitorSiteDeal.getSiteId();
            MonitorSite monitorSite = monitorSiteService.getMonitorSiteBySiteId(siteId);
            if (monitorSite != null) {//检测站点表中存在siteId对应记录，将修改记录
                update(monitorSiteDeal);
                //TODO REVIEW RANWEI DONE_he.lang FIXED controller层只进行权限校验和参数校验等，下面代码应当放入service层

            } else {//检测站点表中不存在siteId对应记录，将插入记录
                add(monitorSiteDeal);
            }
            return null;
        }, OperationType.ADD + "," + OperationType.UPDATE, logDesc, LogUtil.getSiteNameForLog(siteApiService, monitorSiteDeal.getSiteId()));
    }

    private Integer add(MonitorSiteDeal monitorSiteDeal) throws RemoteException, BizException {
        String logDesc = "添加监测站点设置信息" + LogUtil.paramsToLogString(MONITOR_SITE_DEAL, monitorSiteDeal);
        return LogUtil.controlleFunctionWrapper(() -> {
            monitorSiteService.addMonitorSite(monitorSiteDeal);
            return null;
        }, OperationType.ADD, logDesc, LogUtil.getSiteNameForLog(siteApiService, monitorSiteDeal.getSiteId()));
    }

    private Integer update(MonitorSiteDeal monitorSiteDeal) throws RemoteException, BizException {
        String logDesc = "修改监测站点设置信息" + LogUtil.paramsToLogString(MONITOR_SITE_DEAL, monitorSiteDeal);
        return LogUtil.controlleFunctionWrapper(() -> {
            monitorSiteService.updateMonitorSiteBySiteId(monitorSiteDeal);
            return null;
        }, OperationType.UPDATE, logDesc, LogUtil.getSiteNameForLog(siteApiService, monitorSiteDeal.getSiteId()));
    }

    private void checkMonitorDeal(MonitorSiteDeal monitorSiteDeal) throws BizException {
        if (monitorSiteDeal.getSiteId() == null || monitorSiteDeal.getDepartmentName() == null || monitorSiteDeal.getGuarderId() == null) {
            log.error("Invalid parameter: 参数monitorSiteDeal对象中siteId、departmentName、guarderId三个属性中至少有一个存在null值");
            throw new BizException(Constants.INVALID_PARAMETER);
        }
    }

    /**
     * 网站手动监测
     *
     * @param siteId
     * @return
     */
    @RequestMapping(value = "/manual/check", method = RequestMethod.PUT)
    @ResponseBody
    public Object manualMonitoring(Integer siteId, Integer checkJobValue) throws BizException, RemoteException {
        String logDesc = "网站手动监测" + LogUtil.paramsToLogString(Constants.DB_FIELD_SITE_ID, siteId, "checkJobValue", checkJobValue);
        return LogUtil.controlleFunctionWrapper(() -> {
            if (siteId == null && checkJobValue == null) {
                log.error("Invalid parameter: 参数siteId或者checkJobTypeValue存在null值");
                throw new BizException(Constants.INVALID_PARAMETER);
            }
//			authorityService.checkRight(Authority.KPIWEB_MANUALMONITOR_CHECK, siteId);
            EnumCheckJobType checkJobType = null;
            switch (checkJobValue) {
                case (1):
                    checkJobType = EnumCheckJobType.CHECK_HOME_PAGE;
                    break;
                case (2):
                    checkJobType = EnumCheckJobType.CHECK_LINK;
                    break;
                case (3):
                    checkJobType = EnumCheckJobType.CHECK_CONTENT;
                    break;
                case (4):
                    checkJobType = EnumCheckJobType.CHECK_INFO_UPDATE;
                    break;
                case (8):
                    checkJobType = EnumCheckJobType.SERVICE_LINK;
                    break;
                default:
            }
            schedulerService.doCheckJobOnce(siteId, checkJobType);
            return null;
        }, OperationType.REQUEST, logDesc, LogUtil.getSiteNameForLog(siteApiService, siteId));
    }
}

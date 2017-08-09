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
import java.util.Date;

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
        Date startTime = new Date();
        if (siteId == null) {
            log.error("Invalid parameter: 参数siteId存在null值");
            throw new BizException(Constants.INVALID_PARAMETER);
        }
        String logDesc = "查询监测站点设置信息";
        if (!authorityService.hasRight(ContextHelper.getLoginUser().getUserName(), siteId, null, Authority.KPIWEB_MONITORSETUP_SEARCH) && !authorityService.hasRight(ContextHelper
                .getLoginUser().getUserName(), null, null, Authority.KPIWEB_MONITORSETUP_SEARCH)) {
            LogUtil.addOperationLog(OperationType.QUERY, LogUtil.buildFailOperationLogDesc(logDesc), LogUtil.getSiteNameForLog(siteApiService, siteId));
            throw new BizException(Authority.NO_AUTHORITY);
        }
        try {
            MonitorSiteDeal monitorSiteDeal = monitorSiteService.getMonitorSiteDealBySiteId(siteId);
            Date endTime = new Date();
            LogUtil.addOperationLog(OperationType.QUERY, logDesc, LogUtil.getSiteNameForLog(siteApiService, siteId));
            LogUtil.addElapseLog(OperationType.QUERY, LogUtil.buildElapseLogDesc(siteApiService, siteId, logDesc), endTime.getTime()-startTime.getTime());
            return monitorSiteDeal;
        }catch (Exception e){
            LogUtil.addOperationLog(OperationType.QUERY, LogUtil.buildFailOperationLogDesc(logDesc), LogUtil.getSiteNameForLog(siteApiService, siteId));
            throw e;
        }
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
        //TODO REVIEW RANWEI DO_he.lang 参数校验逻辑需要修改，缺少错误日志
        // TODO: 2017/8/9  REVIEW DO_he.lang 圈复杂度上升
        if (monitorSiteDeal.getSiteId() == null || monitorSiteDeal.getDepartmentName() == null || monitorSiteDeal.getIndexUrl() == null || monitorSiteDeal.getGuarderId() == null) {
            log.error("Invalid parameter: 参数monitorSiteDeal对象中siteId、departmentName、indexUrl、guarderId、四个属性中至少有一个存在null值");
            throw new BizException(Constants.INVALID_PARAMETER);
        }
        if (monitorSiteDeal.getIndexUrl() == null || monitorSiteDeal.getIndexUrl().isEmpty()) {
            log.error("Invalid parameter: 当前站点没有首页");
            throw new BizException("当前站点没有首页，不能设置！");
        }
        String logDesc = "设置监测站点设置信息（包括添加和修改）";
        if (!authorityService.hasRight(ContextHelper.getLoginUser().getUserName(), monitorSiteDeal.getSiteId(), null, Authority.KPIWEB_MONITORSETUP_UPDATEADMIN) && !authorityService
                .hasRight(ContextHelper.getLoginUser().getUserName(), null, null, Authority.KPIWEB_MONITORSETUP_UPDATEADMIN)) {
            LogUtil.addOperationLog(OperationType.ADD + OperationType.UPDATE, LogUtil.buildFailOperationLogDesc(logDesc), LogUtil.getSiteNameForLog(siteApiService, monitorSiteDeal.getSiteId()));
            throw new BizException(Authority.NO_AUTHORITY);
        }

        int siteId = monitorSiteDeal.getSiteId();
        MonitorSite monitorSite;
        try {
            monitorSite = monitorSiteService.getMonitorSiteBySiteId(siteId);
        }catch (Exception e){
            LogUtil.addOperationLog(OperationType.QUERY, LogUtil.buildFailOperationLogDesc(logDesc+"：查询当前站点site["+siteId+"]是否已设置成为监测站点"), LogUtil.getSiteNameForLog(siteApiService, siteId));
            throw e;
        }
        if (monitorSite != null) {//检测站点表中存在siteId对应记录，将修改记录
            try {
                monitorSiteService.updateMonitorSiteBySiteId(monitorSiteDeal);
                LogUtil.addOperationLog(OperationType.UPDATE, "修改监测站点设置信息", siteApiService.getSiteById(siteId, "").getSiteName());
            }catch (Exception e){
                LogUtil.addOperationLog(OperationType.UPDATE, LogUtil.buildFailOperationLogDesc("修改监测站点设置信息"), LogUtil.getSiteNameForLog(siteApiService, siteId));
                throw e;
            }
            //TODO REVIEW RANWEI DO_he.lang controller层只进行权限校验和参数校验等，下面代码应当放入service层
            if (monitorSite.getIndexUrl() != null && !monitorSite.getIndexUrl().trim().isEmpty()) {
                schedulerService.removeCheckJob(siteId, EnumCheckJobType.CHECK_HOME_PAGE);
            }
            if (monitorSiteDeal.getIndexUrl() != null && !monitorSiteDeal.getIndexUrl().trim().isEmpty()) {
                schedulerService.addCheckJob(siteId, EnumCheckJobType.CHECK_HOME_PAGE);
            }
        } else {//检测站点表中不存在siteId对应记录，将插入记录
            try {
                monitorSiteService.addMonitorSite(monitorSiteDeal);
                LogUtil.addOperationLog(OperationType.ADD, "添加监测站点设置信息", LogUtil.getSiteNameForLog(siteApiService, siteId));
            }catch (Exception e){
                LogUtil.addOperationLog(OperationType.ADD, LogUtil.buildFailOperationLogDesc("添加监测站点设置信息"), LogUtil.getSiteNameForLog(siteApiService, siteId));
                throw e;
            }

            // 触发监控
            schedulerService.addCheckJob(siteId, EnumCheckJobType.CHECK_HOME_PAGE);
        }
        return null;
    }

    /**
     * 网站手动监测
     *
     * @param siteId
     * @return
     */
    @RequestMapping(value = "/manual/check", method = RequestMethod.PUT)
    @ResponseBody
    public void manualMonitoring(Integer siteId, Integer checkJobValue) throws BizException, RemoteException {
        if (siteId == null && checkJobValue == null) {
            log.error("Invalid parameter: 参数siteId或者checkJobTypeValue存在null值");
            throw new BizException(Constants.INVALID_PARAMETER);
        }
        try {
            EnumCheckJobType checkJobType = null;
            switch (checkJobValue){
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
                default:
            }
            schedulerService.doCheckJobOnce(siteId, checkJobType);
            LogUtil.addOperationLog(OperationType.TASK_SCHEDULE, "网站手动监测", LogUtil.getSiteNameForLog(siteApiService, siteId));
        }catch (Exception e){
            LogUtil.addOperationLog(OperationType.TASK_SCHEDULE, LogUtil.buildFailOperationLogDesc("网站手动监测"), LogUtil.getSiteNameForLog(siteApiService, siteId));
            throw e;
        }

    }
}

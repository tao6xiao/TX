package com.trs.gov.kpi.controller;

import com.trs.gov.kpi.constant.EnumCheckJobType;
import com.trs.gov.kpi.entity.MonitorSite;
import com.trs.gov.kpi.entity.MonitorSiteDeal;
import com.trs.gov.kpi.entity.exception.BizException;
import com.trs.gov.kpi.service.MonitorSiteService;
import com.trs.gov.kpi.service.SchedulerService;
import com.trs.gov.kpi.service.impl.MonitorSiteServiceImpl;
import com.trs.gov.kpi.utils.DataTypeConversion;
import lombok.Setter;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/**
 * Created by HLoach on 2017/5/11.
 * 监控站点设置Controller
 */
@RestController
@RequestMapping("/gov/kpi/setting")
public class MonitorSiteController {

    @Resource @Setter
    MonitorSiteService monitorSiteService;

    @Resource
    SchedulerService schedulerService;

    /**
     * 通过siteId查询监测站点的设置参数
     * @param siteId
     * @return
     * @throws BizException
     */
    @RequestMapping(value = "/site",method = RequestMethod.GET)
    @ResponseBody
    public MonitorSiteDeal queryBySiteId(@RequestParam Integer siteId) throws BizException {
        if(siteId == null){
            throw new BizException("参数siteId存在null值");
        }
        MonitorSiteDeal monitorSiteDeal = monitorSiteService.getMonitorSiteDealBySiteId(siteId);

        return monitorSiteDeal;
    }

    /**
     *  获取参数插入或者修改监测站点设置记录
     * @param monitorSiteDeal
     * @return
     * @throws BizException
     */
    @RequestMapping(value = "/site",method = RequestMethod.POST)
    @ResponseBody
    public Object save(@RequestBody MonitorSiteDeal monitorSiteDeal) throws BizException {
        if(monitorSiteDeal.getSiteId() == null || monitorSiteDeal.getDepartmentName() == null || monitorSiteDeal.getIndexUrl() == null){
            throw new BizException("参数存在null值");
        }

        int siteId = monitorSiteDeal.getSiteId();
        MonitorSite monitorSite = monitorSiteService.getMonitorSiteBySiteId(siteId);
        if(monitorSite != null){//检测站点表中存在siteId对应记录，将修改记录
            monitorSiteService.updateMonitorSiteBySiteId(monitorSiteDeal);

            if (monitorSite.getIndexUrl() != null && !monitorSite.getIndexUrl().trim().isEmpty()) {
                schedulerService.removeCheckJob(siteId, EnumCheckJobType.CHECK_HOME_PAGE);
            }
            if (monitorSiteDeal.getIndexUrl() != null && !monitorSiteDeal.getIndexUrl().trim().isEmpty()) {
                schedulerService.addCheckJob(siteId, EnumCheckJobType.CHECK_HOME_PAGE);
            }

        }else {//检测站点表中不存在siteId对应记录，将插入记录
            monitorSiteService.addMonitorSite(monitorSiteDeal);

            // 触发监控
            schedulerService.addCheckJob(siteId, EnumCheckJobType.CHECK_HOME_PAGE);
        }
        return null;
    }
}

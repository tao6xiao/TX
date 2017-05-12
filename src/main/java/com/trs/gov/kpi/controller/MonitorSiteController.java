package com.trs.gov.kpi.controller;

import com.trs.gov.kpi.entity.MonitorSite;
import com.trs.gov.kpi.entity.MonitorSiteDeal;
import com.trs.gov.kpi.entity.exception.BizException;
import com.trs.gov.kpi.service.MonitorSiteService;
import com.trs.gov.kpi.service.impl.MonitorSiteServiceImpl;
import com.trs.gov.kpi.utils.DataTypeConversion;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * Created by HLoach on 2017/5/11.
 * 监控站点设置Controller
 */
@RestController
@RequestMapping("/setting")
public class MonitorSiteController {
    @Resource
    MonitorSiteService monitorSiteService;

    @RequestMapping(value = "/site",method = RequestMethod.GET)
    @ResponseBody
    private MonitorSiteDeal queryBySiteId(@RequestParam int siteId){
        MonitorSiteDeal monitorSiteDeal = monitorSiteService.getMonitorSiteDealBySiteId(siteId);

        return monitorSiteDeal;
    }

    @RequestMapping(value = "/site",method = RequestMethod.POST)
    @ResponseBody
    private String save(@ModelAttribute MonitorSiteDeal monitorSiteDeal){
        int siteId = monitorSiteDeal.getSiteId();
        MonitorSite monitorSite = monitorSiteService.getMonitorSiteBySiteId(siteId);
        int num = 0;
        if(monitorSite != null){//检测站点表中存在siteId对应记录，将修改记录
            num = monitorSiteService.updateMonitorSiteBySiteId(monitorSiteDeal);

        }else {//检测站点表中不存在siteId对应记录，将插入记录
            num = monitorSiteService.addMonitorSite(monitorSiteDeal);
        }
        if(num == 0){

            return "error";
        }
        return null;
    }
}

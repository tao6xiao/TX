package com.trs.gov.kpi.controller;

import com.trs.gov.kpi.entity.MonitorSite;
import com.trs.gov.kpi.entity.MonitorSiteDeal;
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
    private void save(){//@ModelAttribute MonitorSiteDeal monitorSiteDeal
//        int siteId = monitorSiteDeal.getSiteId();
        MonitorSite monitorSite = monitorSiteService.getMonitorSiteBySiteId(1);
        MonitorSiteServiceImpl monitorSiteService = new MonitorSiteServiceImpl();
        Integer[] siteIdsInt = new Integer[]{1,2};
        monitorSite.setGuarderAccount("zhangsan11");
        MonitorSiteDeal monitorSiteDeal = monitorSiteService.getMonitorSiteDealFromMonitorSiteAndSiteIdsArray(monitorSite, siteIdsInt);
        int num = 0;
        if(monitorSite != null){//检测站点表中存在siteId对应记录，将修改记录
            num = monitorSiteService.updateMonitorSiteBySiteId(monitorSiteDeal);

        }else {//检测站点表中不存在siteId对应记录，将插入记录
            num = monitorSiteService.addMonitorSite(monitorSite);
        }
        System.err.println(num);
        if(num == 0){
//            return "error";
        }
//        return "success";
    }
}

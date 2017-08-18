package com.trs.gov.kpi.controller;

import com.trs.gov.kpi.constant.Constants;
import com.trs.gov.kpi.entity.exception.BizException;
import com.trs.gov.kpi.service.FrequencySetupService;
import com.trs.gov.kpi.service.MonitorSiteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 用于拦截当前站点未配置：检测站点设置+栏目更新频率设置时的拦截
 * Created by he.lang on 2017/7/6.
 */
@Slf4j
@RestController
@RequestMapping("/gov/kpi/setting")
public class MonitorFilterController {

    @Resource
    MonitorSiteService siteService;

    @Resource
    FrequencySetupService setupService;

    /**
     * 当前站点为配置完成时拦截访问
     *
     * @param siteId
     * @return
     * @throws BizException
     */
    @RequestMapping(value = "/filter", method = RequestMethod.GET)
    @ResponseBody
    public String setFilter(@RequestParam("siteId") Integer siteId) throws BizException {
        if (siteId == null) {
            log.error("Invalid parameter: 参数siteId存在null值");
            throw new BizException(Constants.INVALID_PARAMETER);
        }
        if (siteService.getMonitorSiteBySiteId(siteId) == null) {
            String errorInfo = "进入绩效考核，当前站点site[" + siteId + "]不是监测站点，请设置！";
            log.error(errorInfo);
            throw new BizException(errorInfo);
        }
        if (setupService.getCountFrequencySetupBySite(siteId) == 0) {
            String errorInfo = "进入绩效考核，当前站点site[" + siteId + "]没有设置栏目更新频率，请设置！";
            log.error(errorInfo);
            throw new BizException(errorInfo);
        }
        return null;
    }
}

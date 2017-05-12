package com.trs.gov.kpi.controller;

import com.trs.gov.kpi.entity.MonitorFrequencyDeal;
import com.trs.gov.kpi.entity.exception.BizException;
import com.trs.gov.kpi.service.MonitorFrequencyService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by helang on 2017/5/12.
 * 监测频率Controller
 */
@RestController
@RequestMapping(value = "/setting")
public class MonitorFrequencyController {
    @Resource
    MonitorFrequencyService monitorFrequencyService;

    @RequestMapping(value = "/checkfreq", method = RequestMethod.GET)
    @ResponseBody
    public List<MonitorFrequencyDeal> queryBySiteId(@RequestParam Integer siteId) throws BizException {
        if (siteId == null) {
            throw new BizException();
        }
        List<MonitorFrequencyDeal> monitorFrequencyDealList = monitorFrequencyService.queryBySiteId(siteId);
        return monitorFrequencyDealList;
    }

}

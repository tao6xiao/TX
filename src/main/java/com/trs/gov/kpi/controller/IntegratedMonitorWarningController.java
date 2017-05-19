package com.trs.gov.kpi.controller;

import com.trs.gov.kpi.entity.Issue;
import com.trs.gov.kpi.entity.exception.BizException;
import com.trs.gov.kpi.entity.responsedata.ApiPageData;
import com.trs.gov.kpi.service.IntegratedMonitorWarningService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 综合实时监测：预警提醒Controller
 * Created by he.lang on 2017/5/18.
 */
@RestController
@RequestMapping("/gov/kpi/alert")
public class IntegratedMonitorWarningController {

    @Resource
    IntegratedMonitorWarningService integratedMonitorWarningService;

    @RequestMapping(value = "/unhandled", method = RequestMethod.GET)
    @ResponseBody
    public ApiPageData getPageDataWaringList(Integer pageIndex, Integer pageSize, @ModelAttribute Issue issue) throws BizException {
        if (issue.getSiteId() == null) {
            throw new BizException("站点编号为空");
        }
        return null;
    }

    /**
     * 处理对应siteId和id的预警记录（批量和单个）
     *
     * @param siteId
     * @param ids
     * @return
     * @throws BizException
     */
    @RequestMapping(value = "/handle", method = RequestMethod.POST)
    @ResponseBody
    public Object dealWithWarningBySiteIdAndId(@RequestParam Integer siteId, @RequestParam Integer[] ids) throws BizException {
        if (siteId == null || ids == null || ids.length == 0) {
            throw new BizException("参数存在null值");
        }
        int num = integratedMonitorWarningService.dealWithWarningBySiteIdAndId(siteId, ids);
        return null;
    }

    /**
     * 忽略对应siteId和id的预警记录（批量和单个）
     * @param siteId
     * @param ids
     * @return
     * @throws BizException
     */
    @RequestMapping(value = "/ignore", method = RequestMethod.POST)
    @ResponseBody
    public Object ignoreWarningBySiteIdAndId(@RequestParam Integer siteId, @RequestParam Integer[] ids) throws BizException {
        if (siteId == null || ids == null || ids.length == 0) {
            throw new BizException("参数存在null值");
        }
        int num = integratedMonitorWarningService.ignoreWarningBySiteIdAndId(siteId, ids);
        return null;
    }

    /**
     * 删除对应siteId和id的预警记录（批量和单个）
     * @param siteId
     * @param ids
     * @return
     * @throws BizException
     */
    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    @ResponseBody
    public Object deleteWarningBySiteIdAndId(@RequestParam Integer siteId, @RequestParam Integer[] ids) throws BizException {
        if (siteId == null || ids == null || ids.length == 0) {
            throw new BizException("参数存在null值");
        }
        int num = integratedMonitorWarningService.deleteWarningBySiteIdAndId(siteId, ids);
        return null;
    }
}

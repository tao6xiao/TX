package com.trs.gov.kpi.controller;

import com.trs.gov.kpi.entity.exception.BizException;
import com.trs.gov.kpi.entity.requestdata.PageDataRequestParam;
import com.trs.gov.kpi.entity.responsedata.ApiPageData;
import com.trs.gov.kpi.service.IntegratedMonitorIsResolvedService;
import com.trs.gov.kpi.utils.ParamCheckUtil;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 综合监测：已解决Controller，主要是查询
 * Created by he.lang on 2017/5/19.
 */
@RestController
@RequestMapping("/gov/kpi/issuealert")
public class IntegratedMonitorIsResolvedController {

    @Resource
    IntegratedMonitorIsResolvedService integratedMonitorIsResolvedService;

    /**
     * 获取已解决的分页数据
     *
     * @param param
     * @return
     * @throws BizException
     */
    @RequestMapping(value = "/handled", method = RequestMethod.GET)
    @ResponseBody
    public ApiPageData getPageDataIsResolved(@ModelAttribute PageDataRequestParam param) throws BizException {

        ParamCheckUtil.paramCheck(param);

        return integratedMonitorIsResolvedService.getPageDataIsResolvedList(param, true);
    }

    /**
     * 获取已忽略的分页数据
     *
     * @param param
     * @return
     * @throws BizException
     */
    @RequestMapping(value = "/ignored", method = RequestMethod.GET)
    @ResponseBody
    public ApiPageData getPageDataIsIgnored(@ModelAttribute PageDataRequestParam param) throws BizException {

        ParamCheckUtil.paramCheck(param);

        return integratedMonitorIsResolvedService.getPageDataIsResolvedList(param, false);
    }
}

package com.trs.gov.kpi.controller;

import com.trs.gov.kpi.constant.Authority;
import com.trs.gov.kpi.constant.Constants;
import com.trs.gov.kpi.constant.OperationType;
import com.trs.gov.kpi.entity.exception.BizException;
import com.trs.gov.kpi.entity.exception.RemoteException;
import com.trs.gov.kpi.entity.requestdata.PageDataRequestParam;
import com.trs.gov.kpi.entity.responsedata.ApiPageData;
import com.trs.gov.kpi.service.IntegratedMonitorIsResolvedService;
import com.trs.gov.kpi.service.outer.AuthorityService;
import com.trs.gov.kpi.service.outer.SiteApiService;
import com.trs.gov.kpi.utils.LogUtil;
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

    @Resource
    private AuthorityService authorityService;

    @Resource
    SiteApiService siteApiService;

    /**
     * 获取已解决的分页数据
     *
     * @param param
     * @return
     * @throws BizException
     */
    @RequestMapping(value = "/handled", method = RequestMethod.GET)
    @ResponseBody
    public ApiPageData getPageDataIsResolved(@ModelAttribute PageDataRequestParam param) throws BizException, RemoteException {
        String logDesc = "获取已解决的分页数据" + LogUtil.paramsToLogString(Constants.PARAM, param);
        return LogUtil.ControlleFunctionWrapper(() -> {
            ParamCheckUtil.paramCheck(param);
            authorityService.checkRight(Authority.KPIWEB_RESOLVED_SEARCH, param.getSiteId());
            return integratedMonitorIsResolvedService.getPageDataIsResolvedList(param, true);
        }, OperationType.QUERY, logDesc, LogUtil.getSiteNameForLog(siteApiService, param.getSiteId()));
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
    public ApiPageData getPageDataIsIgnored(@ModelAttribute PageDataRequestParam param) throws BizException, RemoteException {
        String logDesc = "获取已忽略的分页数据" + LogUtil.paramsToLogString(Constants.PARAM, param);
        return LogUtil.ControlleFunctionWrapper(() -> {
            ParamCheckUtil.paramCheck(param);
            authorityService.checkRight(Authority.KPIWEB_RESOLVED_SEARCH, param.getSiteId());
            ApiPageData apiPageData = integratedMonitorIsResolvedService.getPageDataIsResolvedList(param, false);
            LogUtil.addOperationLog(OperationType.QUERY, logDesc, LogUtil.getSiteNameForLog(siteApiService, param.getSiteId()));
            return apiPageData;
        }, OperationType.QUERY, logDesc, LogUtil.getSiteNameForLog(siteApiService, param.getSiteId()));
    }
}

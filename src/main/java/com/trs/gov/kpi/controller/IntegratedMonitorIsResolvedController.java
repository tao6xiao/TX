package com.trs.gov.kpi.controller;

import com.trs.gov.kpi.constant.Authority;
import com.trs.gov.kpi.constant.OperationType;
import com.trs.gov.kpi.entity.exception.BizException;
import com.trs.gov.kpi.entity.exception.RemoteException;
import com.trs.gov.kpi.entity.requestdata.PageDataRequestParam;
import com.trs.gov.kpi.entity.responsedata.ApiPageData;
import com.trs.gov.kpi.ids.ContextHelper;
import com.trs.gov.kpi.service.IntegratedMonitorIsResolvedService;
import com.trs.gov.kpi.service.outer.AuthorityService;
import com.trs.gov.kpi.service.outer.SiteApiService;
import com.trs.gov.kpi.utils.LogUtil;
import com.trs.gov.kpi.utils.ParamCheckUtil;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;

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
        Date startTime = new Date();
        ParamCheckUtil.paramCheck(param);
        String logDesc = "获取已解决的分页数据";
        if (!authorityService.hasRight(ContextHelper.getLoginUser().getUserName(), param.getSiteId(), null, Authority.KPIWEB_RESOLVED_SEARCH) && !authorityService.hasRight(ContextHelper
                .getLoginUser().getUserName(), null, null, Authority.KPIWEB_RESOLVED_SEARCH)) {
            LogUtil.addOperationLog(OperationType.QUERY, LogUtil.buildFailOperationLogDesc(logDesc), LogUtil.getSiteNameForLog(siteApiService, param.getSiteId()));
            throw new BizException(Authority.NO_AUTHORITY);
        }
        try {
            ApiPageData apiPageData = integratedMonitorIsResolvedService.getPageDataIsResolvedList(param, true);
            Date endTime = new Date();
            LogUtil.addOperationLog(OperationType.QUERY, logDesc, LogUtil.getSiteNameForLog(siteApiService, param.getSiteId()));
            LogUtil.addElapseLog(OperationType.QUERY, LogUtil.buildElapseLogDesc(siteApiService, param.getSiteId(), logDesc), endTime.getTime() - startTime.getTime());
            return apiPageData;
        }catch (Exception e){
            LogUtil.addOperationLog(OperationType.QUERY, LogUtil.buildFailOperationLogDesc(logDesc), LogUtil.getSiteNameForLog(siteApiService, param.getSiteId()));
            throw e;
        }
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
        Date startTime = new Date();
        ParamCheckUtil.paramCheck(param);
        String logDesc = "获取已忽略的分页数据";
        if (!authorityService.hasRight(ContextHelper.getLoginUser().getUserName(), param.getSiteId(), null, Authority.KPIWEB_RESOLVED_SEARCH) && !authorityService.hasRight(ContextHelper
                .getLoginUser().getUserName(), null, null, Authority.KPIWEB_RESOLVED_SEARCH)) {
            LogUtil.addOperationLog(OperationType.QUERY, LogUtil.buildFailOperationLogDesc(logDesc), LogUtil.getSiteNameForLog(siteApiService, param.getSiteId()));
            throw new BizException(Authority.NO_AUTHORITY);
        }
        try {
            ApiPageData apiPageData = integratedMonitorIsResolvedService.getPageDataIsResolvedList(param, false);
            Date endTime = new Date();
            LogUtil.addOperationLog(OperationType.QUERY, logDesc, LogUtil.getSiteNameForLog(siteApiService, param.getSiteId()));
            LogUtil.addElapseLog(OperationType.QUERY, LogUtil.buildElapseLogDesc(siteApiService, param.getSiteId(), logDesc), endTime.getTime() - startTime.getTime());
            return apiPageData;
        }catch (Exception e){
            LogUtil.addOperationLog(OperationType.QUERY, LogUtil.buildFailOperationLogDesc(logDesc), LogUtil.getSiteNameForLog(siteApiService, param.getSiteId()));
            throw e;
        }
    }
}

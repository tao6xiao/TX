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
import com.trs.gov.kpi.utils.ParamCheckUtil;
import com.trs.gov.kpi.utils.TRSLogUserUtil;
import com.trs.mlf.simplelog.SimpleLogServer;
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
        if (!authorityService.hasRight(ContextHelper.getLoginUser().getUserName(), param.getSiteId(), null, Authority.KPIWEB_RESOLVED_SEARCH) && !authorityService.hasRight(ContextHelper
                .getLoginUser().getUserName(), null, null, Authority.KPIWEB_RESOLVED_SEARCH)) {
            throw new BizException(Authority.NO_AUTHORITY);
        }
        ParamCheckUtil.paramCheck(param);
        SimpleLogServer.getInstance(TRSLogUserUtil.getLogUser()).operation(OperationType.QUERY, "获取已解决的分页数据", siteApiService.getSiteById(param.getSiteId(), "").getSiteName()).info();
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
    public ApiPageData getPageDataIsIgnored(@ModelAttribute PageDataRequestParam param) throws BizException, RemoteException {
        if (!authorityService.hasRight(ContextHelper.getLoginUser().getUserName(), param.getSiteId(), null, Authority.KPIWEB_RESOLVED_SEARCH) && !authorityService.hasRight(ContextHelper
                .getLoginUser().getUserName(), null, null, Authority.KPIWEB_RESOLVED_SEARCH)) {
            throw new BizException(Authority.NO_AUTHORITY);
        }
        ParamCheckUtil.paramCheck(param);
        SimpleLogServer.getInstance(TRSLogUserUtil.getLogUser()).operation(OperationType.QUERY, "获取已忽略的分页数据", siteApiService.getSiteById(param.getSiteId(), "").getSiteName()).info();
        return integratedMonitorIsResolvedService.getPageDataIsResolvedList(param, false);
    }
}

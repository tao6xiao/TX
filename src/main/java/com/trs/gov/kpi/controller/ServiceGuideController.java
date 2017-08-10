package com.trs.gov.kpi.controller;

import com.trs.gov.kpi.constant.Authority;
import com.trs.gov.kpi.constant.Constants;
import com.trs.gov.kpi.constant.OperationType;
import com.trs.gov.kpi.entity.exception.BizException;
import com.trs.gov.kpi.entity.exception.RemoteException;
import com.trs.gov.kpi.entity.outerapi.sp.SGPageDataRes;
import com.trs.gov.kpi.entity.outerapi.sp.SGStatistics;
import com.trs.gov.kpi.entity.requestdata.PageDataRequestParam;
import com.trs.gov.kpi.entity.responsedata.HistoryStatisticsRes;
import com.trs.gov.kpi.service.outer.AuthorityService;
import com.trs.gov.kpi.service.outer.SGService;
import com.trs.gov.kpi.service.outer.SiteApiService;
import com.trs.gov.kpi.utils.LogUtil;
import com.trs.gov.kpi.utils.ParamCheckUtil;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * Created by ranwei on 2017/6/12.
 */
@RestController
@RequestMapping(value = "/gov/kpi/service")
public class ServiceGuideController {

    @Resource
    private SGService sgService;

    @Resource
    private AuthorityService authorityService;

    @Resource
    SiteApiService siteApiService;

    /**
     * 按类型统计问题总数
     *
     * @param param
     * @return
     * @throws BizException
     * @throws RemoteException
     */
    @RequestMapping(value = "/issue/bytype/count", method = RequestMethod.GET)
    public SGStatistics getSPCount(PageDataRequestParam param) throws BizException, RemoteException {
        String logDesc = "查询服务指南按类型统计的问题总数" + LogUtil.paramsToLogString(Constants.PARAM, param);
        return LogUtil.controlleFunctionWrapper(() -> {
            ParamCheckUtil.paramCheck(param);
            authorityService.checkRight(Authority.KPIWEB_SERVICE_SEARCH, param.getSiteId());
            return sgService.getSGCount(param);
        }, OperationType.QUERY, logDesc, LogUtil.getSiteNameForLog(siteApiService, param.getSiteId()));
    }

    /**
     * 查询服务实用问题总数的历史纪录
     *
     * @param param
     * @return
     * @throws BizException
     * @throws RemoteException
     */
    @RequestMapping(value = "/issue/all/count/history", method = RequestMethod.GET)
    public HistoryStatisticsRes getSPHistoryCount(PageDataRequestParam param) throws BizException, RemoteException {
        String logDesc = "查询服务指南问题总数的历史纪录" + LogUtil.paramsToLogString(Constants.PARAM, param);
        return LogUtil.controlleFunctionWrapper(() -> {
            ParamCheckUtil.paramCheck(param);
            authorityService.checkRight(Authority.KPIWEB_SERVICE_SEARCH, param.getSiteId());
            return sgService.getSGHistoryCount(param);
        }, OperationType.QUERY, logDesc, LogUtil.getSiteNameForLog(siteApiService, param.getSiteId()));
    }

    /**
     * 查询服务指南问题总数
     *
     * @param param
     * @return
     * @throws BizException
     * @throws RemoteException
     */
    @RequestMapping(value = "/guide/issue/unhandled", method = RequestMethod.GET)
    public SGPageDataRes getSGList(@ModelAttribute PageDataRequestParam param) throws BizException, RemoteException {
        String logDesc = "查询服务指南问题总数" + LogUtil.paramsToLogString(Constants.PARAM, param);
        return LogUtil.controlleFunctionWrapper(() -> {
            ParamCheckUtil.paramCheck(param);
            authorityService.checkRight(Authority.KPIWEB_SERVICE_SEARCH, param.getSiteId());
            return sgService.getSGList(param);
        }, OperationType.QUERY, logDesc, LogUtil.getSiteNameForLog(siteApiService, param.getSiteId()));
    }


}

package com.trs.gov.kpi.controller;

import com.trs.gov.kpi.constant.Authority;
import com.trs.gov.kpi.constant.OperationType;
import com.trs.gov.kpi.entity.exception.BizException;
import com.trs.gov.kpi.entity.exception.RemoteException;
import com.trs.gov.kpi.entity.requestdata.PageDataRequestParam;
import com.trs.gov.kpi.entity.responsedata.ApiPageData;
import com.trs.gov.kpi.service.LinkAvailabilityService;
import com.trs.gov.kpi.service.outer.AuthorityService;
import com.trs.gov.kpi.utils.LogUtil;
import com.trs.gov.kpi.utils.ParamCheckUtil;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 服务实用中服务链接Controller
 * Created by he.lang on 2017/5/17.
 */
@RestController
@RequestMapping("/gov/kpi/service/link/issue")
public class ServiceLinkController extends IssueHandler {
    @Resource
    private LinkAvailabilityService linkAvailabilityService;

    @Resource
    private AuthorityService authorityService;

    /**
     * 查询服务链接未解决问题列表
     *
     * @param requestParam
     * @return
     * @throws BizException
     */
    @RequestMapping(value = "/unhandled", method = RequestMethod.GET)
    public ApiPageData getServiceLinkList(@ModelAttribute PageDataRequestParam requestParam) throws BizException, RemoteException {
        String logDesc = "查询服务链接未解决问题列表" + LogUtil.paramsToLogString("requestParam", requestParam);
        return LogUtil.controlleFunctionWrapper(() -> {
            ParamCheckUtil.paramCheck(requestParam);
            authorityService.checkRight(Authority.KPIWEB_SERVICE_SEARCH, requestParam.getSiteId());
            return linkAvailabilityService.getServiceLinkList(requestParam);
        }, OperationType.QUERY, logDesc, LogUtil.getSiteNameForLog(siteApiService, requestParam.getSiteId()));
    }

}

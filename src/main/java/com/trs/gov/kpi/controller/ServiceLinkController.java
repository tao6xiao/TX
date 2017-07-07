package com.trs.gov.kpi.controller;

import com.trs.gov.kpi.constant.Authority;
import com.trs.gov.kpi.constant.UrlPath;
import com.trs.gov.kpi.entity.exception.BizException;
import com.trs.gov.kpi.entity.exception.RemoteException;
import com.trs.gov.kpi.entity.requestdata.PageDataRequestParam;
import com.trs.gov.kpi.entity.responsedata.ApiPageData;
import com.trs.gov.kpi.service.LinkAvailabilityService;
import com.trs.gov.kpi.service.outer.AuthorityService;
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
@RequestMapping(UrlPath.SERVICE_LINK_PATH)
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
        if (!authorityService.hasRight(requestParam.getSiteId(), null, Authority.KPIWEB_SERVICE_SEARCH)) {
            throw new BizException(Authority.NO_AUTHORITY);
        }
        ParamCheckUtil.paramCheck(requestParam);
        return linkAvailabilityService.getServiceLinkList(requestParam);
    }

}

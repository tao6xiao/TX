package com.trs.gov.kpi.controller;

import com.trs.gov.kpi.constant.Authority;
import com.trs.gov.kpi.constant.OperationType;
import com.trs.gov.kpi.constant.UrlPath;
import com.trs.gov.kpi.entity.exception.BizException;
import com.trs.gov.kpi.entity.exception.RemoteException;
import com.trs.gov.kpi.entity.requestdata.PageDataRequestParam;
import com.trs.gov.kpi.entity.responsedata.ApiPageData;
import com.trs.gov.kpi.ids.ContextHelper;
import com.trs.gov.kpi.service.LinkAvailabilityService;
import com.trs.gov.kpi.service.outer.AuthorityService;
import com.trs.gov.kpi.service.outer.SiteApiService;
import com.trs.gov.kpi.utils.LogUtil;
import com.trs.gov.kpi.utils.ParamCheckUtil;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Date;

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

    @Resource
    SiteApiService siteApiService;

    /**
     * 查询服务链接未解决问题列表
     *
     * @param requestParam
     * @return
     * @throws BizException
     */
    @RequestMapping(value = "/unhandled", method = RequestMethod.GET)
    public ApiPageData getServiceLinkList(@ModelAttribute PageDataRequestParam requestParam) throws BizException, RemoteException {
        Date startTime = new Date();
        if (!authorityService.hasRight(ContextHelper.getLoginUser().getUserName(), requestParam.getSiteId(), null, Authority.KPIWEB_SERVICE_SEARCH) && !authorityService.hasRight
                (ContextHelper.getLoginUser().getUserName(), null, null, Authority.KPIWEB_SERVICE_SEARCH)) {
            throw new BizException(Authority.NO_AUTHORITY);
        }
        ParamCheckUtil.paramCheck(requestParam);
        ApiPageData apiPageData = linkAvailabilityService.getServiceLinkList(requestParam);
        Date endTime = new Date();
        LogUtil.addOperationLog(OperationType.QUERY, "查询服务链接未解决问题列表", siteApiService.getSiteById(requestParam.getSiteId(), "").getSiteName());
        LogUtil.addElapseLog(OperationType.QUERY, "查询服务链接未解决问题列表", endTime.getTime()-startTime.getTime());
        return apiPageData;
    }

}

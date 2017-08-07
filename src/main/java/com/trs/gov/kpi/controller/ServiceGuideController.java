package com.trs.gov.kpi.controller;

import com.trs.gov.kpi.constant.Authority;
import com.trs.gov.kpi.constant.OperationType;
import com.trs.gov.kpi.entity.exception.BizException;
import com.trs.gov.kpi.entity.exception.RemoteException;
import com.trs.gov.kpi.entity.outerapi.sp.SGPageDataRes;
import com.trs.gov.kpi.entity.outerapi.sp.SGStatistics;
import com.trs.gov.kpi.entity.requestdata.PageDataRequestParam;
import com.trs.gov.kpi.entity.responsedata.History;
import com.trs.gov.kpi.ids.ContextHelper;
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
import java.util.Date;

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
     * @param param
     * @return
     * @throws BizException
     * @throws RemoteException
     */
    @RequestMapping(value = "/issue/bytype/count", method = RequestMethod.GET)
    public SGStatistics getSPCount(PageDataRequestParam param) throws BizException, RemoteException {
        Date startTime = new Date();
        if (!authorityService.hasRight(ContextHelper.getLoginUser().getUserName(), param.getSiteId(), null, Authority.KPIWEB_SERVICE_SEARCH) && !authorityService.hasRight(ContextHelper
                .getLoginUser().getUserName(), null, null, Authority.KPIWEB_SERVICE_SEARCH)) {
            throw new BizException(Authority.NO_AUTHORITY);
        }
        ParamCheckUtil.paramCheck(param);
        SGStatistics sgStatistics = sgService.getSGCount(param);
        Date endTime = new Date();
        LogUtil.addOperationLog(OperationType.QUERY, "查询服务指南按类型统计的问题总数", siteApiService.getSiteById(param.getSiteId(), "").getSiteName());
        LogUtil.addElapseLog(OperationType.QUERY, "查询服务指南按类型统计的问题总数", endTime.getTime()-startTime.getTime());
        return sgStatistics;
    }

    /**
     * 查询服务实用问题总数的历史纪录
     * @param param
     * @return
     * @throws BizException
     * @throws RemoteException
     */
    @RequestMapping(value = "/issue/all/count/history", method = RequestMethod.GET)
    public History getSPHistoryCount(PageDataRequestParam param) throws BizException, RemoteException {
        Date startTime = new Date();
        if (!authorityService.hasRight(ContextHelper.getLoginUser().getUserName(), param.getSiteId(), null, Authority.KPIWEB_SERVICE_SEARCH) && !authorityService.hasRight(ContextHelper
                .getLoginUser().getUserName(), null, null, Authority.KPIWEB_SERVICE_SEARCH)) {
            throw new BizException(Authority.NO_AUTHORITY);
        }
        ParamCheckUtil.paramCheck(param);
        History history = sgService.getSGHistoryCount(param);
        Date endTime = new Date();
        LogUtil.addOperationLog(OperationType.QUERY, "查询服务指南问题总数的历史纪录", siteApiService.getSiteById(param.getSiteId(), "").getSiteName());
        LogUtil.addElapseLog(OperationType.QUERY, "查询服务指南问题总数的历史纪录", endTime.getTime()-startTime.getTime());
        return history;
    }

    /**
     * 查询服务指南问题总数
     * @param param
     * @return
     * @throws BizException
     * @throws RemoteException
     */
    @RequestMapping(value = "/guide/issue/unhandled", method = RequestMethod.GET)
    public SGPageDataRes getSGList(@ModelAttribute PageDataRequestParam param) throws BizException, RemoteException {
        Date startTime = new Date();
        if (!authorityService.hasRight(ContextHelper.getLoginUser().getUserName(), param.getSiteId(), null, Authority.KPIWEB_SERVICE_SEARCH) && !authorityService.hasRight(ContextHelper
                .getLoginUser().getUserName(), null, null, Authority.KPIWEB_SERVICE_SEARCH)) {
            throw new BizException(Authority.NO_AUTHORITY);
        }
        ParamCheckUtil.paramCheck(param);
        SGPageDataRes sgPageDataRes = sgService.getSGList(param);
        Date endTime = new Date();
        LogUtil.addOperationLog(OperationType.QUERY, "查询服务指南问题总数", siteApiService.getSiteById(param.getSiteId(), "").getSiteName());
        LogUtil.addElapseLog(OperationType.QUERY, "查询服务指南问题总数", endTime.getTime()-startTime.getTime());
        return sgPageDataRes;
    }


}

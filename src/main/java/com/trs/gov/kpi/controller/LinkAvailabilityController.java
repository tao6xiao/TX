package com.trs.gov.kpi.controller;


import com.trs.gov.kpi.constant.Authority;
import com.trs.gov.kpi.constant.Constants;
import com.trs.gov.kpi.constant.OperationType;
import com.trs.gov.kpi.constant.UrlPath;
import com.trs.gov.kpi.entity.exception.BizException;
import com.trs.gov.kpi.entity.exception.RemoteException;
import com.trs.gov.kpi.entity.requestdata.PageDataRequestParam;
import com.trs.gov.kpi.entity.responsedata.ApiPageData;
import com.trs.gov.kpi.entity.responsedata.HistoryStatisticsRes;
import com.trs.gov.kpi.entity.responsedata.IndexPage;
import com.trs.gov.kpi.service.LinkAvailabilityService;
import com.trs.gov.kpi.service.outer.AuthorityService;
import com.trs.gov.kpi.utils.LogUtil;
import com.trs.gov.kpi.utils.ParamCheckUtil;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * 链接可用性问题
 */
@RestController
@RequestMapping(UrlPath.LINK_AVAILABILITY_PATH)
public class LinkAvailabilityController extends IssueHandler {

    @Resource
    private LinkAvailabilityService linkAvailabilityService;

    @Resource
    private AuthorityService authorityService;

    /**
     * 查询待解决和已解决问题数量
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/bytype/count", method = RequestMethod.GET)
    public List getIssueCount(@ModelAttribute PageDataRequestParam param) throws BizException, RemoteException {
        String logDesc = "查询链接可用性待解决和已解决问题数量" + LogUtil.paramsToLogString(Constants.PARAM, param);
        return LogUtil.ControlleFunctionWrapper(() -> {
            ParamCheckUtil.paramCheck(param);
            authorityService.checkRight(Authority.KPIWEB_AVAILABILITY_SEARCH, param.getSiteId());
            return linkAvailabilityService.getIssueCount(param);
        }, OperationType.QUERY, logDesc, LogUtil.getSiteNameForLog(siteApiService, param.getSiteId()));
    }

    /**
     * 查询待解决问题数量
     *
     * @param param
     * @return
     * @throws BizException
     */
    @RequestMapping(value = "/unhandled/count", method = RequestMethod.GET)
    public int getUnhandledIssueCount(@ModelAttribute PageDataRequestParam param) throws BizException, RemoteException {
        String logDesc = "查询链接可用性待解决问题数量" + LogUtil.paramsToLogString(Constants.PARAM, param);
        return LogUtil.ControlleFunctionWrapper(() -> {
            ParamCheckUtil.paramCheck(param);
            authorityService.checkRight(Authority.KPIWEB_AVAILABILITY_SEARCH, param.getSiteId());
            return linkAvailabilityService.getUnhandledIssueCount(param);
        }, OperationType.QUERY, logDesc, LogUtil.getSiteNameForLog(siteApiService, param.getSiteId()));
    }


    /**
     * 查询历史记录
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/all/count/history", method = RequestMethod.GET)
    public HistoryStatisticsRes getIssueHistoryCount(@ModelAttribute PageDataRequestParam param) throws BizException, RemoteException {
        String logDesc = "查询链接可用性历史纪录" + LogUtil.paramsToLogString(Constants.PARAM, param);
        return LogUtil.ControlleFunctionWrapper(() -> {
            ParamCheckUtil.paramCheck(param);
            authorityService.checkRight(Authority.KPIWEB_AVAILABILITY_SEARCH, param.getSiteId());
            return linkAvailabilityService.getIssueHistoryCount(param);
        }, OperationType.QUERY, logDesc, LogUtil.getSiteNameForLog(siteApiService, param.getSiteId()));
    }

    /**
     * 查询未解决问题列表
     *
     * @param param
     * @return
     * @throws BizException
     */
    @RequestMapping(value = "/unhandled", method = RequestMethod.GET)
    public ApiPageData getIssueList(@ModelAttribute PageDataRequestParam param) throws BizException, RemoteException {
        String logDesc = "查询链接可用性未解决问题列表" + LogUtil.paramsToLogString(Constants.PARAM, param);
        return LogUtil.ControlleFunctionWrapper(() -> {
            ParamCheckUtil.paramCheck(param);
            authorityService.checkRight(Authority.KPIWEB_AVAILABILITY_SEARCH, param.getSiteId());
            return linkAvailabilityService.getIssueList(param);
        }, OperationType.QUERY, logDesc, LogUtil.getSiteNameForLog(siteApiService, param.getSiteId()));
    }

    /**
     * 首页可用性校验显示
     *
     * @param param
     * @return
     * @throws BizException
     */
    @RequestMapping(value = "/check/index", method = RequestMethod.GET)
    public IndexPage showIndexAvailability(@ModelAttribute PageDataRequestParam param) throws BizException, RemoteException {
        String logDesc = "首页可用性校验" + LogUtil.paramsToLogString(Constants.PARAM, param);
        return LogUtil.ControlleFunctionWrapper(() -> {
            ParamCheckUtil.paramCheck(param);
            authorityService.checkRight(Authority.KPIWEB_AVAILABILITY_SEARCH, param.getSiteId());
            return linkAvailabilityService.showIndexAvailability(param);
        }, OperationType.QUERY, logDesc, LogUtil.getSiteNameForLog(siteApiService, param.getSiteId()));
    }

}

package com.trs.gov.kpi.controller;


import com.trs.gov.kpi.constant.Authority;
import com.trs.gov.kpi.constant.OperationType;
import com.trs.gov.kpi.constant.UrlPath;
import com.trs.gov.kpi.entity.exception.BizException;
import com.trs.gov.kpi.entity.exception.RemoteException;
import com.trs.gov.kpi.entity.requestdata.PageDataRequestParam;
import com.trs.gov.kpi.entity.responsedata.ApiPageData;
import com.trs.gov.kpi.entity.responsedata.History;
import com.trs.gov.kpi.entity.responsedata.IndexPage;
import com.trs.gov.kpi.ids.ContextHelper;
import com.trs.gov.kpi.service.LinkAvailabilityService;
import com.trs.gov.kpi.service.outer.AuthorityService;
import com.trs.gov.kpi.utils.ParamCheckUtil;
import com.trs.gov.kpi.utils.TRSLogUserUtil;
import com.trs.mlf.simplelog.SimpleLogServer;
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
        if (!authorityService.hasRight(ContextHelper.getLoginUser().getUserName(), param.getSiteId(), null, Authority.KPIWEB_AVAILABILITY_SEARCH) && !authorityService.hasRight
                (ContextHelper.getLoginUser().getUserName(), null, null, Authority.KPIWEB_AVAILABILITY_SEARCH)) {
            throw new BizException(Authority.NO_AUTHORITY);
        }
        ParamCheckUtil.paramCheck(param);
        SimpleLogServer.getInstance(TRSLogUserUtil.getLogUser()).operation(OperationType.QUERY, "查询链接可用性待解决和已解决问题数量", siteApiService.getSiteById(param.getSiteId(), "").getSiteName()).info();
        return linkAvailabilityService.getIssueCount(param);
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
        if (!authorityService.hasRight(ContextHelper.getLoginUser().getUserName(), param.getSiteId(), null, Authority.KPIWEB_AVAILABILITY_SEARCH) && !authorityService.hasRight
                (ContextHelper.getLoginUser().getUserName(), null, null, Authority.KPIWEB_AVAILABILITY_SEARCH)) {
            throw new BizException(Authority.NO_AUTHORITY);
        }
        ParamCheckUtil.paramCheck(param);
        SimpleLogServer.getInstance(TRSLogUserUtil.getLogUser()).operation(OperationType.QUERY, "查询链接可用性待解决问题数量", siteApiService.getSiteById(param.getSiteId(), "").getSiteName()).info();
        return linkAvailabilityService.getUnhandledIssueCount(param);
    }


    /**
     * 查询历史记录
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/all/count/history", method = RequestMethod.GET)
    public History getIssueHistoryCount(@ModelAttribute PageDataRequestParam param) throws BizException, RemoteException {
        if (!authorityService.hasRight(ContextHelper.getLoginUser().getUserName(), param.getSiteId(), null, Authority.KPIWEB_AVAILABILITY_SEARCH) && !authorityService.hasRight
                (ContextHelper.getLoginUser().getUserName(), null, null, Authority.KPIWEB_AVAILABILITY_SEARCH)) {
            throw new BizException(Authority.NO_AUTHORITY);
        }
        ParamCheckUtil.paramCheck(param);
        SimpleLogServer.getInstance(TRSLogUserUtil.getLogUser()).operation(OperationType.QUERY, "查询链接可用性历史纪录", siteApiService.getSiteById(param.getSiteId(), "").getSiteName()).info();
        return linkAvailabilityService.getIssueHistoryCount(param);
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
        if (!authorityService.hasRight(ContextHelper.getLoginUser().getUserName(), param.getSiteId(), null, Authority.KPIWEB_AVAILABILITY_SEARCH) && !authorityService.hasRight
                (ContextHelper.getLoginUser().getUserName(), null, null, Authority.KPIWEB_AVAILABILITY_SEARCH)) {
            throw new BizException(Authority.NO_AUTHORITY);
        }
        ParamCheckUtil.paramCheck(param);
        ApiPageData apiPageData = linkAvailabilityService.getIssueList(param);
        SimpleLogServer.getInstance(TRSLogUserUtil.getLogUser()).operation(OperationType.QUERY, "查询链接可用性未解决问题列表", siteApiService.getSiteById(param.getSiteId(), "").getSiteName()).info();
        return apiPageData;
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
        if (!authorityService.hasRight(ContextHelper.getLoginUser().getUserName(), param.getSiteId(), null, Authority.KPIWEB_AVAILABILITY_SEARCH) && !authorityService.hasRight
                (ContextHelper.getLoginUser().getUserName(), null, null, Authority.KPIWEB_AVAILABILITY_SEARCH)) {
            throw new BizException(Authority.NO_AUTHORITY);
        }
        ParamCheckUtil.paramCheck(param);
        IndexPage indexPage = linkAvailabilityService.showIndexAvailability(param);
        SimpleLogServer.getInstance(TRSLogUserUtil.getLogUser()).operation(OperationType.QUERY, "首页可用性校验", siteApiService.getSiteById(param.getSiteId(), "").getSiteName()).info();
        return indexPage;
    }

}

package com.trs.gov.kpi.controller;

import com.trs.gov.kpi.constant.Authority;
import com.trs.gov.kpi.constant.Constants;
import com.trs.gov.kpi.constant.OperationType;
import com.trs.gov.kpi.entity.exception.BizException;
import com.trs.gov.kpi.entity.exception.RemoteException;
import com.trs.gov.kpi.entity.requestdata.PageDataRequestParam;
import com.trs.gov.kpi.entity.responsedata.ApiPageData;
import com.trs.gov.kpi.service.WebPageService;
import com.trs.gov.kpi.service.outer.AuthorityService;
import com.trs.gov.kpi.service.outer.SiteApiService;
import com.trs.gov.kpi.utils.LogUtil;
import com.trs.gov.kpi.utils.ParamCheckUtil;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Arrays;

/**
 * Created by ranwei on 2017/6/6.
 */
@RestController
@RequestMapping("/gov/kpi/analysis/improve")
public class WebPageController {

    @Resource
    private WebPageService webPageService;

    @Resource
    private AuthorityService authorityService;

    @Resource
    SiteApiService siteApiService;

    /**
     * 响应速度的统计查询
     *
     * @param param
     * @return
     * @throws BizException
     * @throws RemoteException
     */
    @RequestMapping(value = "/speed/count", method = RequestMethod.GET)
    public int selectReplySpeedCount(@ModelAttribute PageDataRequestParam param) throws BizException, RemoteException {
        String logDesc = "响应速度统计查询" + LogUtil.paramsToLogString(Constants.PARAM, param);
        return LogUtil.controlleFunctionWrapper(() -> {
            ParamCheckUtil.paramCheck(param);
            authorityService.checkRight(Authority.KPIWEB_IMPROVE_SEARCH, param.getSiteId());
            return webPageService.selectReplySpeedCount(param);
        }, OperationType.QUERY, logDesc, LogUtil.getSiteNameForLog(siteApiService, param.getSiteId()));
    }

    /**
     * 响应速度的列表查询
     *
     * @param param
     * @return
     * @throws BizException
     * @throws RemoteException
     */
    @RequestMapping(value = "/speed", method = RequestMethod.GET)
    public ApiPageData selectReplySpeed(@ModelAttribute PageDataRequestParam param) throws BizException, RemoteException {
        String logDesc = "响应速度列表查询" + LogUtil.paramsToLogString(Constants.PARAM, param);
        return LogUtil.controlleFunctionWrapper(() -> {
            ParamCheckUtil.paramCheck(param);
            authorityService.checkRight(Authority.KPIWEB_IMPROVE_SEARCH, param.getSiteId());
            return webPageService.selectReplySpeed(param);
        }, OperationType.QUERY, logDesc, LogUtil.getSiteNameForLog(siteApiService, param.getSiteId()));

    }

    /**
     * 过大页面的统计查询
     *
     * @param param
     * @return
     * @throws BizException
     * @throws RemoteException
     */
    @RequestMapping(value = "/size/count", method = RequestMethod.GET)
    public int selectPageSpaceCount(@ModelAttribute PageDataRequestParam param) throws BizException, RemoteException {
        String logDesc = "过大页面的统计查询" + LogUtil.paramsToLogString(Constants.PARAM, param);
        return LogUtil.controlleFunctionWrapper(() -> {
            ParamCheckUtil.paramCheck(param);
            authorityService.checkRight(Authority.KPIWEB_IMPROVE_SEARCH, param.getSiteId());
            return webPageService.selectPageSpaceCount(param);
        }, OperationType.QUERY, logDesc, LogUtil.getSiteNameForLog(siteApiService, param.getSiteId()));

    }

    /**
     * 过大页面列表查询
     *
     * @param param
     * @return
     * @throws BizException
     * @throws RemoteException
     */
    @RequestMapping(value = "/size", method = RequestMethod.GET)
    public ApiPageData selectPageSpace(@ModelAttribute PageDataRequestParam param) throws BizException, RemoteException {
        String logDesc = "过大页面列表查询" + LogUtil.paramsToLogString(Constants.PARAM, param);
        return LogUtil.controlleFunctionWrapper(() -> {
            authorityService.checkRight(Authority.KPIWEB_IMPROVE_SEARCH, param.getSiteId());
            ParamCheckUtil.paramCheck(param);
            return webPageService.selectPageSpace(param);
        }, OperationType.QUERY, logDesc, LogUtil.getSiteNameForLog(siteApiService, param.getSiteId()));
    }

    /**
     * 过深页面统计查询
     *
     * @param param
     * @return
     * @throws BizException
     * @throws RemoteException
     */
    @RequestMapping(value = "/depth/count", method = RequestMethod.GET)
    public int selectPageDepthCount(@ModelAttribute PageDataRequestParam param) throws BizException, RemoteException {
        String logDesc = "过深页面统计查询" + LogUtil.paramsToLogString(Constants.PARAM, param);
        return LogUtil.controlleFunctionWrapper(() -> {
            authorityService.checkRight(Authority.KPIWEB_IMPROVE_SEARCH, param.getSiteId());
            ParamCheckUtil.paramCheck(param);
            return webPageService.selectPageDepthCount(param);
        }, OperationType.QUERY, logDesc, LogUtil.getSiteNameForLog(siteApiService, param.getSiteId()));

    }

    /**
     * 过深页面列表查询
     *
     * @param param
     * @return
     * @throws BizException
     * @throws RemoteException
     */
    @RequestMapping(value = "/depth", method = RequestMethod.GET)
    public ApiPageData selectPageDepth(@ModelAttribute PageDataRequestParam param) throws BizException, RemoteException {
        String logDesc = "过深页面列表查询" + LogUtil.paramsToLogString(Constants.PARAM, param);
        return LogUtil.controlleFunctionWrapper(() -> {
            authorityService.checkRight(Authority.KPIWEB_IMPROVE_SEARCH, param.getSiteId());
            ParamCheckUtil.paramCheck(param);
            return webPageService.selectPageDepth(param);
        }, OperationType.QUERY, logDesc, LogUtil.getSiteNameForLog(siteApiService, param.getSiteId()));

    }

    /**
     * 冗余代码统计查询
     *
     * @param param
     * @return
     * @throws BizException
     * @throws RemoteException
     */
    @RequestMapping(value = "/code/count", method = RequestMethod.GET)
    public int selectRepeatCodeCount(@ModelAttribute PageDataRequestParam param) throws BizException, RemoteException {
        String logDesc = "冗余代码统计查询" + LogUtil.paramsToLogString(Constants.PARAM, param);
        return LogUtil.controlleFunctionWrapper(() -> {
            authorityService.checkRight(Authority.KPIWEB_IMPROVE_SEARCH, param.getSiteId());
            ParamCheckUtil.paramCheck(param);
            return webPageService.selectRepeatCodeCount(param);
        }, OperationType.QUERY, logDesc, LogUtil.getSiteNameForLog(siteApiService, param.getSiteId()));

    }

    /**
     * 冗余代码列表查询
     *
     * @param param
     * @return
     * @throws BizException
     * @throws RemoteException
     */
    @RequestMapping(value = "/code", method = RequestMethod.GET)
    public ApiPageData selectRepeatCode(@ModelAttribute PageDataRequestParam param) throws BizException, RemoteException {
        String logDesc = "冗余代码列表查询" + LogUtil.paramsToLogString(Constants.PARAM, param);
        return LogUtil.controlleFunctionWrapper(() -> {
            authorityService.checkRight(Authority.KPIWEB_IMPROVE_SEARCH, param.getSiteId());
            ParamCheckUtil.paramCheck(param);
            return webPageService.selectRepeatCode(param);
        }, OperationType.QUERY, logDesc, LogUtil.getSiteNameForLog(siteApiService, param.getSiteId()));
    }

    /**
     * 过长URL页面统计查询
     *
     * @param param
     * @return
     * @throws BizException
     * @throws RemoteException
     */
    @RequestMapping(value = "/length/count", method = RequestMethod.GET)
    public int selectUrlLengthCount(@ModelAttribute PageDataRequestParam param) throws BizException, RemoteException {
        String logDesc = "过长URL页面统计查询" + LogUtil.paramsToLogString(Constants.PARAM, param);
        return LogUtil.controlleFunctionWrapper(() -> {
            authorityService.checkRight(Authority.KPIWEB_IMPROVE_SEARCH, param.getSiteId());
            ParamCheckUtil.paramCheck(param);
            return webPageService.selectUrlLengthCount(param);
        }, OperationType.QUERY, logDesc, LogUtil.getSiteNameForLog(siteApiService, param.getSiteId()));

    }

    /**
     * 过长URL页面列表查询
     *
     * @param param
     * @return
     * @throws BizException
     * @throws RemoteException
     */
    @RequestMapping(value = "/length", method = RequestMethod.GET)
    public ApiPageData selectUrlLength(@ModelAttribute PageDataRequestParam param) throws BizException, RemoteException {
        String logDesc = "过长URL页面列表查询" + LogUtil.paramsToLogString(Constants.PARAM, param);
        return LogUtil.controlleFunctionWrapper(() -> {
            authorityService.checkRight(Authority.KPIWEB_IMPROVE_SEARCH, param.getSiteId());
            ParamCheckUtil.paramCheck(param);
            return webPageService.selectUrlLength(param);
        }, OperationType.QUERY, logDesc, LogUtil.getSiteNameForLog(siteApiService, param.getSiteId()));

    }

    /**
     * 处理访问优化问题
     *
     * @param siteId
     * @param ids
     * @return
     * @throws BizException
     * @throws RemoteException
     */
    @RequestMapping(value = "/handle", method = RequestMethod.POST)
    public String handlePageByIds(int siteId, Integer[] ids) throws BizException, RemoteException {
        String logDesc = "处理访问优化问题" + LogUtil.paramsToLogString(Constants.DB_FIELD_SITE_ID, siteId, Constants.IDS, ids);
        return LogUtil.controlleFunctionWrapper(() -> {
            authorityService.checkRight(Authority.KPIWEB_IMPROVE_HANDLE, siteId);
            webPageService.handlePageByIds(siteId, Arrays.asList(ids));
            return null;
        }, OperationType.UPDATE, logDesc, LogUtil.getSiteNameForLog(siteApiService, siteId));

    }

    /**
     * 忽略访问优化问题
     *
     * @param siteId
     * @param ids
     * @return
     * @throws RemoteException
     * @throws BizException
     */
    @RequestMapping(value = "/ignore", method = RequestMethod.POST)
    public String ignorePageByIds(int siteId, Integer[] ids) throws RemoteException, BizException {
        String logDesc = "忽略访问优化问题" + LogUtil.paramsToLogString(Constants.DB_FIELD_SITE_ID, siteId, Constants.IDS, ids);
        return LogUtil.controlleFunctionWrapper(() -> {
            authorityService.checkRight(Authority.KPIWEB_IMPROVE_IGNORE, siteId);
            webPageService.ignorePageByIds(siteId, Arrays.asList(ids));
            return null;
        }, OperationType.UPDATE, logDesc, LogUtil.getSiteNameForLog(siteApiService, siteId));

    }

    /**
     * 删除访问优化问题
     *
     * @param siteId
     * @param ids
     * @return
     * @throws RemoteException
     * @throws BizException
     */
    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    public String delPageByIds(int siteId, Integer[] ids) throws RemoteException, BizException {
        String logDesc = "删除访问优化问题" + LogUtil.paramsToLogString(Constants.DB_FIELD_SITE_ID, siteId, Constants.IDS, ids);
        return LogUtil.controlleFunctionWrapper(() -> {
            authorityService.checkRight(Authority.KPIWEB_IMPROVE_DELETE, siteId);
            webPageService.delPageByIds(siteId, Arrays.asList(ids));
            return null;
        }, OperationType.UPDATE, logDesc, LogUtil.getSiteNameForLog(siteApiService, siteId));
    }
}

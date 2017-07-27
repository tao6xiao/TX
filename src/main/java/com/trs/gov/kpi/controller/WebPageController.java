package com.trs.gov.kpi.controller;

import com.trs.gov.kpi.constant.Authority;
import com.trs.gov.kpi.constant.OperationType;
import com.trs.gov.kpi.entity.exception.BizException;
import com.trs.gov.kpi.entity.exception.RemoteException;
import com.trs.gov.kpi.entity.requestdata.PageDataRequestParam;
import com.trs.gov.kpi.entity.responsedata.ApiPageData;
import com.trs.gov.kpi.ids.ContextHelper;
import com.trs.gov.kpi.service.WebPageService;
import com.trs.gov.kpi.service.outer.AuthorityService;
import com.trs.gov.kpi.service.outer.SiteApiService;
import com.trs.gov.kpi.utils.ParamCheckUtil;
import com.trs.gov.kpi.utils.TRSLogUserUtil;
import com.trs.mlf.simplelog.SimpleLogServer;
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
     * @param param
     * @return
     * @throws BizException
     * @throws RemoteException
     */
    @RequestMapping(value = "/speed/count", method = RequestMethod.GET)
    public int selectReplySpeedCount(@ModelAttribute PageDataRequestParam param) throws BizException, RemoteException {
        if (!authorityService.hasRight(ContextHelper.getLoginUser().getUserName(), param.getSiteId(), null, Authority.KPIWEB_IMPROVE_SEARCH) && !authorityService.hasRight(ContextHelper
                .getLoginUser().getUserName(), null, null, Authority.KPIWEB_IMPROVE_SEARCH)) {
            throw new BizException(Authority.NO_AUTHORITY);
        }
        ParamCheckUtil.paramCheck(param);
        int value = webPageService.selectReplySpeedCount(param);
        SimpleLogServer.getInstance(TRSLogUserUtil.getLogUser()).operation(OperationType.QUERY, "响应速度统计查询", siteApiService.getSiteById(param.getSiteId(), "").getSiteName()).info();
        return value;
    }

    /**
     * 响应速度的列表查询
     * @param param
     * @return
     * @throws BizException
     * @throws RemoteException
     */
    @RequestMapping(value = "/speed", method = RequestMethod.GET)
    public ApiPageData selectReplySpeed(@ModelAttribute PageDataRequestParam param) throws BizException, RemoteException {
        if (!authorityService.hasRight(ContextHelper.getLoginUser().getUserName(), param.getSiteId(), null, Authority.KPIWEB_IMPROVE_SEARCH) && !authorityService.hasRight(ContextHelper
                .getLoginUser().getUserName(), null, null, Authority.KPIWEB_IMPROVE_SEARCH)) {
            throw new BizException(Authority.NO_AUTHORITY);
        }
        ParamCheckUtil.paramCheck(param);
        ApiPageData apiPageData = webPageService.selectReplySpeed(param);
        SimpleLogServer.getInstance(TRSLogUserUtil.getLogUser()).operation(OperationType.QUERY, "响应速度列表查询", siteApiService.getSiteById(param.getSiteId(), "").getSiteName()).info();
        return apiPageData;
    }

    /**
     * 过大页面的统计查询
     * @param param
     * @return
     * @throws BizException
     * @throws RemoteException
     */
    @RequestMapping(value = "/size/count", method = RequestMethod.GET)
    public int selectPageSpaceCount(@ModelAttribute PageDataRequestParam param) throws BizException, RemoteException {
        if (!authorityService.hasRight(ContextHelper.getLoginUser().getUserName(), param.getSiteId(), null, Authority.KPIWEB_IMPROVE_SEARCH) && !authorityService.hasRight(ContextHelper
                .getLoginUser().getUserName(), null, null, Authority.KPIWEB_IMPROVE_SEARCH)) {
            throw new BizException(Authority.NO_AUTHORITY);
        }
        ParamCheckUtil.paramCheck(param);
        int value = webPageService.selectPageSpaceCount(param);
        SimpleLogServer.getInstance(TRSLogUserUtil.getLogUser()).operation(OperationType.QUERY, "过大页面的统计查询", siteApiService.getSiteById(param.getSiteId(), "").getSiteName()).info();
        return value;
    }

    /**
     * 过大页面列表查询
     * @param param
     * @return
     * @throws BizException
     * @throws RemoteException
     */
    @RequestMapping(value = "/size", method = RequestMethod.GET)
    public ApiPageData selectPageSpace(@ModelAttribute PageDataRequestParam param) throws BizException, RemoteException {
        if (!authorityService.hasRight(ContextHelper.getLoginUser().getUserName(), param.getSiteId(), null, Authority.KPIWEB_IMPROVE_SEARCH) && !authorityService.hasRight(ContextHelper
                .getLoginUser().getUserName(), null, null, Authority.KPIWEB_IMPROVE_SEARCH)) {
            throw new BizException(Authority.NO_AUTHORITY);
        }
        ParamCheckUtil.paramCheck(param);
        ApiPageData apiPageData = webPageService.selectPageSpace(param);
        SimpleLogServer.getInstance(TRSLogUserUtil.getLogUser()).operation(OperationType.QUERY, "过大页面列表查询", siteApiService.getSiteById(param.getSiteId(), "").getSiteName()).info();
        return apiPageData;
    }

    /**
     * 过深页面统计查询
     * @param param
     * @return
     * @throws BizException
     * @throws RemoteException
     */
    @RequestMapping(value = "/depth/count", method = RequestMethod.GET)
    public int selectPageDepthCount(@ModelAttribute PageDataRequestParam param) throws BizException, RemoteException {
        if (!authorityService.hasRight(ContextHelper.getLoginUser().getUserName(), param.getSiteId(), null, Authority.KPIWEB_IMPROVE_SEARCH) && !authorityService.hasRight(ContextHelper
                .getLoginUser().getUserName(), null, null, Authority.KPIWEB_IMPROVE_SEARCH)) {
            throw new BizException(Authority.NO_AUTHORITY);
        }
        ParamCheckUtil.paramCheck(param);
        int value = webPageService.selectPageDepthCount(param);
        SimpleLogServer.getInstance(TRSLogUserUtil.getLogUser()).operation(OperationType.QUERY, "过深页面统计查询", siteApiService.getSiteById(param.getSiteId(), "").getSiteName()).info();
        return value;
    }

    /**
     * 过深页面列表查询
     * @param param
     * @return
     * @throws BizException
     * @throws RemoteException
     */
    @RequestMapping(value = "/depth", method = RequestMethod.GET)
    public ApiPageData selectPageDepth(@ModelAttribute PageDataRequestParam param) throws BizException, RemoteException {
        if (!authorityService.hasRight(ContextHelper.getLoginUser().getUserName(), param.getSiteId(), null, Authority.KPIWEB_IMPROVE_SEARCH) && !authorityService.hasRight(ContextHelper
                .getLoginUser().getUserName(), null, null, Authority.KPIWEB_IMPROVE_SEARCH)) {
            throw new BizException(Authority.NO_AUTHORITY);
        }
        ParamCheckUtil.paramCheck(param);
        ApiPageData apiPageData = webPageService.selectPageDepth(param);
        SimpleLogServer.getInstance(TRSLogUserUtil.getLogUser()).operation(OperationType.QUERY, "过深页面列表查询", siteApiService.getSiteById(param.getSiteId(), "").getSiteName()).info();
        return apiPageData;
    }

    /**
     * 冗余代码统计查询
     * @param param
     * @return
     * @throws BizException
     * @throws RemoteException
     */
    @RequestMapping(value = "/code/count", method = RequestMethod.GET)
    public int selectRepeatCodeCount(@ModelAttribute PageDataRequestParam param) throws BizException, RemoteException {
        if (!authorityService.hasRight(ContextHelper.getLoginUser().getUserName(), param.getSiteId(), null, Authority.KPIWEB_IMPROVE_SEARCH) && !authorityService.hasRight(ContextHelper
                .getLoginUser().getUserName(), null, null, Authority.KPIWEB_IMPROVE_SEARCH)) {
            throw new BizException(Authority.NO_AUTHORITY);
        }
        ParamCheckUtil.paramCheck(param);
        int value = webPageService.selectRepeatCodeCount(param);
        SimpleLogServer.getInstance(TRSLogUserUtil.getLogUser()).operation(OperationType.QUERY, "冗余代码统计查询", siteApiService.getSiteById(param.getSiteId(), "").getSiteName()).info();
        return value;
    }

    /**
     * 冗余代码列表查询
     * @param param
     * @return
     * @throws BizException
     * @throws RemoteException
     */
    @RequestMapping(value = "/code", method = RequestMethod.GET)
    public ApiPageData selectRepeatCode(@ModelAttribute PageDataRequestParam param) throws BizException, RemoteException {
        if (!authorityService.hasRight(ContextHelper.getLoginUser().getUserName(), param.getSiteId(), null, Authority.KPIWEB_IMPROVE_SEARCH) && !authorityService.hasRight(ContextHelper
                .getLoginUser().getUserName(), null, null, Authority.KPIWEB_IMPROVE_SEARCH)) {
            throw new BizException(Authority.NO_AUTHORITY);
        }
        ParamCheckUtil.paramCheck(param);
        ApiPageData apiPageData = webPageService.selectRepeatCode(param);
        SimpleLogServer.getInstance(TRSLogUserUtil.getLogUser()).operation(OperationType.QUERY, "冗余代码列表查询", siteApiService.getSiteById(param.getSiteId(), "").getSiteName()).info();
        return apiPageData;
    }

    /**
     * 过长URL页面统计查询
     * @param param
     * @return
     * @throws BizException
     * @throws RemoteException
     */
    @RequestMapping(value = "/length/count", method = RequestMethod.GET)
    public int selectUrlLengthCount(@ModelAttribute PageDataRequestParam param) throws BizException, RemoteException {
        if (!authorityService.hasRight(ContextHelper.getLoginUser().getUserName(), param.getSiteId(), null, Authority.KPIWEB_IMPROVE_SEARCH) && !authorityService.hasRight(ContextHelper
                .getLoginUser().getUserName(), null, null, Authority.KPIWEB_IMPROVE_SEARCH)) {
            throw new BizException(Authority.NO_AUTHORITY);
        }
        ParamCheckUtil.paramCheck(param);
        int value = webPageService.selectUrlLengthCount(param);
        SimpleLogServer.getInstance(TRSLogUserUtil.getLogUser()).operation(OperationType.QUERY, "过长URL页面统计查询", siteApiService.getSiteById(param.getSiteId(), "").getSiteName()).info();
        return value;
    }

    /**
     * 过长URL页面列表查询
     * @param param
     * @return
     * @throws BizException
     * @throws RemoteException
     */
    @RequestMapping(value = "/length", method = RequestMethod.GET)
    public ApiPageData selectUrlLength(@ModelAttribute PageDataRequestParam param) throws BizException, RemoteException {
        if (!authorityService.hasRight(ContextHelper.getLoginUser().getUserName(), param.getSiteId(), null, Authority.KPIWEB_IMPROVE_SEARCH) && !authorityService.hasRight(ContextHelper
                .getLoginUser().getUserName(), null, null, Authority.KPIWEB_IMPROVE_SEARCH)) {
            throw new BizException(Authority.NO_AUTHORITY);
        }
        ParamCheckUtil.paramCheck(param);
        ApiPageData apiPageData = webPageService.selectUrlLength(param);
        SimpleLogServer.getInstance(TRSLogUserUtil.getLogUser()).operation(OperationType.QUERY, "过长URL页面列表查询", siteApiService.getSiteById(param.getSiteId(), "").getSiteName()).info();
        return apiPageData;
    }

    /**
     * 处理访问优化问题
     * @param siteId
     * @param ids
     * @return
     * @throws BizException
     * @throws RemoteException
     */
    @RequestMapping(value = "/handle", method = RequestMethod.POST)
    public String handlePageByIds(int siteId, Integer[] ids) throws BizException, RemoteException {
        if (!authorityService.hasRight(ContextHelper.getLoginUser().getUserName(), siteId, null, Authority.KPIWEB_IMPROVE_HANDLE) && !authorityService.hasRight(ContextHelper.getLoginUser
                ().getUserName(), null, null, Authority.KPIWEB_IMPROVE_HANDLE)) {
            throw new BizException(Authority.NO_AUTHORITY);
        }
        webPageService.handlePageByIds(siteId, Arrays.asList(ids));
        SimpleLogServer.getInstance(TRSLogUserUtil.getLogUser()).operation(OperationType.UPDATE, "处理访问优化问题", siteApiService.getSiteById(siteId, "").getSiteName()).info();
        return null;
    }

    /**
     * 忽略访问优化问题
     * @param siteId
     * @param ids
     * @return
     * @throws RemoteException
     * @throws BizException
     */
    @RequestMapping(value = "/ignore", method = RequestMethod.POST)
    public String ignorePageByIds(int siteId, Integer[] ids) throws RemoteException, BizException {
        if (!authorityService.hasRight(ContextHelper.getLoginUser().getUserName(), siteId, null, Authority.KPIWEB_IMPROVE_IGNORE) && !authorityService.hasRight(ContextHelper.getLoginUser
                ().getUserName(), null, null, Authority.KPIWEB_IMPROVE_IGNORE)) {
            throw new BizException(Authority.NO_AUTHORITY);
        }
        webPageService.ignorePageByIds(siteId, Arrays.asList(ids));
        SimpleLogServer.getInstance(TRSLogUserUtil.getLogUser()).operation(OperationType.UPDATE, "忽略访问优化问题", siteApiService.getSiteById(siteId, "").getSiteName()).info();
        return null;
    }

    /**
     * 删除访问优化问题
     * @param siteId
     * @param ids
     * @return
     * @throws RemoteException
     * @throws BizException
     */
    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    public String delPageByIds(int siteId, Integer[] ids) throws RemoteException, BizException {
        if (!authorityService.hasRight(ContextHelper.getLoginUser().getUserName(), siteId, null, Authority.KPIWEB_IMPROVE_DELETE) && !authorityService.hasRight(ContextHelper.getLoginUser
                ().getUserName(), null, null, Authority.KPIWEB_IMPROVE_DELETE)) {
            throw new BizException(Authority.NO_AUTHORITY);
        }
        webPageService.delPageByIds(siteId, Arrays.asList(ids));
        SimpleLogServer.getInstance(TRSLogUserUtil.getLogUser()).operation(OperationType.DELETE, "删除访问优化问题", siteApiService.getSiteById(siteId, "").getSiteName()).info();
        return null;
    }
}

package com.trs.gov.kpi.controller;

import com.trs.gov.kpi.constant.Authority;
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
import java.util.Date;

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
        String logDesc = "响应速度统计查询";
        try {
            Date startTime = new Date();
            ParamCheckUtil.paramCheck(param);
            authorityService.checkRight(Authority.KPIWEB_IMPROVE_SEARCH, param.getSiteId());
            int value = webPageService.selectReplySpeedCount(param);
            Date endTime = new Date();
            LogUtil.addOperationLog(OperationType.QUERY, logDesc, LogUtil.getSiteNameForLog(siteApiService, param.getSiteId()));
            LogUtil.addElapseLog(OperationType.QUERY, LogUtil.buildElapseLogDesc(siteApiService, param.getSiteId(), logDesc), endTime.getTime() - startTime.getTime());
            return value;
        } catch (Exception e) {
            LogUtil.addOperationLog(OperationType.QUERY, LogUtil.buildFailOperationLogDesc(logDesc), LogUtil.getSiteNameForLog(siteApiService, param.getSiteId()));
            throw e;
        }

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
        String logDesc = "响应速度列表查询";
        try {
            Date startTime = new Date();
            ParamCheckUtil.paramCheck(param);
            authorityService.checkRight(Authority.KPIWEB_IMPROVE_SEARCH, param.getSiteId());
            ApiPageData apiPageData = webPageService.selectReplySpeed(param);
            Date endTime = new Date();
            LogUtil.addOperationLog(OperationType.QUERY, logDesc, LogUtil.getSiteNameForLog(siteApiService, param.getSiteId()));
            LogUtil.addElapseLog(OperationType.QUERY, LogUtil.buildElapseLogDesc(siteApiService, param.getSiteId(), logDesc), endTime.getTime() - startTime.getTime());
            return apiPageData;
        } catch (Exception e) {
            LogUtil.addOperationLog(OperationType.QUERY, LogUtil.buildFailOperationLogDesc(logDesc), LogUtil.getSiteNameForLog(siteApiService, param.getSiteId()));
            throw e;
        }

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
        String logDesc = "过大页面的统计查询";
        try {
            Date startTime = new Date();
            ParamCheckUtil.paramCheck(param);
            authorityService.checkRight(Authority.KPIWEB_IMPROVE_SEARCH, param.getSiteId());
            int value = webPageService.selectPageSpaceCount(param);
            Date endTime = new Date();
            LogUtil.addOperationLog(OperationType.QUERY, logDesc, LogUtil.getSiteNameForLog(siteApiService, param.getSiteId()));
            LogUtil.addElapseLog(OperationType.QUERY, LogUtil.buildElapseLogDesc(siteApiService, param.getSiteId(), logDesc), endTime.getTime() - startTime.getTime());
            return value;
        } catch (Exception e) {
            LogUtil.addOperationLog(OperationType.QUERY, LogUtil.buildFailOperationLogDesc(logDesc), LogUtil.getSiteNameForLog(siteApiService, param.getSiteId()));
            throw e;
        }

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
        String logDesc = "过大页面列表查询";
        try {
            Date startTime = new Date();
            authorityService.checkRight(Authority.KPIWEB_IMPROVE_SEARCH, param.getSiteId());
            ParamCheckUtil.paramCheck(param);
            ApiPageData apiPageData = webPageService.selectPageSpace(param);
            Date endTime = new Date();
            LogUtil.addOperationLog(OperationType.QUERY, logDesc, LogUtil.getSiteNameForLog(siteApiService, param.getSiteId()));
            LogUtil.addElapseLog(OperationType.QUERY, LogUtil.buildElapseLogDesc(siteApiService, param.getSiteId(), logDesc), endTime.getTime() - startTime.getTime());
            return apiPageData;
        } catch (Exception e) {
            LogUtil.addOperationLog(OperationType.QUERY, LogUtil.buildFailOperationLogDesc(logDesc), LogUtil.getSiteNameForLog(siteApiService, param.getSiteId()));
            throw e;
        }

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
        String logDesc = "过深页面统计查询";
        try {
            Date startTime = new Date();
            authorityService.checkRight(Authority.KPIWEB_IMPROVE_SEARCH, param.getSiteId());
            ParamCheckUtil.paramCheck(param);
            int value = webPageService.selectPageDepthCount(param);
            Date endTime = new Date();
            LogUtil.addOperationLog(OperationType.QUERY, logDesc, LogUtil.getSiteNameForLog(siteApiService, param.getSiteId()));
            LogUtil.addElapseLog(OperationType.QUERY, LogUtil.buildElapseLogDesc(siteApiService, param.getSiteId(), logDesc), endTime.getTime() - startTime.getTime());
            return value;
        } catch (Exception e) {
            LogUtil.addOperationLog(OperationType.QUERY, LogUtil.buildFailOperationLogDesc(logDesc), LogUtil.getSiteNameForLog(siteApiService, param.getSiteId()));
            throw e;
        }

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
        String logDesc = "过深页面列表查询";
        try {
            Date startTime = new Date();
            authorityService.checkRight(Authority.KPIWEB_IMPROVE_SEARCH, param.getSiteId());
            ParamCheckUtil.paramCheck(param);
            ApiPageData apiPageData = webPageService.selectPageDepth(param);
            Date endTime = new Date();
            LogUtil.addOperationLog(OperationType.QUERY, logDesc, LogUtil.getSiteNameForLog(siteApiService, param.getSiteId()));
            LogUtil.addElapseLog(OperationType.QUERY, LogUtil.buildElapseLogDesc(siteApiService, param.getSiteId(), logDesc), endTime.getTime() - startTime.getTime());
            return apiPageData;
        } catch (Exception e) {
            LogUtil.addOperationLog(OperationType.QUERY, LogUtil.buildFailOperationLogDesc(logDesc), LogUtil.getSiteNameForLog(siteApiService, param.getSiteId()));
            throw e;
        }

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
        String logDesc = "冗余代码统计查询";
        try {
            Date startTime = new Date();
            authorityService.checkRight(Authority.KPIWEB_IMPROVE_SEARCH, param.getSiteId());
            ParamCheckUtil.paramCheck(param);
            int value = webPageService.selectRepeatCodeCount(param);
            Date endTime = new Date();
            LogUtil.addOperationLog(OperationType.QUERY, logDesc, LogUtil.getSiteNameForLog(siteApiService, param.getSiteId()));
            LogUtil.addElapseLog(OperationType.QUERY, LogUtil.buildElapseLogDesc(siteApiService, param.getSiteId(), logDesc), endTime.getTime() - startTime.getTime());
            return value;
        } catch (Exception e) {
            LogUtil.addOperationLog(OperationType.QUERY, LogUtil.buildFailOperationLogDesc(logDesc), LogUtil.getSiteNameForLog(siteApiService, param.getSiteId()));
            throw e;
        }

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
        String logDesc = "冗余代码列表查询";
        try {
            Date startTime = new Date();
            authorityService.checkRight(Authority.KPIWEB_IMPROVE_SEARCH, param.getSiteId());
            ParamCheckUtil.paramCheck(param);
            ApiPageData apiPageData = webPageService.selectRepeatCode(param);
            Date endTime = new Date();
            LogUtil.addOperationLog(OperationType.QUERY, logDesc, LogUtil.getSiteNameForLog(siteApiService, param.getSiteId()));
            LogUtil.addElapseLog(OperationType.QUERY, LogUtil.buildElapseLogDesc(siteApiService, param.getSiteId(), logDesc), endTime.getTime() - startTime.getTime());
            return apiPageData;
        } catch (Exception e) {
            LogUtil.addOperationLog(OperationType.QUERY, LogUtil.buildFailOperationLogDesc(logDesc), LogUtil.getSiteNameForLog(siteApiService, param.getSiteId()));
            throw e;
        }

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
        String logDesc = "过长URL页面统计查询";
        try {
            Date startTime = new Date();
            authorityService.checkRight(Authority.KPIWEB_IMPROVE_SEARCH, param.getSiteId());
            ParamCheckUtil.paramCheck(param);
            int value = webPageService.selectUrlLengthCount(param);
            Date endTime = new Date();
            LogUtil.addOperationLog(OperationType.QUERY, logDesc, LogUtil.getSiteNameForLog(siteApiService, param.getSiteId()));
            LogUtil.addElapseLog(OperationType.QUERY, LogUtil.buildElapseLogDesc(siteApiService, param.getSiteId(), logDesc), endTime.getTime() - startTime.getTime());
            return value;
        } catch (Exception e) {
            LogUtil.addOperationLog(OperationType.QUERY, LogUtil.buildFailOperationLogDesc(logDesc), LogUtil.getSiteNameForLog(siteApiService, param.getSiteId()));
            throw e;
        }

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
        String logDesc = "过长URL页面列表查询";
        try {
            Date startTime = new Date();
            authorityService.checkRight(Authority.KPIWEB_IMPROVE_SEARCH, param.getSiteId());
            ParamCheckUtil.paramCheck(param);
            Date endTime = new Date();
            ApiPageData apiPageData = webPageService.selectUrlLength(param);
            LogUtil.addOperationLog(OperationType.QUERY, logDesc, LogUtil.getSiteNameForLog(siteApiService, param.getSiteId()));
            LogUtil.addElapseLog(OperationType.QUERY, LogUtil.buildElapseLogDesc(siteApiService, param.getSiteId(), logDesc), endTime.getTime() - startTime.getTime());
            return apiPageData;
        } catch (Exception e) {
            LogUtil.addOperationLog(OperationType.QUERY, LogUtil.buildFailOperationLogDesc(logDesc), LogUtil.getSiteNameForLog(siteApiService, param.getSiteId()));
            throw e;
        }

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
        String logDesc = "处理访问优化问题";
        try {
            authorityService.checkRight(Authority.KPIWEB_IMPROVE_HANDLE, siteId);
            webPageService.handlePageByIds(siteId, Arrays.asList(ids));
            LogUtil.addOperationLog(OperationType.UPDATE, logDesc, LogUtil.getSiteNameForLog(siteApiService, siteId));
            return null;
        } catch (Exception e) {
            LogUtil.addOperationLog(OperationType.UPDATE, LogUtil.buildFailOperationLogDesc(logDesc), LogUtil.getSiteNameForLog(siteApiService, siteId));
            throw e;
        }

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
        String logDesc = "忽略访问优化问题";
        try {
            authorityService.checkRight(Authority.KPIWEB_IMPROVE_IGNORE, siteId);
            webPageService.ignorePageByIds(siteId, Arrays.asList(ids));
            LogUtil.addOperationLog(OperationType.UPDATE, logDesc, LogUtil.getSiteNameForLog(siteApiService, siteId));
            return null;
        } catch (Exception e) {
            LogUtil.addOperationLog(OperationType.UPDATE, LogUtil.buildFailOperationLogDesc(logDesc), LogUtil.getSiteNameForLog(siteApiService, siteId));
            throw e;
        }

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
        String logDesc = "删除访问优化问题";
        try {
            authorityService.checkRight(Authority.KPIWEB_IMPROVE_DELETE, siteId);
            webPageService.delPageByIds(siteId, Arrays.asList(ids));
            LogUtil.addOperationLog(OperationType.DELETE, logDesc, LogUtil.getSiteNameForLog(siteApiService, siteId));
            return null;
        } catch (Exception e) {
            LogUtil.addOperationLog(OperationType.DELETE, LogUtil.buildFailOperationLogDesc(logDesc), LogUtil.getSiteNameForLog(siteApiService, siteId));
            throw e;
        }
    }
}

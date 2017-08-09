package com.trs.gov.kpi.controller;

import com.trs.gov.kpi.constant.Authority;
import com.trs.gov.kpi.constant.OperationType;
import com.trs.gov.kpi.entity.exception.BizException;
import com.trs.gov.kpi.entity.exception.RemoteException;
import com.trs.gov.kpi.entity.requestdata.BasRequest;
import com.trs.gov.kpi.entity.responsedata.History;
import com.trs.gov.kpi.ids.ContextHelper;
import com.trs.gov.kpi.service.outer.AuthorityService;
import com.trs.gov.kpi.service.outer.BasService;
import com.trs.gov.kpi.service.outer.SiteApiService;
import com.trs.gov.kpi.utils.LogUtil;
import com.trs.gov.kpi.utils.ParamCheckUtil;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.text.ParseException;
import java.util.Date;

/**
 * Created by ranwei on 2017/6/13.
 */
@RestController
@RequestMapping(value = "/gov/kpi/analysis/user")
public class UserAnalysisController {

    @Resource
    private BasService basService;

    @Resource
    private AuthorityService authorityService;

    @Resource
    SiteApiService siteApiService;

    /**
     * 访问量统计信息查询
     *
     * @param basRequest
     * @return
     * @throws BizException
     * @throws RemoteException
     * @throws ParseException
     */
    @RequestMapping(value = "/access", method = RequestMethod.GET)
    public Integer getVisits(@ModelAttribute BasRequest basRequest) throws BizException, RemoteException, ParseException {
        Date startTime = new Date();
        check(basRequest);
        String logDesc = "访问量统计信息查询";
        if (!authorityService.hasRight(ContextHelper.getLoginUser().getUserName(), basRequest.getSiteId(), null, Authority.KPIWEB_ANALYSIS_VIEWS) && !authorityService.hasRight
                (ContextHelper.getLoginUser().getUserName(), null, null, Authority.KPIWEB_ANALYSIS_VIEWS)) {
            LogUtil.addOperationLog(OperationType.QUERY, LogUtil.buildFailOperationLogDesc(logDesc), LogUtil.getSiteNameForLog(siteApiService, basRequest.getSiteId()));
            throw new BizException(Authority.NO_AUTHORITY);
        }
        try {
            Integer value = basService.getVisits(basRequest);
            Date endTime = new Date();
            LogUtil.addOperationLog(OperationType.QUERY, logDesc, LogUtil.getSiteNameForLog(siteApiService, basRequest.getSiteId()));
            LogUtil.addElapseLog(OperationType.QUERY, LogUtil.buildElapseLogDesc(siteApiService, basRequest.getSiteId(), logDesc), endTime.getTime() - startTime.getTime());
            return value;
        } catch (Exception e) {
            LogUtil.addOperationLog(OperationType.QUERY, LogUtil.buildFailOperationLogDesc(logDesc), LogUtil.getSiteNameForLog(siteApiService, basRequest.getSiteId()));
            throw e;
        }
    }

    /**
     * 访问量历史记录查询
     *
     * @param basRequest
     * @return
     * @throws BizException
     * @throws RemoteException
     * @throws ParseException
     */
    @RequestMapping(value = "/access/history", method = RequestMethod.GET)
    public History getHistoryVisits(@ModelAttribute BasRequest basRequest) throws BizException, RemoteException, ParseException {
        Date startTime = new Date();
        check(basRequest);
        String logDesc = "访问量历史记录查询";
        if (!authorityService.hasRight(ContextHelper.getLoginUser().getUserName(), basRequest.getSiteId(), null, Authority.KPIWEB_ANALYSIS_VIEWS) && !authorityService.hasRight
                (ContextHelper.getLoginUser().getUserName(), null, null, Authority.KPIWEB_ANALYSIS_VIEWS)) {
            LogUtil.addOperationLog(OperationType.QUERY, LogUtil.buildFailOperationLogDesc(logDesc), LogUtil.getSiteNameForLog(siteApiService, basRequest.getSiteId()));
            throw new BizException(Authority.NO_AUTHORITY);
        }
        try {
            History history = basService.getHistoryVisits(basRequest);
            Date endTime = new Date();
            LogUtil.addOperationLog(OperationType.QUERY, logDesc, LogUtil.getSiteNameForLog(siteApiService, basRequest.getSiteId()));
            LogUtil.addElapseLog(OperationType.QUERY, LogUtil.buildElapseLogDesc(siteApiService, basRequest.getSiteId(), logDesc), endTime.getTime() - startTime.getTime());
            return history;
        } catch (Exception e) {
            LogUtil.addOperationLog(OperationType.QUERY, LogUtil.buildFailOperationLogDesc(logDesc), LogUtil.getSiteNameForLog(siteApiService, basRequest.getSiteId()));
            throw e;
        }
    }

    /**
     * 最近一个月次均停留时间查询
     *
     * @param basRequest
     * @return
     * @throws BizException
     * @throws RemoteException
     */
    @RequestMapping(value = "/stay", method = RequestMethod.GET)
    public Integer getStayTime(@ModelAttribute BasRequest basRequest) throws BizException, RemoteException {
        Date startTime = new Date();
        check(basRequest);
        String logDesc = "最近一个月次均停留时间查询";
        if (!authorityService.hasRight(ContextHelper.getLoginUser().getUserName(), basRequest.getSiteId(), null, Authority.KPIWEB_ANALYSIS_STAYTIME) && !authorityService.hasRight
                (ContextHelper.getLoginUser().getUserName(), null, null, Authority.KPIWEB_ANALYSIS_STAYTIME)) {
            LogUtil.addOperationLog(OperationType.QUERY, LogUtil.buildFailOperationLogDesc(logDesc), LogUtil.getSiteNameForLog(siteApiService, basRequest.getSiteId()));
            throw new BizException(Authority.NO_AUTHORITY);
        }
        try {
            Integer value = basService.getStayTime(basRequest);
            Date endTime = new Date();
            LogUtil.addOperationLog(OperationType.QUERY, logDesc, LogUtil.getSiteNameForLog(siteApiService, basRequest.getSiteId()));
            LogUtil.addElapseLog(OperationType.QUERY, LogUtil.buildElapseLogDesc(siteApiService, basRequest.getSiteId(), logDesc), endTime.getTime() - startTime.getTime());
            return value;
        } catch (Exception e) {
            LogUtil.addOperationLog(OperationType.QUERY, LogUtil.buildFailOperationLogDesc(logDesc), LogUtil.getSiteNameForLog(siteApiService, basRequest.getSiteId()));
            throw e;
        }

    }

    /**
     * 停留时间历史记录查询
     *
     * @param basRequest
     * @return
     * @throws BizException
     * @throws RemoteException
     * @throws ParseException
     */
    @RequestMapping(value = "/stay/history", method = RequestMethod.GET)
    public History getHistoryStayTime(@ModelAttribute BasRequest basRequest) throws BizException, RemoteException, ParseException {
        String logDesc = "停留时间历史记录查询";
        try {
            Date startTime = new Date();
            check(basRequest);
            if (!authorityService.hasRight(ContextHelper.getLoginUser().getUserName(), basRequest.getSiteId(), null, Authority.KPIWEB_ANALYSIS_STAYTIME) && !authorityService.hasRight
                    (ContextHelper.getLoginUser().getUserName(), null, null, Authority.KPIWEB_ANALYSIS_STAYTIME)) {
                throw new BizException(Authority.NO_AUTHORITY);
            }
            History history = basService.getHistoryStayTime(basRequest);
            Date endTime = new Date();
            LogUtil.addOperationLog(OperationType.QUERY, logDesc, LogUtil.getSiteNameForLog(siteApiService, basRequest.getSiteId()));
            LogUtil.addElapseLog(OperationType.QUERY, LogUtil.buildElapseLogDesc(siteApiService, basRequest.getSiteId(), logDesc), endTime.getTime() - startTime.getTime());
            return history;
        } catch (Exception e) {
            LogUtil.addOperationLog(OperationType.QUERY, LogUtil.buildFailOperationLogDesc(logDesc), LogUtil.getSiteNameForLog(siteApiService, basRequest.getSiteId()));
            throw e;
        }
    }

    private void check(BasRequest basRequest) throws BizException {
        ParamCheckUtil.checkCommonTime(basRequest.getBeginDateTime());
        ParamCheckUtil.checkCommonTime(basRequest.getEndDateTime());
    }
}

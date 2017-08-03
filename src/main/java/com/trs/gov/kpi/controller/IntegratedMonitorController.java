package com.trs.gov.kpi.controller;

import com.trs.gov.kpi.constant.Authority;
import com.trs.gov.kpi.constant.OperationType;
import com.trs.gov.kpi.entity.exception.BizException;
import com.trs.gov.kpi.entity.exception.RemoteException;
import com.trs.gov.kpi.entity.requestdata.PageDataRequestParam;
import com.trs.gov.kpi.entity.responsedata.HistoryStatistics;
import com.trs.gov.kpi.entity.responsedata.Statistics;
import com.trs.gov.kpi.ids.ContextHelper;
import com.trs.gov.kpi.service.IntegratedMonitorService;
import com.trs.gov.kpi.service.outer.AuthorityService;
import com.trs.gov.kpi.service.outer.SiteApiService;
import com.trs.gov.kpi.utils.LogUtil;
import com.trs.gov.kpi.utils.ParamCheckUtil;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.text.ParseException;
import java.util.List;

/**
 * 综合实时监测
 */
@RestController
@RequestMapping("/gov/kpi")
public class IntegratedMonitorController {

    @Resource
    private IntegratedMonitorService integratedMonitorService;

    @Resource
    private AuthorityService authorityService;

    @Resource
    SiteApiService siteApiService;

    /**
     * 查询当前的绩效指数得分
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/index/now", method = RequestMethod.GET)
    public Double getPerformance(@ModelAttribute PageDataRequestParam param) throws BizException, ParseException, RemoteException {
        if (!authorityService.hasRight(ContextHelper.getLoginUser().getUserName(), param.getSiteId(), null, Authority.KPIWEB_MONITOR_SEARCH) && !authorityService.hasRight(ContextHelper
                .getLoginUser().getUserName(), null, null, Authority.KPIWEB_MONITOR_SEARCH)) {
            throw new BizException(Authority.NO_AUTHORITY);
        }
        ParamCheckUtil.paramCheck(param);
        Double score = integratedMonitorService.getPerformance(param);
        LogUtil.addOperationLog(OperationType.QUERY, "查询当前的绩效指数得分", siteApiService.getSiteById(param.getSiteId(), "").getSiteName());
        return score;
    }

    /**
     * 查询绩效指数得分的历史记录
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/index/history", method = RequestMethod.GET)
    public List<HistoryStatistics> getHistoryPerformance(@ModelAttribute PageDataRequestParam param) throws BizException, ParseException, RemoteException {
        if (!authorityService.hasRight(ContextHelper.getLoginUser().getUserName(), param.getSiteId(), null, Authority.KPIWEB_MONITOR_SEARCH) && !authorityService.hasRight(ContextHelper
                .getLoginUser().getUserName(), null, null, Authority.KPIWEB_MONITOR_SEARCH)) {
            throw new BizException(Authority.NO_AUTHORITY);
        }
        ParamCheckUtil.paramCheck(param);
        List<HistoryStatistics> list = integratedMonitorService.getHistoryPerformance(param);
        LogUtil.addOperationLog(OperationType.QUERY, "查询绩效指数得分的历史记录", siteApiService.getSiteById(param.getSiteId(), "").getSiteName());
        return list;
    }

    /**
     * 查询所有问题数量
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/issue/all/count", method = RequestMethod.GET)
    public List<Statistics> getAllIssueCount(@ModelAttribute PageDataRequestParam param) throws BizException, RemoteException {
        if (!authorityService.hasRight(ContextHelper.getLoginUser().getUserName(), param.getSiteId(), null, Authority.KPIWEB_MONITOR_SEARCH) && !authorityService.hasRight(ContextHelper
                .getLoginUser().getUserName(), null, null, Authority.KPIWEB_MONITOR_SEARCH)) {
            throw new BizException(Authority.NO_AUTHORITY);
        }
        ParamCheckUtil.paramCheck(param);
        List<Statistics> list = integratedMonitorService.getAllIssueCount(param);
        LogUtil.addOperationLog(OperationType.QUERY, "查询所有问题数量", siteApiService.getSiteById(param.getSiteId(), "").getSiteName());
        return list;
    }

    /**
     * 查询各类问题的待解决问题数
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/issue/unhandled/bytype/count", method = RequestMethod.GET)
    public List<Statistics> getUnhandledIssueCount(@ModelAttribute PageDataRequestParam param) throws BizException, RemoteException {
        if (!authorityService.hasRight(ContextHelper.getLoginUser().getUserName(), param.getSiteId(), null, Authority.KPIWEB_MONITOR_SEARCH) && !authorityService.hasRight(ContextHelper
                .getLoginUser().getUserName(), null, null, Authority.KPIWEB_MONITOR_SEARCH)) {
            throw new BizException(Authority.NO_AUTHORITY);
        }
        ParamCheckUtil.paramCheck(param);
        List<Statistics> list = integratedMonitorService.getUnhandledIssueCount(param);
        LogUtil.addOperationLog(OperationType.QUERY, "查询各类问题的待解决问题数", siteApiService.getSiteById(param.getSiteId(), "").getSiteName());
        return list;
    }

    /**
     * 查询各类预警的待解决数
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/issue/warning/bytype/count")
    public List<Statistics> getWarningCount(@ModelAttribute PageDataRequestParam param) throws BizException, RemoteException {
        if (!authorityService.hasRight(ContextHelper.getLoginUser().getUserName(), param.getSiteId(), null, Authority.KPIWEB_MONITOR_SEARCH) && !authorityService.hasRight(ContextHelper
                .getLoginUser().getUserName(), null, null, Authority.KPIWEB_MONITOR_SEARCH)) {
            throw new BizException(Authority.NO_AUTHORITY);
        }
        ParamCheckUtil.paramCheck(param);
        List<Statistics> list = integratedMonitorService.getWarningCount(param);
        LogUtil.addOperationLog(OperationType.QUERY, "查询各类预警的待解决数", siteApiService.getSiteById(param.getSiteId(), "").getSiteName());
        return list;
    }


}

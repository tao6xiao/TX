package com.trs.gov.kpi.controller;

import com.trs.gov.kpi.constant.Authority;
import com.trs.gov.kpi.entity.exception.BizException;
import com.trs.gov.kpi.entity.exception.RemoteException;
import com.trs.gov.kpi.entity.requestdata.PageDataRequestParam;
import com.trs.gov.kpi.entity.responsedata.HistoryStatistics;
import com.trs.gov.kpi.entity.responsedata.Statistics;
import com.trs.gov.kpi.service.IntegratedMonitorService;
import com.trs.gov.kpi.service.outer.AuthorityService;
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

    /**
     * 查询当前的绩效指数得分
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/index/now", method = RequestMethod.GET)
    public Double getPerformance(@ModelAttribute PageDataRequestParam param) throws BizException, ParseException, RemoteException {
        if (!authorityService.hasRight(param.getSiteId(), null, Authority.KPIWEB_MONITOR_SEARCH) && !authorityService.hasRight(null, null, Authority.KPIWEB_MONITOR_SEARCH)) {
            throw new BizException(Authority.NO_AUTHORITY);
        }
        ParamCheckUtil.paramCheck(param);
        return integratedMonitorService.getPerformance(param);
    }

    /**
     * 查询绩效指数得分的历史记录
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/index/history", method = RequestMethod.GET)
    public List<HistoryStatistics> getHistoryPerformance(@ModelAttribute PageDataRequestParam param) throws BizException, ParseException, RemoteException {
        if (!authorityService.hasRight(param.getSiteId(), null, Authority.KPIWEB_MONITOR_SEARCH) && !authorityService.hasRight(null, null, Authority.KPIWEB_MONITOR_SEARCH)) {
            throw new BizException(Authority.NO_AUTHORITY);
        }
        ParamCheckUtil.paramCheck(param);
        return integratedMonitorService.getHistoryPerformance(param);
    }

    /**
     * 查询所有问题数量
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/issue/all/count", method = RequestMethod.GET)
    public List<Statistics> getAllIssueCount(@ModelAttribute PageDataRequestParam param) throws BizException, RemoteException {
        if (!authorityService.hasRight(param.getSiteId(), null, Authority.KPIWEB_MONITOR_SEARCH) && !authorityService.hasRight(null, null, Authority.KPIWEB_MONITOR_SEARCH)) {
            throw new BizException(Authority.NO_AUTHORITY);
        }
        ParamCheckUtil.paramCheck(param);
        return integratedMonitorService.getAllIssueCount(param);
    }

    /**
     * 查询各类问题的待解决问题数
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/issue/unhandled/bytype/count", method = RequestMethod.GET)
    public List<Statistics> getUnhandledIssueCount(@ModelAttribute PageDataRequestParam param) throws BizException, RemoteException {
        if (!authorityService.hasRight(param.getSiteId(), null, Authority.KPIWEB_MONITOR_SEARCH) && !authorityService.hasRight(null, null, Authority.KPIWEB_MONITOR_SEARCH)) {
            throw new BizException(Authority.NO_AUTHORITY);
        }
        ParamCheckUtil.paramCheck(param);
        return integratedMonitorService.getUnhandledIssueCount(param);
    }

    /**
     * 查询各类预警的待解决数
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/issue/warning/bytype/count")
    public List<Statistics> getWarningCount(@ModelAttribute PageDataRequestParam param) throws BizException, RemoteException {
        if (!authorityService.hasRight(param.getSiteId(), null, Authority.KPIWEB_MONITOR_SEARCH) && !authorityService.hasRight(null, null, Authority.KPIWEB_MONITOR_SEARCH)) {
            throw new BizException(Authority.NO_AUTHORITY);
        }
        ParamCheckUtil.paramCheck(param);
        return integratedMonitorService.getWarningCount(param);
    }


}

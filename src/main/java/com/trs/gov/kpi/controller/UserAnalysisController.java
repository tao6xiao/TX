package com.trs.gov.kpi.controller;

import com.trs.gov.kpi.constant.Authority;
import com.trs.gov.kpi.constant.OperationType;
import com.trs.gov.kpi.entity.exception.BizException;
import com.trs.gov.kpi.entity.exception.RemoteException;
import com.trs.gov.kpi.entity.requestdata.BasRequest;
import com.trs.gov.kpi.entity.responsedata.HistoryStatisticsRes;
import com.trs.gov.kpi.service.outer.AuthorityService;
import com.trs.gov.kpi.service.outer.BasService;
import com.trs.gov.kpi.service.outer.SiteApiService;
import com.trs.gov.kpi.utils.LogUtil;
import com.trs.gov.kpi.utils.ParamCheckUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.text.ParseException;

/**
 * Created by ranwei on 2017/6/13.
 */
@RestController
@RequestMapping(value = "/gov/kpi/analysis/user")
@Slf4j
public class UserAnalysisController {

    @Resource
    private BasService basService;

    @Resource
    private AuthorityService authorityService;

    @Resource
    SiteApiService siteApiService;

    private static final String BAS_REQUEST = "basRequest";

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
        String logDesc = "访问量统计信息查询" + LogUtil.paramsToLogString(BAS_REQUEST, basRequest);
        return LogUtil.ControlleFunctionWrapper(() -> {
            check(basRequest);
            authorityService.checkRight(Authority.KPIWEB_ANALYSIS_VIEWS, basRequest.getSiteId());
            Integer value = null;
            try {
                value = basService.getVisits(basRequest);
            } catch (ParseException e) {
                log.error("", e);
                throw new BizException("");
            }
            return value;
        }, OperationType.QUERY, logDesc, LogUtil.getSiteNameForLog(siteApiService, basRequest.getSiteId()));
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
    public HistoryStatisticsRes getHistoryVisits(@ModelAttribute BasRequest basRequest) throws BizException, RemoteException, ParseException {
        String logDesc = "访问量历史记录查询" + LogUtil.paramsToLogString(BAS_REQUEST, basRequest);
        return LogUtil.ControlleFunctionWrapper(() -> {
            check(basRequest);
            authorityService.checkRight(Authority.KPIWEB_ANALYSIS_VIEWS, basRequest.getSiteId());

            HistoryStatisticsRes historyStatisticsRes = null;
            try {
                historyStatisticsRes = basService.getHistoryVisits(basRequest);
            } catch (ParseException e) {
                log.error("", e);
                throw new BizException("");
            }
            return historyStatisticsRes;

        }, OperationType.QUERY, logDesc, LogUtil.getSiteNameForLog(siteApiService, basRequest.getSiteId()));
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
        String logDesc = "最近一个月次均停留时间查询" + LogUtil.paramsToLogString(BAS_REQUEST, basRequest);
        return LogUtil.ControlleFunctionWrapper(() -> {
            check(basRequest);
            authorityService.checkRight(Authority.KPIWEB_ANALYSIS_STAYTIME, basRequest.getSiteId());
            return basService.getStayTime(basRequest);
        }, OperationType.QUERY, logDesc, LogUtil.getSiteNameForLog(siteApiService, basRequest.getSiteId()));
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
    public HistoryStatisticsRes getHistoryStayTime(@ModelAttribute BasRequest basRequest) throws BizException, RemoteException {
        String logDesc = "停留时间历史记录查询";
        return LogUtil.ControlleFunctionWrapper(() -> {
            check(basRequest);
            authorityService.checkRight(Authority.KPIWEB_ANALYSIS_STAYTIME, basRequest.getSiteId());
            HistoryStatisticsRes historyStatisticsRes = null;
            try {
                historyStatisticsRes = basService.getHistoryStayTime(basRequest);
            } catch (ParseException e) {
                log.error("", e);
                throw new BizException("");
            }
            return historyStatisticsRes;
        }, OperationType.QUERY, logDesc, LogUtil.getSiteNameForLog(siteApiService, basRequest.getSiteId()));
    }

    private void check(BasRequest basRequest) throws BizException {
        ParamCheckUtil.checkCommonTime(basRequest.getBeginDateTime());
        ParamCheckUtil.checkCommonTime(basRequest.getEndDateTime());
    }
}

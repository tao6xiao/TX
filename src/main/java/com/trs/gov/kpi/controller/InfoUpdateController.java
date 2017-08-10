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
import com.trs.gov.kpi.entity.responsedata.MonthUpdateResponse;
import com.trs.gov.kpi.entity.responsedata.Statistics;
import com.trs.gov.kpi.service.InfoUpdateService;
import com.trs.gov.kpi.service.outer.AuthorityService;
import com.trs.gov.kpi.utils.LogUtil;
import com.trs.gov.kpi.utils.ParamCheckUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

/**
 * 信息更新问题
 */
@Slf4j
@RestController
@RequestMapping(UrlPath.INFO_UPDATE_PATH)
public class InfoUpdateController extends IssueHandler {

    @Resource
    private InfoUpdateService infoUpdateService;

    @Resource
    private AuthorityService authorityService;

    /**
     * 查询已解决、预警和更新不及时的数量
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/bytype/count", method = RequestMethod.GET)
    public List getIssueCount(@ModelAttribute PageDataRequestParam param) throws BizException, RemoteException {
        String logDesc = "查询信息更新已解决、预警和更新不及时的数量" + LogUtil.paramsToLogString(Constants.PARAM, param);
        return LogUtil.ControlleFunctionWrapper(() -> {
            ParamCheckUtil.paramCheck(param);
            authorityService.checkRight(Authority.KPIWEB_INFOUPDATE_SEARCH, param.getSiteId());
            List list = infoUpdateService.getIssueCount(param);
            LogUtil.addOperationLog(OperationType.QUERY, logDesc, LogUtil.getSiteNameForLog(siteApiService, param.getSiteId()));
            return list;
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
        String logDesc = "查询信息更新历史纪录" + LogUtil.paramsToLogString(Constants.PARAM, param);
        return LogUtil.ControlleFunctionWrapper(() -> {
            ParamCheckUtil.paramCheck(param);
            authorityService.checkRight(Authority.KPIWEB_INFOUPDATE_SEARCH, param.getSiteId());
            HistoryStatisticsRes historyStatisticsRes = infoUpdateService.getIssueHistoryCount(param);
            LogUtil.addOperationLog(OperationType.QUERY, logDesc, LogUtil.getSiteNameForLog(siteApiService, param.getSiteId()));
            return historyStatisticsRes;
        }, OperationType.QUERY, logDesc, LogUtil.getSiteNameForLog(siteApiService, param.getSiteId()));
    }

    /**
     * 查询待解决的问题列表
     *
     * @param param
     * @return
     * @throws BizException
     */
    @RequestMapping(value = "/unhandled", method = RequestMethod.GET)
    public ApiPageData getIssueList(@ModelAttribute PageDataRequestParam param) throws BizException, RemoteException {
        String logDesc = "查询信息更新待解决问题列表" + LogUtil.paramsToLogString(Constants.PARAM, param);
        return LogUtil.ControlleFunctionWrapper(() -> {
            ParamCheckUtil.paramCheck(param);
            authorityService.checkRight(Authority.KPIWEB_INFOUPDATE_SEARCH, param.getSiteId());
            ApiPageData apiPageData = infoUpdateService.get(param);
            LogUtil.addOperationLog(OperationType.QUERY, logDesc, LogUtil.getSiteNameForLog(siteApiService, param.getSiteId()));
            return apiPageData;
        }, OperationType.QUERY, logDesc, LogUtil.getSiteNameForLog(siteApiService, param.getSiteId()));
    }

    /**
     * 获取栏目信息更新不及时及空栏目的统计信息
     *
     * @param param
     * @return
     * @throws BizException
     * @throws ParseException
     * @throws RemoteException
     */
    @RequestMapping(value = "/bygroup/count", method = RequestMethod.GET)
    @ResponseBody
    public List<Statistics> getUpdateNotInTimeCountList(@ModelAttribute PageDataRequestParam param) throws BizException, ParseException, RemoteException {
        String logDesc = "获取栏目信息更新不及时的统计信息" + LogUtil.paramsToLogString(Constants.PARAM, param);
        return LogUtil.ControlleFunctionWrapper(() -> {
            ParamCheckUtil.paramCheck(param);
            authorityService.checkRight(Authority.KPIWEB_INFOUPDATE_SEARCH, param.getSiteId());
            List<Statistics> list = null;
            try {
                list = infoUpdateService.getUpdateNotInTimeCountList(param);
            } catch (ParseException e) {
                log.error("", e);
                throw new BizException("");
            }
            LogUtil.addOperationLog(OperationType.QUERY, logDesc, LogUtil.getSiteNameForLog(siteApiService, param.getSiteId()));
            return list;
        }, OperationType.QUERY, logDesc, LogUtil.getSiteNameForLog(siteApiService, param.getSiteId()));
    }

    /**
     * 获取更新不及时栏目的更新不及时总月数以及空栏目
     *
     * @param siteId
     * @return
     * @throws BizException
     * @throws RemoteException
     */
    @RequestMapping(value = "/month/count", method = RequestMethod.GET)
    @ResponseBody
    public MonthUpdateResponse getNotInTimeCountMonth(@RequestParam("siteId") Integer siteId) throws BizException, RemoteException {
        String logDesc = "获取更新不及时栏目的更新不及时总月数以及空栏目" + LogUtil.paramsToLogString(Constants.DB_FIELD_SITE_ID, siteId);
        return LogUtil.ControlleFunctionWrapper(() -> {
            if (siteId == null) {
                log.error("Invalid parameter: 参数siteId为null值");
                throw new BizException(Constants.INVALID_PARAMETER);
            }
            authorityService.checkRight(Authority.KPIWEB_INFOUPDATE_SEARCH, siteId);
            MonthUpdateResponse response = infoUpdateService.getNotInTimeCountMonth(siteId);
            LogUtil.addOperationLog(OperationType.QUERY, logDesc, LogUtil.getSiteNameForLog(siteApiService, siteId));
            return response;
        }, OperationType.QUERY, logDesc, LogUtil.getSiteNameForLog(siteApiService, siteId));
    }
}

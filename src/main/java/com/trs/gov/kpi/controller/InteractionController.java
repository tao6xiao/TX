package com.trs.gov.kpi.controller;

import com.trs.gov.kpi.constant.Authority;
import com.trs.gov.kpi.constant.Constants;
import com.trs.gov.kpi.constant.OperationType;
import com.trs.gov.kpi.entity.exception.BizException;
import com.trs.gov.kpi.entity.exception.RemoteException;
import com.trs.gov.kpi.entity.outerapi.nbhd.NBHDPageDataResult;
import com.trs.gov.kpi.entity.outerapi.nbhd.NBHDRequestParam;
import com.trs.gov.kpi.entity.outerapi.nbhd.NBHDStatisticsRes;
import com.trs.gov.kpi.entity.responsedata.HistoryStatistics;
import com.trs.gov.kpi.entity.responsedata.HistoryStatisticsRes;
import com.trs.gov.kpi.service.outer.AuthorityService;
import com.trs.gov.kpi.service.outer.InteractionService;
import com.trs.gov.kpi.service.outer.SiteApiService;
import com.trs.gov.kpi.utils.LogUtil;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * Created by ranwei on 2017/6/9.
 */
@RestController
@RequestMapping(value = "/gov/kpi/interaction")
public class InteractionController {

    @Resource
    private InteractionService interactionService;

    @Resource
    private AuthorityService authorityService;

    @Resource
    private SiteApiService siteApiService;

    /**
     * 查询问政互动的数量
     *
     * @param param
     * @return
     * @throws RemoteException
     */
    @RequestMapping(value = "/issue/bytype/count", method = RequestMethod.GET)
    public NBHDStatisticsRes getGovMsgBoxesCount(@ModelAttribute NBHDRequestParam param) throws RemoteException, BizException {
        String logDesc = "查询问政互动的信件列表" + LogUtil.paramsToLogString(Constants.PARAM, param);
        return LogUtil.ControlleFunctionWrapper(() -> {
            authorityService.checkRight(Authority.KPIWEB_NBHD_SEARCH, param.getSiteId());
            NBHDStatisticsRes res = interactionService.getGovMsgBoxesCount(param);
            LogUtil.addOperationLog(OperationType.QUERY, logDesc, LogUtil.getSiteNameForLog(siteApiService, param.getSiteId()));
            return res;
        }, OperationType.QUERY, logDesc, LogUtil.getSiteNameForLog(siteApiService, param.getSiteId()));
    }

    /**
     * 查询问政互动的数量的历史记录
     *
     * @param param
     * @return
     * @throws RemoteException
     */
    @RequestMapping(value = "/issue/all/count/history", method = RequestMethod.GET)
    public HistoryStatisticsRes getGovMsgHistoryCount(@ModelAttribute NBHDRequestParam param) throws RemoteException, BizException {
        String logDesc = "查询问政互动的数量" + LogUtil.paramsToLogString(Constants.PARAM, param);
        return LogUtil.ControlleFunctionWrapper(() -> {
            authorityService.checkRight(Authority.KPIWEB_NBHD_SEARCH, param.getSiteId());
            List<HistoryStatistics> historyStatisticsList = interactionService.getGovMsgHistoryCount(param);
            LogUtil.addOperationLog(OperationType.QUERY, logDesc, LogUtil.getSiteNameForLog(siteApiService, param.getSiteId()));
            return new HistoryStatisticsRes(new Date(), historyStatisticsList);
        }, OperationType.QUERY, logDesc, LogUtil.getSiteNameForLog(siteApiService, param.getSiteId()));
    }

    /**
     * 查询问政互动的信件列表
     *
     * @param param
     * @return
     * @throws RemoteException
     */
    @RequestMapping(value = "/msg/unhandled", method = RequestMethod.GET)
    public NBHDPageDataResult getGovMsgBoxes(@ModelAttribute NBHDRequestParam param) throws RemoteException, BizException {
        String logDesc = "查询问政互动的信件列表" + LogUtil.paramsToLogString(Constants.PARAM, param);
        return LogUtil.ControlleFunctionWrapper(() -> {
            authorityService.checkRight(Authority.KPIWEB_NBHD_SEARCH, param.getSiteId());
            NBHDPageDataResult result = interactionService.getGovMsgBoxes(param);
            LogUtil.addOperationLog(OperationType.QUERY, logDesc, LogUtil.getSiteNameForLog(siteApiService, param.getSiteId()));
            return result;
        }, OperationType.QUERY, logDesc, LogUtil.getSiteNameForLog(siteApiService, param.getSiteId()));
    }

}

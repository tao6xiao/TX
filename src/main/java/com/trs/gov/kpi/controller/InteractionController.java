package com.trs.gov.kpi.controller;

import com.trs.gov.kpi.constant.Authority;
import com.trs.gov.kpi.constant.OperationType;
import com.trs.gov.kpi.entity.exception.BizException;
import com.trs.gov.kpi.entity.exception.RemoteException;
import com.trs.gov.kpi.entity.outerapi.nbhd.NBHDPageDataResult;
import com.trs.gov.kpi.entity.outerapi.nbhd.NBHDRequestParam;
import com.trs.gov.kpi.entity.outerapi.nbhd.NBHDStatisticsRes;
import com.trs.gov.kpi.entity.responsedata.HistoryStatistics;
import com.trs.gov.kpi.ids.ContextHelper;
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
        Date startTime = new Date();
        checkAuthority(param);
        String logDesc = "查询问政互动的信件列表";
        try {
            NBHDStatisticsRes res = interactionService.getGovMsgBoxesCount(param);
            Date endTime = new Date();
            LogUtil.addOperationLog(OperationType.QUERY, logDesc, LogUtil.getSiteNameForLog(siteApiService, param.getSiteId()));
            LogUtil.addElapseLog(OperationType.QUERY, LogUtil.buildElapseLogDesc(siteApiService, param.getSiteId(), logDesc), endTime.getTime() - startTime.getTime());
            return res;
        } catch (Exception e) {
            LogUtil.addOperationLog(OperationType.QUERY, LogUtil.buildFailOperationLogDesc(logDesc), LogUtil.getSiteNameForLog(siteApiService, param.getSiteId()));
            throw e;
        }

    }

    /**
     * 查询问政互动的数量的历史记录
     *
     * @param param
     * @return
     * @throws RemoteException
     */
    @RequestMapping(value = "/issue/all/count/history", method = RequestMethod.GET)
    public List<HistoryStatistics> getGovMsgHistoryCount(@ModelAttribute NBHDRequestParam param) throws RemoteException, BizException {
        Date startTime = new Date();
        checkAuthority(param);
        String logDesc = "查询问政互动的数量";
        try {
            List<HistoryStatistics> historyStatisticsList = interactionService.getGovMsgHistoryCount(param);
            Date endTime = new Date();
            LogUtil.addOperationLog(OperationType.QUERY, logDesc, LogUtil.getSiteNameForLog(siteApiService, param.getSiteId()));
            LogUtil.addElapseLog(OperationType.QUERY, LogUtil.buildElapseLogDesc(siteApiService, param.getSiteId(), logDesc), endTime.getTime() - startTime.getTime());
            return historyStatisticsList;
        } catch (Exception e) {
            LogUtil.addOperationLog(OperationType.QUERY, LogUtil.buildFailOperationLogDesc(logDesc), LogUtil.getSiteNameForLog(siteApiService, param.getSiteId()));
            throw e;
        }
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
        Date startTime = new Date();
        checkAuthority(param);
        String logDesc = "查询问政互动的信件列表";
        try {
            NBHDPageDataResult result = interactionService.getGovMsgBoxes(param);
            Date endTime = new Date();
            LogUtil.addOperationLog(OperationType.QUERY, logDesc, LogUtil.getSiteNameForLog(siteApiService, param.getSiteId()));
            LogUtil.addElapseLog(OperationType.QUERY, LogUtil.buildElapseLogDesc(siteApiService, param.getSiteId(), logDesc), endTime.getTime() - startTime.getTime());
            return result;
        } catch (Exception e) {
            LogUtil.addOperationLog(OperationType.QUERY, LogUtil.buildFailOperationLogDesc(logDesc), LogUtil.getSiteNameForLog(siteApiService, param.getSiteId()));
            throw e;
        }
    }

    private void checkAuthority(NBHDRequestParam param) throws RemoteException, BizException {
        if (!authorityService.hasRight(ContextHelper.getLoginUser().getUserName(), null, null, Authority.KPIWEB_NBHD_SEARCH) && !authorityService.hasRight(ContextHelper.getLoginUser()
                .getUserName(), param.getSiteId(), null, Authority.KPIWEB_NBHD_SEARCH)) {
            LogUtil.addOperationLog(OperationType.QUERY, LogUtil.buildFailOperationLogDesc("问政互动相关查询"), LogUtil.getSiteNameForLog(siteApiService, param.getSiteId()));
            throw new BizException(Authority.NO_AUTHORITY);
        }
    }

}

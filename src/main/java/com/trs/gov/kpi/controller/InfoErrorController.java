package com.trs.gov.kpi.controller;


import com.trs.gov.kpi.constant.Authority;
import com.trs.gov.kpi.constant.Constants;
import com.trs.gov.kpi.constant.OperationType;
import com.trs.gov.kpi.constant.UrlPath;
import com.trs.gov.kpi.entity.exception.BizException;
import com.trs.gov.kpi.entity.exception.RemoteException;
import com.trs.gov.kpi.entity.requestdata.PageDataRequestParam;
import com.trs.gov.kpi.entity.responsedata.ApiPageData;
import com.trs.gov.kpi.entity.responsedata.HistoryStatisticsResp;
import com.trs.gov.kpi.service.InfoErrorService;
import com.trs.gov.kpi.service.outer.AuthorityService;
import com.trs.gov.kpi.utils.LogUtil;
import com.trs.gov.kpi.utils.ParamCheckUtil;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;


/**
 * 信息错误问题
 */
@RestController
@RequestMapping("/gov/kpi/content/issue")
public class InfoErrorController extends IssueHandler {

    @Resource
    private InfoErrorService infoErrorService;

    @Resource
    private AuthorityService authorityService;

    /**
     * 查询待解决和已解决问题数量
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/bytype/count", method = RequestMethod.GET, produces="application/json;charset=UTF-8")
    public List getIssueCount(@ModelAttribute PageDataRequestParam param) throws BizException, RemoteException {
        String logDesc = "查询信息错误待解决和已解决问题数量" + LogUtil.paramsToLogString(Constants.PARAM, param);
        return LogUtil.controlleFunctionWrapper(() -> {
            ParamCheckUtil.paramCheck(param);
            authorityService.checkRight(Authority.KPIWEB_INFOERROR_SEARCH, param.getSiteId());
            return infoErrorService.getIssueCount(param);
        }, OperationType.QUERY, logDesc, LogUtil.getSiteNameForLog(siteApiService, param.getSiteId()));
    }

    /**
     * 查询历史记录
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/all/count/history", method = RequestMethod.GET)
    public HistoryStatisticsResp getIssueHistoryCount(@ModelAttribute PageDataRequestParam param) throws BizException, RemoteException {
        String logDesc = "查询信息错误历史记录" + LogUtil.paramsToLogString(Constants.PARAM, param);
        return LogUtil.controlleFunctionWrapper(() -> {
            ParamCheckUtil.paramCheck(param);
            authorityService.checkRight(Authority.KPIWEB_INFOERROR_SEARCH, param.getSiteId());
            return infoErrorService.getIssueHistoryCount(param);
        }, OperationType.QUERY, logDesc, LogUtil.getSiteNameForLog(siteApiService, param.getSiteId()));
    }

    /**
     * 查询待解决问题列表
     *
     * @param param
     * @return
     * @throws BizException
     */
    @RequestMapping(value = "/unhandled", method = RequestMethod.GET)
    public ApiPageData getIssueList(@ModelAttribute PageDataRequestParam param) throws BizException, RemoteException {
        String logDesc = "查询信息错误待解决问题列表" + LogUtil.paramsToLogString(Constants.PARAM, param);
        return LogUtil.controlleFunctionWrapper(() -> {
            ParamCheckUtil.paramCheck(param);
            authorityService.checkRight(Authority.KPIWEB_INFOERROR_SEARCH, param.getSiteId());
            return infoErrorService.getInfoErrorList(param);
        }, OperationType.QUERY, logDesc, LogUtil.getSiteNameForLog(siteApiService, param.getSiteId()));
    }

}

package com.trs.gov.kpi.controller;


import com.trs.gov.kpi.constant.Authority;
import com.trs.gov.kpi.constant.UrlPath;
import com.trs.gov.kpi.entity.exception.BizException;
import com.trs.gov.kpi.entity.exception.RemoteException;
import com.trs.gov.kpi.entity.requestdata.PageDataRequestParam;
import com.trs.gov.kpi.entity.responsedata.ApiPageData;
import com.trs.gov.kpi.entity.responsedata.History;
import com.trs.gov.kpi.ids.ContextHelper;
import com.trs.gov.kpi.service.InfoErrorService;
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
import java.util.List;


/**
 * 信息错误问题
 */
@RestController
@RequestMapping(UrlPath.INFO_ERROR_PATH)
public class InfoErrorController extends IssueHandler {

    @Resource
    private InfoErrorService infoErrorService;

    @Resource
    private AuthorityService authorityService;

    @Resource
    SiteApiService siteApiService;

    /**
     * 查询待解决和已解决问题数量
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/bytype/count", method = RequestMethod.GET)
    public List getIssueCount(@ModelAttribute PageDataRequestParam param) throws BizException, RemoteException {
        if (!authorityService.hasRight(ContextHelper.getLoginUser().getUserName(), param.getSiteId(), null, Authority.KPIWEB_INFOERROR_SEARCH) && !authorityService.hasRight(ContextHelper
                .getLoginUser().getUserName(), null, null, Authority.KPIWEB_INFOERROR_SEARCH)) {
            throw new BizException(Authority.NO_AUTHORITY);
        }
        ParamCheckUtil.paramCheck(param);
        SimpleLogServer.getInstance(TRSLogUserUtil.getLogUser()).operation("查询信息错误待解决和已解决问题数量", "查询信息错误待解决和已解决问题数量", siteApiService.getSiteById(param.getSiteId(), "").getSiteName()).info();
        return infoErrorService.getIssueCount(param);
    }

    /**
     * 查询历史记录
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/all/count/history", method = RequestMethod.GET)
    public History getIssueHistoryCount(@ModelAttribute PageDataRequestParam param) throws BizException, RemoteException {
        if (!authorityService.hasRight(ContextHelper.getLoginUser().getUserName(), param.getSiteId(), null, Authority.KPIWEB_INFOERROR_SEARCH) && !authorityService.hasRight(ContextHelper
                .getLoginUser().getUserName(), null, null, Authority.KPIWEB_INFOERROR_SEARCH)) {
            throw new BizException(Authority.NO_AUTHORITY);
        }
        ParamCheckUtil.paramCheck(param);
        SimpleLogServer.getInstance(TRSLogUserUtil.getLogUser()).operation("查询信息错误历史记录", "查询信息错误历史记录", siteApiService.getSiteById(param.getSiteId(), "").getSiteName()).info();
        return infoErrorService.getIssueHistoryCount(param);
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
        if (!authorityService.hasRight(ContextHelper.getLoginUser().getUserName(), param.getSiteId(), null, Authority.KPIWEB_INFOERROR_SEARCH) && !authorityService.hasRight(ContextHelper
                .getLoginUser().getUserName(), null, null, Authority.KPIWEB_INFOERROR_SEARCH)) {
            throw new BizException(Authority.NO_AUTHORITY);
        }
        ParamCheckUtil.paramCheck(param);
        SimpleLogServer.getInstance(TRSLogUserUtil.getLogUser()).operation("查询信息错误待解决问题列表", "查询信息错误待解决问题列表", siteApiService.getSiteById(param.getSiteId(), "").getSiteName()).info();
        return infoErrorService.getInfoErrorList(param);
    }

}

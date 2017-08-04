package com.trs.gov.kpi.controller;

import com.trs.gov.kpi.constant.Authority;
import com.trs.gov.kpi.constant.OperationType;
import com.trs.gov.kpi.constant.UrlPath;
import com.trs.gov.kpi.entity.exception.BizException;
import com.trs.gov.kpi.entity.exception.RemoteException;
import com.trs.gov.kpi.entity.requestdata.PageDataRequestParam;
import com.trs.gov.kpi.entity.responsedata.ApiPageData;
import com.trs.gov.kpi.ids.ContextHelper;
import com.trs.gov.kpi.service.IssueService;
import com.trs.gov.kpi.service.outer.AuthorityService;
import com.trs.gov.kpi.utils.LogUtil;
import com.trs.gov.kpi.utils.ParamCheckUtil;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Date;

/**
 * 综合实时监测：待解决问题Controller
 */
@RestController
@RequestMapping(UrlPath.INTEGRATED_MONITOR_ISSUE_PATH)
public class IntegratedMonitorIssueController extends IssueHandler {

    @Resource
    private IssueService issueService;

    @Resource
    private AuthorityService authorityService;

    /**
     * 查询所有未解决问题列表
     *
     * @param param
     * @return
     * @throws BizException
     */
    @RequestMapping(value = "/unhandled", method = RequestMethod.GET)
    public ApiPageData getAllIssueList(@ModelAttribute PageDataRequestParam param) throws BizException, RemoteException {
        Date startTime = new Date();
        if (!authorityService.hasRight(ContextHelper.getLoginUser().getUserName(), param.getSiteId(), null, Authority.KPIWEB_ISSUE_SEARCH) && !authorityService.hasRight(ContextHelper
                .getLoginUser().getUserName(), null, null, Authority.KPIWEB_ISSUE_SEARCH)) {
            throw new BizException(Authority.NO_AUTHORITY);
        }
        ParamCheckUtil.paramCheck(param);
        ApiPageData apiPageData = issueService.get(param);
        Date endTime = new Date();
        LogUtil.addOperationLog(OperationType.QUERY, "查询所有未解决问题列表", siteApiService.getSiteById(param.getSiteId(), "").getSiteName());
        LogUtil.addElapseLog(OperationType.QUERY, "查询所有未解决问题列表", endTime.getTime()-startTime.getTime());
        return apiPageData;
    }

}

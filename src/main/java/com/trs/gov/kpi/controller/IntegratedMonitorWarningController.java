package com.trs.gov.kpi.controller;

import com.trs.gov.kpi.constant.Authority;
import com.trs.gov.kpi.constant.OperationType;
import com.trs.gov.kpi.constant.UrlPath;
import com.trs.gov.kpi.entity.exception.BizException;
import com.trs.gov.kpi.entity.exception.RemoteException;
import com.trs.gov.kpi.entity.requestdata.PageDataRequestParam;
import com.trs.gov.kpi.entity.responsedata.ApiPageData;
import com.trs.gov.kpi.ids.ContextHelper;
import com.trs.gov.kpi.service.IntegratedMonitorWarningService;
import com.trs.gov.kpi.service.outer.AuthorityService;
import com.trs.gov.kpi.utils.LogUtil;
import com.trs.gov.kpi.utils.ParamCheckUtil;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.text.ParseException;
import java.util.Date;

/**
 * 综合实时监测：预警提醒Controller
 * Created by he.lang on 2017/5/18.
 */
@RestController
@RequestMapping(UrlPath.INTEGRATED_MONITOR_WARNING_PATH)
public class IntegratedMonitorWarningController extends IssueHandler {

    @Resource
    IntegratedMonitorWarningService integratedMonitorWarningService;

    @Resource
    private AuthorityService authorityService;

    /**
     * 查询预警提醒的分页数据（未处理、未忽略、未删除）
     *
     * @param param
     * @return
     * @throws BizException
     */
    @RequestMapping(value = "/unhandled", method = RequestMethod.GET)
    @ResponseBody
    public ApiPageData getPageDataWaringList(@ModelAttribute PageDataRequestParam param) throws BizException, ParseException, RemoteException {
        Date startTime = new Date();
        if (!authorityService.hasRight(ContextHelper.getLoginUser().getUserName(), param.getSiteId(), null, Authority.KPIWEB_WARNING_SEARCH) && !authorityService.hasRight(ContextHelper
                .getLoginUser().getUserName(), null, null, Authority.KPIWEB_WARNING_SEARCH)) {
            throw new BizException(Authority.NO_AUTHORITY);
        }
        ParamCheckUtil.paramCheck(param);
        ApiPageData apiPageData = integratedMonitorWarningService.get(param);
        Date endTime = new Date();
        LogUtil.addOperationLog(OperationType.QUERY, "查询预警提醒的分页数据", siteApiService.getSiteById(param.getSiteId(), "").getSiteName());
        LogUtil.addElapseLog(OperationType.QUERY, "查询预警提醒的分页数据", endTime.getTime()-startTime.getTime());
        return apiPageData;
    }
}

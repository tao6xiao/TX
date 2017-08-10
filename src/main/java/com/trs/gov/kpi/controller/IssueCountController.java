package com.trs.gov.kpi.controller;

import com.trs.gov.kpi.constant.Authority;
import com.trs.gov.kpi.constant.Constants;
import com.trs.gov.kpi.constant.OperationType;
import com.trs.gov.kpi.entity.exception.BizException;
import com.trs.gov.kpi.entity.exception.RemoteException;
import com.trs.gov.kpi.entity.requestdata.IssueCountByTypeRequest;
import com.trs.gov.kpi.entity.requestdata.IssueCountRequest;
import com.trs.gov.kpi.entity.responsedata.*;
import com.trs.gov.kpi.ids.ContextHelper;
import com.trs.gov.kpi.service.IssueCountService;
import com.trs.gov.kpi.service.outer.AuthorityService;
import com.trs.gov.kpi.service.outer.SiteApiService;
import com.trs.gov.kpi.service.outer.UserApiService;
import com.trs.gov.kpi.utils.LogUtil;
import com.trs.gov.kpi.utils.ParamCheckUtil;
import com.trs.gov.kpi.utils.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * 统计分析->问题统计controller
 * Created by he.lang on 2017/6/7.
 */
@Slf4j
@RestController
@RequestMapping(value = "/gov/kpi/analysis/issue")
public class IssueCountController {

    @Resource
    IssueCountService countService;

    @Resource
    private AuthorityService authorityService;

    @Resource
    private SiteApiService siteApiService;

    @Resource
    private UserApiService userApiService;

    private static final String REQUEST = "request";

    /**
     * 分类查询问题数量统计
     *
     * @param request
     * @return
     * @throws BizException
     */
    @RequestMapping(value = "/count", method = RequestMethod.GET)
    @ResponseBody
    public List<Statistics> countSort(@ModelAttribute IssueCountRequest request) throws BizException, RemoteException {
        String logDesc = "问题统计中分类查询问题数量统计" + LogUtil.paramsToLogString(REQUEST, request);
        return LogUtil.ControlleFunctionWrapper(() -> {
            ParamCheckUtil.paramCheck(request);
            checkAuthority(request);
            List<Statistics> list = countService.countSort(request);
            LogUtil.addOperationLog(OperationType.QUERY, logDesc, getSystemName(request));
            return list;
        }, OperationType.QUERY, logDesc, getSystemName(request));
    }

    private String getSystemName(IssueCountRequest request) {
        StringBuilder builder = new StringBuilder();
        Integer[] siteIds = StringUtil.stringToIntegerArray(request.getSiteIds());
        for (int i = 0; i < siteIds.length; i++) {
            builder.append(LogUtil.getSiteNameForLog(siteApiService, siteIds[i]));
            builder.append(",");
        }
        if (builder.length() != 0) {
            builder = builder.deleteCharAt(builder.lastIndexOf(","));
        }
        return builder.toString();
    }

    /**
     * 分类查询统计历史数量
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/count/history", method = RequestMethod.GET)
    @ResponseBody
    public HistoryStatisticsRes historyCountSort(@ModelAttribute IssueCountRequest request) throws BizException, RemoteException {
        String logDesc = "问题统计中分类查询统计历史数量" + LogUtil.paramsToLogString(REQUEST, request);
        return LogUtil.ControlleFunctionWrapper(() -> {
            ParamCheckUtil.paramCheck(request);
            checkAuthority(request);
            HistoryStatisticsRes historyStatisticsRes = countService.historyCountSort(request);
            LogUtil.addOperationLog(OperationType.QUERY, logDesc, getSystemName(request));
            return historyStatisticsRes;
        }, OperationType.QUERY, logDesc, getSystemName(request));
    }

    /**
     * 部门分类查询统计数量
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/bydept/count", method = RequestMethod.GET)
    @ResponseBody
    public List<DeptCountResponse> deptCountSort(@ModelAttribute IssueCountRequest request) throws BizException, RemoteException {
        String logDesc = "问题统计中部门分类查询统计数量" + LogUtil.paramsToLogString(REQUEST, request);
        return LogUtil.ControlleFunctionWrapper(() -> {
            Date startTime = new Date();
            ParamCheckUtil.paramCheck(request);
            checkAuthority(request);
            List<DeptCountResponse> deptCountResponses = countService.deptCountSort(request);
            Date endTime = new Date();
            LogUtil.addOperationLog(OperationType.QUERY, logDesc, getSystemName(request));
            LogUtil.addElapseLog(OperationType.QUERY, logDesc + "，相关站点:" + getSystemName(request), endTime.getTime() - startTime.getTime());
            return deptCountResponses;
        }, OperationType.QUERY, logDesc, getSystemName(request));
    }

    /**
     * 根据问题类型部门分类查询统计数量
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/unhandled/count", method = RequestMethod.GET)
    @ResponseBody
    public List<DeptCount> getDeptIssueCountByType(@ModelAttribute IssueCountByTypeRequest request) throws BizException, RemoteException {
        String logDesc = "问题统计中根据问题类型部门分类查询统计数量" + LogUtil.paramsToLogString(REQUEST, request);
        return LogUtil.ControlleFunctionWrapper(() -> {
            ParamCheckUtil.paramCheck(request);
            if (request.getTypeId() > 5 || request.getTypeId() < 1) {
                throw new BizException(Constants.INVALID_PARAMETER);
            }
            checkAuthority(request);
            List<DeptCount> deptCountList = countService.getDeptCountByType(request);
            LogUtil.addOperationLog(OperationType.QUERY, logDesc, getSystemName(request));
            return deptCountList;
        }, OperationType.QUERY, logDesc, getSystemName(request));
    }


    /**
     * 部门分类归纳查询统计数量
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/bytype/count", method = RequestMethod.GET)
    @ResponseBody
    public DeptInductionResponse[] deptInductionSort(@ModelAttribute IssueCountRequest request) throws BizException, RemoteException {
        String logDesc = "问题统计中部门分类归纳统计数量查询" + LogUtil.paramsToLogString(REQUEST, request);
        return LogUtil.ControlleFunctionWrapper(() -> {
            ParamCheckUtil.paramCheck(request);
            checkAuthority(request);
            DeptInductionResponse[] inductionResponseArray = countService.deptInductionSort(request);
            LogUtil.addOperationLog(OperationType.QUERY, logDesc, getSystemName(request));
            return inductionResponseArray;
        }, OperationType.QUERY, logDesc, getSystemName(request));
    }


    private void checkAuthority(IssueCountRequest request) throws RemoteException, BizException {
        String userName = ContextHelper.getLoginUser().getUserName();
        if (!authorityService.hasRight(userName, null, Authority.KPIWEB_STATISTICS_ISSUE)) {
            String[] siteIds = request.getSiteIds().split(",");
            for (String siteId : siteIds) {
                if (!authorityService.hasRight(ContextHelper.getLoginUser().getUserName(), Integer.parseInt(siteId), Authority.KPIWEB_STATISTICS_ISSUE)) {
                    throw new BizException(Authority.NO_AUTHORITY);
                }
            }
        }
    }
}

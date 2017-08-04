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
        Date startTime = new Date();
        checkAuthority(request);
        ParamCheckUtil.paramCheck(request);
        List<Statistics> list = countService.countSort(request);
        Date endTime = new Date();
        LogUtil.addOperationLog(OperationType.QUERY, "问题统计中分类查询问题数量统计", getSystemName(request));
        LogUtil.addElapseLog(OperationType.QUERY, "问题统计中分类查询问题数量统计", endTime.getTime()-startTime.getTime());
        return list;
    }

    private String getSystemName(IssueCountRequest request) throws RemoteException {
        StringBuilder builder = new StringBuilder();
        Integer[] siteIds = StringUtil.stringToIntegerArray(request.getSiteIds());
        for (int i = 0; i < siteIds.length; i++) {
            builder.append(siteApiService.getSiteById(siteIds[i], "").getSiteName());
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
    public History historyCountSort(@ModelAttribute IssueCountRequest request) throws BizException, RemoteException {
        Date startTime = new Date();
        checkAuthority(request);
        ParamCheckUtil.paramCheck(request);
        History history = countService.historyCountSort(request);
        Date endTime = new Date();
        LogUtil.addOperationLog(OperationType.QUERY, "问题统计中分类查询统计历史数量", getSystemName(request));
        LogUtil.addElapseLog(OperationType.QUERY, "问题统计中分类查询统计历史数量", endTime.getTime()-startTime.getTime());
        return history;
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
        Date startTime = new Date();
        checkAuthority(request);
        ParamCheckUtil.paramCheck(request);
        List<DeptCountResponse> deptCountResponses = countService.deptCountSort(request);
        Date endTime = new Date();
        LogUtil.addOperationLog(OperationType.QUERY, "问题统计中部门分类查询统计数量", getSystemName(request));
        LogUtil.addElapseLog(OperationType.QUERY, "问题统计中部门分类查询统计数量", endTime.getTime()-startTime.getTime());
        return deptCountResponses;
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
        Date startTime = new Date();
        checkAuthority(request);
        ParamCheckUtil.paramCheck(request);
        if (request.getTypeId() > 5 || request.getTypeId() < 1) {
            throw new BizException(Constants.INVALID_PARAMETER);
        }
        List<DeptCount> deptCountList = countService.getDeptCountByType(request);
        Date endTime = new Date();
        LogUtil.addOperationLog(OperationType.QUERY, "问题统计中根据问题类型部门分类查询统计数量", getSystemName(request));
        LogUtil.addElapseLog(OperationType.QUERY, "问题统计中根据问题类型部门分类查询统计数量", endTime.getTime()-startTime.getTime());
        return deptCountList;
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
        Date startTime = new Date();
        checkAuthority(request);
        ParamCheckUtil.paramCheck(request);
        DeptInductionResponse[] inductionResponseArray = countService.deptInductionSort(request);
        Date endTime = new Date();
        LogUtil.addOperationLog(OperationType.QUERY, "问题统计中部门分类归纳统计数量查询", getSystemName(request));
        LogUtil.addElapseLog(OperationType.QUERY, "问题统计中部门分类归纳统计数量查询", endTime.getTime()-startTime.getTime());
        return inductionResponseArray;
    }


    private void checkAuthority(IssueCountRequest request) throws RemoteException, BizException {
        if (!authorityService.hasRight(ContextHelper.getLoginUser().getUserName(), null, null, Authority.KPIWEB_STATISTICS_ISSUE)) {
            String[] siteIds = request.getSiteIds().split(",");
            for (int i = 0; i < siteIds.length; i++) {
                if (!authorityService.hasRight(ContextHelper.getLoginUser().getUserName(), Integer.parseInt(siteIds[i]), null, Authority.KPIWEB_STATISTICS_ISSUE)) {
                    throw new BizException(Authority.NO_AUTHORITY);
                }
            }
        }
    }
}

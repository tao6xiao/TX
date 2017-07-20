package com.trs.gov.kpi.controller;

import com.trs.gov.kpi.constant.Authority;
import com.trs.gov.kpi.constant.Constants;
import com.trs.gov.kpi.entity.exception.BizException;
import com.trs.gov.kpi.entity.exception.RemoteException;
import com.trs.gov.kpi.entity.requestdata.IssueCountByTypeRequest;
import com.trs.gov.kpi.entity.requestdata.IssueCountRequest;
import com.trs.gov.kpi.entity.responsedata.*;
import com.trs.gov.kpi.ids.ContextHelper;
import com.trs.gov.kpi.service.IssueCountService;
import com.trs.gov.kpi.service.outer.AuthorityService;
import com.trs.gov.kpi.utils.ParamCheckUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
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
        checkAuthority(request);
        ParamCheckUtil.paramCheck(request);
        return countService.countSort(request);
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
        checkAuthority(request);
        ParamCheckUtil.paramCheck(request);
        return countService.historyCountSort(request);
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
        checkAuthority(request);
        ParamCheckUtil.paramCheck(request);
        return countService.deptCountSort(request);
    }

    /**
     * 更具问题类型部门分类查询统计数量
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/unhandled/count", method = RequestMethod.GET)
    @ResponseBody
    public List<DeptCount> getDeptIssueCountByType(@ModelAttribute IssueCountByTypeRequest request) throws BizException, RemoteException {
        checkAuthority(request);
        ParamCheckUtil.paramCheck(request);
        if (request.getTypeId() > 5 || request.getTypeId() < 1) {
            throw new BizException(Constants.INVALID_PARAMETER);
        }

        return countService.getDeptCountByType(request);
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
        checkAuthority(request);
        ParamCheckUtil.paramCheck(request);
        return countService.deptInductionSort(request);
    }


    private void checkAuthority(IssueCountRequest request) throws RemoteException, BizException {
        String roleId = authorityService.getRoleByUser(ContextHelper.getLoginUser().getUserName());
        if (Authority.PLATFORM_ROLE_ID.equals(roleId)) {
            if (!authorityService.hasRight(ContextHelper.getLoginUser().getUserName(), null, null, Authority.KPIWEB_STATISTICS_ISSUE)) {
                throw new BizException(Authority.NO_AUTHORITY);
            }
        } else if (Authority.SITE_ROLE_ID.equals(roleId)) {
            String[] siteIds = request.getSiteIds().split(",");
            for (int i = 0; i < siteIds.length; i++) {
                if (!authorityService.hasRight(ContextHelper.getLoginUser().getUserName(), Integer.parseInt(siteIds[i]), null, Authority.KPIWEB_STATISTICS_ISSUE)) {
                    throw new BizException(Authority.NO_AUTHORITY);
                }
            }
        }
    }
}

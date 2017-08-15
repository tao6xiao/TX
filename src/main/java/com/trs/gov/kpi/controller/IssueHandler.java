package com.trs.gov.kpi.controller;

import com.trs.gov.kpi.constant.Constants;
import com.trs.gov.kpi.constant.OperationType;
import com.trs.gov.kpi.entity.exception.BizException;
import com.trs.gov.kpi.entity.exception.RemoteException;
import com.trs.gov.kpi.service.IssueService;
import com.trs.gov.kpi.service.outer.SiteApiService;
import com.trs.gov.kpi.utils.LogUtil;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import java.util.Arrays;

/**
 * Created by he.lang on 2017/6/8.
 */
public class IssueHandler {

    @Resource
    private IssueService issueService;

    @Resource
    SiteApiService siteApiService;

    /**
     * 批量处理
     *
     * @param siteId
     * @param ids
     * @return
     */
    @RequestMapping(value = "/handle", method = RequestMethod.POST)
    public String handIssuesByIds(int siteId, Integer[] ids) throws RemoteException, BizException {
        String logDesc = "处理问题" + LogUtil.paramsToLogString(Constants.DB_FIELD_SITE_ID, siteId, Constants.IDS, ids);
        return LogUtil.controlleFunctionWrapper(() -> {
            issueService.handIssuesByIds(siteId, Arrays.asList(ids));
            return null;
        }, OperationType.UPDATE, LogUtil.buildFailOperationLogDesc(logDesc), LogUtil.getSiteNameForLog(siteApiService, siteId));
    }

    /**
     * 批量忽略
     *
     * @param siteId
     * @param ids
     * @return
     */
    @RequestMapping(value = "/ignore", method = RequestMethod.POST)
    public String ignoreIssuesByIds(int siteId, Integer[] ids) throws RemoteException, BizException {
        String logDesc = "忽略问题" + LogUtil.paramsToLogString(Constants.DB_FIELD_SITE_ID, siteId, Constants.IDS, ids);
        return LogUtil.controlleFunctionWrapper(() -> {
            issueService.ignoreIssuesByIds(siteId, Arrays.asList(ids));
            return null;
        }, OperationType.UPDATE, LogUtil.buildFailOperationLogDesc(logDesc), LogUtil.getSiteNameForLog(siteApiService, siteId));
    }

    /**
     * 批量删除
     *
     * @param siteId
     * @param ids
     * @return
     */
    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    public String delIssueByIds(int siteId, Integer[] ids) throws RemoteException, BizException {
        String logDesc = "删除问题" + LogUtil.paramsToLogString(Constants.DB_FIELD_SITE_ID, siteId, Constants.IDS, ids);
        return LogUtil.controlleFunctionWrapper(() -> {
            issueService.delIssueByIds(siteId, Arrays.asList(ids));
            return null;
        }, OperationType.DELETE, LogUtil.buildFailOperationLogDesc(logDesc), LogUtil.getSiteNameForLog(siteApiService, siteId));
    }

    /**
     * 批量更新所属部门
     *
     * @param siteId
     * @param ids
     * @param deptId
     * @return
     */
    @RequestMapping(value = "/updatedept", method = RequestMethod.POST)
    public String updateDeptByIds(int siteId, Integer[] ids, int deptId) throws RemoteException, BizException {
        String logDesc = "修改所属部门" + LogUtil.paramsToLogString(Constants.DB_FIELD_SITE_ID, siteId, Constants.IDS, ids);
        return LogUtil.controlleFunctionWrapper(() -> {
            issueService.updateDeptByIds(siteId, Arrays.asList(ids), deptId);
            return null;
        }, OperationType.UPDATE, LogUtil.buildFailOperationLogDesc(logDesc), LogUtil.getSiteNameForLog(siteApiService, siteId));
    }

}

package com.trs.gov.kpi.controller;

import com.trs.gov.kpi.constant.OperationType;
import com.trs.gov.kpi.constant.UrlPath;
import com.trs.gov.kpi.entity.exception.BizException;
import com.trs.gov.kpi.entity.exception.RemoteException;
import com.trs.gov.kpi.service.IssueService;
import com.trs.gov.kpi.service.outer.SiteApiService;
import com.trs.gov.kpi.utils.TRSLogUserUtil;
import com.trs.mlf.simplelog.SimpleLogServer;
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
    @RequestMapping(value = UrlPath.HANDLE_PATH, method = RequestMethod.POST)
    public String handIssuesByIds(int siteId, Integer[] ids) throws BizException, RemoteException {
        issueService.handIssuesByIds(siteId, Arrays.asList(ids));
        SimpleLogServer.getInstance(TRSLogUserUtil.getLogUser()).operation(OperationType.UPDATE, "处理问题", siteApiService.getSiteById(siteId, "").getSiteName()).info();
        return null;
    }

    /**
     * 批量忽略
     *
     * @param siteId
     * @param ids
     * @return
     */
    @RequestMapping(value = UrlPath.IGNORE_PATH, method = RequestMethod.POST)
    public String ignoreIssuesByIds(int siteId, Integer[] ids) throws BizException, RemoteException {
        issueService.ignoreIssuesByIds(siteId, Arrays.asList(ids));
        SimpleLogServer.getInstance(TRSLogUserUtil.getLogUser()).operation(OperationType.UPDATE, "忽略问题", siteApiService.getSiteById(siteId, "").getSiteName()).info();
        return null;
    }

    /**
     * 批量删除
     *
     * @param siteId
     * @param ids
     * @return
     */
    @RequestMapping(value = UrlPath.DELETE_PATH, method = RequestMethod.DELETE)
    public String delIssueByIds(int siteId, Integer[] ids) throws BizException, RemoteException {
        issueService.delIssueByIds(siteId, Arrays.asList(ids));
        SimpleLogServer.getInstance(TRSLogUserUtil.getLogUser()).operation(OperationType.DELETE, "删除问题", siteApiService.getSiteById(siteId, "").getSiteName()).info();
        return null;
    }

    /**
     * 批量更新所属部门
     *
     * @param siteId
     * @param ids
     * @param deptId
     * @return
     */
    @RequestMapping(value = UrlPath.UPDATE_DEPT_PATH, method = RequestMethod.POST)
    public String updateDeptByIds(int siteId, Integer[] ids, int deptId) throws BizException, RemoteException {
        issueService.updateDeptByIds(siteId, Arrays.asList(ids), deptId);
        SimpleLogServer.getInstance(TRSLogUserUtil.getLogUser()).operation(OperationType.UPDATE, "修改所属部门", siteApiService.getSiteById(siteId, "").getSiteName()).info();
        return null;
    }

}

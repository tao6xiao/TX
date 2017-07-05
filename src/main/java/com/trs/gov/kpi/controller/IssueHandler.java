package com.trs.gov.kpi.controller;

import com.trs.gov.kpi.constant.Authority;
import com.trs.gov.kpi.entity.exception.BizException;
import com.trs.gov.kpi.entity.exception.RemoteException;
import com.trs.gov.kpi.service.IssueService;
import com.trs.gov.kpi.service.outer.AuthorityService;
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
    private AuthorityService authorityService;

    /**
     * 批量处理
     *
     * @param siteId
     * @param ids
     * @return
     */
    @RequestMapping(value = "/handle", method = RequestMethod.POST)
    public String handIssuesByIds(int siteId, Integer[] ids) throws RemoteException, BizException {
        if (authorityService.hasRight(siteId, null, Authority.KPIWEB_OPERATE_HANDLE)) {
            throw new BizException(Authority.NO_AUTHORITY);
        }
        issueService.handIssuesByIds(siteId, Arrays.asList(ids));
        return null;
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
        if (authorityService.hasRight(siteId, null, Authority.KPIWEB_OPERATE_IGNORE)) {
            throw new BizException(Authority.NO_AUTHORITY);
        }
        issueService.ignoreIssuesByIds(siteId, Arrays.asList(ids));
        return null;
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
        if (authorityService.hasRight(siteId, null, Authority.KPIWEB_OPERATE_DELETE)) {
            throw new BizException(Authority.NO_AUTHORITY);
        }
        issueService.delIssueByIds(siteId, Arrays.asList(ids));
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
    @RequestMapping(value = "/updatedept", method = RequestMethod.POST)
    public String updateDeptByIds(int siteId, Integer[] ids, int deptId) throws RemoteException, BizException {
        if (authorityService.hasRight(siteId, null, Authority.KPIWEB_OPERATE_UPDATEDEPT)) {
            throw new BizException(Authority.NO_AUTHORITY);
        }
        issueService.updateDeptByIds(siteId, Arrays.asList(ids), deptId);
        return null;
    }

}

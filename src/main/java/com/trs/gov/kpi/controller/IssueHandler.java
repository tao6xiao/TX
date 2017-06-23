package com.trs.gov.kpi.controller;

import com.trs.gov.kpi.service.IssueService;
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

    /**
     * 批量处理
     *
     * @param siteId
     * @param ids
     * @return
     */
    @RequestMapping(value = "/handle", method = RequestMethod.POST)
    public String handIssuesByIds(int siteId, Integer[] ids) {
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
    public String ignoreIssuesByIds(int siteId, Integer[] ids) {
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
    public String delIssueByIds(int siteId, Integer[] ids) {
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
    public String updateDeptByIds(int siteId, Integer[] ids, int deptId) {
        issueService.updateDeptByIds(siteId, Arrays.asList(ids), deptId);
        return null;
    }

}

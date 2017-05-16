package com.trs.gov.kpi.service.impl;

import com.trs.gov.kpi.dao.OperationMapper;
import com.trs.gov.kpi.entity.IssueBase;
import com.trs.gov.kpi.service.OperationService;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * Created by rw103 on 2017/5/13.
 */
public class OperationServiceImpl implements OperationService {

    @Resource
    private OperationMapper operationMapper;

    @Override
    public int getHandledIssueCount(IssueBase issueBase) {
        return operationMapper.getHandledIssueCount(issueBase);
    }

    @Override
    public int getUnhandledIssueCount(IssueBase issueBase) {
        return operationMapper.getUnhandledIssueCount(issueBase);
    }

    @Override
    public int getUpdateNotIntimeCount(IssueBase issueBase) {
        return getUpdateNotIntimeCount(issueBase);
    }

    @Override
    public int getUpdateWarningCount(IssueBase issueBase) {
        return operationMapper.getUpdateWarningCount(issueBase);
    }

    @Override
    public void handIssueById(int siteId, int id) {
        operationMapper.handIssueById(siteId, id);
    }

    @Override
    public void handIssuesByIds(int siteId, List<Integer> ids) {
        operationMapper.handIssuesByIds(siteId, ids);
    }

    @Override
    public void ignoreIssueById(int siteId, int id) {
        operationMapper.ignoreIssueById(siteId, id);
    }

    @Override
    public void ignoreIssuesByIds(int siteId, List<Integer> ids) {
        operationMapper.ignoreIssuesByIds(siteId, ids);
    }

    @Override
    public void delIssueByIds(int siteId, List<Integer> ids) {
        operationMapper.delIssueByIds(siteId, ids);
    }

    @Override
    public Date getEarliestIssueTime() {
        return operationMapper.getEarliestIssueTime();
    }
}

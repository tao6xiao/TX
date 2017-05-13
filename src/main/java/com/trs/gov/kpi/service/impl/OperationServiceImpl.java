package com.trs.gov.kpi.service.impl;

import com.trs.gov.kpi.dao.OperationMapper;
import com.trs.gov.kpi.service.OperationService;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by rw103 on 2017/5/13.
 */
public class OperationServiceImpl implements OperationService {

    @Resource
    private OperationMapper operationMapper;

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
}

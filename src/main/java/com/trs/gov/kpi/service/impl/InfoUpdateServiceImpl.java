package com.trs.gov.kpi.service.impl;

import com.trs.gov.kpi.dao.InfoUpdateMapper;
import com.trs.gov.kpi.entity.InfoUpdate;
import com.trs.gov.kpi.entity.IssueBase;
import com.trs.gov.kpi.service.InfoUpdateService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by rw103 on 2017/5/13.
 */
@Service
public class InfoUpdateServiceImpl extends OperationServiceImpl implements InfoUpdateService {

    @Resource
    private InfoUpdateMapper infoUpdateMapper;

    @Override
    public int getHandledIssueCount(IssueBase issueBase) {
        return infoUpdateMapper.getHandledIssueCount(issueBase);
    }

    @Override
    public int getUpdateNotIntimeCount(IssueBase issueBase) {
        return infoUpdateMapper.getUpdateNotIntimeCount(issueBase);
    }

    @Override
    public int getUpdateWarningCount(IssueBase issueBase) {
        return infoUpdateMapper.getUpdateWarningCount(issueBase);
    }

    @Override
    public int getIssueHistoryCount(InfoUpdate infoUpdate) {
        return infoUpdateMapper.getIssueHistoryCount(infoUpdate);
    }

    @Override
    public List<InfoUpdate> getIssueList(Integer currPage, Integer pageSize, InfoUpdate infoUpdate) {
        return infoUpdateMapper.getIssueList(currPage, pageSize, infoUpdate);
    }
}

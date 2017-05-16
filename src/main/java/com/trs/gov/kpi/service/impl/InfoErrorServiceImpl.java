package com.trs.gov.kpi.service.impl;

import com.trs.gov.kpi.dao.InfoErrorMapper;
import com.trs.gov.kpi.entity.InfoError;
import com.trs.gov.kpi.entity.IssueBase;
import com.trs.gov.kpi.service.InfoErrorService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by ranwei on 2017/5/15.
 */
@Service
public class InfoErrorServiceImpl extends OperationServiceImpl implements InfoErrorService {

    @Resource
    private InfoErrorMapper infoErrorMapper;

    @Override
    public int getHandledIssueCount(IssueBase issueBase) {
        return infoErrorMapper.getHandledIssueCount(issueBase);
    }

    @Override
    public int getUnhandledIssueCount(IssueBase issueBase) {
        return infoErrorMapper.getUnhandledIssueCount(issueBase);
    }

    @Override
    public int getIssueHistoryCount(InfoError infoError) {
        return infoErrorMapper.getIssueHistoryCount(infoError);
    }

    @Override
    public List<InfoError> getIssueList(Integer currPage, Integer pageSize, InfoError infoError) {
        return infoErrorMapper.getIssueList(currPage, pageSize, infoError);
    }
}

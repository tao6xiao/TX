package com.trs.gov.kpi.service.impl;

import com.trs.gov.kpi.constant.InfoErrorType;
import com.trs.gov.kpi.constant.LinkIssueType;
import com.trs.gov.kpi.dao.InfoErrorMapper;
import com.trs.gov.kpi.entity.InfoError;
import com.trs.gov.kpi.entity.IssueBase;
import com.trs.gov.kpi.entity.responsedata.Statistics;
import com.trs.gov.kpi.service.InfoErrorService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
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
    public int getIssueHistoryCount(IssueBase issueBase) {
        return infoErrorMapper.getIssueHistoryCount(issueBase);
    }

    @Override
    public List<InfoError> getIssueList(Integer currPage, Integer pageSize, IssueBase issueBase) {
        return infoErrorMapper.getIssueList(currPage, pageSize, issueBase);
    }

    @Override
    public List<Statistics> getIssueCountByType(IssueBase issueBase) {

        int typosCount = infoErrorMapper.getTyposCount(issueBase);
        Statistics typosStatistics = new Statistics();
        typosStatistics.setCount(typosCount);
        typosStatistics.setType(InfoErrorType.TYPOS.value);
        typosStatistics.setName(InfoErrorType.TYPOS.name);

        int sensitiveWordsCount = infoErrorMapper.getSensitiveWordsCount(issueBase);
        Statistics sensitiveWordsStatistics = new Statistics();
        sensitiveWordsStatistics.setCount(sensitiveWordsCount);
        sensitiveWordsStatistics.setType(InfoErrorType.SENSITIVE_WORDS.value);
        sensitiveWordsStatistics.setName(InfoErrorType.SENSITIVE_WORDS.name);

        List<Statistics> list = new ArrayList<>();
        list.add(typosStatistics);
        list.add(sensitiveWordsStatistics);

        return list;
    }
}

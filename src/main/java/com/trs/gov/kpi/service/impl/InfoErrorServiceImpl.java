package com.trs.gov.kpi.service.impl;

import com.trs.gov.kpi.constant.InfoErrorType;
import com.trs.gov.kpi.dao.InfoErrorMapper;
import com.trs.gov.kpi.entity.HistoryDate;
import com.trs.gov.kpi.entity.InfoError;
import com.trs.gov.kpi.entity.IssueBase;
import com.trs.gov.kpi.entity.responsedata.HistoryStatistics;
import com.trs.gov.kpi.entity.responsedata.Statistics;
import com.trs.gov.kpi.service.InfoErrorService;
import com.trs.gov.kpi.utils.DateSplitUtil;
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
    public List<HistoryStatistics> getIssueHistoryCount(IssueBase issueBase) {
        List<HistoryDate> dateList = DateSplitUtil.getHistoryDateList(issueBase.getBeginDateTime(), issueBase.getEndDateTime());
        List<HistoryStatistics> list = new ArrayList<>();
        for (HistoryDate date : dateList) {
            HistoryStatistics historyStatistics = new HistoryStatistics();
            issueBase.setBeginDateTime(date.getBeginDate());
            issueBase.setEndDateTime(date.getEndDate());
            historyStatistics.setValue(infoErrorMapper.getIssueHistoryCount(issueBase));
            historyStatistics.setTime(date.getMonth());
            list.add(historyStatistics);
        }
        return list;
    }

    @Override
    public List<InfoError> getIssueList(Integer pageIndex, Integer pageSize, IssueBase issueBase) {
        List<InfoError> infoErrorList = infoErrorMapper.getIssueList(pageIndex, pageSize, issueBase);
        for (InfoError info : infoErrorList) {
            if (info.getIssueTypeId() == InfoErrorType.TYPOS.value) {
                info.setIssueTypeName(InfoErrorType.TYPOS.name);
            } else if (info.getIssueTypeId() == InfoErrorType.SENSITIVE_WORDS.value) {
                info.setIssueTypeName(InfoErrorType.SENSITIVE_WORDS.name);
            }
        }

        return infoErrorList;
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

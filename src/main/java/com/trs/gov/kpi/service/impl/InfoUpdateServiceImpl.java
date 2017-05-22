package com.trs.gov.kpi.service.impl;

import com.trs.gov.kpi.constant.InfoUpdateType;
import com.trs.gov.kpi.constant.UpdateWarningType;
import com.trs.gov.kpi.dao.InfoUpdateMapper;
import com.trs.gov.kpi.entity.InfoUpdate;
import com.trs.gov.kpi.entity.IssueBase;
import com.trs.gov.kpi.entity.responsedata.Statistics;
import com.trs.gov.kpi.service.InfoUpdateService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
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
    public int getUnhandledIssueCount(IssueBase issueBase) {
        return infoUpdateMapper.getUnhandledIssueCount(issueBase);
    }

    @Override
    public int getUpdateWarningCount(IssueBase issueBase) {
        return infoUpdateMapper.getUpdateWarningCount(issueBase);
    }

    @Override
    public int getIssueHistoryCount(IssueBase issueBase) {
        return infoUpdateMapper.getIssueHistoryCount(issueBase);
    }

    @Override
    public List<InfoUpdate> getIssueList(Integer currPage, Integer pageSize, IssueBase issueBase) {
        return infoUpdateMapper.getIssueList(currPage, pageSize, issueBase);
    }

    @Override
    public List<Statistics> getIssueCountByType(IssueBase issueBase) {

        int updateNotIntimeCount = infoUpdateMapper.getUpdateNotIntimeCount(issueBase);
        Statistics updateNotIntimeStatistics = new Statistics();
        updateNotIntimeStatistics.setCount(updateNotIntimeCount);
        updateNotIntimeStatistics.setType(InfoUpdateType.UPDATE_NOT_INTIME.value);
        updateNotIntimeStatistics.setName(InfoUpdateType.UPDATE_NOT_INTIME.name);

        List<Statistics> list = new ArrayList<>();
        list.add(updateNotIntimeStatistics);

        return list;
    }

    @Override
    public List<Statistics> getWarningCountByType(IssueBase issueBase) {

        int updateWarningCount = infoUpdateMapper.getUpdateWarningCount(issueBase);
        Statistics updateWarningStatistics = new Statistics();
        updateWarningStatistics.setCount(updateWarningCount);
        updateWarningStatistics.setType(UpdateWarningType.UPDATE_WARNING.value);
        updateWarningStatistics.setName(UpdateWarningType.UPDATE_WARNING.name);

        int selfWarningCount = infoUpdateMapper.getSelfWarningCount(issueBase);
        Statistics selfWarningStatistics = new Statistics();
        selfWarningStatistics.setCount(selfWarningCount);
        selfWarningStatistics.setType(UpdateWarningType.SELF_CHECK_WARNING.value);
        selfWarningStatistics.setName(UpdateWarningType.SELF_CHECK_WARNING.name);

        List<Statistics> list = new ArrayList<>();
        list.add(updateWarningStatistics);
        list.add(selfWarningStatistics);

        return list;
    }
}

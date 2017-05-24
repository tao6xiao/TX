package com.trs.gov.kpi.service.impl;

import com.trs.gov.kpi.entity.IssueBase;
import com.trs.gov.kpi.entity.IssueIndicator;
import com.trs.gov.kpi.entity.responsedata.Statistics;
import com.trs.gov.kpi.service.InfoErrorService;
import com.trs.gov.kpi.service.InfoUpdateService;
import com.trs.gov.kpi.service.IntegratedMonitorService;
import com.trs.gov.kpi.service.LinkAvailabilityService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by ranwei on 2017/5/24.
 */
@Service
public class IntegratedMonitorServiceImpl implements IntegratedMonitorService {

    @Resource
    private LinkAvailabilityService linkAvailabilityService;

    @Resource
    private InfoUpdateService infoUpdateService;

    @Resource
    private InfoErrorService infoErrorService;

    @Override
    public List<Statistics> getAllIssueCount(IssueBase issueBase) {

        int handledCount = linkAvailabilityService.getHandledIssueCount(issueBase) + infoUpdateService.getHandledIssueCount(issueBase) + infoErrorService.getHandledIssueCount(issueBase);
        int unhandledCount = linkAvailabilityService.getUnhandledIssueCount(issueBase) + infoUpdateService.getUnhandledIssueCount(issueBase) + infoErrorService.getUnhandledIssueCount(issueBase);
        int warningCount = infoUpdateService.getUpdateWarningCount(issueBase);

        Statistics handledStatistics = new Statistics();
        handledStatistics.setName(IssueIndicator.SOLVED.name);
        handledStatistics.setType(IssueIndicator.SOLVED.value);
        handledStatistics.setCount(handledCount);

        Statistics unhandledStatistics = new Statistics();
        unhandledStatistics.setName(IssueIndicator.UN_SOLVED.name);
        unhandledStatistics.setType(IssueIndicator.UN_SOLVED.value);
        unhandledStatistics.setCount(unhandledCount);

        Statistics warningStatistics = new Statistics();
        warningStatistics.setName(IssueIndicator.WARNING.name);
        warningStatistics.setType(IssueIndicator.WARNING.value);
        warningStatistics.setCount(warningCount);

        List<Statistics> list = new ArrayList<>();
        list.add(handledStatistics);
        list.add(unhandledStatistics);
        list.add(warningStatistics);

        return list;
    }

    @Override
    public List<Statistics> getUnhandledIssueCount(IssueBase issueBase) {

        List<Statistics> linkAvailabilityList = linkAvailabilityService.getIssueCountByType(issueBase);
        List<Statistics> infoUpdateList = infoUpdateService.getIssueCountByType(issueBase);
        List<Statistics> infoErrorList = infoErrorService.getIssueCountByType(issueBase);

        linkAvailabilityList.addAll(infoUpdateList);
        linkAvailabilityList.addAll(infoErrorList);

        return linkAvailabilityList;
    }

    @Override
    public List<Statistics> getWarningCount(IssueBase issueBase) {

        List<Statistics> list = infoUpdateService.getWarningCountByType(issueBase);

        return list;
    }
}

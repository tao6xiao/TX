package com.trs.gov.kpi.controller;

import com.trs.gov.kpi.entity.IssueIndicator;
import com.trs.gov.kpi.entity.IssueBase;
import com.trs.gov.kpi.entity.WarningIndicator;
import com.trs.gov.kpi.entity.responsedata.Statistics;
import com.trs.gov.kpi.service.InfoErrorService;
import com.trs.gov.kpi.service.InfoUpdateService;
import com.trs.gov.kpi.service.LinkAvailabilityService;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ranwei on 2017/5/16.
 */
@RestController
@RequestMapping("/gov/kpi/issue")
public class IntegratedMonitorController {

    @Resource
    private LinkAvailabilityService linkAvailabilityService;

    @Resource
    private InfoUpdateService infoUpdateService;

    @Resource
    private InfoErrorService infoErrorService;

    @RequestMapping(value = "/all/count", method = RequestMethod.GET)
    public Integer getAllIssueCount(@ModelAttribute IssueBase issueBase) {
        int linkAvailabilityCount = linkAvailabilityService.getHandledIssueCount(issueBase) + linkAvailabilityService.getUnhandledIssueCount(issueBase);
        int infoUpdateCount = infoUpdateService.getHandledIssueCount(issueBase) + infoUpdateService.getUpdateNotIntimeCount(issueBase) + infoUpdateService.getUpdateWarningCount(issueBase);
        int infoErrorCount = infoErrorService.getHandledIssueCount(issueBase) + infoErrorService.getUnhandledIssueCount(issueBase);
        return linkAvailabilityCount + infoErrorCount + infoUpdateCount;
    }

    @RequestMapping(value = "/unhandled/bytype/count", method = RequestMethod.GET)
    public List<Statistics> getUnhandledIssueCount(@ModelAttribute IssueBase issueBase) {
        int linkAvailabilityCount = linkAvailabilityService.getUnhandledIssueCount(issueBase);
        int infoUpdateCount = infoUpdateService.getUpdateNotIntimeCount(issueBase);

        Statistics linkAvailabilityStatistics = new Statistics();
        linkAvailabilityStatistics.setCount(linkAvailabilityCount);
        linkAvailabilityStatistics.setType(IssueIndicator.INVALID_LINK.value);
        linkAvailabilityStatistics.setName(IssueIndicator.INVALID_LINK.name);

        Statistics infoUpdateStatistics = new Statistics();
        infoUpdateStatistics.setCount(infoUpdateCount);
        infoUpdateStatistics.setType(IssueIndicator.UPDATE_NOT_INTIME.value);
        infoUpdateStatistics.setName(IssueIndicator.UPDATE_NOT_INTIME.name);

        List<Statistics> list = new ArrayList<>();
        list.add(linkAvailabilityStatistics);
        list.add(infoUpdateStatistics);

        return list;
    }

    @RequestMapping(value = "/warning/bytype/count")
    public List<Statistics> getWarningCount(@ModelAttribute IssueBase issueBase){
        int infoUpdateCount = infoUpdateService.getUpdateWarningCount(issueBase);
        Statistics infoUpdateStatistics = new Statistics();
        infoUpdateStatistics.setCount(infoUpdateCount);
        infoUpdateStatistics.setType(WarningIndicator.UPDATE_WARNING.value);
        infoUpdateStatistics.setName(WarningIndicator.UPDATE_WARNING.name);

        List<Statistics> list = new ArrayList<>();
        list.add(infoUpdateStatistics);

        return list;
    }


}

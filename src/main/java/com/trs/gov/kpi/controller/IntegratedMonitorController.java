package com.trs.gov.kpi.controller;

import com.trs.gov.kpi.entity.IssueBase;
import com.trs.gov.kpi.service.InfoErrorService;
import com.trs.gov.kpi.service.InfoUpdateService;
import com.trs.gov.kpi.service.LinkAvailabilityService;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

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
        return linkAvailabilityCount+infoErrorCount+infoUpdateCount;
    }


}

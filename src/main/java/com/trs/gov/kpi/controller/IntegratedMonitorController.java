package com.trs.gov.kpi.controller;

import com.trs.gov.kpi.constant.InfoWarningType;
import com.trs.gov.kpi.constant.LinkIssueType;
import com.trs.gov.kpi.entity.IssueBase;
import com.trs.gov.kpi.entity.IssueType;
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
 * 综合实时监测
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


    /**
     * 查询所有问题数量
     *
     * @param issueBase
     * @return
     */
    @RequestMapping(value = "/all/count", method = RequestMethod.GET)
    public Integer getAllIssueCount(@ModelAttribute IssueBase issueBase) {
        int linkAvailabilityCount = linkAvailabilityService.getHandledIssueCount(issueBase) + linkAvailabilityService.getUnhandledIssueCount(issueBase);
        int infoUpdateCount = infoUpdateService.getHandledIssueCount(issueBase) + infoUpdateService.getUnhandledIssueCount(issueBase) + infoUpdateService.getUpdateWarningCount(issueBase);
        int infoErrorCount = infoErrorService.getHandledIssueCount(issueBase) + infoErrorService.getUnhandledIssueCount(issueBase);
        return linkAvailabilityCount + infoErrorCount + infoUpdateCount;
    }

    /**
     * 查询各类问题的待解决问题数
     *
     * @param issueBase
     * @return
     */
    @RequestMapping(value = "/unhandled/bytype/count", method = RequestMethod.GET)
    public List<Statistics> getUnhandledIssueCount(@ModelAttribute IssueBase issueBase) {
        List list1 = linkAvailabilityService.getIssueCountByType(issueBase);
        List list2 = infoUpdateService.getIssueCountByType(issueBase);
        List list3 = infoErrorService.getIssueCountByType(issueBase);

        return null;
    }

    /**
     * 查询各类预警的待解决数
     *
     * @param issueBase
     * @return
     */
    @RequestMapping(value = "/warning/bytype/count")
    public List<Statistics> getWarningCount(@ModelAttribute IssueBase issueBase) {
        int infoUpdateCount = infoUpdateService.getUpdateWarningCount(issueBase);
        Statistics infoUpdateStatistics = new Statistics();
        infoUpdateStatistics.setCount(infoUpdateCount);
        infoUpdateStatistics.setType(InfoWarningType.UPDATE_WARNING.value);
        infoUpdateStatistics.setName(InfoWarningType.UPDATE_WARNING.name);

        List<Statistics> list = new ArrayList<>();
        list.add(infoUpdateStatistics);

        return list;
    }


}

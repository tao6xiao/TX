package com.trs.gov.kpi.controller;

import com.trs.gov.kpi.entity.IssueBase;
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

        //重置查询条件，使查询所有
        List<Integer> list = new ArrayList<>();
        Integer exception = 0;
        list.add(exception);
        issueBase.setIds(list);
        issueBase.setSearchText("");

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
        List linkAvailabilityList = linkAvailabilityService.getIssueCountByType(issueBase);
        List infoUpdateList = infoUpdateService.getIssueCountByType(issueBase);
        List infoErrorList = infoErrorService.getIssueCountByType(issueBase);

        linkAvailabilityList.addAll(infoUpdateList);
        linkAvailabilityList.addAll(infoErrorList);

        return linkAvailabilityList;
    }

    /**
     * 查询各类预警的待解决数
     *
     * @param issueBase
     * @return
     */
    @RequestMapping(value = "/warning/bytype/count")
    public List<Statistics> getWarningCount(@ModelAttribute IssueBase issueBase) {
        List<Statistics> list = infoUpdateService.getWarningCountByType(issueBase);

        return list;
    }


}

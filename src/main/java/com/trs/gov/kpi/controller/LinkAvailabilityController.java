package com.trs.gov.kpi.controller;


import com.trs.gov.kpi.entity.IssueCount;
import com.trs.gov.kpi.entity.LinkAvailability;
import com.trs.gov.kpi.entity.SolveStatus;
import com.trs.gov.kpi.service.LinkAvailabilityService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by rw103 on 2017/5/11.
 */
@RestController
@RequestMapping("/available/issue")
public class LinkAvailabilityController {

    @Resource
    private LinkAvailabilityService linkAvailabilityService;


    @RequestMapping(value = "/count", method = RequestMethod.GET)
    public List getIssueCount(int siteId) {

        IssueCount unhandledIssueCount = new IssueCount();
        unhandledIssueCount.setType(SolveStatus.UN_SOLVED.value);
        unhandledIssueCount.setCount(linkAvailabilityService.getUnhandledIssueCount(siteId));

        IssueCount handledIssueCount = new IssueCount();
        handledIssueCount.setType(SolveStatus.SOLVED.value);
        handledIssueCount.setCount(linkAvailabilityService.getHandledIssueCount(siteId));

        List list = new ArrayList();
        list.add(unhandledIssueCount);
        list.add(handledIssueCount);

        return list;
    }


    @RequestMapping(value = "/unhandled", method = RequestMethod.GET)
    public List<LinkAvailability> getIssueList(int currPage, int pageSize,@ModelAttribute LinkAvailability linkAvailability) {
        List<LinkAvailability> issueList = linkAvailabilityService.getIssueList(currPage, pageSize, linkAvailability);
        return issueList;
    }

    @RequestMapping(value = "/handle", method = RequestMethod.GET)
    public String handIssueById(int siteId, int id) {
        linkAvailabilityService.handIssueById(siteId, id);
        return "";
    }

    @RequestMapping("/handle/batch")
    public String handIssuesByIds(int siteId, Integer[] ids) {
        linkAvailabilityService.handIssuesByIds(siteId, Arrays.asList(ids));
        return "";
    }

    @RequestMapping(value = "/ignore", method = RequestMethod.POST)
    public String ignoreIssueById(int siteId, int id) {
        linkAvailabilityService.ignoreIssueById(siteId, id);
        return "";
    }

    @RequestMapping("/ignore/batch")
    public String ignoreIssuesByIds(int siteId, Integer[] ids) {
        linkAvailabilityService.ignoreIssuesByIds(siteId, Arrays.asList(ids));
        return "";
    }


    @RequestMapping("/delete")
    public String delIssueByIds(int siteId, Integer[] ids) {
        linkAvailabilityService.delIssueByIds(siteId, Arrays.asList(ids));
        return "";
    }
}

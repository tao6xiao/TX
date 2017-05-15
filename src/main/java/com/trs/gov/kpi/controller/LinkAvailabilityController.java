package com.trs.gov.kpi.controller;


import com.trs.gov.kpi.entity.*;
import com.trs.gov.kpi.entity.responsedata.ApiPageData;
import com.trs.gov.kpi.entity.responsedata.Pager;
import com.trs.gov.kpi.service.LinkAvailabilityService;
import com.trs.gov.kpi.utils.IssueCounter;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

/**
 * Created by rw103 on 2017/5/11.
 */
@RestController
@RequestMapping("/gov/kpi/available/issue")
public class LinkAvailabilityController {

    @Resource
    private LinkAvailabilityService linkAvailabilityService;


    @RequestMapping(value = "/count", method = RequestMethod.GET)
    public List getIssueCount(int siteId) {
        return IssueCounter.getIssueCount(linkAvailabilityService, siteId);
    }


    @RequestMapping(value = "/unhandled", method = RequestMethod.GET)
    public ApiPageData getIssueList(int currPage, int pageSize, @ModelAttribute LinkAvailability linkAvailability) {
        List<LinkAvailability> list = linkAvailabilityService.getIssueList(currPage, pageSize, linkAvailability);
        Pager pager = new Pager();
        if (linkAvailability != null) {
            pager.setCurrPage(currPage);
            pager.setPageSize(pageSize);
            int count = linkAvailabilityService.getUnhandledIssueCount(linkAvailability.getSiteId());
            pager.setItemCount(count);
            int pageCount = count % pageSize == 0 ? count / pageSize : count / pageSize + 1;
            pager.setPageCount(pageCount);
        }
        ApiPageData data = new ApiPageData();
        data.setData(list);
        data.setPager(pager);
        return data;
    }

    @RequestMapping(value = "/handle", method = RequestMethod.POST)
    public String handIssueById(int siteId, int id) {
        linkAvailabilityService.handIssueById(siteId, id);
        return "";
    }

    @RequestMapping(value = "/handle/batch", method = RequestMethod.POST)
    public String handIssuesByIds(int siteId, Integer[] ids) {
        linkAvailabilityService.handIssuesByIds(siteId, Arrays.asList(ids));
        return "";
    }

    @RequestMapping(value = "/ignore", method = RequestMethod.POST)
    public String ignoreIssueById(int siteId, int id) {
        linkAvailabilityService.ignoreIssueById(siteId, id);
        return "";
    }

    @RequestMapping(value = "/ignore/batch", method = RequestMethod.POST)
    public String ignoreIssuesByIds(int siteId, Integer[] ids) {
        linkAvailabilityService.ignoreIssuesByIds(siteId, Arrays.asList(ids));
        return "";
    }


    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public String delIssueByIds(int siteId, Integer[] ids) {
        linkAvailabilityService.delIssueByIds(siteId, Arrays.asList(ids));
        return "";
    }
}

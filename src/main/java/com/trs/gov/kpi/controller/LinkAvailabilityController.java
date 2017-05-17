package com.trs.gov.kpi.controller;


import com.trs.gov.kpi.entity.HistoryDate;
import com.trs.gov.kpi.entity.IssueBase;
import com.trs.gov.kpi.entity.LinkAvailability;
import com.trs.gov.kpi.entity.LinkIssueType;
import com.trs.gov.kpi.entity.exception.BizException;
import com.trs.gov.kpi.entity.responsedata.ApiPageData;
import com.trs.gov.kpi.entity.responsedata.HistoryStatistics;
import com.trs.gov.kpi.service.LinkAvailabilityService;
import com.trs.gov.kpi.utils.DateSplitUtil;
import com.trs.gov.kpi.utils.InitEndTime;
import com.trs.gov.kpi.utils.IssueCounter;
import com.trs.gov.kpi.utils.PageInfoDeal;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by rw103 on 2017/5/11.
 */
@RestController
@RequestMapping("/gov/kpi/available/issue")
public class LinkAvailabilityController {

    @Resource
    private LinkAvailabilityService linkAvailabilityService;


    @RequestMapping(value = "/bytype/count", method = RequestMethod.GET)
    public List getIssueCount(IssueBase issueBase) {
        if (issueBase.getEndDateTime() != null && !issueBase.getEndDateTime().trim().isEmpty()) {
            issueBase.setEndDateTime(InitEndTime.initTime(issueBase.getEndDateTime()));//结束日期加一
        }
        return IssueCounter.getIssueCount(linkAvailabilityService, issueBase);
    }


    @RequestMapping(value = "/all/count/history", method = RequestMethod.GET)
    public List getIssueHistoryCount(@ModelAttribute LinkAvailability linkAvailability) {
        if (linkAvailability.getBeginDateTime() == null || linkAvailability.getBeginDateTime().trim().isEmpty()) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            linkAvailability.setBeginDateTime(sdf.format(linkAvailabilityService.getEarliestIssueTime()));
        }
        if (linkAvailability.getEndDateTime() == null || linkAvailability.getEndDateTime().trim().isEmpty()) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            linkAvailability.setEndDateTime(sdf.format(new Date()));
        }
        List<HistoryDate> dateList = DateSplitUtil.getHistoryDateList(linkAvailability.getBeginDateTime(), linkAvailability.getEndDateTime());
        List<HistoryStatistics> list = new ArrayList<>();
        for (HistoryDate date : dateList) {
            HistoryStatistics historyStatistics = new HistoryStatistics();
            linkAvailability.setBeginDateTime(date.getBeginDate());
            linkAvailability.setEndDateTime(date.getEndDate());
            historyStatistics.setValue(linkAvailabilityService.getIssueHistoryCount(linkAvailability));
            historyStatistics.setTime(date.getMonth());
            list.add(historyStatistics);
        }
        return list;
    }

    @RequestMapping(value = "/unhandled", method = RequestMethod.GET)
    public ApiPageData getIssueList(Integer currPage, Integer pageSize, @ModelAttribute LinkAvailability linkAvailability) throws BizException {

        if (linkAvailability.getSiteId() == null) {
            throw new BizException("站点编号为空");
        }
        int itemCount = linkAvailabilityService.getUnhandledIssueCount(linkAvailability);
        ApiPageData apiPageData = PageInfoDeal.getApiPageData(currPage, pageSize, itemCount);
        List<LinkAvailability> linkAvailabilityList = linkAvailabilityService.getIssueList(apiPageData.getPager().getCurrPage() - 1, apiPageData.getPager().getPageSize(), linkAvailability);
        for (LinkAvailability link : linkAvailabilityList) {
            if (link.getIssueTypeId() == LinkIssueType.INVALID_LINK.value) {
                link.setIssueTypeName(LinkIssueType.INVALID_LINK.name);
            } else if (link.getIssueTypeId() == LinkIssueType.INVALID_IMAGE.value) {
                link.setIssueTypeName(LinkIssueType.INVALID_IMAGE.name);
            } else if (link.getIssueTypeId() == LinkIssueType.LINK_TIME_OUT.value) {
                link.setIssueTypeName(LinkIssueType.LINK_TIME_OUT.name);
            }
        }
        apiPageData.setData(linkAvailabilityList);
        return apiPageData;
    }

    @RequestMapping(value = "/handle", method = RequestMethod.POST)
    public String handIssueById(int siteId, int id) {
        linkAvailabilityService.handIssueById(siteId, id);
        return null;
    }

    @RequestMapping(value = "/handle/batch", method = RequestMethod.POST)
    public String handIssuesByIds(int siteId, Integer[] ids) {
        linkAvailabilityService.handIssuesByIds(siteId, Arrays.asList(ids));
        return null;
    }

    @RequestMapping(value = "/ignore", method = RequestMethod.POST)
    public String ignoreIssueById(int siteId, int id) {
        linkAvailabilityService.ignoreIssueById(siteId, id);
        return null;
    }

    @RequestMapping(value = "/ignore/batch", method = RequestMethod.POST)
    public String ignoreIssuesByIds(int siteId, Integer[] ids) {
        linkAvailabilityService.ignoreIssuesByIds(siteId, Arrays.asList(ids));
        return null;
    }


    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public String delIssueByIds(int siteId, Integer[] ids) {
        linkAvailabilityService.delIssueByIds(siteId, Arrays.asList(ids));
        return null;
    }
}

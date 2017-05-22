package com.trs.gov.kpi.controller;


import com.trs.gov.kpi.constant.LinkIssueType;
import com.trs.gov.kpi.entity.HistoryDate;
import com.trs.gov.kpi.entity.IssueBase;
import com.trs.gov.kpi.entity.LinkAvailability;
import com.trs.gov.kpi.entity.exception.BizException;
import com.trs.gov.kpi.entity.responsedata.ApiPageData;
import com.trs.gov.kpi.entity.responsedata.HistoryStatistics;
import com.trs.gov.kpi.service.LinkAvailabilityService;
import com.trs.gov.kpi.utils.*;
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
 * 链接可用性问题
 */
@RestController
@RequestMapping("/gov/kpi/available/issue")
public class LinkAvailabilityController {

    @Resource
    private LinkAvailabilityService linkAvailabilityService;


    /**
     * 查询待解决和已解决问题数量
     *
     * @param issueBase
     * @return
     */
    @RequestMapping(value = "/bytype/count", method = RequestMethod.GET)
    public List getIssueCount(IssueBase issueBase) {
        if (issueBase.getEndDateTime() != null && !issueBase.getEndDateTime().trim().isEmpty()) {
            issueBase.setEndDateTime(InitEndTime.initTime(issueBase.getEndDateTime()));//结束日期加一
        }
        if (issueBase.getSearchText() == null) {
            issueBase.setSearchText("");
        }
        if (issueBase.getSearchText() == null || issueBase.getSearchText().trim().isEmpty()) {
            List list = new ArrayList();
            Integer exception = 0;
            list.add(exception);
            issueBase.setIds(list);
        }
        return IssueCounter.getIssueCount(linkAvailabilityService, issueBase);
    }


    /**
     * 查询历史记录
     *
     * @param issueBase
     * @return
     */
    @RequestMapping(value = "/all/count/history", method = RequestMethod.GET)
    public List getIssueHistoryCount(@ModelAttribute IssueBase issueBase) {
        if (issueBase.getBeginDateTime() == null || issueBase.getBeginDateTime().trim().isEmpty()) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            issueBase.setBeginDateTime(sdf.format(linkAvailabilityService.getEarliestIssueTime()));
        }
        if (issueBase.getEndDateTime() == null || issueBase.getEndDateTime().trim().isEmpty()) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            issueBase.setEndDateTime(sdf.format(new Date()));
        }
        List<HistoryDate> dateList = DateSplitUtil.getHistoryDateList(issueBase.getBeginDateTime(), issueBase.getEndDateTime());
        List<HistoryStatistics> list = new ArrayList<>();
        for (HistoryDate date : dateList) {
            HistoryStatistics historyStatistics = new HistoryStatistics();
            issueBase.setBeginDateTime(date.getBeginDate());
            issueBase.setEndDateTime(date.getEndDate());
            historyStatistics.setValue(linkAvailabilityService.getIssueHistoryCount(issueBase));
            historyStatistics.setTime(date.getMonth());
            list.add(historyStatistics);
        }
        return list;
    }

    /**
     * 查询未解决问题列表
     *
     * @param pageIndex
     * @param pageSize
     * @param issueBase
     * @return
     * @throws BizException
     */
    @RequestMapping(value = "/unhandled", method = RequestMethod.GET)
    public ApiPageData getIssueList(Integer pageSize, Integer pageIndex, @ModelAttribute IssueBase issueBase) throws BizException {

        if (issueBase.getSiteId() == null) {
            throw new BizException("站点编号为空");
        }
        if (issueBase.getSearchText() != null && !issueBase.getSearchText().trim().isEmpty()) {
            List list = InitQueryFiled.init(issueBase.getSearchText(), linkAvailabilityService);
            if(list.size() == 0 || list == null){
                list.add(0);
            }
            issueBase.setIds(list);
        }
        if (issueBase.getSearchText() == null || issueBase.getSearchText().trim().isEmpty()) {
            List<Integer> list = new ArrayList<>();
            Integer exception = 0;
            list.add(exception);
            issueBase.setIds(list);
        }
        if (issueBase.getSearchText() == null) {
            issueBase.setSearchText("");
        }
        int itemCount = linkAvailabilityService.getUnhandledIssueCount(issueBase);
        ApiPageData apiPageData = PageInfoDeal.buildApiPageData(pageIndex, pageSize, itemCount);
        List<LinkAvailability> linkAvailabilityList = linkAvailabilityService.getIssueList((apiPageData.getPager().getCurrPage() - 1)*apiPageData.getPager().getPageSize(), apiPageData.getPager().getPageSize(), issueBase);
        for (LinkAvailability link : linkAvailabilityList) {
            if (link.getIssueTypeId() == LinkIssueType.INVALID_LINK.value) {
                link.setIssueTypeName(LinkIssueType.INVALID_LINK.name);
            } else if (link.getIssueTypeId() == LinkIssueType.INVALID_IMAGE.value) {
                link.setIssueTypeName(LinkIssueType.INVALID_IMAGE.name);
            } else if (link.getIssueTypeId() == LinkIssueType.CONNECTION_TIME_OUT.value) {
                link.setIssueTypeName(LinkIssueType.CONNECTION_TIME_OUT.name);
            }
        }
        apiPageData.setData(linkAvailabilityList);
        return apiPageData;
    }


    /**
     * 批量处理
     *
     * @param siteId
     * @param ids
     * @return
     */
    @RequestMapping(value = "/handle", method = RequestMethod.POST)
    public String handIssuesByIds(int siteId, Integer[] ids) {
        linkAvailabilityService.handIssuesByIds(siteId, Arrays.asList(ids));
        return null;
    }


    /**
     * 批量忽略
     *
     * @param siteId
     * @param ids
     * @return
     */
    @RequestMapping(value = "/ignore", method = RequestMethod.POST)
    public String ignoreIssuesByIds(int siteId, Integer[] ids) {
        linkAvailabilityService.ignoreIssuesByIds(siteId, Arrays.asList(ids));
        return null;
    }


    /**
     * 批量删除
     *
     * @param siteId
     * @param ids
     * @return
     */
    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    public String delIssueByIds(int siteId, Integer[] ids) {
        linkAvailabilityService.delIssueByIds(siteId, Arrays.asList(ids));
        return null;
    }
}

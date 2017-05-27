package com.trs.gov.kpi.service.impl;

import com.trs.gov.kpi.constant.Status;
import com.trs.gov.kpi.constant.Types;
import com.trs.gov.kpi.dao.IssueMapper;
import com.trs.gov.kpi.dao.LinkAvailabilityMapper;
import com.trs.gov.kpi.entity.*;
import com.trs.gov.kpi.entity.dao.DBPager;
import com.trs.gov.kpi.entity.requestdata.PageDataRequestParam;
import com.trs.gov.kpi.entity.responsedata.*;
import com.trs.gov.kpi.entity.dao.QueryFilter;
import com.trs.gov.kpi.service.LinkAvailabilityService;
import com.trs.gov.kpi.service.helper.LinkAvailabilityServiceHelper;
import com.trs.gov.kpi.utils.DateSplitUtil;
import com.trs.gov.kpi.utils.InitTime;
import com.trs.gov.kpi.utils.PageInfoDeal;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by rw103 on 2017/5/11.
 */
@Service
public class LinkAvailabilityServiceImpl  implements LinkAvailabilityService {

    @Resource
    private LinkAvailabilityMapper linkAvailabilityMapper;

    @Resource
    private IssueMapper issueMapper;

    @Override
    public List<Statistics> getIssueCount(PageDataRequestParam param) {

        param.setBeginDateTime(InitTime.checkBeginDateTime(param.getBeginDateTime(), getEarliestIssueTime()));
        param.setEndDateTime(InitTime.checkEndDateTime(param.getEndDateTime()));

        Statistics handledIssueStatistics = new Statistics();
        handledIssueStatistics.setCount(getHandledIssueCount(param));
        handledIssueStatistics.setType(IssueIndicator.SOLVED.value);
        handledIssueStatistics.setName(IssueIndicator.SOLVED.name);

        Statistics unhandledIssueStatistics = new Statistics();
        unhandledIssueStatistics.setCount(getUnhandledIssueCount(param));
        unhandledIssueStatistics.setType(IssueIndicator.UN_SOLVED.value);
        unhandledIssueStatistics.setName(IssueIndicator.UN_SOLVED.name);

        List<Statistics> list = new ArrayList<>();
        list.add(handledIssueStatistics);
        list.add(unhandledIssueStatistics);

        return list;
    }

    @Override
    public int getHandledIssueCount(PageDataRequestParam param) {

        QueryFilter queryFilter = LinkAvailabilityServiceHelper.toFilter(param);
        queryFilter.addCond("typeId", Types.IssueType.LINK_AVAILABLE_ISSUE.value);
        queryFilter.addCond("isResolved", Arrays.asList(Status.Resolve.IGNORED.value, Status.Resolve.RESOLVED.value));
        queryFilter.addCond("isDel", Status.Delete.UN_DELETE.value);

        return issueMapper.count(queryFilter);
    }

    @Override
    public int getUnhandledIssueCount(PageDataRequestParam param) {

        QueryFilter queryFilter = LinkAvailabilityServiceHelper.toFilter(param);
        queryFilter.addCond("typeId", Types.IssueType.LINK_AVAILABLE_ISSUE.value);
        queryFilter.addCond("isResolved", Status.Resolve.UN_RESOLVED.value);
        queryFilter.addCond("isDel", Status.Delete.UN_DELETE.value);

        return issueMapper.count(queryFilter);
    }

    @Override
    public List<HistoryStatistics> getIssueHistoryCount(PageDataRequestParam param) {

        param.setBeginDateTime(InitTime.checkBeginDateTime(param.getBeginDateTime(), getEarliestIssueTime()));
        param.setEndDateTime(InitTime.checkEndDateTime(param.getEndDateTime()));

        List<HistoryDate> dateList = DateSplitUtil.getHistoryDateList(param.getBeginDateTime(), param.getEndDateTime());
        List<HistoryStatistics> list = new ArrayList<>();
        for (HistoryDate date : dateList) {
            HistoryStatistics historyStatistics = new HistoryStatistics();
            QueryFilter queryFilter = LinkAvailabilityServiceHelper.toFilter(param);
            queryFilter.addCond("typeId", Types.IssueType.LINK_AVAILABLE_ISSUE.value);
            queryFilter.addCond("issueTime", date.getBeginDate()).setRangeBegin(true);
            queryFilter.addCond("issueTime", date.getEndDate()).setRangeEnd(true);
            historyStatistics.setValue(issueMapper.count(queryFilter));
            historyStatistics.setTime(date.getMonth());
            list.add(historyStatistics);
        }

        return list;
    }

    @Override
    public ApiPageData getIssueList(PageDataRequestParam param) {

        param.setBeginDateTime(InitTime.checkBeginDateTime(param.getBeginDateTime(), getEarliestIssueTime()));
        param.setEndDateTime(InitTime.checkEndDateTime(param.getEndDateTime()));

        QueryFilter queryFilter = LinkAvailabilityServiceHelper.toFilter(param);
        queryFilter.addCond("typeId", Types.IssueType.LINK_AVAILABLE_ISSUE.value);
        queryFilter.addCond("isResolved", Status.Resolve.UN_RESOLVED.value);
        queryFilter.addCond("isDel", Status.Delete.UN_DELETE.value);
        int count = issueMapper.count(queryFilter);

        ApiPageData apiPageData = PageInfoDeal.buildApiPageData(param.getPageIndex(), param.getPageSize(), count);

        queryFilter.setPager(new DBPager((apiPageData.getPager().getCurrPage() - 1) * apiPageData.getPager().getPageSize(), apiPageData.getPager().getPageSize()));
        List<LinkAvailability> linkAvailabilitieList = issueMapper.selectLinkAvailability(queryFilter);

        List<LinkAvailabilityResponse> list = new ArrayList<>();
        for (LinkAvailability link : linkAvailabilitieList) {
            LinkAvailabilityResponse linkAvailabilityResponse = new LinkAvailabilityResponse();
            linkAvailabilityResponse.setId(link.getId());
            linkAvailabilityResponse.setIssueTypeName(Types.LinkAvailableIssueType.valueOf(link.getIssueTypeId()).name);
            linkAvailabilityResponse.setInvalidLink(link.getInvalidLink());
            linkAvailabilityResponse.setSnapshot(link.getSnapshot());
            linkAvailabilityResponse.setCheckTime(link.getCheckTime());
            list.add(linkAvailabilityResponse);
        }
        apiPageData.setData(list);

        return apiPageData;
    }

    @Override
    public void insertLinkAvailability(LinkAvailabilityResponse linkAvailabilityResponse) {

        Issue issue = getIssueByLinkAvaliability(linkAvailabilityResponse);
        issueMapper.insert(issue);
    }

    @Override
    public List<LinkAvailabilityResponse> getUnsolvedIssueList(QueryFilter filter) {
        filter.addCond("isResolved", Integer.valueOf(0));
        filter.addCond("isDel", Integer.valueOf(0));
        return linkAvailabilityMapper.getIssueListBySql(filter.getCondFields(), filter.getSortFields(), filter.getPager());
    }

    @Override
    public int getUnsolvedIssueCount(QueryFilter filter) {
        filter.addCond("isResolved", Integer.valueOf(0));
        filter.addCond("isDel", Integer.valueOf(0));
        return linkAvailabilityMapper.getIssueCount(filter.getCondFields());
    }

    private Issue getIssueByLinkAvaliability(LinkAvailabilityResponse linkAvailabilityResponse) {

        Issue issue = new Issue();
        issue.setId(linkAvailabilityResponse.getId() == null ? null : linkAvailabilityResponse.getId());
        issue.setSiteId(linkAvailabilityResponse.getSiteId());
        issue.setTypeId(Types.IssueType.LINK_AVAILABLE_ISSUE.value);
        issue.setSubTypeId(linkAvailabilityResponse.getIssueTypeId());
        issue.setDetail(linkAvailabilityResponse.getInvalidLink());
        issue.setIssueTime(linkAvailabilityResponse.getCheckTime());
        issue.setCustomer1(linkAvailabilityResponse.getSnapshot());
        return issue;
    }

    @Override
    public int getIndexAvailability(PageDataRequestParam param) {

        param.setBeginDateTime(InitTime.checkBeginDateTime(param.getBeginDateTime(), getEarliestIssueTime()));
        param.setEndDateTime(InitTime.checkEndDateTime(param.getEndDateTime()));

        String indexUrl = getIndexUrl(param);

        QueryFilter queryFilter = LinkAvailabilityServiceHelper.toFilter(param);
        queryFilter.addCond("detail",indexUrl);
        queryFilter.addCond("isResolved",Status.Resolve.UN_RESOLVED.value);
        queryFilter.addCond("isDel",Status.Delete.UN_DELETE.value);
        int flag = issueMapper.getIndexAvailability(queryFilter);

        //返回0表示不可用，1表示可用
        if (flag > 0) {
            return 0;
        } else {
            return 1;
        }
    }

    @Override
    public String getIndexUrl(PageDataRequestParam param) {
        return issueMapper.getIndexUrl(param);
    }

    @Override
    public IndexPage showIndexAvailability(PageDataRequestParam param) {

        String indexUrl = getIndexUrl(param);
        IndexPage indexPage = new IndexPage();
        indexPage.setIndexUrl(indexUrl);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        if (getIndexAvailability(param) == 1) {
            indexPage.setIndexAvailable(true);
            indexPage.setMonitorTime(sdf.format(new Date()));
        } else {
            indexPage.setIndexAvailable(false);

            param.setBeginDateTime(InitTime.checkBeginDateTime(param.getBeginDateTime(), getEarliestIssueTime()));
            param.setEndDateTime(InitTime.checkEndDateTime(param.getEndDateTime()));

            QueryFilter queryFilter = LinkAvailabilityServiceHelper.toFilter(param);
            queryFilter.addCond("detail",indexUrl);
            queryFilter.addCond("isResolved",Status.Resolve.UN_RESOLVED.value);
            queryFilter.addCond("isDel",Status.Delete.UN_DELETE.value);
            indexPage.setMonitorTime(sdf.format(issueMapper.getMonitorTime(queryFilter)));
        }
        return indexPage;
    }

    @Override
    public void handIssuesByIds(int siteId, List<Integer> ids) {
        issueMapper.handIssuesByIds(siteId, ids);
    }

    @Override
    public void ignoreIssuesByIds(int siteId, List<Integer> ids) {
        issueMapper.ignoreIssuesByIds(siteId, ids);
    }

    @Override
    public void delIssueByIds(int siteId, List<Integer> ids) {
        issueMapper.delIssueByIds(siteId, ids);
    }

    @Override
    public Date getEarliestIssueTime() {
        return issueMapper.getEarliestIssueTime();
    }
}

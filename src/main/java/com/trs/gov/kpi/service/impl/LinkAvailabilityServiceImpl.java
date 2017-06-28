package com.trs.gov.kpi.service.impl;

import com.trs.gov.kpi.constant.IssueIndicator;
import com.trs.gov.kpi.constant.IssueTableField;
import com.trs.gov.kpi.constant.Status;
import com.trs.gov.kpi.constant.Types;
import com.trs.gov.kpi.dao.IssueMapper;
import com.trs.gov.kpi.entity.HistoryDate;
import com.trs.gov.kpi.entity.Issue;
import com.trs.gov.kpi.entity.LinkAvailability;
import com.trs.gov.kpi.entity.dao.QueryFilter;
import com.trs.gov.kpi.entity.requestdata.PageDataRequestParam;
import com.trs.gov.kpi.entity.responsedata.*;
import com.trs.gov.kpi.service.LinkAvailabilityService;
import com.trs.gov.kpi.service.helper.QueryFilterHelper;
import com.trs.gov.kpi.utils.DBUtil;
import com.trs.gov.kpi.utils.DateUtil;
import com.trs.gov.kpi.utils.PageInfoDeal;
import com.trs.gov.kpi.utils.StringUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by rw103 on 2017/5/11.
 */
@Service
public class LinkAvailabilityServiceImpl implements LinkAvailabilityService {

    @Resource
    private IssueMapper issueMapper;

    @Override
    public List<Statistics> getIssueCount(PageDataRequestParam param) {

        Statistics handledIssueStatistics = new Statistics();
        handledIssueStatistics.setCount(getHandledIssueCount(param));
        handledIssueStatistics.setType(IssueIndicator.SOLVED.value);
        handledIssueStatistics.setName(IssueIndicator.SOLVED.getName());

        Statistics unhandledIssueStatistics = new Statistics();
        unhandledIssueStatistics.setCount(getUnhandledIssueCount(param));
        unhandledIssueStatistics.setType(IssueIndicator.UN_SOLVED.value);
        unhandledIssueStatistics.setName(IssueIndicator.UN_SOLVED.getName());

        List<Statistics> list = new ArrayList<>();
        list.add(handledIssueStatistics);
        list.add(unhandledIssueStatistics);

        return list;
    }

    @Override
    public int getHandledIssueCount(PageDataRequestParam param) {

        QueryFilter queryFilter = QueryFilterHelper.toFilter(param);
        queryFilter.addCond(IssueTableField.TYPE_ID, Types.IssueType.LINK_AVAILABLE_ISSUE.value);
        queryFilter.addCond(IssueTableField.IS_RESOLVED, Arrays.asList(Status.Resolve.IGNORED.value, Status.Resolve.RESOLVED.value));
        queryFilter.addCond(IssueTableField.IS_DEL, Status.Delete.UN_DELETE.value);

        return issueMapper.count(queryFilter);
    }

    @Override
    public int getUnhandledIssueCount(PageDataRequestParam param) {

        QueryFilter queryFilter = QueryFilterHelper.toFilter(param);
        queryFilter.addCond(IssueTableField.TYPE_ID, Types.IssueType.LINK_AVAILABLE_ISSUE.value);
        queryFilter.addCond(IssueTableField.IS_RESOLVED, Status.Resolve.UN_RESOLVED.value);
        queryFilter.addCond(IssueTableField.IS_DEL, Status.Delete.UN_DELETE.value);

        return issueMapper.count(queryFilter);
    }

    @Override
    public History getIssueHistoryCount(PageDataRequestParam param) throws ParseException {

        if (StringUtil.isEmpty(param.getBeginDateTime()) && StringUtil.isEmpty(param.getEndDateTime())) {
            String date = DateUtil.toString(new Date());
            param.setBeginDateTime(DateUtil.getDefaultBeginDate(date, param.getGranularity()));
            param.setEndDateTime(date);
        }

        List<HistoryDate> dateList = DateUtil.splitDate(param.getBeginDateTime(), param.getEndDateTime(), param.getGranularity());
        List<HistoryStatistics> list = new ArrayList<>();
        for (HistoryDate date : dateList) {
            HistoryStatistics historyStatistics = new HistoryStatistics();
            QueryFilter queryFilter = QueryFilterHelper.toFilter(param);
            queryFilter.addCond(IssueTableField.TYPE_ID, Types.IssueType.LINK_AVAILABLE_ISSUE.value);
            queryFilter.addCond(IssueTableField.ISSUE_TIME, date.getBeginDate()).setRangeBegin(true);
            queryFilter.addCond(IssueTableField.ISSUE_TIME, date.getEndDate()).setRangeEnd(true);
            historyStatistics.setValue(issueMapper.count(queryFilter));
            historyStatistics.setTime(date.getDate());
            list.add(historyStatistics);
        }

        return new History(new Date(), list);
    }

    @Override
    public ApiPageData getIssueList(PageDataRequestParam param) {

        if (!StringUtil.isEmpty(param.getSearchText())) {
            param.setSearchText(StringUtil.escape(param.getSearchText()));
        }

        QueryFilter queryFilter = QueryFilterHelper.toFilter(param, Types.IssueType.LINK_AVAILABLE_ISSUE);
        queryFilter.addCond(IssueTableField.TYPE_ID, Types.IssueType.LINK_AVAILABLE_ISSUE.value);
        queryFilter.addCond(IssueTableField.IS_RESOLVED, Status.Resolve.UN_RESOLVED.value);
        queryFilter.addCond(IssueTableField.IS_DEL, Status.Delete.UN_DELETE.value);
        int count = issueMapper.count(queryFilter);

        Pager pager = PageInfoDeal.buildResponsePager(param.getPageIndex(), param.getPageSize(), count);

        queryFilter.setPager(pager);
        List<LinkAvailability> linkAvailabilitieList = issueMapper.selectLinkAvailability(queryFilter);

        return new ApiPageData(pager, toResponseList(linkAvailabilitieList));
    }

    @Override
    public ApiPageData getServiceLinkList(PageDataRequestParam param) {

        QueryFilter queryFilter = QueryFilterHelper.toFilter(param, Types.IssueType.SERVICE_LINK_AVAILABLE);
        queryFilter.addCond(IssueTableField.TYPE_ID, Types.IssueType.SERVICE_LINK_AVAILABLE.value);
        queryFilter.addCond(IssueTableField.IS_RESOLVED, Status.Resolve.UN_RESOLVED.value);
        queryFilter.addCond(IssueTableField.IS_DEL, Status.Delete.UN_DELETE.value);
        int count = issueMapper.count(queryFilter);

        Pager pager = PageInfoDeal.buildResponsePager(param.getPageIndex(), param.getPageSize(), count);

        queryFilter.setPager(pager);
        List<LinkAvailability> linkAvailabilitieList = issueMapper.selectLinkAvailability(queryFilter);

        return new ApiPageData(pager, toResponseList(linkAvailabilitieList));
    }

    private List<LinkAvailabilityResponse> toResponseList(List<LinkAvailability> linkAvailabilitieList) {
        List<LinkAvailabilityResponse> list = new ArrayList<>();
        for (LinkAvailability link : linkAvailabilitieList) {
            LinkAvailabilityResponse linkAvailabilityResponse = new LinkAvailabilityResponse();
            linkAvailabilityResponse.setId(link.getId());
            linkAvailabilityResponse.setIssueTypeName(Types.LinkAvailableIssueType.valueOf(link.getIssueTypeId()).getName());
            linkAvailabilityResponse.setInvalidLink(link.getInvalidLink());
            linkAvailabilityResponse.setSnapshot(link.getSnapshot());
            linkAvailabilityResponse.setCheckTime(link.getCheckTime());
            list.add(linkAvailabilityResponse);
        }
        return list;
    }

    @Override
    public void insertLinkAvailability(LinkAvailabilityResponse linkAvailabilityResponse) {

        Issue issue = getIssueByLinkAvaliability(linkAvailabilityResponse);
        issueMapper.insert(DBUtil.toRow(issue));
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
    public boolean isIndexAvailable(PageDataRequestParam param) {

        String indexUrl = getIndexUrl(param);

        QueryFilter queryFilter = QueryFilterHelper.toFilter(param);
        queryFilter.addCond(IssueTableField.DETAIL, indexUrl);
        queryFilter.addCond(IssueTableField.IS_RESOLVED, Status.Resolve.UN_RESOLVED.value);
        queryFilter.addCond(IssueTableField.IS_DEL, Status.Delete.UN_DELETE.value);
        int flag = issueMapper.getIndexAvailability(queryFilter);

        return flag <= 0;
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
        if (isIndexAvailable(param)) {
            indexPage.setIndexAvailable(true);
            indexPage.setMonitorTime(DateUtil.toString(new Date()));
        } else {
            indexPage.setIndexAvailable(false);

            QueryFilter queryFilter = QueryFilterHelper.toFilter(param);
            queryFilter.addCond(IssueTableField.DETAIL, indexUrl);
            queryFilter.addCond(IssueTableField.IS_RESOLVED, Status.Resolve.UN_RESOLVED.value);
            queryFilter.addCond(IssueTableField.IS_DEL, Status.Delete.UN_DELETE.value);
            indexPage.setMonitorTime(DateUtil.toString(issueMapper.getMonitorTime(queryFilter)));
        }
        return indexPage;
    }

}

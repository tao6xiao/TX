package com.trs.gov.kpi.service.impl;

import com.trs.gov.kpi.constant.*;
import com.trs.gov.kpi.dao.IssueMapper;
import com.trs.gov.kpi.entity.HistoryDate;
import com.trs.gov.kpi.entity.Issue;
import com.trs.gov.kpi.entity.LinkAvailability;
import com.trs.gov.kpi.entity.dao.QueryFilter;
import com.trs.gov.kpi.entity.dao.Table;
import com.trs.gov.kpi.entity.exception.RemoteException;
import com.trs.gov.kpi.entity.requestdata.PageDataRequestParam;
import com.trs.gov.kpi.entity.responsedata.*;
import com.trs.gov.kpi.service.LinkAvailabilityService;
import com.trs.gov.kpi.service.MonitorRecordService;
import com.trs.gov.kpi.service.helper.QueryFilterHelper;
import com.trs.gov.kpi.service.outer.DeptApiService;
import com.trs.gov.kpi.service.outer.SiteApiService;
import com.trs.gov.kpi.utils.DBUtil;
import com.trs.gov.kpi.utils.DateUtil;
import com.trs.gov.kpi.utils.PageInfoDeal;
import com.trs.gov.kpi.utils.StringUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
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

    @Resource
    private DeptApiService deptApiService;

    @Resource
    private MonitorRecordService monitorRecordService;

    @Resource
    private SiteApiService siteApiService;

    @Override
    public List<Statistics> getIssueCount(PageDataRequestParam param) throws RemoteException {

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
    public int getHandledIssueCount(PageDataRequestParam param) throws RemoteException {

        QueryFilter queryFilter = QueryFilterHelper.toFilter(param);
        queryFilter.addCond(IssueTableField.TYPE_ID, Types.IssueType.LINK_AVAILABLE_ISSUE.value);
        queryFilter.addCond(IssueTableField.IS_RESOLVED, Arrays.asList(Status.Resolve.IGNORED.value, Status.Resolve.RESOLVED.value));
        queryFilter.addCond(IssueTableField.IS_DEL, Status.Delete.UN_DELETE.value);

        return issueMapper.count(queryFilter);
    }

    @Override
    public int getUnhandledIssueCount(PageDataRequestParam param) throws RemoteException {

        QueryFilter queryFilter = QueryFilterHelper.toFilter(param);
        queryFilter.addCond(IssueTableField.TYPE_ID, Types.IssueType.LINK_AVAILABLE_ISSUE.value);
        queryFilter.addCond(IssueTableField.IS_RESOLVED, Status.Resolve.UN_RESOLVED.value);
        queryFilter.addCond(IssueTableField.IS_DEL, Status.Delete.UN_DELETE.value);

        return issueMapper.count(queryFilter);
    }

    @Override
    public HistoryStatisticsRes getIssueHistoryCount(PageDataRequestParam param) {
        DateUtil.setDefaultDate(param);

        List<HistoryDate> dateList = DateUtil.splitDate(param.getBeginDateTime(), param.getEndDateTime(), param.getGranularity());
        List<HistoryStatistics> list = new ArrayList<>();
        for (HistoryDate date : dateList) {
            HistoryStatistics historyStatistics = new HistoryStatistics();
            QueryFilter queryFilter = new QueryFilter(Table.ISSUE);
            queryFilter.addCond(IssueTableField.SITE_ID, param.getSiteId());
            queryFilter.addCond(IssueTableField.TYPE_ID, Types.IssueType.LINK_AVAILABLE_ISSUE.value);
            queryFilter.addCond(IssueTableField.ISSUE_TIME, date.getBeginDate()).setRangeBegin(true);
            queryFilter.addCond(IssueTableField.ISSUE_TIME, date.getEndDate()).setRangeEnd(true);
            historyStatistics.setValue(issueMapper.count(queryFilter));
            historyStatistics.setTime(date.getDate());
            list.add(historyStatistics);
        }

        return new HistoryStatisticsRes(monitorRecordService.getMonitorEndTime(param.getSiteId(), Types.MonitorRecordNameType.TASK_CHECK_LINK.value), list);
    }

    @Override
    public ApiPageData getIssueList(PageDataRequestParam param) throws RemoteException {

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

        return new ApiPageData(pager, toResponseList(linkAvailabilitieList, Types.IssueType.LINK_AVAILABLE_ISSUE));
    }

    @Override
    public ApiPageData getServiceLinkList(PageDataRequestParam param) throws RemoteException {

        QueryFilter queryFilter = QueryFilterHelper.toFilter(param, Types.IssueType.SERVICE_LINK_AVAILABLE);
        queryFilter.addCond(IssueTableField.TYPE_ID, Types.IssueType.SERVICE_LINK_AVAILABLE.value);
        queryFilter.addCond(IssueTableField.IS_RESOLVED, Status.Resolve.UN_RESOLVED.value);
        queryFilter.addCond(IssueTableField.IS_DEL, Status.Delete.UN_DELETE.value);
        int count = issueMapper.count(queryFilter);

        Pager pager = PageInfoDeal.buildResponsePager(param.getPageIndex(), param.getPageSize(), count);

        queryFilter.setPager(pager);
        List<LinkAvailability> linkAvailabilitieList = issueMapper.selectLinkAvailability(queryFilter);

        return new ApiPageData(pager, toResponseList(linkAvailabilitieList, Types.IssueType.SERVICE_LINK_AVAILABLE));
    }

    private List<LinkAvailabilityResponse> toResponseList(List<LinkAvailability> linkAvailabilitieList, Types.IssueType issueType) throws RemoteException {
        List<LinkAvailabilityResponse> list = new ArrayList<>();
        for (LinkAvailability link : linkAvailabilitieList) {
            LinkAvailabilityResponse linkAvailabilityResponse = new LinkAvailabilityResponse();
            linkAvailabilityResponse.setId(link.getId());
            if (Types.IssueType.LINK_AVAILABLE_ISSUE.equals(issueType)) {
                linkAvailabilityResponse.setIssueTypeName(Types.LinkAvailableIssueType.valueOf(link.getIssueTypeId()).getName());
            } else {
                linkAvailabilityResponse.setIssueTypeName(Types.ServiceLinkIssueType.valueOf(link.getIssueTypeId()).getName());
            }
            linkAvailabilityResponse.setInvalidLink(link.getInvalidLink());
            linkAvailabilityResponse.setSnapshot(link.getSnapshot());
            linkAvailabilityResponse.setCheckTime(link.getCheckTime());
            if (link.getDeptId() == null) {
                linkAvailabilityResponse.setDeptName(Constants.EMPTY_STRING);
            } else {
                linkAvailabilityResponse.setDeptName(deptApiService.findDeptById("", link.getDeptId()).getGName());
            }
            list.add(linkAvailabilityResponse);
        }
        return list;
    }

    @Override
    public void insertLinkAvailability(LinkAvailabilityResponse linkAvailabilityResponse) {

        Issue issue = getIssueByLinkAvaliability(linkAvailabilityResponse);
        issueMapper.insert(DBUtil.toRow(issue));
    }

    @Override
    public boolean existLinkAvailability(Integer siteId, String invalidLink) {
        QueryFilter filter = new QueryFilter(Table.ISSUE);
        filter.addCond(IssueTableField.TYPE_ID, Types.IssueType.LINK_AVAILABLE_ISSUE.value);
        filter.addCond(IssueTableField.IS_RESOLVED, Status.Resolve.UN_RESOLVED.value);
        filter.addCond(IssueTableField.IS_DEL, Status.Delete.UN_DELETE.value);
        filter.addCond(IssueTableField.SITE_ID, siteId);
        filter.addCond(IssueTableField.DETAIL, invalidLink);
        return issueMapper.count(filter) > 0;
    }

    private Issue getIssueByLinkAvaliability(LinkAvailabilityResponse linkAvailabilityResponse) {

        Issue issue = new Issue();
        issue.setId(linkAvailabilityResponse.getId() == null ? null : linkAvailabilityResponse.getId());
        issue.setSiteId(linkAvailabilityResponse.getSiteId());
        issue.setTypeId(Types.IssueType.LINK_AVAILABLE_ISSUE.value);
        issue.setSubTypeId(linkAvailabilityResponse.getIssueTypeId());
        issue.setDetail(linkAvailabilityResponse.getInvalidLink());
        issue.setIssueTime(linkAvailabilityResponse.getCheckTime());
        issue.setCheckTime(linkAvailabilityResponse.getCheckTime());
        issue.setCustomer1(linkAvailabilityResponse.getSnapshot());
        return issue;
    }

    @Override
    public IndexPage showIndexAvailability(PageDataRequestParam param) throws RemoteException {

        // TODO REVIEW DO_li.hao 需要考虑首页检测任务没有执行完成的情况
        String indexUrl = siteApiService.getSiteById(param.getSiteId(), null).getWebHttp();
        Date endTime = monitorRecordService.getMonitorEndTime(param.getSiteId(), Types.MonitorRecordNameType.TASK_CHECK_HOME_PAGE.value);
        IndexPage indexPage = new IndexPage();
        indexPage.setIndexUrl(indexUrl);
        indexPage.setMonitorTime(DateUtil.toString(endTime));
        QueryFilter queryFilter = new QueryFilter(Table.ISSUE);
        queryFilter.addCond(IssueTableField.SITE_ID, param.getSiteId());
        queryFilter.addCond(IssueTableField.TYPE_ID, Types.IssueType.LINK_AVAILABLE_ISSUE.value);
        queryFilter.addCond(IssueTableField.SUBTYPE_ID, Types.LinkAvailableIssueType.INVALID_HOME_PAGE.value);
        queryFilter.addCond(IssueTableField.DETAIL, indexUrl);
        queryFilter.addCond(IssueTableField.ISSUE_TIME, monitorRecordService.getMonitorStartTime(param.getSiteId(), Types.MonitorRecordNameType.TASK_CHECK_HOME_PAGE.value)).setRangeBegin(true);
        queryFilter.addCond(IssueTableField.ISSUE_TIME, endTime).setRangeEnd(true);
        queryFilter.addCond(IssueTableField.IS_DEL, Status.Delete.UN_DELETE.value);
        queryFilter.addCond(IssueTableField.IS_RESOLVED, Status.Resolve.UN_RESOLVED.value);
        int count = issueMapper.count(queryFilter);
        if (count > 0) {
            indexPage.setIndexAvailable(false);
        } else {
            indexPage.setIndexAvailable(true);
        }
        return indexPage;
    }

}

package com.trs.gov.kpi.service.impl;

import com.trs.gov.kpi.constant.IssueType;
import com.trs.gov.kpi.constant.LinkIssueType;
import com.trs.gov.kpi.constant.LinkType;
import com.trs.gov.kpi.dao.IssueMapper;
import com.trs.gov.kpi.dao.LinkAvailabilityMapper;
import com.trs.gov.kpi.entity.HistoryDate;
import com.trs.gov.kpi.entity.Issue;
import com.trs.gov.kpi.entity.IssueBase;
import com.trs.gov.kpi.entity.LinkAvailability;
import com.trs.gov.kpi.entity.dao.QueryFilter;
import com.trs.gov.kpi.entity.responsedata.HistoryStatistics;
import com.trs.gov.kpi.entity.responsedata.IndexPage;
import com.trs.gov.kpi.entity.responsedata.Statistics;
import com.trs.gov.kpi.service.LinkAvailabilityService;
import com.trs.gov.kpi.utils.DateSplitUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by rw103 on 2017/5/11.
 */
@Service
public class LinkAvailabilityServiceImpl extends OperationServiceImpl implements LinkAvailabilityService {

    @Resource
    private LinkAvailabilityMapper linkAvailabilityMapper;

    @Resource
    private IssueMapper issueMapper;

    @Override
    public int getHandledIssueCount(IssueBase issueBase) {
        return linkAvailabilityMapper.getHandledIssueCount(issueBase);
    }

    @Override
    public int getUnhandledIssueCount(IssueBase issueBase) {
        return linkAvailabilityMapper.getUnhandledIssueCount(issueBase);
    }

    @Override
    public List<HistoryStatistics> getIssueHistoryCount(IssueBase issueBase) {

        List<HistoryDate> dateList = DateSplitUtil.getHistoryDateList(issueBase.getBeginDateTime(), issueBase.getEndDateTime());
        List<HistoryStatistics> list = new ArrayList<>();
        for (HistoryDate date : dateList) {
            HistoryStatistics historyStatistics = new HistoryStatistics();
            issueBase.setBeginDateTime(date.getBeginDate());
            issueBase.setEndDateTime(date.getEndDate());
            historyStatistics.setValue(linkAvailabilityMapper.getIssueHistoryCount(issueBase));
            historyStatistics.setTime(date.getMonth());
            list.add(historyStatistics);
        }

        return list;
    }

    @Override
    public List<LinkAvailability> getIssueList(Integer startIndex, Integer pageSize, IssueBase issueBase) {


        List<LinkAvailability> linkAvailabilityList = linkAvailabilityMapper.getIssueList(startIndex, pageSize, issueBase);
        for (LinkAvailability link : linkAvailabilityList) {
            if (link.getIssueTypeId() == LinkIssueType.INVALID_LINK.value) {
                link.setIssueTypeName(LinkIssueType.INVALID_LINK.name);
            } else if (link.getIssueTypeId() == LinkIssueType.INVALID_IMAGE.value) {
                link.setIssueTypeName(LinkIssueType.INVALID_IMAGE.name);
            } else if (link.getIssueTypeId() == LinkIssueType.CONNECTION_TIME_OUT.value) {
                link.setIssueTypeName(LinkIssueType.CONNECTION_TIME_OUT.name);
            }
        }

        return linkAvailabilityList;
    }

    @Override
    public void insertLinkAvailability(LinkAvailability linkAvailability) {

        Issue issue = getIssueByLinkAvaliability(linkAvailability);
        issueMapper.insert(issue);
    }

    @Override
    public List<LinkAvailability> getUnsolvedIssueList(QueryFilter filter) {
        filter.addCond("isResolved", Integer.valueOf(0));
        filter.addCond("isDel", Integer.valueOf(0));
        return linkAvailabilityMapper.getIssueListBySql(null, filter.getCondFields(), filter.getPager());
    }

    @Override
    public int getUnsolvedIssueCount(QueryFilter filter) {
        filter.addCond("isResolved", Integer.valueOf(0));
        filter.addCond("isDel", Integer.valueOf(0));
        return linkAvailabilityMapper.getIssueCount(filter.getCondFields());
    }

    private Issue getIssueByLinkAvaliability(LinkAvailability linkAvailability) {

        Issue issue = new Issue();
//        issue.setId(linkAvailability.getId() == null? null : Integer.valueOf(linkAvailability.getId()));
        issue.setId(linkAvailability.getId() == null ? null : linkAvailability.getId());
        issue.setSiteId(linkAvailability.getSiteId());
        issue.setTypeId(IssueType.AVAILABLE_ISSUE.getCode());
        issue.setSubTypeId(LinkType.getTypeByName(linkAvailability.getIssueTypeName()).getCode());
        issue.setDetail(linkAvailability.getInvalidLink());
        issue.setIssueTime(linkAvailability.getCheckTime());
        issue.setCustomer1(linkAvailability.getSnapshot());
        return issue;
    }

    @Override
    public List<Statistics> getIssueCountByType(IssueBase issueBase) {

        int invalidLinkCount = linkAvailabilityMapper.getInvalidLinkCount(issueBase);
        Statistics invalidLinkStatistics = new Statistics();
        invalidLinkStatistics.setCount(invalidLinkCount);
        invalidLinkStatistics.setType(LinkIssueType.INVALID_LINK.value);
        invalidLinkStatistics.setName(LinkIssueType.INVALID_LINK.name);

        int invalidImageCount = linkAvailabilityMapper.getInvalidImageCount(issueBase);
        Statistics invalidImageStatistics = new Statistics();
        invalidImageStatistics.setCount(invalidImageCount);
        invalidImageStatistics.setType(LinkIssueType.INVALID_IMAGE.value);
        invalidImageStatistics.setName(LinkIssueType.INVALID_IMAGE.name);

        int connTimeoutCount = linkAvailabilityMapper.getConnTimeoutCount(issueBase);
        Statistics connTimeoutStatistics = new Statistics();
        connTimeoutStatistics.setCount(connTimeoutCount);
        connTimeoutStatistics.setType(LinkIssueType.CONNECTION_TIME_OUT.value);
        connTimeoutStatistics.setName(LinkIssueType.CONNECTION_TIME_OUT.name);

        List<Statistics> list = new ArrayList<>();
        list.add(invalidLinkStatistics);
        list.add(invalidImageStatistics);
        list.add(connTimeoutStatistics);

        return list;
    }

    @Override
    public int getIndexAvailability(String indexUrl, IssueBase issueBase) {
        int flag = linkAvailabilityMapper.getIndexAvailability(indexUrl, issueBase);

        //返回0表示不可用，1表示可用
        if (flag > 0) {
            return 0;
        } else {
            return 1;
        }
    }

    @Override
    public String getIndexUrl(IssueBase issueBase) {
        return linkAvailabilityMapper.getIndexUrl(issueBase);
    }

    @Override
    public IndexPage showIndexAvailability(IssueBase issueBase) {

        String indexUrl = getIndexUrl(issueBase);
        IndexPage indexPage = new IndexPage();
        indexPage.setIndexUrl(indexUrl);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        if (getIndexAvailability(indexUrl, issueBase) == 1) {
            indexPage.setIndexAvailable(true);
            indexPage.setMonitorTime(sdf.format(new Date()));
        } else {
            indexPage.setIndexAvailable(false);
            indexPage.setMonitorTime(sdf.format(linkAvailabilityMapper.getMonitorTime(indexUrl, issueBase)));
        }
        return indexPage;
    }
}

package com.trs.gov.kpi.service.impl;

import com.trs.gov.kpi.constant.IssueType;
import com.trs.gov.kpi.constant.LinkIssueType;
import com.trs.gov.kpi.constant.LinkType;
import com.trs.gov.kpi.dao.IssueMapper;
import com.trs.gov.kpi.dao.LinkAvailabilityMapper;
import com.trs.gov.kpi.entity.Issue;
import com.trs.gov.kpi.entity.IssueBase;
import com.trs.gov.kpi.entity.IssueBase;
import com.trs.gov.kpi.entity.LinkAvailability;
import com.trs.gov.kpi.entity.dao.QueryFilter;
import com.trs.gov.kpi.entity.requestdata.PageDataRequestParam;
import com.trs.gov.kpi.entity.responsedata.Statistics;
import com.trs.gov.kpi.service.LinkAvailabilityService;
import com.trs.gov.kpi.service.helper.LinkAvailabilityServiceHelper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
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
    public int getIssueHistoryCount(IssueBase issueBase) {
        return linkAvailabilityMapper.getIssueHistoryCount(issueBase);
    }

    @Override
    public List<LinkAvailability> getIssueList(Integer currPage, Integer pageSize, IssueBase issueBase) {
        return linkAvailabilityMapper.getIssueList(currPage, pageSize, issueBase);
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
        return linkAvailabilityMapper.getIssueListBySql(filter.getCondFields(), filter.getSortFields(), filter.getPager());
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
    public Date getMonitorTime(String indexUrl, IssueBase issueBase) {
        return linkAvailabilityMapper.getMonitorTime(indexUrl, issueBase);
    }
}

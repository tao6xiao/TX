package com.trs.gov.kpi.service.impl;

import com.trs.gov.kpi.constant.IssueType;
import com.trs.gov.kpi.constant.LinkType;
import com.trs.gov.kpi.dao.IssueMapper;
import com.trs.gov.kpi.dao.LinkAvailabilityMapper;
import com.trs.gov.kpi.entity.Issue;
import com.trs.gov.kpi.entity.LinkAvailability;
import com.trs.gov.kpi.service.LinkAvailabilityService;
import com.trs.gov.kpi.service.OperationService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
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
    public int getHandledIssueCount(int siteId) {
        return linkAvailabilityMapper.getHandledIssueCount(siteId);
    }

    @Override
    public int getUnhandledIssueCount(int siteId) {
        return linkAvailabilityMapper.getUnhandledIssueCount(siteId);
    }

    @Override
    public int getUnhandledIssueCountByTime(int siteId) {
        return linkAvailabilityMapper.getUnhandledIssueCountByTime(siteId);
    }

    @Override
    public List<LinkAvailability> getIssueList(Integer currPage, Integer pageSize, LinkAvailability linkAvailability) {
        return linkAvailabilityMapper.getIssueList(currPage, pageSize, linkAvailability);
    }

    @Override
    public void insertLinkAvailability(LinkAvailability linkAvailability) {

        Issue issue = getIssueByLinkAvaliability(linkAvailability);
        issueMapper.insert(issue);
    }

    private Issue getIssueByLinkAvaliability(LinkAvailability linkAvailability) {

        Issue issue = new Issue();
        issue.setId(linkAvailability.getId() == null? null : Integer.valueOf(linkAvailability.getId()));
        issue.setSiteId(linkAvailability.getSiteId());
        issue.setTypeId(IssueType.AVAILABLE_ISSUE.getCode());
        issue.setSubTypeId(LinkType.getTypeByName(linkAvailability.getIssueTypeName()).getCode());
        issue.setDetail(linkAvailability.getInvalidLink());
        issue.setIssueTime(linkAvailability.getCheckTime());
        issue.setCustomer1(linkAvailability.getSnapshot());
        return issue;
    }


}

package com.trs.gov.kpi.service.impl;

import com.trs.gov.kpi.dao.LinkAvailabilityMapper;
import com.trs.gov.kpi.entity.LinkAvailability;
import com.trs.gov.kpi.service.LinkAvailabilityService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by rw103 on 2017/5/11.
 */
@Service
public class LinkAvailabilityServiceImpl implements LinkAvailabilityService {

    @Resource
    private LinkAvailabilityMapper linkAvailabilityMapper;

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
    public List<LinkAvailability> getIssueList(int currPage, int pageSize, LinkAvailability linkAvailability) {
        return linkAvailabilityMapper.getIssueList(currPage, pageSize, linkAvailability);
    }

    @Override
    public void handIssueById(int siteId, int id) {
        linkAvailabilityMapper.handIssueById(siteId, id);
    }

    @Override
    public void handIssuesByIds(int siteId, List<Integer> ids) {
        linkAvailabilityMapper.handIssuesByIds(siteId, ids);
    }

    @Override
    public void ignoreIssueById(int siteId, int id) {
        linkAvailabilityMapper.ignoreIssueById(siteId, id);
    }

    @Override
    public void ignoreIssuesByIds(int siteId, List<Integer> ids) {
        linkAvailabilityMapper.ignoreIssuesByIds(siteId, ids);
    }

    @Override
    public void delIssueByIds(int siteId, List<Integer> ids) {
        linkAvailabilityMapper.delIssueByIds(siteId, ids);
    }
}

package com.trs.gov.kpi.service.impl;

import com.trs.gov.kpi.dao.LinkAvailabilityMapper;
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


}

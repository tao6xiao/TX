package com.trs.gov.kpi.service;

import com.trs.gov.kpi.entity.LinkAvailability;

import java.util.List;

/**
 * Created by rw103 on 2017/5/11.
 */
public interface LinkAvailabilityService extends OperationService {


    int getIssueHistoryCount(LinkAvailability linkAvailability);

    List<LinkAvailability> getIssueList(Integer currPage, Integer pageSize, LinkAvailability linkAvailability);

}

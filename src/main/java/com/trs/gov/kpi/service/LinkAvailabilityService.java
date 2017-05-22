package com.trs.gov.kpi.service;

import com.trs.gov.kpi.entity.LinkAvailability;
import com.trs.gov.kpi.entity.dao.QueryFilter;
import com.trs.gov.kpi.entity.requestdata.PageDataRequestParam;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by rw103 on 2017/5/11.
 */
public interface LinkAvailabilityService extends OperationService {

    int getIssueHistoryCount(LinkAvailability linkAvailability);

    List<LinkAvailability> getIssueList(Integer currPage, Integer pageSize, LinkAvailability linkAvailability);

    void insertLinkAvailability(LinkAvailability linkAvailability);

    List<LinkAvailability> getUnsolvedIssueList(QueryFilter filter);

    int getUnsolvedIssueCount(QueryFilter filter);
}

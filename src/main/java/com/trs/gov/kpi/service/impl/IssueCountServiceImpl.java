package com.trs.gov.kpi.service.impl;

import com.trs.gov.kpi.constant.*;
import com.trs.gov.kpi.dao.IssueMapper;
import com.trs.gov.kpi.entity.dao.QueryFilter;
import com.trs.gov.kpi.entity.dao.Table;
import com.trs.gov.kpi.entity.responsedata.Statistics;
import com.trs.gov.kpi.service.IssueCountService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by he.lang on 2017/6/7.
 */
@Service
public class IssueCountServiceImpl implements IssueCountService {
    @Resource
    IssueMapper issueMapper;

    @Override
    public List<Statistics> countSort(Integer[] siteIds) {
        List<Statistics> list = new ArrayList<>();
        //待解决预警
        int waringCount = 0;
        for (int i = 0; i < siteIds.length; i++) {
            QueryFilter filter = new QueryFilter(Table.ISSUE);
            filter.addCond(IssueTableField.SITE_ID, siteIds[i]);
            filter.addCond(IssueTableField.IS_DEL, Status.Delete.UN_DELETE.value);
            filter.addCond(IssueTableField.IS_RESOLVED, Status.Resolve.UN_RESOLVED.value);
            filter.addCond(IssueTableField.TYPE_ID, Constants.WANRNING_BEGIN_ID).setRangeBegin(true);
            filter.addCond(IssueTableField.TYPE_ID, Constants.WANRNING_END_ID).setRangeEnd(true);
            int count = issueMapper.count(filter);
            waringCount = waringCount + count;
        }
        Statistics statistics = getByTypeAndCount(IssueIndicator.WARNING, waringCount);
        list.add(statistics);

        //待解决问题
        int issueCount = 0;
        for (int i = 0; i < siteIds.length; i++) {
            QueryFilter filter = new QueryFilter(Table.ISSUE);
            filter.addCond(IssueTableField.SITE_ID, siteIds[i]);
            filter.addCond(IssueTableField.IS_DEL, Status.Delete.UN_DELETE.value);
            filter.addCond(IssueTableField.IS_RESOLVED, Status.Resolve.UN_RESOLVED.value);
            filter.addCond(IssueTableField.TYPE_ID, Constants.ISSUE_BEGIN_ID).setRangeBegin(true);
            filter.addCond(IssueTableField.TYPE_ID, Constants.ISSUE_END_ID).setRangeEnd(true);
            int count = issueMapper.count(filter);
            issueCount = issueCount + count;
        }
        statistics = getByTypeAndCount(IssueIndicator.UN_SOLVED_ISSUE, issueCount);
        list.add(statistics);

        //待解决问题和预警
        statistics = getByTypeAndCount(IssueIndicator.UN_SOLVED_ALL, issueCount+waringCount);
        list.add(statistics);

        //已解决问题和预警
        int resolvedCount = 0;
        for (int i = 0; i < siteIds.length; i++) {
            QueryFilter filter = new QueryFilter(Table.ISSUE);
            filter.addCond(IssueTableField.SITE_ID, siteIds[i]);
            filter.addCond(IssueTableField.IS_DEL, Status.Delete.UN_DELETE.value);
            filter.addCond(IssueTableField.IS_RESOLVED, Status.Resolve.RESOLVED.value);
            int count = issueMapper.count(filter);
            resolvedCount = resolvedCount + count;
        }
        statistics = getByTypeAndCount(IssueIndicator.SOLVED_ALL, resolvedCount);
        list.add(statistics);
        return list;
    }

    private Statistics getByTypeAndCount(IssueIndicator type, int count) {
        Statistics statistics = new Statistics();
        statistics.setType(type.value);
        statistics.setName(type.getName());
        statistics.setCount(count);
        return statistics;
    }
}

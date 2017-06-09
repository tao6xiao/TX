package com.trs.gov.kpi.service.impl;

import com.trs.gov.kpi.constant.*;
import com.trs.gov.kpi.dao.IssueMapper;
import com.trs.gov.kpi.entity.HistoryDate;
import com.trs.gov.kpi.entity.dao.QueryFilter;
import com.trs.gov.kpi.entity.dao.Table;
import com.trs.gov.kpi.entity.requestdata.IssueCountRequest;
import com.trs.gov.kpi.entity.responsedata.HistoryStatistics;
import com.trs.gov.kpi.entity.responsedata.IssueHistoryCountResponse;
import com.trs.gov.kpi.entity.responsedata.Statistics;
import com.trs.gov.kpi.service.InfoErrorService;
import com.trs.gov.kpi.service.IssueCountService;
import com.trs.gov.kpi.service.helper.QueryFilterHelper;
import com.trs.gov.kpi.utils.DateUtil;
import com.trs.gov.kpi.utils.InitTime;
import com.trs.gov.kpi.utils.StringUtil;
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

    @Resource
    InfoErrorService infoErrorService;

    @Override
    public List<Statistics> countSort(IssueCountRequest request) {
        List<Statistics> list = new ArrayList<>();
        Integer[] siteIds = StringUtil.stringToIntegerArray(request.getSiteIds());
        //待解决预警
        int waringCount = 0;
        for (int i = 0; i < siteIds.length; i++) {
            QueryFilter filter = QueryFilterHelper.toFilter(request);
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
            QueryFilter filter = QueryFilterHelper.toFilter(request);
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
        statistics = getByTypeAndCount(IssueIndicator.UN_SOLVED_ALL, issueCount + waringCount);
        list.add(statistics);

        //已解决问题和预警
        int resolvedCount = 0;
        for (int i = 0; i < siteIds.length; i++) {
            QueryFilter filter = QueryFilterHelper.toFilter(request);
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

    @Override
    public List<IssueHistoryCountResponse> historyCountSort(IssueCountRequest request) {
        Integer[] siteIds = StringUtil.stringToIntegerArray(request.getSiteIds());
        request.setBeginDateTime(InitTime.initBeginDateTime(request.getBeginDateTime(), issueMapper.getEarliestIssueTime()));
        request.setEndDateTime(InitTime.initEndDateTime(request.getEndDateTime()));
        List<HistoryDate> dateList = DateUtil.splitDateByMonth(request.getBeginDateTime(), request.getEndDateTime());

        List<IssueHistoryCountResponse> historyResponseList = new ArrayList<>();

        //待解决问题
        IssueHistoryCountResponse historyResponse = buildHistoryResponse(IssueIndicator.UN_SOLVED_ISSUE, dateList, siteIds);
        historyResponseList.add(historyResponse);

        //待解决预警
        historyResponse = buildHistoryResponse(IssueIndicator.WARNING, dateList, siteIds);
        historyResponseList.add(historyResponse);

        //待解决问题和预警
        historyResponse = buildHistoryResponse(IssueIndicator.UN_SOLVED_ALL, dateList, siteIds);
        historyResponseList.add(historyResponse);

        return historyResponseList;
    }

    private IssueHistoryCountResponse buildHistoryResponse(IssueIndicator type, List<HistoryDate> dateList, Integer[] siteIds) {
        IssueHistoryCountResponse historyResponse = new IssueHistoryCountResponse();
        historyResponse.setType(type.value);
        historyResponse.setName(type.getName());
        List<HistoryStatistics> list = new ArrayList<>();
        for (HistoryDate date : dateList) {
            HistoryStatistics historyStatistics = new HistoryStatistics();
            int count = 0;
            for (int i = 0; i < siteIds.length; i++) {
                QueryFilter filter = new QueryFilter(Table.ISSUE);
                filter.addCond(IssueTableField.SITE_ID, siteIds[i]);
                filter.addCond(IssueTableField.ISSUE_TIME, date.getBeginDate()).setRangeBegin(true);
                filter.addCond(IssueTableField.ISSUE_TIME, date.getEndDate()).setRangeEnd(true);
                filter.addCond(IssueTableField.IS_DEL, Status.Delete.UN_DELETE.value);
                filter.addCond(IssueTableField.IS_RESOLVED, Status.Resolve.UN_RESOLVED.value);
                if (type == IssueIndicator.UN_SOLVED_ISSUE) {
                    filter.addCond(IssueTableField.TYPE_ID, Constants.ISSUE_BEGIN_ID).setRangeBegin(true);
                    filter.addCond(IssueTableField.TYPE_ID, Constants.ISSUE_END_ID).setRangeEnd(true);
                } else if (type == IssueIndicator.WARNING) {
                    filter.addCond(IssueTableField.TYPE_ID, Constants.WANRNING_BEGIN_ID).setRangeBegin(true);
                    filter.addCond(IssueTableField.TYPE_ID, Constants.WANRNING_END_ID).setRangeEnd(true);
                }
                count = count + issueMapper.count(filter);
            }
            historyStatistics.setValue(count);
            historyStatistics.setTime(date.getMonth());
            list.add(historyStatistics);
        }
        historyResponse.setData(list);
        return historyResponse;
    }

    private Statistics getByTypeAndCount(IssueIndicator type, int count) {
        Statistics statistics = new Statistics();
        statistics.setType(type.value);
        statistics.setName(type.getName());
        statistics.setCount(count);
        return statistics;
    }
}

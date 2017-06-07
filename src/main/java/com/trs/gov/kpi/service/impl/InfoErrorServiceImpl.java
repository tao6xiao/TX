package com.trs.gov.kpi.service.impl;

import com.trs.gov.kpi.constant.IssueTableField;
import com.trs.gov.kpi.constant.Status;
import com.trs.gov.kpi.constant.Types;
import com.trs.gov.kpi.dao.IssueMapper;
import com.trs.gov.kpi.entity.HistoryDate;
import com.trs.gov.kpi.entity.InfoError;
import com.trs.gov.kpi.constant.IssueIndicator;
import com.trs.gov.kpi.entity.dao.QueryFilter;
import com.trs.gov.kpi.entity.requestdata.PageDataRequestParam;
import com.trs.gov.kpi.entity.responsedata.*;
import com.trs.gov.kpi.service.InfoErrorService;
import com.trs.gov.kpi.service.helper.QueryFilterHelper;
import com.trs.gov.kpi.utils.DateUtil;
import com.trs.gov.kpi.utils.InitTime;
import com.trs.gov.kpi.utils.PageInfoDeal;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by ranwei on 2017/5/15.
 */
@Service
public class InfoErrorServiceImpl implements InfoErrorService {

    @Resource
    private IssueMapper issueMapper;

    @Override
    public List<Statistics> getIssueCount(PageDataRequestParam param) {

        param.setBeginDateTime(InitTime.initBeginDateTime(param.getBeginDateTime(), getEarliestIssueTime()));
        param.setEndDateTime(InitTime.initEndDateTime(param.getEndDateTime()));

        QueryFilter queryFilter = QueryFilterHelper.toFilter(param);
        queryFilter.addCond(IssueTableField.TYPE_ID, Types.IssueType.INFO_ERROR_ISSUE.value);
        queryFilter.addCond(IssueTableField.IS_DEL, Status.Delete.UN_DELETE.value);
        queryFilter.addCond(IssueTableField.IS_RESOLVED, Arrays.asList(Status.Resolve.IGNORED.value, Status.Resolve.RESOLVED.value));
        int handledCount = issueMapper.count(queryFilter);

        Statistics handledIssueStatistics = new Statistics();
        handledIssueStatistics.setCount(handledCount);
        handledIssueStatistics.setType(IssueIndicator.SOLVED.value);
        handledIssueStatistics.setName(IssueIndicator.SOLVED.getName());

        queryFilter = QueryFilterHelper.toFilter(param);
        queryFilter.addCond(IssueTableField.TYPE_ID, Types.IssueType.INFO_ERROR_ISSUE.value);
        queryFilter.addCond(IssueTableField.IS_RESOLVED, Status.Resolve.UN_RESOLVED.value);
        queryFilter.addCond(IssueTableField.IS_DEL, Status.Delete.UN_DELETE.value);
        int unhandledCount = issueMapper.count(queryFilter);

        Statistics unhandledIssueStatistics = new Statistics();
        unhandledIssueStatistics.setCount(unhandledCount);
        unhandledIssueStatistics.setType(IssueIndicator.UN_SOLVED.value);
        unhandledIssueStatistics.setName(IssueIndicator.UN_SOLVED.getName());

        List<Statistics> list = new ArrayList<>();
        list.add(handledIssueStatistics);
        list.add(unhandledIssueStatistics);

        return list;
    }

    @Override
    public List<HistoryStatistics> getIssueHistoryCount(PageDataRequestParam param) {

        param.setBeginDateTime(InitTime.initBeginDateTime(param.getBeginDateTime(), getEarliestIssueTime()));
        param.setEndDateTime(InitTime.initEndDateTime(param.getEndDateTime()));

        List<HistoryDate> dateList = DateUtil.splitDateByMonth(param.getBeginDateTime(), param.getEndDateTime());
        List<HistoryStatistics> list = new ArrayList<>();
        for (HistoryDate date : dateList) {
            HistoryStatistics historyStatistics = new HistoryStatistics();
            QueryFilter queryFilter = QueryFilterHelper.toFilter(param);
            queryFilter.addCond(IssueTableField.TYPE_ID, Types.IssueType.INFO_ERROR_ISSUE.value);
            queryFilter.addCond(IssueTableField.ISSUE_TIME, date.getBeginDate()).setRangeBegin(true);
            queryFilter.addCond(IssueTableField.ISSUE_TIME, date.getEndDate()).setRangeEnd(true);
            historyStatistics.setValue(issueMapper.count(queryFilter));
            historyStatistics.setTime(date.getMonth());
            list.add(historyStatistics);
        }
        return list;
    }

    @Override
    public ApiPageData getIssueList(PageDataRequestParam param) {

        param.setBeginDateTime(InitTime.initBeginDateTime(param.getBeginDateTime(), getEarliestIssueTime()));
        param.setEndDateTime(InitTime.initEndDateTime(param.getEndDateTime()));

        QueryFilter queryFilter = QueryFilterHelper.toFilter(param, Types.IssueType.INFO_ERROR_ISSUE);
        queryFilter.addCond(IssueTableField.TYPE_ID, Types.IssueType.INFO_ERROR_ISSUE.value);
        queryFilter.addCond(IssueTableField.IS_RESOLVED, Status.Resolve.UN_RESOLVED.value);
        queryFilter.addCond(IssueTableField.IS_DEL, Status.Delete.UN_DELETE.value);


        int count = issueMapper.count(queryFilter);
        Pager pager = PageInfoDeal.buildResponsePager(param.getPageIndex(), param.getPageSize(), count);
        queryFilter.setPager(pager);

        List<InfoError> infoErrorList = issueMapper.selectInfoError(queryFilter);
        List<InfoErrorResponse> infoErrorResponses = new ArrayList<>();
        for (InfoError infoError : infoErrorList) {
            InfoErrorResponse infoErrorResponse = new InfoErrorResponse();
            infoErrorResponse.setId(infoError.getId());
            infoErrorResponse.setIssueTypeName(Types.InfoErrorIssueType.valueOf(infoError.getSubTypeId()).getName());
            infoErrorResponse.setSnapshot(infoError.getSnapshot());
            infoErrorResponse.setCheckTime(infoError.getCheckTime());
            if(infoError.getErrorDetail() != null) {
                infoErrorResponse.setErrorDetail(infoError.getErrorDetail());
                infoErrorResponses.add(infoErrorResponse);
            }
        }

        return new ApiPageData(pager, infoErrorResponses);
    }


    @Override
    public void handIssuesByIds(int siteId, List<Integer> ids) {
        issueMapper.handIssuesByIds(siteId, ids);
    }

    @Override
    public void ignoreIssuesByIds(int siteId, List<Integer> ids) {
        issueMapper.ignoreIssuesByIds(siteId, ids);
    }

    @Override
    public void delIssueByIds(int siteId, List<Integer> ids) {
        issueMapper.delIssueByIds(siteId, ids);
    }

    @Override
    public Date getEarliestIssueTime() {
        return issueMapper.getEarliestIssueTime();
    }
}

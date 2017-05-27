package com.trs.gov.kpi.service.impl;

import com.trs.gov.kpi.constant.Status;
import com.trs.gov.kpi.constant.Types;
import com.trs.gov.kpi.dao.IssueMapper;
import com.trs.gov.kpi.entity.HistoryDate;
import com.trs.gov.kpi.entity.InfoError;
import com.trs.gov.kpi.entity.IssueIndicator;
import com.trs.gov.kpi.entity.dao.DBPager;
import com.trs.gov.kpi.entity.dao.QueryFilter;
import com.trs.gov.kpi.entity.requestdata.PageDataRequestParam;
import com.trs.gov.kpi.entity.responsedata.ApiPageData;
import com.trs.gov.kpi.entity.responsedata.HistoryStatistics;
import com.trs.gov.kpi.entity.responsedata.InfoErrorResponse;
import com.trs.gov.kpi.entity.responsedata.Statistics;
import com.trs.gov.kpi.service.InfoErrorService;
import com.trs.gov.kpi.service.helper.LinkAvailabilityServiceHelper;
import com.trs.gov.kpi.utils.DateSplitUtil;
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

        param.setBeginDateTime(InitTime.checkBeginDateTime(param.getBeginDateTime(), getEarliestIssueTime()));
        param.setEndDateTime(InitTime.checkEndDateTime(param.getEndDateTime()));

        Statistics handledIssueStatistics = new Statistics();
        handledIssueStatistics.setCount(getHandledIssueCount(param));
        handledIssueStatistics.setType(IssueIndicator.SOLVED.value);
        handledIssueStatistics.setName(IssueIndicator.SOLVED.name);

        Statistics unhandledIssueStatistics = new Statistics();
        unhandledIssueStatistics.setCount(getUnhandledIssueCount(param));
        unhandledIssueStatistics.setType(IssueIndicator.UN_SOLVED.value);
        unhandledIssueStatistics.setName(IssueIndicator.UN_SOLVED.name);

        List<Statistics> list = new ArrayList<>();
        list.add(handledIssueStatistics);
        list.add(unhandledIssueStatistics);

        return list;
    }

    @Override
    public int getHandledIssueCount(PageDataRequestParam param) {

        QueryFilter queryFilter = LinkAvailabilityServiceHelper.toFilter(param);
        queryFilter.addCond("typeId", Types.IssueType.INFO_ERROR_ISSUE.value);
        queryFilter.addCond("isResolved", Arrays.asList(Status.Resolve.IGNORED.value, Status.Resolve.RESOLVED.value));
        queryFilter.addCond("issueTime", param.getBeginDateTime()).setBeginTime(true);
        queryFilter.addCond("issueTime", param.getEndDateTime()).setEndTime(true);

        return issueMapper.count(queryFilter);
    }

    @Override
    public int getUnhandledIssueCount(PageDataRequestParam param) {

        QueryFilter queryFilter = LinkAvailabilityServiceHelper.toFilter(param);
        queryFilter.addCond("typeId", Types.IssueType.INFO_ERROR_ISSUE.value);
        queryFilter.addCond("isResolved", Status.Resolve.UN_RESOLVED.value);
        queryFilter.addCond("isDel", Status.Delete.UN_DELETE.value);
        queryFilter.addCond("issueTime", param.getBeginDateTime()).setBeginTime(true);
        queryFilter.addCond("issueTime", param.getEndDateTime()).setEndTime(true);

        return issueMapper.count(queryFilter);
    }

    @Override
    public List<HistoryStatistics> getIssueHistoryCount(PageDataRequestParam param) {

        param.setBeginDateTime(InitTime.checkBeginDateTime(param.getBeginDateTime(), getEarliestIssueTime()));
        param.setEndDateTime(InitTime.checkEndDateTime(param.getEndDateTime()));

        List<HistoryDate> dateList = DateSplitUtil.getHistoryDateList(param.getBeginDateTime(), param.getEndDateTime());
        List<HistoryStatistics> list = new ArrayList<>();
        for (HistoryDate date : dateList) {
            HistoryStatistics historyStatistics = new HistoryStatistics();
            QueryFilter queryFilter = LinkAvailabilityServiceHelper.toFilter(param);
            queryFilter.addCond("typeId", Types.IssueType.INFO_ERROR_ISSUE.value);
            queryFilter.addCond("issueTime", date.getBeginDate()).setBeginTime(true);
            queryFilter.addCond("issueTime", date.getEndDate()).setEndTime(true);
            historyStatistics.setValue(issueMapper.count(queryFilter));
            historyStatistics.setTime(date.getMonth());
            list.add(historyStatistics);
        }
        return list;
    }

    @Override
    public ApiPageData getIssueList(PageDataRequestParam param) {

        param.setBeginDateTime(InitTime.checkBeginDateTime(param.getBeginDateTime(), getEarliestIssueTime()));
        param.setEndDateTime(InitTime.checkEndDateTime(param.getEndDateTime()));

        QueryFilter queryFilter = LinkAvailabilityServiceHelper.toFilter(param);
        queryFilter.addCond("typeId", Types.IssueType.INFO_ERROR_ISSUE.value);
        queryFilter.addCond("isResolved", Status.Resolve.UN_RESOLVED.value);
        queryFilter.addCond("isDel", Status.Delete.UN_DELETE.value);
        queryFilter.addCond("issueTime", param.getBeginDateTime()).setBeginTime(true);
        queryFilter.addCond("issueTime", param.getEndDateTime()).setEndTime(true);

        int count = issueMapper.count(queryFilter);
        ApiPageData apiPageData = PageInfoDeal.buildApiPageData(param.getPageIndex(), param.getPageSize(), count);
        queryFilter.setPager(new DBPager((apiPageData.getPager().getCurrPage() - 1) * apiPageData.getPager().getPageSize(), apiPageData.getPager().getPageSize()));

        List<InfoError> infoErrorList = issueMapper.selectInfoError(queryFilter);
        List<InfoErrorResponse> infoErrorResponses = new ArrayList<>();
        for (InfoError infoError : infoErrorList) {
            InfoErrorResponse infoErrorResponse = new InfoErrorResponse();
            infoErrorResponse.setId(infoError.getId());
            infoErrorResponse.setIssueTypeName(Types.InfoErrorIssueType.valueOf(infoError.getSubTypeId()).name);
            infoErrorResponse.setSnapshot(infoError.getSnapshot());
            infoErrorResponse.setCheckTime(infoError.getCheckTime());
            infoErrorResponses.add(infoErrorResponse);
        }
        apiPageData.setData(infoErrorResponses);

        return apiPageData;
    }

    @Override
    public List<Statistics> getIssueCountByType(PageDataRequestParam param) {

        param.setBeginDateTime(InitTime.checkBeginDateTime(param.getBeginDateTime(), getEarliestIssueTime()));
        param.setEndDateTime(InitTime.checkEndDateTime(param.getEndDateTime()));

        QueryFilter queryFilter = LinkAvailabilityServiceHelper.toFilter(param);
        queryFilter.addCond("subTypeId", Types.InfoErrorIssueType.TYPOS.value);
        queryFilter.addCond("isResolved", Status.Resolve.UN_RESOLVED.value);
        queryFilter.addCond("isDel", Status.Delete.UN_DELETE.value);
        queryFilter.addCond("issueTime", param.getBeginDateTime()).setBeginTime(true);
        queryFilter.addCond("issueTime", param.getEndDateTime()).setEndTime(true);

        int typosCount = issueMapper.count(queryFilter);
        Statistics typosStatistics = new Statistics();
        typosStatistics.setCount(typosCount);
        typosStatistics.setType(Types.InfoErrorIssueType.TYPOS.value);
        typosStatistics.setName(Types.InfoErrorIssueType.TYPOS.name);

        queryFilter = LinkAvailabilityServiceHelper.toFilter(param);
        queryFilter.addCond("subTypeId", Types.InfoErrorIssueType.SENSITIVE_WORDS.value);
        queryFilter.addCond("isResolved", Status.Resolve.UN_RESOLVED.value);
        queryFilter.addCond("isDel", Status.Delete.UN_DELETE.value);
        queryFilter.addCond("issueTime", param.getBeginDateTime()).setBeginTime(true);
        queryFilter.addCond("issueTime", param.getEndDateTime()).setEndTime(true);

        int sensitiveWordsCount = issueMapper.count(queryFilter);
        Statistics sensitiveWordsStatistics = new Statistics();
        sensitiveWordsStatistics.setCount(sensitiveWordsCount);
        sensitiveWordsStatistics.setType(Types.InfoErrorIssueType.SENSITIVE_WORDS.value);
        sensitiveWordsStatistics.setName(Types.InfoErrorIssueType.SENSITIVE_WORDS.name);

        List<Statistics> list = new ArrayList<>();
        list.add(typosStatistics);
        list.add(sensitiveWordsStatistics);

        return list;
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

package com.trs.gov.kpi.service.impl;

import com.trs.gov.kpi.constant.IssueIndicator;
import com.trs.gov.kpi.constant.IssueTableField;
import com.trs.gov.kpi.constant.Status;
import com.trs.gov.kpi.constant.Types;
import com.trs.gov.kpi.dao.IssueMapper;
import com.trs.gov.kpi.entity.HistoryDate;
import com.trs.gov.kpi.entity.InfoError;
import com.trs.gov.kpi.entity.InfoErrorOrder;
import com.trs.gov.kpi.entity.dao.QueryFilter;
import com.trs.gov.kpi.entity.exception.RemoteException;
import com.trs.gov.kpi.entity.outerapi.Channel;
import com.trs.gov.kpi.entity.requestdata.PageDataRequestParam;
import com.trs.gov.kpi.entity.requestdata.WorkOrderRequest;
import com.trs.gov.kpi.entity.responsedata.*;
import com.trs.gov.kpi.service.InfoErrorService;
import com.trs.gov.kpi.service.helper.QueryFilterHelper;
import com.trs.gov.kpi.service.outer.SiteApiService;
import com.trs.gov.kpi.utils.DateUtil;
import com.trs.gov.kpi.utils.InitTime;
import com.trs.gov.kpi.utils.PageInfoDeal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by ranwei on 2017/5/15.
 */
@Slf4j
@Service
public class InfoErrorServiceImpl implements InfoErrorService {

    @Resource
    private IssueMapper issueMapper;

    @Resource
    private SiteApiService siteApiService;

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
            if (infoError.getErrorDetail() != null) {
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

    @Override
    public ApiPageData selectInfoErrorOrder(WorkOrderRequest request) throws RemoteException {

        QueryFilter filter = QueryFilterHelper.toFilter(request);
        filter.addCond(IssueTableField.TYPE_ID, Types.IssueType.INFO_ERROR_ISSUE.value);
        filter.addCond(IssueTableField.WORK_ORDER_STATUS, request.getWorkOrderStatus());
        filter.addCond(IssueTableField.IS_RESOLVED, request.getSolveStatus());
        filter.addCond(IssueTableField.IS_DEL, Status.Delete.UN_DELETE.value);

        int itemCount = issueMapper.count(filter);
        Pager pager = PageInfoDeal.buildResponsePager(request.getPageIndex(), request.getPageSize(), itemCount);
        filter.setPager(pager);

        List<InfoErrorOrder> infoErrorOrderList = issueMapper.selectInfoErrorOrder(filter);
        List<InfoErrorOrderRes> list = new ArrayList<>();
        for (InfoErrorOrder infoErrorOrder : infoErrorOrderList) {
            InfoErrorOrderRes infoErrorOrderRes = new InfoErrorOrderRes();
            infoErrorOrderRes.setId(infoErrorOrder.getId());
            infoErrorOrderRes.setChnlName(getChannelName(infoErrorOrder.getChnlId()));
            infoErrorOrderRes.setSiteName(siteApiService.getSiteById(infoErrorOrder.getSiteId(), null).getSiteDesc());
            infoErrorOrderRes.setIssueTypeName(Types.InfoErrorIssueType.valueOf(infoErrorOrder.getSubTypeId()).getName());
//            infoErrorOrderRes.setDepartment();TODO
            infoErrorOrderRes.setUrl(infoErrorOrder.getDetail());
            infoErrorOrderRes.setCheckTime(infoErrorOrder.getIssueTime());
            infoErrorOrderRes.setSolveStatus(infoErrorOrder.getIsResolved());
            infoErrorOrderRes.setIsDeleted(infoErrorOrder.getIsDel());
            infoErrorOrderRes.setWorkOrderStatus(infoErrorOrder.getWorkOrderStatus());
            list.add(infoErrorOrderRes);
        }

        return new ApiPageData(pager, list);
    }

    @Override
    public InfoErrorOrderRes getInfoErrorOrderById(WorkOrderRequest request) throws RemoteException {

        QueryFilter filter = QueryFilterHelper.toFilter(request);
        filter.addCond(IssueTableField.ID, request.getId());

        List<InfoErrorOrder> infoErrorOrderList = issueMapper.selectInfoErrorOrder(filter);
        InfoErrorOrderRes infoErrorOrderRes = new InfoErrorOrderRes();
        for (InfoErrorOrder infoErrorOrder : infoErrorOrderList) {
            infoErrorOrderRes.setId(infoErrorOrder.getId());
            infoErrorOrderRes.setChnlName(getChannelName(infoErrorOrder.getChnlId()));
            infoErrorOrderRes.setSiteName(siteApiService.getSiteById(infoErrorOrder.getSiteId(), null).getSiteDesc());
            infoErrorOrderRes.setIssueTypeName(Types.InfoErrorIssueType.valueOf(infoErrorOrder.getSubTypeId()).getName());
//            infoErrorOrderRes.setDepartment();TODO
            infoErrorOrderRes.setUrl(infoErrorOrder.getDetail());
            infoErrorOrderRes.setCheckTime(infoErrorOrder.getIssueTime());
            infoErrorOrderRes.setSolveStatus(infoErrorOrder.getIsResolved());
            infoErrorOrderRes.setIsDeleted(infoErrorOrder.getIsDel());
            infoErrorOrderRes.setWorkOrderStatus(infoErrorOrder.getWorkOrderStatus());
        }

        return infoErrorOrderRes;
    }

    private String getChannelName(Integer channelId) {
        if (channelId == null) {
            return "";
        }
        Channel channel = null;
        try {
            channel = siteApiService.getChannelById(channelId, null);
        } catch (RemoteException e) {
            log.error("", e);
        }
        return checkChannelName(channel);
    }

    private String checkChannelName(Channel channel) {
        if (channel == null || channel.getChnlName() == null) {
            return "";
        }
        return channel.getChnlName();
    }
}

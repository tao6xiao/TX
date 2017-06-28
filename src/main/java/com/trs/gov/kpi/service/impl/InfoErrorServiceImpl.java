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
import com.trs.gov.kpi.entity.outerapi.Site;
import com.trs.gov.kpi.entity.requestdata.PageDataRequestParam;
import com.trs.gov.kpi.entity.requestdata.WorkOrderRequest;
import com.trs.gov.kpi.entity.responsedata.*;
import com.trs.gov.kpi.service.InfoErrorService;
import com.trs.gov.kpi.service.helper.QueryFilterHelper;
import com.trs.gov.kpi.service.outer.SiteApiService;
import com.trs.gov.kpi.utils.ChnlCheckUtil;
import com.trs.gov.kpi.utils.DateUtil;
import com.trs.gov.kpi.utils.PageInfoDeal;
import com.trs.gov.kpi.utils.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.ParseException;
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
    public History getIssueHistoryCount(PageDataRequestParam param) throws ParseException {
        if (StringUtil.isEmpty(param.getBeginDateTime()) && StringUtil.isEmpty(param.getEndDateTime())) {
            String date = DateUtil.toString(new Date());
            param.setBeginDateTime(DateUtil.getDefaultBeginDate(date, param.getGranularity()));
            param.setEndDateTime(date);
        }
        List<HistoryDate> dateList = DateUtil.splitDate(param.getBeginDateTime(), param.getEndDateTime(), param.getGranularity());
        List<HistoryStatistics> list = new ArrayList<>();
        for (HistoryDate date : dateList) {
            HistoryStatistics historyStatistics = new HistoryStatistics();
            QueryFilter queryFilter = QueryFilterHelper.toFilter(param);
            queryFilter.addCond(IssueTableField.TYPE_ID, Types.IssueType.INFO_ERROR_ISSUE.value);
            queryFilter.addCond(IssueTableField.ISSUE_TIME, date.getBeginDate()).setRangeBegin(true);
            queryFilter.addCond(IssueTableField.ISSUE_TIME, date.getEndDate()).setRangeEnd(true);
            historyStatistics.setValue(issueMapper.count(queryFilter));
            historyStatistics.setTime(date.getDate());
            list.add(historyStatistics);
        }
        return new History(new Date(), list);
    }

    @Override
    public ApiPageData getInfoErrorList(PageDataRequestParam param) {

        if (!StringUtil.isEmpty(param.getSearchText())) {
            param.setSearchText(StringUtil.escape(param.getSearchText()));
        }

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
    public ApiPageData selectInfoErrorOrder(WorkOrderRequest request) throws RemoteException {

        QueryFilter filter = QueryFilterHelper.toFilter(request, siteApiService);
        filter.addCond(IssueTableField.TYPE_ID, Types.IssueType.INFO_ERROR_ISSUE.value);
        filter.addCond(IssueTableField.WORK_ORDER_STATUS, request.getWorkOrderStatus());
        filter.addCond(IssueTableField.IS_RESOLVED, request.getSolveStatus());
        filter.addCond(IssueTableField.IS_DEL, Status.Delete.UN_DELETE.value);

        int itemCount = issueMapper.count(filter);
        Pager pager = PageInfoDeal.buildResponsePager(request.getPageIndex(), request.getPageSize(), itemCount);
        filter.setPager(pager);

        List<InfoErrorOrder> infoErrorOrderList = issueMapper.selectInfoErrorOrder(filter);
        List<InfoErrorOrderRes> list = toOrderResponse(infoErrorOrderList);

        return new ApiPageData(pager, list);
    }

    @Override
    public InfoErrorOrderRes getInfoErrorOrderById(WorkOrderRequest request) throws RemoteException {

        QueryFilter filter = QueryFilterHelper.toFilter(request, siteApiService);
        filter.addCond(IssueTableField.ID, request.getId());

        List<InfoErrorOrder> infoErrorOrderList = issueMapper.selectInfoErrorOrder(filter);
        List<InfoErrorOrderRes> list = toOrderResponse(infoErrorOrderList);

        if (list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }


    private List<InfoErrorOrderRes> toOrderResponse(List<InfoErrorOrder> infoErrorOrderList) {
        List<InfoErrorOrderRes> list = new ArrayList<>();
        for (InfoErrorOrder infoErrorOrder : infoErrorOrderList) {
            InfoErrorOrderRes infoErrorOrderRes = new InfoErrorOrderRes();
            infoErrorOrderRes.setId(infoErrorOrder.getId());
            infoErrorOrderRes.setChnlName(ChnlCheckUtil.getChannelName(infoErrorOrder.getChnlId(), siteApiService));

            try {
                final Site site = siteApiService.getSiteById(infoErrorOrder.getSiteId(), null);
                if (site != null) {
                    infoErrorOrderRes.setSiteName(site.getSiteName());
                }
            } catch (RemoteException e) {
                log.error("", e);
                infoErrorOrderRes.setSiteName("站点[id=" + infoErrorOrder.getSiteId() + "]");
            }

            infoErrorOrderRes.setParentTypeName(Types.IssueType.valueOf(infoErrorOrder.getTypeId()).getName());
            infoErrorOrderRes.setIssueTypeName(Types.InfoErrorIssueType.valueOf(infoErrorOrder.getSubTypeId()).getName());
//            infoErrorOrderRes.setDepartment();TODO
            infoErrorOrderRes.setUrl(infoErrorOrder.getDetail());
            infoErrorOrderRes.setCheckTime(infoErrorOrder.getIssueTime());
            infoErrorOrderRes.setSolveStatus(infoErrorOrder.getIsResolved());
            infoErrorOrderRes.setIsDeleted(infoErrorOrder.getIsDel());
            infoErrorOrderRes.setWorkOrderStatus(infoErrorOrder.getWorkOrderStatus());
            list.add(infoErrorOrderRes);
        }
        return list;
    }
}

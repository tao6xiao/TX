package com.trs.gov.kpi.service.impl;

import com.trs.gov.kpi.constant.*;
import com.trs.gov.kpi.dao.ChnlGroupMapper;
import com.trs.gov.kpi.dao.IssueMapper;
import com.trs.gov.kpi.entity.HistoryDate;
import com.trs.gov.kpi.entity.InfoUpdate;
import com.trs.gov.kpi.entity.InfoUpdateOrder;
import com.trs.gov.kpi.entity.dao.QueryFilter;
import com.trs.gov.kpi.entity.dao.Table;
import com.trs.gov.kpi.entity.exception.RemoteException;
import com.trs.gov.kpi.entity.outerapi.Channel;
import com.trs.gov.kpi.entity.requestdata.PageDataRequestParam;
import com.trs.gov.kpi.entity.requestdata.WorkOrderRequest;
import com.trs.gov.kpi.entity.responsedata.*;
import com.trs.gov.kpi.service.InfoUpdateService;
import com.trs.gov.kpi.service.helper.QueryFilterHelper;
import com.trs.gov.kpi.service.outer.SiteApiService;
import com.trs.gov.kpi.service.outer.SiteChannelServiceHelper;
import com.trs.gov.kpi.utils.ChnlCheckUtil;
import com.trs.gov.kpi.utils.DateUtil;
import com.trs.gov.kpi.utils.PageInfoDeal;
import com.trs.gov.kpi.utils.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.ParseException;
import java.util.*;

import static com.trs.gov.kpi.constant.Constants.WARNING_BEGIN_ID;

/**
 * Created by rw103 on 2017/5/13.
 */
@Slf4j
@Service
public class InfoUpdateServiceImpl implements InfoUpdateService {

    @Resource
    private SiteApiService siteApiService;

    @Resource
    private SiteChannelServiceHelper siteChannelServiceHelper;

    @Resource
    private IssueMapper issueMapper;

    @Resource
    private ChnlGroupMapper chnlGroupMapper;

    @Override
    public List<Statistics> getIssueCount(PageDataRequestParam param) {
        QueryFilter filter = QueryFilterHelper.toFilter(param);
        filter.addCond(IssueTableField.TYPE_ID, Arrays.asList(Types.IssueType.INFO_UPDATE_ISSUE.value, Types.IssueType.INFO_UPDATE_WARNING.value));
        filter.addCond(IssueTableField.IS_RESOLVED, Arrays.asList(Status.Resolve.IGNORED.value, Status.Resolve.RESOLVED.value));
        filter.addCond(IssueTableField.IS_DEL, Status.Delete.UN_DELETE.value);
        int resolvedCount = issueMapper.count(filter);


        filter = QueryFilterHelper.toFilter(param);
        filter.addCond(IssueTableField.TYPE_ID, Types.IssueType.INFO_UPDATE_ISSUE.value);
        filter.addCond(IssueTableField.IS_DEL, Status.Delete.UN_DELETE.value);
        filter.addCond(IssueTableField.IS_RESOLVED, Status.Resolve.UN_RESOLVED.value);
        int updateCount = issueMapper.count(filter);

        filter = QueryFilterHelper.toFilter(param);
        filter.addCond(IssueTableField.TYPE_ID, Types.IssueType.INFO_UPDATE_WARNING.value);
        filter.addCond(IssueTableField.IS_DEL, Status.Delete.UN_DELETE.value);
        filter.addCond(IssueTableField.IS_RESOLVED, Status.Resolve.UN_RESOLVED.value);
        int warningCount = issueMapper.count(filter);

        List<Statistics> statisticsList = new ArrayList<>();


        Statistics statistics = new Statistics();
        statistics.setType(IssueIndicator.SOLVED_ALL.value);
        statistics.setName(IssueIndicator.SOLVED_ALL.getName());
        statistics.setCount(resolvedCount);
        statisticsList.add(statistics);

        statistics = new Statistics();
        statistics.setType(IssueIndicator.UN_SOLVED_ISSUE.value);
        statistics.setName(IssueIndicator.UN_SOLVED_ISSUE.getName());
        statistics.setCount(updateCount);
        statisticsList.add(statistics);

        statistics = new Statistics();
        statistics.setType(IssueIndicator.WARNING.value);
        statistics.setName(IssueIndicator.WARNING.getName());
        statistics.setCount(warningCount);
        statisticsList.add(statistics);

        return statisticsList;
    }

    @Override
    public History getIssueHistoryCount(PageDataRequestParam param) {
        if (StringUtil.isEmpty(param.getBeginDateTime())) {
            param.setBeginDateTime(DateUtil.toString(issueMapper.getEarliestIssueTime()));
        }
        if (StringUtil.isEmpty(param.getEndDateTime())) {
            param.setEndDateTime(DateUtil.toString(new Date()));
        }
        List<HistoryDate> dateList = DateUtil.splitDateByMonth(param.getBeginDateTime(), param.getEndDateTime());
        List<HistoryStatistics> list = new ArrayList<>();
        for (HistoryDate date : dateList) {
            HistoryStatistics historyStatistics = new HistoryStatistics();
            QueryFilter filter = QueryFilterHelper.toFilter(param);
            filter.addCond(IssueTableField.TYPE_ID, Types.IssueType.INFO_UPDATE_ISSUE.value);
            filter.addCond(IssueTableField.ISSUE_TIME, date.getBeginDate()).setRangeBegin(true);
            filter.addCond(IssueTableField.ISSUE_TIME, date.getEndDate()).setRangeEnd(true);

            historyStatistics.setValue(issueMapper.count(filter));
            historyStatistics.setTime(date.getMonth());
            list.add(historyStatistics);
        }
        return new History(new Date(), list);
    }

    /**
     * 获取栏目信息更新不及时问题的统计信息
     *
     * @param param
     * @return
     * @throws ParseException
     * @throws RemoteException
     */
    @Override
    public List<Statistics> getUpdateNotInTimeCountList(PageDataRequestParam param) throws ParseException, RemoteException {
        int count = 0;
        List<Statistics> statisticsList = new ArrayList<>();

        QueryFilter filter = QueryFilterHelper.toFilter(param);
        filter.addCond(IssueTableField.TYPE_ID, Types.IssueType.INFO_UPDATE_ISSUE.value);
        filter.addCond(IssueTableField.SUBTYPE_ID, Types.InfoUpdateIssueType.UPDATE_NOT_INTIME.value);
        filter.addCond(IssueTableField.IS_DEL, Status.Delete.UN_DELETE.value);
        filter.addCond(IssueTableField.IS_RESOLVED, Status.Resolve.UN_RESOLVED.value);
        filter.addGroupField(IssueTableField.CUSTOMER2);
        //获取所有
        List<Map<Integer, Integer>> countList = issueMapper.countList(filter);
        for (Map map : countList) {
            Set<Integer> keySet = map.keySet();
            if (keySet.toArray()[0] == null) {
                continue;
            }
            count++;
        }
        Statistics statistics = getStatisticsByCount(EnumIndexUpdateType.ALL.getCode(), count);
        statisticsList.add(statistics);

        //获取A类
        Set<Integer> childChnlIds = null;
        List<Integer> chnlIds = chnlGroupMapper.selectAChnlIds(param.getSiteId(), EnumChannelGroup.COMMON.getId());
        if (!chnlIds.isEmpty()) {
            childChnlIds = getAllChildChnlIds(chnlIds, param);
        }
        filter = QueryFilterHelper.toFilter(param);
        filter.addCond(IssueTableField.TYPE_ID, Types.IssueType.INFO_UPDATE_ISSUE.value);
        filter.addCond(IssueTableField.SUBTYPE_ID, Types.InfoUpdateIssueType.UPDATE_NOT_INTIME.value);
        filter.addCond(IssueTableField.IS_DEL, Status.Delete.UN_DELETE.value);
        filter.addCond(IssueTableField.IS_RESOLVED, Status.Resolve.UN_RESOLVED.value);
        filter.addCond(IssueTableField.CUSTOMER2, childChnlIds);
        filter.addGroupField(IssueTableField.CUSTOMER2);
        countList = issueMapper.countList(filter);
        count = 0;
        for (Map map : countList) {
            Set<Integer> keySet = map.keySet();
            if (keySet.toArray()[0] == null) {
                continue;
            }
            count++;
        }
        statistics = getStatisticsByCount(EnumIndexUpdateType.A_TYPE.getCode(), count);
        statisticsList.add(statistics);

        //获取首页
        Set<Integer> childChnlIds2 = null;
        List<Channel> channelList = siteApiService.getChildChannel(param.getSiteId(), 0, null);
        if (!channelList.isEmpty()) {
            childChnlIds2 = new HashSet<>();
            for (Channel channel : channelList) {
                childChnlIds2.add(channel.getChannelId());
            }
        }
        filter = QueryFilterHelper.toFilter(param);
        filter.addCond(IssueTableField.TYPE_ID, Types.IssueType.INFO_UPDATE_ISSUE.value);
        filter.addCond(IssueTableField.SUBTYPE_ID, Types.InfoUpdateIssueType.UPDATE_NOT_INTIME.value);
        filter.addCond(IssueTableField.IS_DEL, Status.Delete.UN_DELETE.value);
        filter.addCond(IssueTableField.IS_RESOLVED, Status.Resolve.UN_RESOLVED.value);
        filter.addCond(IssueTableField.CUSTOMER2, childChnlIds2);
        filter.addGroupField(IssueTableField.CUSTOMER2);
        countList = issueMapper.countList(filter);
        count = 0;
        for (Map map : countList) {
            Set<Integer> keySet = map.keySet();
            if (keySet.toArray()[0] == null) {
                continue;
            }
            count++;
        }
        statistics = getStatisticsByCount(EnumIndexUpdateType.HOMEPAGE.getCode(), count);
        statisticsList.add(statistics);

        //获取空白栏目
        count = siteChannelServiceHelper.getEmptyChannel(param.getSiteId()).size();
        statistics = getStatisticsByCount(EnumIndexUpdateType.NULL_CHANNEL.getCode(), count);
        statisticsList.add(statistics);

        return statisticsList;
    }

    private Set<Integer> getAllChildChnlIds(List<Integer> chnlIds, PageDataRequestParam param) throws RemoteException {
        Set<Integer> childChnlIds = new HashSet<>();
        for (Integer chnlId : chnlIds) {
            if (chnlId != null) {
                childChnlIds.add(chnlId);
                childChnlIds = siteApiService.getAllChildChnlIds(null, param.getSiteId(), chnlId, childChnlIds);
            }
        }
        return childChnlIds;
    }

    @Override
    public ApiPageData get(PageDataRequestParam param) {
        QueryFilter filter = QueryFilterHelper.toFilter(param, Types.IssueType.INFO_UPDATE_ISSUE, Types.IssueType.INFO_UPDATE_WARNING);
        filter.addCond(IssueTableField.TYPE_ID, Arrays.asList(Types.IssueType.INFO_UPDATE_ISSUE.value, Types.IssueType.INFO_UPDATE_WARNING.value));
        filter.addCond(IssueTableField.IS_DEL, Status.Delete.UN_DELETE.value);
        filter.addCond(IssueTableField.IS_RESOLVED, Status.Resolve.UN_RESOLVED.value);
        int itemCount = issueMapper.count(filter);
        Pager pager = PageInfoDeal.buildResponsePager(param.getPageIndex(), param.getPageSize(), itemCount);
        filter.setPager(pager);
        List<InfoUpdate> infoUpdateList = issueMapper.selectInfoUpdate(filter);
        List<InfoUpdateResponse> infoUpdateResponseList = toResponse(infoUpdateList);

        return new ApiPageData(pager, infoUpdateResponseList);
    }

    private List<InfoUpdateResponse> toResponse(List<InfoUpdate> infoUpdateList) {
        List<InfoUpdateResponse> responseList = new ArrayList<>();
        if (infoUpdateList == null) {
            return responseList;
        }
        InfoUpdateResponse infoUpdateResponse = null;
        for (InfoUpdate infoUpdate : infoUpdateList) {
            infoUpdateResponse = new InfoUpdateResponse();
            infoUpdateResponse.setChnlName(ChnlCheckUtil.getChannelName(infoUpdate.getChnlId(), siteApiService));
            infoUpdateResponse.setId(infoUpdate.getId());
            infoUpdateResponse.setChnlUrl(infoUpdate.getChnlUrl());
            infoUpdateResponse.setCheckTime(infoUpdate.getCheckTime());
            if (infoUpdate.getSubTypeId() < WARNING_BEGIN_ID) {
                infoUpdateResponse.setIssueTypeName(Types.InfoUpdateIssueType.valueOf(infoUpdate.getSubTypeId()).getName());
            } else {
                infoUpdateResponse.setIssueTypeName(Types.InfoUpdateWarningType.valueOf(infoUpdate.getSubTypeId()).getName());
            }

            responseList.add(infoUpdateResponse);
        }
        return responseList;
    }

    private Statistics getStatisticsByCount(int i, int count) {
        Statistics statistics = new Statistics();
        statistics.setCount(count);
        statistics.setType(i);
        statistics.setName(EnumIndexUpdateType.valueOf(i).getName());
        return statistics;
    }

    @Override
    public ApiPageData selectInfoUpdateOrder(WorkOrderRequest request) throws RemoteException {
        QueryFilter filter = QueryFilterHelper.toFilter(request, siteApiService);
        filter.addCond(IssueTableField.TYPE_ID, Types.IssueType.INFO_UPDATE_ISSUE.value);
        filter.addCond(IssueTableField.WORK_ORDER_STATUS, request.getWorkOrderStatus());
        filter.addCond(IssueTableField.IS_RESOLVED, request.getSolveStatus());
        filter.addCond(IssueTableField.IS_DEL, Status.Delete.UN_DELETE.value);
        int itemCount = issueMapper.count(filter);
        Pager pager = PageInfoDeal.buildResponsePager(request.getPageIndex(), request.getPageSize(), itemCount);
        filter.setPager(pager);
        List<InfoUpdateOrder> infoUpdateOrderList = issueMapper.selectInfoUpdateOrder(filter);
        List<InfoUpdateOrderRes> list = toOrderResponse(infoUpdateOrderList);

        return new ApiPageData(pager, list);
    }

    @Override
    public InfoUpdateOrderRes getInfoUpdateOrderById(WorkOrderRequest request) throws RemoteException {
        QueryFilter filter = QueryFilterHelper.toFilter(request, siteApiService);
        filter.addCond(IssueTableField.ID, request.getId());

        List<InfoUpdateOrder> infoUpdateOrderList = issueMapper.selectInfoUpdateOrder(filter);
        List<InfoUpdateOrderRes> list = toOrderResponse(infoUpdateOrderList);
        if (list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }

    @Override
    public MonthUpdateResponse getNotInTimeCountMonth(int siteId) throws RemoteException {
        MonthUpdateResponse monthUpdateResponse = new MonthUpdateResponse();
        List<UpdateNotInTimeChnl> notInTimeChnls = new ArrayList<>();
        List<EmptyChnl> emptyChnls = new ArrayList<>();
        QueryFilter filter = new QueryFilter(Table.ISSUE);
        filter.addCond(IssueTableField.SITE_ID, siteId);
        filter.addCond(IssueTableField.IS_RESOLVED, Status.Resolve.UN_RESOLVED.value);
        filter.addCond(IssueTableField.IS_DEL, Status.Delete.UN_DELETE.value);
        filter.addGroupField(IssueTableField.CUSTOMER2);
        List<InfoUpdate> updateList = issueMapper.selectInfoUpdate(filter);
        for (InfoUpdate update : updateList) {
            if (update.getChnlId() == null) {
                continue;
            }
            buildNotInTimeChnls(update, siteId, notInTimeChnls);
        }
        monthUpdateResponse.setUpdateNotInTimeChnl(notInTimeChnls);
        buildEmptyChnls(siteId, emptyChnls);
        monthUpdateResponse.setEmptyChnl(emptyChnls);
        return monthUpdateResponse;
    }

    private void buildEmptyChnls(int siteId, List<EmptyChnl> emptyChnls) throws RemoteException {
        List<Integer> chnlIdList = siteChannelServiceHelper.getEmptyChannel(siteId);
        for (Integer chnlId : chnlIdList) {
            if (chnlId != null) {
                Channel chnl = siteApiService.getChannelById(chnlId, "");
                if (chnl != null) {
                    EmptyChnl emptyChnl = new EmptyChnl();
                    emptyChnl.setChnlId(chnlId);
                    emptyChnl.setChnlName(chnl.getChnlName());
                    emptyChnls.add(emptyChnl);
                }
            }
        }
    }

    private void buildNotInTimeChnls(InfoUpdate update, int siteId, List<UpdateNotInTimeChnl> notInTimeChnls) throws RemoteException {
        QueryFilter filter = new QueryFilter(Table.ISSUE);
        filter.addCond(IssueTableField.SITE_ID, siteId);
        filter.addCond(IssueTableField.IS_RESOLVED, Status.Resolve.UN_RESOLVED.value);
        filter.addCond(IssueTableField.IS_DEL, Status.Delete.UN_DELETE.value);
        filter.addCond(IssueTableField.CUSTOMER2, update.getChnlId());
        Date maxIssueTime = issueMapper.getMaxIssueTime(filter);
        Date nowTime = new Date();
        double countMonth;
        Calendar c = Calendar.getInstance();

        c.setTime(maxIssueTime);
        int yearIssue = c.get(Calendar.YEAR);
        int monthIssue = c.get(Calendar.MONTH);
        int dayIssue = c.get(Calendar.DATE);

        c.setTime(nowTime);
        int yearNow = c.get(Calendar.YEAR);
        int monthNow = c.get(Calendar.MONTH);
        int dayNow = c.get(Calendar.DATE);

        if (monthNow < monthIssue) {
            return;
        }
        if (yearIssue == yearNow) {
            countMonth = (double) (monthNow - monthIssue);
        } else {
            countMonth = (double) (12 * (yearNow - yearIssue) + monthNow - monthIssue);
        }
        if (dayNow > dayIssue) {
            if (dayNow - dayIssue <= 15) {
                countMonth += 0.5;
            } else {
                countMonth += 1;
            }
        } else if (dayNow < dayIssue) {
            c.setTime(maxIssueTime);
            int maxDayOfMonthIssue = c.getActualMaximum(Calendar.DAY_OF_MONTH);
            if ((maxDayOfMonthIssue - dayIssue + dayNow) <= 15) {
                countMonth -= 0.5;
            }
        }
        Channel chnl = siteApiService.getChannelById(update.getChnlId(), "");
        if (chnl != null) {
            UpdateNotInTimeChnl notInTimeChnl = new UpdateNotInTimeChnl();
            notInTimeChnl.setChnlId(update.getChnlId());
            notInTimeChnl.setChnlName(chnl.getChnlName());
            notInTimeChnl.setCountMonth(countMonth);
            notInTimeChnls.add(notInTimeChnl);
        }
    }


    private List<InfoUpdateOrderRes> toOrderResponse(List<InfoUpdateOrder> infoUpdateOrderList) throws RemoteException {
        List<InfoUpdateOrderRes> responseList = new ArrayList<>();
        for (InfoUpdateOrder infoUpdateOrder : infoUpdateOrderList) {
            InfoUpdateOrderRes infoUpdateOrderRes = new InfoUpdateOrderRes();
            infoUpdateOrderRes.setId(infoUpdateOrder.getId());
            infoUpdateOrderRes.setChnlName(ChnlCheckUtil.getChannelName(infoUpdateOrder.getChnlId(), siteApiService));
            infoUpdateOrderRes.setSiteName(siteApiService.getSiteById(infoUpdateOrder.getSiteId(), null).getSiteDesc());
            infoUpdateOrderRes.setParentTypeName(Types.IssueType.valueOf(infoUpdateOrder.getTypeId()).getName());
            infoUpdateOrderRes.setIssueTypeName(Types.InfoUpdateIssueType.valueOf(infoUpdateOrder.getSubTypeId()).getName());
//            infoUpdateOrderRes.setDepartment();TODO
            infoUpdateOrderRes.setChnlUrl(infoUpdateOrder.getDetail());
            infoUpdateOrderRes.setCheckTime(infoUpdateOrder.getIssueTime());
            infoUpdateOrderRes.setSolveStatus(infoUpdateOrder.getIsResolved());
            infoUpdateOrderRes.setIsDeleted(infoUpdateOrder.getIsDel());
            infoUpdateOrderRes.setWorkOrderStatus(infoUpdateOrder.getWorkOrderStatus());
            responseList.add(infoUpdateOrderRes);
        }
        return responseList;
    }
}

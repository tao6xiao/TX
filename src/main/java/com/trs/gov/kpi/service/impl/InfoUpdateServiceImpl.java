package com.trs.gov.kpi.service.impl;

import com.trs.gov.kpi.constant.*;
import com.trs.gov.kpi.dao.ChnlGroupMapper;
import com.trs.gov.kpi.dao.IssueMapper;
import com.trs.gov.kpi.entity.HistoryDate;
import com.trs.gov.kpi.entity.InfoUpdate;
import com.trs.gov.kpi.entity.IssueIndicator;
import com.trs.gov.kpi.entity.dao.DBPager;
import com.trs.gov.kpi.entity.dao.QueryFilter;
import com.trs.gov.kpi.entity.exception.RemoteException;
import com.trs.gov.kpi.entity.outerapi.Channel;
import com.trs.gov.kpi.entity.requestdata.PageDataRequestParam;
import com.trs.gov.kpi.entity.responsedata.ApiPageData;
import com.trs.gov.kpi.entity.responsedata.HistoryStatistics;
import com.trs.gov.kpi.entity.responsedata.InfoUpdateResponse;
import com.trs.gov.kpi.entity.responsedata.Statistics;
import com.trs.gov.kpi.service.InfoUpdateService;
import com.trs.gov.kpi.service.helper.QueryFilterHelper;
import com.trs.gov.kpi.service.outer.SiteApiService;
import com.trs.gov.kpi.service.outer.SiteChannelServiceHelper;
import com.trs.gov.kpi.utils.DateSplitUtil;
import com.trs.gov.kpi.utils.DateUtil;
import com.trs.gov.kpi.utils.PageInfoDeal;
import com.trs.gov.kpi.utils.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.ParseException;
import java.util.*;

/**
 * Created by rw103 on 2017/5/13.
 */
@Slf4j
@Service
public class InfoUpdateServiceImpl implements InfoUpdateService {

//    @Resource
//    private InfoUpdateMapper infoUpdateMapper;

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
        filter.addCond(IssueTableField.TYPE_ID, Types.IssueType.INFO_UPDATE_ISSUE.value);
        filter.addCond(IssueTableField.IS_RESOLVED, Arrays.asList(Status.Resolve.IGNORED.value, Status.Resolve.RESOLVED.value));
        filter.addCond(IssueTableField.IS_DEL, Status.Delete.UN_DELETE.value);
        int resolvedCount = issueMapper.count(filter);


        filter = QueryFilterHelper.toFilter(param);
        filter.addCond(IssueTableField.TYPE_ID, Types.IssueType.INFO_UPDATE_ISSUE.value);
        filter.addCond(IssueTableField.IS_DEL, Status.Delete.UN_DELETE.value);
        filter.addCond(IssueTableField.IS_RESOLVED, Status.Resolve.UN_RESOLVED.value);
        int updateCount = issueMapper.count(filter);

        List<Statistics> statisticsList = new ArrayList<>();


        Statistics statistics = new Statistics();
        statistics.setType(IssueIndicator.SOLVED.value);
        statistics.setName(IssueIndicator.SOLVED.name);
        statistics.setCount(resolvedCount);
        statisticsList.add(statistics);

        statistics = new Statistics();
        statistics.setType(IssueIndicator.UPDATE_NOT_INTIME.value);
        statistics.setName(IssueIndicator.UPDATE_NOT_INTIME.name);
        statistics.setCount(updateCount);
        statisticsList.add(statistics);

        return statisticsList;
    }

//    @Override
//    public int getUnhandledIssueCount(IssueBase issueBase) {
//        return infoUpdateMapper.getUnhandledIssueCount(issueBase);
//    }
//
//    @Override
//    public int getUpdateWarningCount(IssueBase issueBase) {
//        return infoUpdateMapper.getUpdateWarningCount(issueBase) + infoUpdateMapper.getSelfWarningCount(issueBase);
//    }

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
    public List<HistoryStatistics> getIssueHistoryCount(PageDataRequestParam param) {
        if (StringUtil.isEmpty(param.getBeginDateTime())) {
            param.setBeginDateTime(DateUtil.toString(issueMapper.getEarliestIssueTime()));
        }
        if (StringUtil.isEmpty(param.getEndDateTime())) {
            param.setEndDateTime(DateUtil.toString(new Date()));
        }
        List<HistoryDate> dateList = DateSplitUtil.getHistoryDateList(param.getBeginDateTime(), param.getEndDateTime());
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
        return list;
    }

//    @Override
//    public List<InfoUpdateResponse> getIssueList(PageDataRequestParam param) {
//        List<InfoUpdateResponse> infoUpdateResponseList = new ArrayList<>();
//        QueryFilter filter = QueryFilterHelper.toFilter(param);
//
//        List<Issue> issueList = null;
//
//        for (InfoUpdateResponse info : infoUpdateResponseList) {
//            if (info.getIssueTypeId() != null) {
//                info.setIssueTypeName(Types.InfoUpdateIssueType.valueOf(info.getIssueTypeId()).name);
//            }
//            // get channl name
//            try {
//                if (info.getChnlId() != null) {
//                    Channel chnl = siteApiService.getChannelById(info.getChnlId(), "");
//                    if (chnl != null && !StringUtil.isEmpty(chnl.getChnlName())) {
//                        info.setChnlName(chnl.getChnlName());
//                    }
//                }
//            } catch (RemoteException e) {
//                log.error("", e);
//            }
//        }
//        return infoUpdateResponseList;
//    }

//    @Override
//    public List<Statistics> getIssueCountByType(IssueBase issueBase) {
//
//        int updateNotIntimeCount = infoUpdateMapper.getUpdateNotIntimeCount(issueBase);
//        Statistics updateNotIntimeStatistics = new Statistics();
//        updateNotIntimeStatistics.setCount(updateNotIntimeCount);
//        updateNotIntimeStatistics.setType(Types.InfoUpdateIssueType.UPDATE_NOT_INTIME.value);
//        updateNotIntimeStatistics.setName(Types.InfoUpdateIssueType.UPDATE_NOT_INTIME.name);
//
//        List<Statistics> list = new ArrayList<>();
//        list.add(updateNotIntimeStatistics);
//
//        return list;
//    }

//    @Override
//    public List<Statistics> getWarningCountByType(IssueBase issueBase) {
//
//        int updateWarningCount = infoUpdateMapper.getUpdateWarningCount(issueBase);
//        Statistics updateWarningStatistics = new Statistics();
//        updateWarningStatistics.setCount(updateWarningCount);
//        updateWarningStatistics.setType(Types.InfoUpdateWarningType.UPDATE_WARNING.value);
//        updateWarningStatistics.setName(Types.InfoUpdateWarningType.UPDATE_WARNING.name);
//
//        int selfWarningCount = infoUpdateMapper.getSelfWarningCount(issueBase);
//        Statistics selfWarningStatistics = new Statistics();
//        selfWarningStatistics.setCount(selfWarningCount);
//        selfWarningStatistics.setType(Types.InfoUpdateWarningType.SELF_CHECK_WARNING.value);
//        selfWarningStatistics.setName(Types.InfoUpdateWarningType.SELF_CHECK_WARNING.name);
//
//        List<Statistics> list = new ArrayList<>();
//        list.add(updateWarningStatistics);
//        list.add(selfWarningStatistics);
//
//        return list;
//    }

//    @Override
//    public int getHandledIssueCount(IssueBase issueBase) {
//        return 0;
//    }

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
//        Date ealiestTime = getEarliestIssueTime();
//        Date beginSetTime = InitTime.CheckBeginDateTime(beginDateTime, ealiestTime);
//        Date endSetTime = InitTime.CheckEndDateTime(endDateTime);
//        IssueBase issueBase = new IssueBase();
//        issueBase.setSiteId(siteId);
//        issueBase.setBeginDateTime(InitTime.getStringTime(beginSetTime));
//        issueBase.setEndDateTime(InitTime.getStringTime(endSetTime));

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
            childChnlIds = new HashSet<>();
            for (Integer chnlId : chnlIds) {
                if (chnlId != null) {
                    childChnlIds.add(chnlId);
                    childChnlIds = siteApiService.getAllChildChnlIds(null, param.getSiteId(), chnlId, childChnlIds);
                }
            }
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

//    @Override
//    public int getAllUpdateNotInTime(Integer siteId, String beginDateTime, String endDateTime) throws ParseException {
//        return 0;
//    }

//    @Override
//    public int getAllDateUpdateNotInTime(IssueBase issueBase) throws ParseException {
////        IssueBase issueBase = getIssueBase(siteId, beginDateTime, endDateTime);
//        //获取所有
//        int count = infoUpdateMapper.getAllDateUpdateNotInTime(issueBase);
//        return count;
//    }

    @Override
    public ApiPageData get(PageDataRequestParam param) throws RemoteException {
        QueryFilter filter = QueryFilterHelper.toFilter(param, Types.IssueType.INFO_UPDATE_ISSUE);
        filter.addCond(IssueTableField.TYPE_ID, Types.IssueType.INFO_UPDATE_ISSUE.value);
        filter.addCond(IssueTableField.IS_DEL, Status.Delete.UN_DELETE.value);
        filter.addCond(IssueTableField.IS_RESOLVED, Status.Resolve.UN_RESOLVED.value);
        int itemCount = issueMapper.count(filter);
        ApiPageData apiPageData = PageInfoDeal.buildApiPageData(param.getPageIndex(), param.getPageSize(), itemCount);
        filter.setPager(new DBPager((apiPageData.getPager().getCurrPage() - 1) * apiPageData.getPager().getPageSize(), apiPageData.getPager().getPageSize()));
        List<InfoUpdate> infoUpdateList = issueMapper.selectInfoUpdate(filter);
        List<InfoUpdateResponse> infoUpdateResponseList = toResponse(infoUpdateList);
        apiPageData.setData(infoUpdateResponseList);
        return apiPageData;
    }

    private List<InfoUpdateResponse> toResponse(List<InfoUpdate> infoUpdateList) {
        List<InfoUpdateResponse> responseList = new ArrayList<>();
        if (infoUpdateList == null) {
            return responseList;
        }
        InfoUpdateResponse infoUpdateResponse = null;
        for (InfoUpdate infoUpdate : infoUpdateList) {
            infoUpdateResponse = new InfoUpdateResponse();
            infoUpdateResponse.setChnlName(getChannelName(infoUpdate.getChnlId()));
            infoUpdateResponse.setId(infoUpdate.getId());
            infoUpdateResponse.setChnlUrl(infoUpdate.getChnlUrl());
            infoUpdateResponse.setCheckTime(infoUpdate.getCheckTime());
            infoUpdateResponse.setIssueTypeName(Types.InfoUpdateIssueType.valueOf(infoUpdate.getSubTypeId()).name);
            responseList.add(infoUpdateResponse);
        }
        return responseList;
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
        if (channel == null) {
            return "";
        }
        String chnlName = channel.getChnlName();
        if (chnlName == null) {
            return "";
        }
        return chnlName;
    }

//    private IssueBase getIssueBase(Integer siteId, String beginDateTime, String endDateTime) throws ParseException {
//        Date ealiestTime = getEarliestIssueTime();
//        beginDateTime = InitTime.checkBeginDateTime(beginDateTime, ealiestTime);
//        endDateTime = InitTime.checkEndDateTime(endDateTime);
//        IssueBase issueBase = new IssueBase();
//        issueBase.setSiteId(siteId);
//        issueBase.setBeginDateTime(beginDateTime);
//        issueBase.setEndDateTime(endDateTime);
//        return issueBase;
//    }

    private Statistics getStatisticsByCount(int i, int count) {
        Statistics statistics = new Statistics();
        statistics.setCount(count);
        statistics.setType(i);
        statistics.setName(EnumIndexUpdateType.valueOf(i).getName());
        return statistics;
    }
}

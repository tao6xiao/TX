package com.trs.gov.kpi.service.impl;

import com.trs.gov.kpi.constant.EnumIndexUpdateType;
import com.trs.gov.kpi.constant.InfoUpdateType;
import com.trs.gov.kpi.constant.UpdateWarningType;
import com.trs.gov.kpi.dao.InfoUpdateMapper;
import com.trs.gov.kpi.entity.InfoUpdate;
import com.trs.gov.kpi.entity.IssueBase;
import com.trs.gov.kpi.entity.exception.RemoteException;
import com.trs.gov.kpi.entity.outerapi.Channel;
import com.trs.gov.kpi.entity.responsedata.Statistics;
import com.trs.gov.kpi.service.InfoUpdateService;
import com.trs.gov.kpi.service.OperationService;
import com.trs.gov.kpi.service.outer.SiteApiService;
import com.trs.gov.kpi.utils.InitTime;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.ParseException;
import java.util.*;

/**
 * Created by rw103 on 2017/5/13.
 */
@Service
public class InfoUpdateServiceImpl extends OperationServiceImpl implements InfoUpdateService {

    @Resource
    private InfoUpdateMapper infoUpdateMapper;

    @Resource
    private SiteApiService siteApiService;

    @Override
    public int getHandledIssueCount(IssueBase issueBase) {
        return infoUpdateMapper.getHandledIssueCount(issueBase);
    }

    @Override
    public int getUnhandledIssueCount(IssueBase issueBase) {
        return infoUpdateMapper.getUnhandledIssueCount(issueBase);
    }

    @Override
    public int getUpdateWarningCount(IssueBase issueBase) {
        return infoUpdateMapper.getUpdateWarningCount(issueBase);
    }

    @Override
    public int getIssueHistoryCount(IssueBase issueBase) {
        return infoUpdateMapper.getIssueHistoryCount(issueBase);
    }

    @Override
    public List<InfoUpdate> getIssueList(Integer currPage, Integer pageSize, IssueBase issueBase) {
        return infoUpdateMapper.getIssueList(currPage, pageSize, issueBase);
    }

    @Override
    public List<Statistics> getIssueCountByType(IssueBase issueBase) {

        int updateNotIntimeCount = infoUpdateMapper.getUpdateNotIntimeCount(issueBase);
        Statistics updateNotIntimeStatistics = new Statistics();
        updateNotIntimeStatistics.setCount(updateNotIntimeCount);
        updateNotIntimeStatistics.setType(InfoUpdateType.UPDATE_NOT_INTIME.value);
        updateNotIntimeStatistics.setName(InfoUpdateType.UPDATE_NOT_INTIME.name);

        List<Statistics> list = new ArrayList<>();
        list.add(updateNotIntimeStatistics);

        return list;
    }

    @Override
    public List<Statistics> getWarningCountByType(IssueBase issueBase) {

        int updateWarningCount = infoUpdateMapper.getUpdateWarningCount(issueBase);
        Statistics updateWarningStatistics = new Statistics();
        updateWarningStatistics.setCount(updateWarningCount);
        updateWarningStatistics.setType(UpdateWarningType.UPDATE_WARNING.value);
        updateWarningStatistics.setName(UpdateWarningType.UPDATE_WARNING.name);

        int selfWarningCount = infoUpdateMapper.getSelfWarningCount(issueBase);
        Statistics selfWarningStatistics = new Statistics();
        selfWarningStatistics.setCount(selfWarningCount);
        selfWarningStatistics.setType(UpdateWarningType.SELF_CHECK_WARNING.value);
        selfWarningStatistics.setName(UpdateWarningType.SELF_CHECK_WARNING.name);

        List<Statistics> list = new ArrayList<>();
        list.add(updateWarningStatistics);
        list.add(selfWarningStatistics);

        return list;
    }

    /**
     * 获取栏目信息更新不及时问题的统计信息
     *
     * @param siteId
     * @param beginDateTime
     * @param endDateTime
     * @return
     * @throws ParseException
     * @throws RemoteException
     */
    @Override
    public List<Statistics> getUpdateNotInTimeCountList(Integer siteId, String beginDateTime, String endDateTime) throws ParseException, RemoteException {
        int count = 0;
        List<Statistics> statisticsList = new ArrayList<>();
        Date ealiestTime = getEarliestIssueTime();
        Date beginSetTime = InitTime.CheckBeginDateTime(beginDateTime, ealiestTime);
        Date endSetTime = InitTime.CheckEndDateTime(endDateTime);
        IssueBase issueBase = new IssueBase();
        issueBase.setSiteId(siteId);
        issueBase.setBeginDateTime(InitTime.getStringTime(beginSetTime));
        issueBase.setEndDateTime(InitTime.getStringTime(endSetTime));

        //获取所有
        List<Map<Integer, Integer>> map = infoUpdateMapper.getAllUpdateNotInTime(issueBase);
        count = map.size();
        Statistics statistics = getStatisticsByCount(EnumIndexUpdateType.ALL.getCode(), count);
        statisticsList.add(statistics);

        //获取A类
        Set<Integer> childChnlIds = null;
        List<Integer> chnlIds = infoUpdateMapper.getIdsUpdateNotInTime(issueBase);
        if (!chnlIds.isEmpty()) {
            childChnlIds = new HashSet<>();
            for (Integer chnlId : chnlIds) {
                childChnlIds.add(chnlId);
                childChnlIds = siteApiService.getAllChildChnlIds(null, siteId, chnlId, childChnlIds);
            }
        }
        map = infoUpdateMapper.getUpdateNotInTime(issueBase, childChnlIds);
        count = map.size();
        statistics = getStatisticsByCount(EnumIndexUpdateType.A_TYPE.getCode(), count);
        statisticsList.add(statistics);

        //获取首页
        Set<Integer> childChnlIds2 = null;
        List<Channel> channelList = siteApiService.getChildChannel(siteId, 0, null);
        if (!channelList.isEmpty()) {
            childChnlIds2 = new HashSet<>();
            for (Channel channel : channelList) {
                childChnlIds2.add(channel.getChannelId());
            }
        }
        map = infoUpdateMapper.getUpdateNotInTime(issueBase, childChnlIds2);
        count = map.size();
        statistics = getStatisticsByCount(EnumIndexUpdateType.HOMEPAGE.getCode(), count);
        statisticsList.add(statistics);

        return statisticsList;
    }

    private Statistics getStatisticsByCount(int i, int count) {
        Statistics statistics = new Statistics();
        statistics.setCount(count);
        statistics.setType(i);
        statistics.setName(EnumIndexUpdateType.valueOf(i).getName());
        return statistics;
    }
}

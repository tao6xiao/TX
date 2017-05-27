package com.trs.gov.kpi.service;

import com.trs.gov.kpi.entity.responsedata.InfoUpdateResponse;
import com.trs.gov.kpi.entity.IssueBase;
import com.trs.gov.kpi.entity.exception.RemoteException;
import com.trs.gov.kpi.entity.requestdata.PageDataRequestParam;
import com.trs.gov.kpi.entity.responsedata.ApiPageData;
import com.trs.gov.kpi.entity.responsedata.HistoryStatistics;
import com.trs.gov.kpi.entity.responsedata.Statistics;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

/**
 * Created by rw103 on 2017/5/13.
 */
public interface InfoUpdateService {

    List<Statistics> getIssueCount(PageDataRequestParam param);

    List<HistoryStatistics> getIssueHistoryCount(PageDataRequestParam param);

//    List<InfoUpdateResponse> getIssueList(PageDataRequestParam param);

//    List<Statistics> getIssueCountByType(IssueBase issueBase);

//    List<Statistics> getWarningCountByType(IssueBase issueBase);

//    int getHandledIssueCount(IssueBase issueBase);

//    int getUnhandledIssueCount(IssueBase issueBase);

//    int getUpdateWarningCount(IssueBase issueBase);

    void handIssuesByIds(int siteId, List<Integer> ids);

    void ignoreIssuesByIds(int siteId, List<Integer> ids);

    void delIssueByIds(int siteId, List<Integer> ids);

    Date getEarliestIssueTime();

    /**
     * 获取栏目信息更新不及时问题的统计信息
     *
     * @param param
     * @return
     */
    List<Statistics> getUpdateNotInTimeCountList(PageDataRequestParam param) throws ParseException, RemoteException;

//    /**
//     * 获取栏目更新不及时的总数
//     * @param siteId
//     * @param beginDateTime
//     * @param endDateTime
//     * @return
//     */
//    int getAllUpdateNotInTime(Integer siteId, String beginDateTime, String endDateTime) throws ParseException;

//    /**
//     * 获取更新不及时的数据条数
//     *
//     * @param issueBase
//     * @return
//     */
//    int getAllDateUpdateNotInTime(IssueBase issueBase) throws ParseException;


    ApiPageData get(PageDataRequestParam param) throws RemoteException;


}

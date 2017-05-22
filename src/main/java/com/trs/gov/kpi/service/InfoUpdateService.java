package com.trs.gov.kpi.service;

import com.trs.gov.kpi.entity.InfoUpdate;
import com.trs.gov.kpi.entity.IssueBase;
import com.trs.gov.kpi.entity.exception.RemoteException;
import com.trs.gov.kpi.entity.responsedata.Statistics;

import java.text.ParseException;
import java.util.List;

/**
 * Created by rw103 on 2017/5/13.
 */
public interface InfoUpdateService extends OperationService {

    int getIssueHistoryCount(IssueBase issueBase);

    List<InfoUpdate> getIssueList(Integer currPage, Integer pageSize, IssueBase issueBase);

    List<Statistics> getIssueCountByType(IssueBase issueBase);

    List<Statistics> getWarningCountByType(IssueBase issueBase);

    /**
     * 获取栏目信息更新不及时问题的统计信息
     * @param siteId
     * @param beginDateTime
     * @param endDateTime
     * @return
     */
    List<Statistics> getUpdateNotInTimeCountList(Integer siteId, String beginDateTime, String endDateTime) throws ParseException, RemoteException;
}

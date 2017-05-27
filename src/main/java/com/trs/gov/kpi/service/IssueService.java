package com.trs.gov.kpi.service;

import com.trs.gov.kpi.entity.IssueBase;
import com.trs.gov.kpi.entity.requestdata.PageDataRequestParam;
import com.trs.gov.kpi.entity.responsedata.ApiPageData;
import com.trs.gov.kpi.entity.responsedata.IssueIsNotResolvedResponse;

import java.util.Date;
import java.util.List;

/**
 * Created by wangxuan on 2017/5/17.
 */
public interface IssueService{

//    int getAllIssueCount(IssueBase issue);

//    List<IssueIsNotResolvedResponse> getAllIssueList(Integer currPage, Integer pageSize, IssueBase issue);

    void handIssuesByIds(int siteId, List<Integer> ids);

    void ignoreIssuesByIds(int siteId, List<Integer> ids);

    void delIssueByIds(int siteId, List<Integer> ids);

    Date getEarliestIssueTime();

    /**
     * 获取待解决问题列表
     * @param param
     * @return
     */
    ApiPageData get( PageDataRequestParam param);
}

package com.trs.gov.kpi.service.impl;

import com.trs.gov.kpi.constant.Status;
import com.trs.gov.kpi.dao.IssueMapper;
import com.trs.gov.kpi.entity.Issue;
import com.trs.gov.kpi.entity.IssueBase;
import com.trs.gov.kpi.entity.dao.DBPager;
import com.trs.gov.kpi.entity.dao.QueryFilter;
import com.trs.gov.kpi.entity.requestdata.PageDataRequestParam;
import com.trs.gov.kpi.entity.responsedata.ApiPageData;
import com.trs.gov.kpi.entity.responsedata.IssueIsNotResolvedResponse;
import com.trs.gov.kpi.service.IssueService;
import com.trs.gov.kpi.service.helper.LinkAvailabilityServiceHelper;
import com.trs.gov.kpi.utils.DateUtil;
import com.trs.gov.kpi.utils.IssueDataUtil;
import com.trs.gov.kpi.utils.PageInfoDeal;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 综合实时监测   查询待解决、已处理、已忽略的问题和预警
 */
@Service
public class IssueServiceImpl implements IssueService {

    @Resource
    private IssueMapper issueMapper;

    @Override
    public int getAllIssueCount(IssueBase issue) {
        return issueMapper.getAllIssueCount(issue);
    }

    @Override
    public List<IssueIsNotResolvedResponse> getAllIssueList(Integer currPage, Integer pageSize, IssueBase issue) {
        List<Issue> issueList = issueMapper.getAllIssueList(currPage, pageSize, issue);
        issueList = IssueDataUtil.getIssueListToSetSubTypeName(issueList);
        List<IssueIsNotResolvedResponse> issueIsNotResolvedResponseList = new ArrayList<>();
        IssueIsNotResolvedResponse issueIsNotResolvedResponse = null;
        for (Issue is :issueList) {
            issueIsNotResolvedResponse = getIssueIsNotResolvedResponseDetailByIssue(is);
            issueIsNotResolvedResponseList.add(issueIsNotResolvedResponse);
        }
        return issueIsNotResolvedResponseList;
    }

    private IssueIsNotResolvedResponse getIssueIsNotResolvedResponseDetailByIssue(Issue is) {
        IssueIsNotResolvedResponse issueIsNotResolvedResponse = new IssueIsNotResolvedResponse();
        issueIsNotResolvedResponse.setId(is.getId());
        issueIsNotResolvedResponse.setIssueTypeName(is.getSubTypeName());
        issueIsNotResolvedResponse.setDetail(is.getDetail());
        issueIsNotResolvedResponse.setIssueTime(DateUtil.toString(is.getIssueTime()));
        return issueIsNotResolvedResponse;
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
    public ApiPageData get(PageDataRequestParam param) {
        QueryFilter filter = LinkAvailabilityServiceHelper.toFilter(param);
        filter.addCond("isResolved", Status.Resolve.UN_RESOLVED.value);
        filter.addCond("isDel",Status.Delete.UN_DELETE.value);
        filter.addCond("typeId",1).setRangeBegin(true);
        filter.addCond("typeId",50).setRangeEnd(true);

        int itemCount = issueMapper.count(filter);
        ApiPageData apiPageData = PageInfoDeal.buildApiPageData(param.getPageIndex(), param.getPageSize(), itemCount);
        filter.setPager(new DBPager((apiPageData.getPager().getCurrPage() - 1) * apiPageData.getPager().getPageSize(),apiPageData.getPager().getPageSize()));
        List<Issue> issueList = issueMapper.select(filter);
        List<IssueIsNotResolvedResponse> responseByIssueList = getResponseByIssueList(issueList);
        apiPageData.setData(responseByIssueList);
        return apiPageData;
    }

    private List<IssueIsNotResolvedResponse> getResponseByIssueList(List<Issue> issueList) {
        issueList = IssueDataUtil.getIssueListToSetSubTypeName(issueList);
        List<IssueIsNotResolvedResponse> issueIsNotResolvedResponseList = new ArrayList<>();
        IssueIsNotResolvedResponse issueIsNotResolvedResponse = null;
        for (Issue is :issueList) {
            issueIsNotResolvedResponse = getIssueIsNotResolvedResponseDetailByIssue(is);
            issueIsNotResolvedResponseList.add(issueIsNotResolvedResponse);
        }
        return issueIsNotResolvedResponseList;
    }
}

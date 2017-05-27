package com.trs.gov.kpi.service.impl;

import com.trs.gov.kpi.dao.IssueMapper;
import com.trs.gov.kpi.entity.Issue;
import com.trs.gov.kpi.entity.IssueBase;
import com.trs.gov.kpi.entity.responsedata.IssueIsNotResolvedResponse;
import com.trs.gov.kpi.service.IssueService;
import com.trs.gov.kpi.utils.DateUtil;
import com.trs.gov.kpi.utils.IssueDataUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 综合实时监测   查询待解决、已处理、已忽略的问题和预警
 */
@Service
public class IssueServiceImpl extends OperationServiceImpl implements IssueService {

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
}

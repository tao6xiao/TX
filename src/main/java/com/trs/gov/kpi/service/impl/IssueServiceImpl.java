package com.trs.gov.kpi.service.impl;

import com.trs.gov.kpi.dao.IssueMapper;
import com.trs.gov.kpi.entity.Issue;
import com.trs.gov.kpi.entity.responsedata.IssueIsNotResolvedResponseDetail;
import com.trs.gov.kpi.service.IssueService;
import com.trs.gov.kpi.utils.InitTime;
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
    public int getAllIssueCount(Issue issue) {
        return issueMapper.getAllIssueCount(issue);
    }

    @Override
    public List<IssueIsNotResolvedResponseDetail> getAllIssueList(Integer currPage, Integer pageSize, Issue issue) {
        List<Issue> issueList = issueMapper.getAllIssueList(currPage, pageSize, issue);
        issueList = IssueDataUtil.getIssueListToSetSubTypeName(issueList);
        List<IssueIsNotResolvedResponseDetail> issueIsNotResolvedResponseDetailList = new ArrayList<>();
        IssueIsNotResolvedResponseDetail issueIsNotResolvedResponseDetail = null;
        for (Issue is :issueList) {
            issueIsNotResolvedResponseDetail = getIssueIsNotResolvedResponseDetailByIssue(is);
            issueIsNotResolvedResponseDetailList.add(issueIsNotResolvedResponseDetail);
        }
        return issueIsNotResolvedResponseDetailList;
    }

    private IssueIsNotResolvedResponseDetail getIssueIsNotResolvedResponseDetailByIssue(Issue is) {
        IssueIsNotResolvedResponseDetail issueIsNotResolvedResponseDetail = new IssueIsNotResolvedResponseDetail();
        issueIsNotResolvedResponseDetail.setId(is.getId());
        issueIsNotResolvedResponseDetail.setIssueTypeName(is.getSubTypeName());
        issueIsNotResolvedResponseDetail.setDetail(is.getDetail());
        issueIsNotResolvedResponseDetail.setIssueTime(InitTime.getStringTime(is.getIssueTime()));
        return issueIsNotResolvedResponseDetail;
    }
}

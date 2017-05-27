package com.trs.gov.kpi.service.impl;

import com.trs.gov.kpi.dao.IssueMapper;
import com.trs.gov.kpi.entity.Issue;
import com.trs.gov.kpi.entity.IssueBase;
import com.trs.gov.kpi.entity.responsedata.IssueIsResolvedResponse;
import com.trs.gov.kpi.service.IntegratedMonitorIsResolvedService;
import com.trs.gov.kpi.utils.DateUtil;
import com.trs.gov.kpi.utils.IssueDataUtil;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by he.lang on 2017/5/19.
 */
@Service
public class IntegratedMonitorIsResolvedServiceImpl extends OperationServiceImpl implements IntegratedMonitorIsResolvedService {

    @Resource
    IssueMapper issueMapper;

    @Override
    public List<IssueIsResolvedResponse> getPageDataIsResolvedList(Integer pageIndex, Integer pageSize, @ModelAttribute IssueBase issue) {
        int pageCalculate = pageIndex * pageSize;
        List<Issue> issueList = issueMapper.selectPageDataIsResolvedList(pageCalculate, pageSize, issue);
        issueList = IssueDataUtil.getIssueListToSetSubTypeName(issueList);
        List<IssueIsResolvedResponse> issueIsResolvedResponseDetailList = new ArrayList<>();
        IssueIsResolvedResponse issueIsResolvedResponseDetail = null;
        for (Issue is :issueList) {
            issueIsResolvedResponseDetail = getIssueIsResolvedResponseDetailByIssue(is);
            issueIsResolvedResponseDetailList.add(issueIsResolvedResponseDetail);
        }
        return issueIsResolvedResponseDetailList;
    }

    @Override
    public int getPageDataIsResolvedItemCount(IssueBase issue) {
        int num = issueMapper.selectPageDataIsResolvedItemCount(issue);
        return num;
    }

    private IssueIsResolvedResponse getIssueIsResolvedResponseDetailByIssue(Issue is) {
        IssueIsResolvedResponse issueIsResolvedResponseDetail = new IssueIsResolvedResponse();
        issueIsResolvedResponseDetail.setId(is.getId());
        issueIsResolvedResponseDetail.setIssueTypeName(is.getSubTypeName());
        issueIsResolvedResponseDetail.setDetail(is.getDetail());
        issueIsResolvedResponseDetail.setIssueTime(DateUtil.toString(is.getIssueTime()));
        return issueIsResolvedResponseDetail;
    }
}

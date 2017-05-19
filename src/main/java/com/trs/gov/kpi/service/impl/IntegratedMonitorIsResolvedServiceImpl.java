package com.trs.gov.kpi.service.impl;

import com.trs.gov.kpi.dao.IssueMapper;
import com.trs.gov.kpi.entity.Issue;
import com.trs.gov.kpi.entity.responsedata.IssueIsResolvedResponseDetail;
import com.trs.gov.kpi.service.IntegratedMonitorIsResolvedService;
import com.trs.gov.kpi.service.OperationService;
import com.trs.gov.kpi.utils.InitEndTime;
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
    public List<IssueIsResolvedResponseDetail> getPageDataIsResolvedList(Integer pageIndex, Integer pageSize, @ModelAttribute Issue issue) {
        int pageCalculate = pageIndex * pageSize;
        List<Issue> issueList = issueMapper.selectPageDataIsResolvedList(pageCalculate, pageSize, issue);
        issueList = IssueDataUtil.getIssueListToSetSubTypeName(issueList);
        List<IssueIsResolvedResponseDetail> issueIsResolvedResponseDetailList = new ArrayList<>();
        IssueIsResolvedResponseDetail issueIsResolvedResponseDetail = null;
        for (Issue is :issueList) {
            issueIsResolvedResponseDetail = getIssueIsResolvedResponseDetailByIssue(is);
            issueIsResolvedResponseDetailList.add(issueIsResolvedResponseDetail);
        }
        return issueIsResolvedResponseDetailList;
    }

    @Override
    public int getPageDataIsResolvedItemCount(Issue issue) {
        int num = issueMapper.selectPageDataIsResolvedItemCount(issue);
        return num;
    }

    private IssueIsResolvedResponseDetail getIssueIsResolvedResponseDetailByIssue(Issue is) {
        IssueIsResolvedResponseDetail issueIsResolvedResponseDetail = new IssueIsResolvedResponseDetail();
        issueIsResolvedResponseDetail.setId(is.getId());
        issueIsResolvedResponseDetail.setIssueTypeName(is.getSubTypeName());
        issueIsResolvedResponseDetail.setDetail(is.getDetail());
        issueIsResolvedResponseDetail.setIssueTime(InitEndTime.getStringTime(is.getIssueTime()));
        return issueIsResolvedResponseDetail;
    }
}

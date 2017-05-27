package com.trs.gov.kpi.service.impl;

import com.trs.gov.kpi.dao.IssueMapper;
import com.trs.gov.kpi.dao.OperationMapper;
import com.trs.gov.kpi.entity.Issue;
import com.trs.gov.kpi.entity.IssueBase;
import com.trs.gov.kpi.entity.responsedata.IssueWarningResponse;
import com.trs.gov.kpi.service.IntegratedMonitorWarningService;
import com.trs.gov.kpi.utils.DateUtil;
import com.trs.gov.kpi.utils.IssueDataUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by he.lang on 2017/5/18.
 */
@Service
public class IntegratedMonitorWarningServiceImpl extends OperationServiceImpl implements IntegratedMonitorWarningService {
    @Resource
    IssueMapper issueMapper;

    @Resource
    OperationMapper operationMapper;

    @Override
    public int dealWithWarningBySiteIdAndId(int siteId, Integer[] ids) {
        List<Integer> idList = Arrays.asList(ids);
        operationMapper.handIssuesByIds(siteId, idList);
        return 0;
    }

    @Override
    public int ignoreWarningBySiteIdAndId(int siteId, Integer[] ids) {
        List<Integer> idList = Arrays.asList(ids);
        operationMapper.ignoreIssuesByIds(siteId, idList);
        return 0;
    }

    @Override
    public int deleteWarningBySiteIdAndId(int siteId, Integer[] ids) {
        List<Integer> idList = Arrays.asList(ids);
        operationMapper.delIssueByIds(siteId, idList);
        return 0;
    }

    @Override
    public List<IssueWarningResponse> getPageDataWaringList(Integer pageIndex, Integer pageSize, IssueBase issue) throws ParseException {
        int pageCalculate = pageIndex * pageSize;
        List<Issue> issueList = issueMapper.getAllWarningList(pageCalculate, pageSize, issue);
        issueList = IssueDataUtil.getIssueListToSetSubTypeName(issueList);
        List<IssueWarningResponse> issueWarningResponseList = new ArrayList<>();
        IssueWarningResponse issueWarningResponse = null;
        for (Issue is: issueList) {
            issueWarningResponse = getIssueWarningResponseDetailByIssue(is);
            issueWarningResponseList.add(issueWarningResponse);
        }
        return issueWarningResponseList;
    }

    private IssueWarningResponse getIssueWarningResponseDetailByIssue(Issue is) throws ParseException {
        IssueWarningResponse issueWarningResponse = new IssueWarningResponse();
        issueWarningResponse.setId(is.getId());
        issueWarningResponse.setIssueTime(DateUtil.toString(is.getIssueTime()));
        issueWarningResponse.setDetail(is.getDetail());
        issueWarningResponse.setChnlName(is.getCustomer1());
        issueWarningResponse.setIssueTypeName(is.getSubTypeName());
        Date issueTime = is.getIssueTime();
        Date nowTime = new Date();
        Long between = nowTime.getTime() - issueTime.getTime();
        Long limitTime = between/(24*60*60*1000);
        issueWarningResponse.setLimitTime(limitTime);
        return issueWarningResponse;
    }

    @Override
    public int getItemCount(IssueBase issue) {
        int itemCount = issueMapper.getAllWarningCount(issue);
        return itemCount;
    }

}

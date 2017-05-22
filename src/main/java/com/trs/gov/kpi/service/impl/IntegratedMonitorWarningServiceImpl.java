package com.trs.gov.kpi.service.impl;

import com.trs.gov.kpi.dao.IssueMapper;
import com.trs.gov.kpi.dao.OperationMapper;
import com.trs.gov.kpi.entity.Issue;
import com.trs.gov.kpi.entity.responsedata.IssueWarningResponseDetail;
import com.trs.gov.kpi.service.IntegratedMonitorWarningService;
import com.trs.gov.kpi.utils.InitTime;
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
    public List<IssueWarningResponseDetail> getPageDataWaringList(Integer pageIndex, Integer pageSize, Issue issue) throws ParseException {
        int pageCalculate = pageIndex * pageSize;
        List<Issue> issueList = issueMapper.getAllWarningList(pageCalculate, pageSize, issue);
        issueList = IssueDataUtil.getIssueListToSetSubTypeName(issueList);
        List<IssueWarningResponseDetail> issueWarningResponseDetailList = new ArrayList<>();
        IssueWarningResponseDetail issueWarningResponseDetail = null;
        for (Issue is: issueList) {
            issueWarningResponseDetail = getIssueWarningResponseDetailByIssue(is);
            issueWarningResponseDetailList.add(issueWarningResponseDetail);
        }
        return issueWarningResponseDetailList;
    }

    private IssueWarningResponseDetail getIssueWarningResponseDetailByIssue(Issue is) throws ParseException {
        IssueWarningResponseDetail issueWarningResponseDetail = new IssueWarningResponseDetail();
        issueWarningResponseDetail.setId(is.getId());
        issueWarningResponseDetail.setIssueTime(InitTime.getStringTime(is.getIssueTime()));
        issueWarningResponseDetail.setDetail(is.getDetail());
        issueWarningResponseDetail.setChnlName(is.getCustomer1());
        issueWarningResponseDetail.setIssueTypeName(is.getSubTypeName());
        Date issueTime = is.getIssueTime();
        Date nowTime = new Date();
        String nowTimeStr = InitTime.getNowTimeFormat(nowTime);
        nowTime = InitTime.getNowTimeFormat(nowTimeStr);
        Long between = nowTime.getTime() - issueTime.getTime();
        Long limitTime = between/(24*60*60*1000);
        issueWarningResponseDetail.setLimitTime(limitTime);
        return issueWarningResponseDetail;
    }

    @Override
    public int getItemCount(Issue issue) {
        int itemCount = issueMapper.getAllWarningCount(issue);
        return itemCount;
    }

}

package com.trs.gov.kpi.service.impl;

import com.trs.gov.kpi.constant.IssueTableField;
import com.trs.gov.kpi.constant.Status;
import com.trs.gov.kpi.constant.Types;
import com.trs.gov.kpi.dao.IssueMapper;
import com.trs.gov.kpi.entity.Issue;
import com.trs.gov.kpi.entity.dao.DBPager;
import com.trs.gov.kpi.entity.dao.QueryFilter;
import com.trs.gov.kpi.entity.requestdata.PageDataRequestParam;
import com.trs.gov.kpi.entity.responsedata.ApiPageData;
import com.trs.gov.kpi.entity.responsedata.IssueWarningResponse;
import com.trs.gov.kpi.service.IntegratedMonitorWarningService;
import com.trs.gov.kpi.service.helper.QueryFilterHelper;
import com.trs.gov.kpi.utils.DateUtil;
import com.trs.gov.kpi.utils.IssueDataUtil;
import com.trs.gov.kpi.utils.PageInfoDeal;
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
public class IntegratedMonitorWarningServiceImpl implements IntegratedMonitorWarningService {
    @Resource
    IssueMapper issueMapper;


    @Override
    public int dealWithWarningBySiteIdAndId(int siteId, Integer[] ids) {
        List<Integer> idList = Arrays.asList(ids);
        issueMapper.handIssuesByIds(siteId, idList);
        return 0;
    }

    @Override
    public int ignoreWarningBySiteIdAndId(int siteId, Integer[] ids) {
        List<Integer> idList = Arrays.asList(ids);
        issueMapper.ignoreIssuesByIds(siteId, idList);
        return 0;
    }

    @Override
    public int deleteWarningBySiteIdAndId(int siteId, Integer[] ids) {
        List<Integer> idList = Arrays.asList(ids);
        issueMapper.delIssueByIds(siteId, idList);
        return 0;
    }

//    @Override
//    public List<IssueWarningResponse> getPageDataWaringList(Integer pageIndex, Integer pageSize, IssueBase issue) throws ParseException {
//        int pageCalculate = pageIndex * pageSize;
//        List<Issue> issueList = issueMapper.getAllWarningList(pageCalculate, pageSize, issue);
//        issueList = IssueDataUtil.getIssueListToSetSubTypeName(issueList);
//        List<IssueWarningResponse> issueWarningResponseList = new ArrayList<>();
//        IssueWarningResponse issueWarningResponse = null;
//        for (Issue is: issueList) {
//            issueWarningResponse = getIssueWarningResponseDetailByIssue(is);
//            issueWarningResponseList.add(issueWarningResponse);
//        }
//        return issueWarningResponseList;
//    }

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
        Long limitTime = between / (24 * 60 * 60 * 1000);
        issueWarningResponse.setLimitTime(limitTime);
        return issueWarningResponse;
    }

//    @Override
//    public int getItemCount(IssueBase issue) {
//        int itemCount = issueMapper.getAllWarningCount(issue);
//        return itemCount;
//    }

    @Override
    public ApiPageData get(PageDataRequestParam param) throws ParseException {
        QueryFilter filter = QueryFilterHelper.toFilter(param, Types.IssueType.INFO_UPDATE_WARNING, Types.IssueType.RESPOND_WARNING);
        filter.addCond(IssueTableField.IS_RESOLVED, Status.Resolve.UN_RESOLVED.value);
        filter.addCond(IssueTableField.IS_DEL, Status.Delete.UN_DELETE.value);
        filter.addCond(IssueTableField.TYPE_ID, 51).setRangeBegin(true);
        filter.addCond(IssueTableField.TYPE_ID, 100).setRangeEnd(true);
        int itemCount = issueMapper.count(filter);
        ApiPageData apiPageData = PageInfoDeal.buildApiPageData(param.getPageIndex(), param.getPageSize(), itemCount);
        filter.setPager(new DBPager((apiPageData.getPager().getCurrPage() - 1) * apiPageData.getPager().getPageSize(), apiPageData.getPager().getPageSize()));
        List<Issue> issueList = issueMapper.select(filter);
        List<IssueWarningResponse> responseByIssueList = getReopnseByIssueList(issueList);
        apiPageData.setData(responseByIssueList);
        return apiPageData;
    }

    private List<IssueWarningResponse> getReopnseByIssueList(List<Issue> issueList) throws ParseException {
        issueList = IssueDataUtil.getIssueListToSetSubTypeName(issueList);
        List<IssueWarningResponse> issueWarningResponseList = new ArrayList<>();
        IssueWarningResponse issueWarningResponse = null;
        for (Issue is : issueList) {
            issueWarningResponse = getIssueWarningResponseDetailByIssue(is);
            issueWarningResponseList.add(issueWarningResponse);
        }
        return issueWarningResponseList;
    }

}

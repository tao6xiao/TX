package com.trs.gov.kpi.service.impl;

import com.trs.gov.kpi.constant.Constants;
import com.trs.gov.kpi.constant.IssueTableField;
import com.trs.gov.kpi.constant.Status;
import com.trs.gov.kpi.constant.Types;
import com.trs.gov.kpi.dao.IssueMapper;
import com.trs.gov.kpi.entity.Issue;
import com.trs.gov.kpi.entity.dao.QueryFilter;
import com.trs.gov.kpi.entity.exception.BizException;
import com.trs.gov.kpi.entity.exception.RemoteException;
import com.trs.gov.kpi.entity.requestdata.PageDataRequestParam;
import com.trs.gov.kpi.entity.responsedata.ApiPageData;
import com.trs.gov.kpi.entity.responsedata.IssueWarningResponse;
import com.trs.gov.kpi.entity.responsedata.Pager;
import com.trs.gov.kpi.service.IntegratedMonitorWarningService;
import com.trs.gov.kpi.service.helper.QueryFilterHelper;
import com.trs.gov.kpi.service.outer.DeptApiService;
import com.trs.gov.kpi.utils.DateUtil;
import com.trs.gov.kpi.utils.IssueDataUtil;
import com.trs.gov.kpi.utils.PageInfoDeal;
import com.trs.gov.kpi.utils.StringUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by he.lang on 2017/5/18.
 */
@Service
public class IntegratedMonitorWarningServiceImpl implements IntegratedMonitorWarningService {
    @Resource
    IssueMapper issueMapper;

    @Resource
    DeptApiService deptApiService;

    private IssueWarningResponse getIssueWarningResponseDetailByIssue(Issue is) throws RemoteException {
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
        if(is.getDeptId() == null){
            issueWarningResponse.setDeptName(Constants.EMPTY_STRING);
        }else {
            issueWarningResponse.setDeptName(deptApiService.findDeptById("", is.getDeptId()).getGName());
        }
        return issueWarningResponse;
    }

    @Override
    public ApiPageData get(PageDataRequestParam param) throws BizException, RemoteException {

        if (!StringUtil.isEmpty(param.getSearchText())) {
            param.setSearchText(StringUtil.escape(param.getSearchText()));
        }

        QueryFilter filter = QueryFilterHelper.toFilter(param, Types.IssueType.INFO_UPDATE_WARNING, Types.IssueType.RESPOND_WARNING);
        filter.addCond(IssueTableField.IS_RESOLVED, Status.Resolve.UN_RESOLVED.value);
        filter.addCond(IssueTableField.IS_DEL, Status.Delete.UN_DELETE.value);
        filter.addCond(IssueTableField.TYPE_ID, 51).setRangeBegin(true);
        filter.addCond(IssueTableField.TYPE_ID, 100).setRangeEnd(true);
        int itemCount = issueMapper.count(filter);
        Pager pager = PageInfoDeal.buildResponsePager(param.getPageIndex(), param.getPageSize(), itemCount);
        filter.setPager(pager);
        List<Issue> issueList = issueMapper.select(filter);
        List<IssueWarningResponse> responseByIssueList = getReopnseByIssueList(issueList);

        return new ApiPageData(pager, responseByIssueList);
    }

    private List<IssueWarningResponse> getReopnseByIssueList(List<Issue> issueList) throws RemoteException {
        List<Issue> fullIssueList = IssueDataUtil.getIssueListToSetSubTypeName(issueList);
        List<IssueWarningResponse> issueWarningResponseList = new ArrayList<>();
        for (Issue is : fullIssueList) {
            issueWarningResponseList.add(getIssueWarningResponseDetailByIssue(is));
        }
        return issueWarningResponseList;
    }

}

package com.trs.gov.kpi.service.impl;

import com.trs.gov.kpi.constant.Constants;
import com.trs.gov.kpi.constant.IssueTableField;
import com.trs.gov.kpi.constant.Status;
import com.trs.gov.kpi.constant.Types;
import com.trs.gov.kpi.dao.IssueMapper;
import com.trs.gov.kpi.entity.Issue;
import com.trs.gov.kpi.entity.dao.QueryFilter;
import com.trs.gov.kpi.entity.exception.RemoteException;
import com.trs.gov.kpi.entity.requestdata.PageDataRequestParam;
import com.trs.gov.kpi.entity.responsedata.ApiPageData;
import com.trs.gov.kpi.entity.responsedata.IssueIsResolvedResponse;
import com.trs.gov.kpi.entity.responsedata.Pager;
import com.trs.gov.kpi.service.IntegratedMonitorIsResolvedService;
import com.trs.gov.kpi.service.helper.QueryFilterHelper;
import com.trs.gov.kpi.service.outer.DeptApiService;
import com.trs.gov.kpi.utils.DateUtil;
import com.trs.gov.kpi.utils.IssueDataUtil;
import com.trs.gov.kpi.utils.PageInfoDeal;
import com.trs.gov.kpi.utils.StringUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by he.lang on 2017/5/19.
 */
@Service
public class IntegratedMonitorIsResolvedServiceImpl implements IntegratedMonitorIsResolvedService {

    @Resource
    IssueMapper issueMapper;

    @Resource
    DeptApiService deptApiService;

    @Override
    public ApiPageData getPageDataIsResolvedList(PageDataRequestParam param, Boolean isResolved) throws RemoteException {

        if (!StringUtil.isEmpty(param.getSearchText())) {
            param.setSearchText(StringUtil.escape(param.getSearchText()));
        }

        QueryFilter filter = QueryFilterHelper.toFilter(param, Types.IssueType.INFO_UPDATE_ISSUE, Types.IssueType.LINK_AVAILABLE_ISSUE, Types.IssueType.INFO_ERROR_ISSUE, Types.IssueType
                .INFO_UPDATE_WARNING, Types.IssueType.RESPOND_WARNING);
        if (isResolved) {
            filter.addCond(IssueTableField.IS_RESOLVED, Status.Resolve.RESOLVED.value);
        } else {
            filter.addCond(IssueTableField.IS_RESOLVED, Status.Resolve.IGNORED.value);
        }
        filter.addCond(IssueTableField.IS_DEL, Status.Delete.UN_DELETE.value);
        filter.addCond(IssueTableField.TYPE_ID, Constants.ISSUE_BEGIN_ID).setRangeBegin(true);
        filter.addCond(IssueTableField.TYPE_ID, Constants.WARNING_END_ID).setRangeEnd(true);

        int itemCount = issueMapper.count(filter);

        Pager pager = PageInfoDeal.buildResponsePager(param.getPageIndex(), param.getPageSize(), itemCount);
        filter.setPager(pager);
        List<Issue> issueList = issueMapper.select(filter);
        issueList = IssueDataUtil.getIssueListToSetSubTypeName(issueList);

        List<IssueIsResolvedResponse> issueIsResolvedResponseDetailList = new ArrayList<>();
        for (Issue is : issueList) {
            issueIsResolvedResponseDetailList.add(getIssueIsResolvedResponseDetailByIssue(is));
        }

        return new ApiPageData(pager, issueIsResolvedResponseDetailList);
    }

    private IssueIsResolvedResponse getIssueIsResolvedResponseDetailByIssue(Issue is) throws RemoteException {
        IssueIsResolvedResponse issueIsResolvedResponseDetail = new IssueIsResolvedResponse();
        issueIsResolvedResponseDetail.setId(is.getId());
        issueIsResolvedResponseDetail.setIssueTypeName(is.getSubTypeName());
        issueIsResolvedResponseDetail.setDetail(is.getDetail());
        issueIsResolvedResponseDetail.setIssueTime(DateUtil.toString(is.getIssueTime()));
        if(is.getDeptId() == null){
            issueIsResolvedResponseDetail.setDeptName(Constants.EMPTY_STRING);
        }else {
            issueIsResolvedResponseDetail.setDeptName(deptApiService.findDeptById("", is.getDeptId()).getGName());
        }
        return issueIsResolvedResponseDetail;
    }
}

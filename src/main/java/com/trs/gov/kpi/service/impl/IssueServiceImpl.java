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
import com.trs.gov.kpi.entity.responsedata.IssueIsNotResolvedResponse;
import com.trs.gov.kpi.entity.responsedata.Pager;
import com.trs.gov.kpi.service.IssueService;
import com.trs.gov.kpi.service.helper.QueryFilterHelper;
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

    private IssueIsNotResolvedResponse toNotResolvedResponse(Issue is) {
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
        QueryFilter filter = QueryFilterHelper.toFilter(param, Types.IssueType.INFO_UPDATE_ISSUE, Types.IssueType.LINK_AVAILABLE_ISSUE, Types.IssueType.INFO_ERROR_ISSUE);
        filter.addCond(IssueTableField.IS_RESOLVED, Status.Resolve.UN_RESOLVED.value);
        filter.addCond(IssueTableField.IS_DEL, Status.Delete.UN_DELETE.value);
        filter.addCond(IssueTableField.TYPE_ID, 1).setRangeBegin(true);
        filter.addCond(IssueTableField.TYPE_ID, 50).setRangeEnd(true);

        int itemCount = issueMapper.count(filter);
        Pager pager = PageInfoDeal.buildResponsePager(param.getPageIndex(), param.getPageSize(), itemCount);
        filter.setPager(pager);
        List<Issue> issueList = issueMapper.select(filter);
        List<IssueIsNotResolvedResponse> responseByIssueList = toResponse(issueList);

        return new ApiPageData(pager, responseByIssueList);
    }

    private List<IssueIsNotResolvedResponse> toResponse(List<Issue> issueList) {
        issueList = IssueDataUtil.getIssueListToSetSubTypeName(issueList);
        List<IssueIsNotResolvedResponse> issueIsNotResolvedResponseList = new ArrayList<>();
        for (Issue is : issueList) {
            issueIsNotResolvedResponseList.add(toNotResolvedResponse(is));
        }
        return issueIsNotResolvedResponseList;
    }
}

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
import com.trs.gov.kpi.entity.responsedata.IssueIsResolvedResponse;
import com.trs.gov.kpi.service.IntegratedMonitorIsResolvedService;
import com.trs.gov.kpi.service.helper.QueryFilterHelper;
import com.trs.gov.kpi.utils.DateUtil;
import com.trs.gov.kpi.utils.IssueDataUtil;
import com.trs.gov.kpi.utils.PageInfoDeal;
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

    @Override
    public ApiPageData getPageDataIsResolvedList(PageDataRequestParam param, Boolean isResolved) {


        QueryFilter filter = QueryFilterHelper.toFilter(param, Types.IssueType.INFO_UPDATE_ISSUE, Types.IssueType.LINK_AVAILABLE_ISSUE, Types.IssueType.INFO_ERROR_ISSUE);
        if (isResolved) {
            filter.addCond(IssueTableField.IS_RESOLVED, Status.Resolve.RESOLVED.value);
        } else {
            filter.addCond(IssueTableField.IS_RESOLVED, Status.Resolve.IGNORED.value);
        }
        filter.addCond(IssueTableField.IS_DEL, Status.Delete.UN_DELETE.value);
        filter.addCond(IssueTableField.TYPE_ID, 1).setRangeBegin(true);
        filter.addCond(IssueTableField.TYPE_ID, 50).setRangeEnd(true);

        int itemCount = issueMapper.count(filter);

        ApiPageData apiPageData = PageInfoDeal.buildApiPageData(param.getPageIndex(), param.getPageSize(), itemCount);
        filter.setPager(new DBPager((apiPageData.getPager().getCurrPage() - 1) * apiPageData.getPager().getPageSize(), apiPageData.getPager().getPageSize()));
        List<Issue> issueList = issueMapper.select(filter);
        issueList = IssueDataUtil.getIssueListToSetSubTypeName(issueList);

        List<IssueIsResolvedResponse> issueIsResolvedResponseDetailList = new ArrayList<>();
        IssueIsResolvedResponse issueIsResolvedResponseDetail = null;
        for (Issue is : issueList) {
            issueIsResolvedResponseDetail = getIssueIsResolvedResponseDetailByIssue(is);
            issueIsResolvedResponseDetailList.add(issueIsResolvedResponseDetail);
        }
        apiPageData.setData(issueIsResolvedResponseDetailList);
        return apiPageData;
    }

//    @Override
//    public int getPageDataIsResolvedItemCount(PageDataRequestParam param, Boolean isResolved) {
//
//        QueryFilter filter = QueryFilterHelper.toFilter(param);
//        if(isResolved){
//            filter.addCond("isResolved", Status.Resolve.RESOLVED.value);
//        }else {
//            filter.addCond("isResolved", Status.Resolve.IGNORED.value);
//        }
//        filter.addCond("isDel", Status.Delete.UN_DELETE.value);
//        filter.addCond("typeId", 1).setRangeBegin(true);
//        filter.addCond("typeId", 50).setRangeEnd(true);
//
//        int num = issueMapper.count(filter);
//        return num;
//    }

    private IssueIsResolvedResponse getIssueIsResolvedResponseDetailByIssue(Issue is) {
        IssueIsResolvedResponse issueIsResolvedResponseDetail = new IssueIsResolvedResponse();
        issueIsResolvedResponseDetail.setId(is.getId());
        issueIsResolvedResponseDetail.setIssueTypeName(is.getSubTypeName());
        issueIsResolvedResponseDetail.setDetail(is.getDetail());
        issueIsResolvedResponseDetail.setIssueTime(DateUtil.toString(is.getIssueTime()));
        return issueIsResolvedResponseDetail;
    }
}

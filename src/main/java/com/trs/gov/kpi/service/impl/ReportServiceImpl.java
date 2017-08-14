package com.trs.gov.kpi.service.impl;

import com.trs.gov.kpi.constant.ReportTableField;
import com.trs.gov.kpi.dao.ReportMapper;
import com.trs.gov.kpi.entity.Report;
import com.trs.gov.kpi.entity.dao.QueryFilter;
import com.trs.gov.kpi.entity.exception.BizException;
import com.trs.gov.kpi.entity.exception.RemoteException;
import com.trs.gov.kpi.entity.requestdata.ReportRequestParam;
import com.trs.gov.kpi.entity.responsedata.ApiPageData;
import com.trs.gov.kpi.entity.responsedata.Pager;
import com.trs.gov.kpi.entity.responsedata.ReportResponse;
import com.trs.gov.kpi.service.ReportService;
import com.trs.gov.kpi.service.helper.QueryFilterHelper;
import com.trs.gov.kpi.service.outer.SiteApiService;
import com.trs.gov.kpi.utils.PageInfoDeal;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ranwei on 2017/6/12.
 */
@Service
public class ReportServiceImpl implements ReportService {

    @Resource
    private ReportMapper reportMapper;

    @Resource
    private SiteApiService siteApiService;

    @Override
    public ApiPageData selectReportList(ReportRequestParam param, boolean isTimeNode) throws RemoteException, BizException {
        QueryFilter filter = QueryFilterHelper.toReportFilter(param, isTimeNode);
        if (isTimeNode) {
            filter.addCond(ReportTableField.TYPE, 1);
        } else {
            filter.addCond(ReportTableField.TYPE, 2);
        }
        int itemCount = reportMapper.selectReportCount(filter);
        Pager pager = PageInfoDeal.buildResponsePager(param.getPageIndex(), param.getPageSize(), itemCount);
        filter.setPager(pager);
        List<Report> reportList = reportMapper.selectReportList(filter);
        List<ReportResponse> list = new ArrayList<>();
        for (Report report : reportList) {
            ReportResponse reportResponse = new ReportResponse();
            reportResponse.setId(report.getId());
            reportResponse.setSiteName(siteApiService.getSiteById(report.getSiteId(), null).getSiteDesc());
            reportResponse.setTitle(report.getTitle());
            reportResponse.setCrTime(report.getCrTime());
            list.add(reportResponse);
        }
        return new ApiPageData(pager, list);
    }

    @Override
    public String getReportPath(ReportRequestParam param, boolean isTimeNode) throws BizException {

        QueryFilter filter = QueryFilterHelper.toReportFilter(param, isTimeNode);
        filter.addCond(ReportTableField.ID, param.getId());
        if (isTimeNode) {
            filter.addCond(ReportTableField.TYPE, 1);
        } else {
            filter.addCond(ReportTableField.TYPE, 2);
        }

        return reportMapper.selectPathById(filter);
    }
}

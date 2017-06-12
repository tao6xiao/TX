package com.trs.gov.kpi.controller;

import com.trs.gov.kpi.entity.exception.RemoteException;
import com.trs.gov.kpi.entity.requestdata.PageDataRequestParam;
import com.trs.gov.kpi.entity.responsedata.ApiPageData;
import com.trs.gov.kpi.service.ReportService;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.text.ParseException;

/**
 * Created by ranwei on 2017/6/9.
 */
@RestController
@RequestMapping(value = "/gov/kpi/report")
public class ReportController {

    @Resource
    private ReportService reportService;

    //TODO  按时间节点和时间区间导出报表

    @RequestMapping(value = "/timenode", method = RequestMethod.GET)
    public ApiPageData selectReportByNode(@ModelAttribute PageDataRequestParam requestParam) throws RemoteException, ParseException {

        return reportService.selectReportList(requestParam, true);
    }

    @RequestMapping(value = "/timenode/export", method = RequestMethod.GET)
    public String exportReportByNode() {
        return null;
    }

    @RequestMapping(value = "/timeinterval", method = RequestMethod.GET)
    public ApiPageData selectReportByInterval(@ModelAttribute PageDataRequestParam requestParam) throws RemoteException, ParseException {

        return reportService.selectReportList(requestParam, false);
    }

    @RequestMapping(value = "/timeinterval/export", method = RequestMethod.GET)
    public String exportReportByInterval() {
        return null;
    }

}

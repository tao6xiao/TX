package com.trs.gov.kpi.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by ranwei on 2017/6/9.
 */
@RestController
@RequestMapping(value = "/gov/kpi/report")
public class ReportController {

    //TODO  按时间节点和时间区间导出报表

    @RequestMapping(value = "/timenode", method = RequestMethod.GET)
    public String selectReportByNode() {
        return null;
    }

    @RequestMapping(value = "/timenode/export", method = RequestMethod.GET)
    public String exportReportByNode() {
        return null;
    }

    @RequestMapping(value = "/timeinterval", method = RequestMethod.GET)
    public String selectReportByInterval() {
        return null;
    }

    @RequestMapping(value = "/timeinterval/export", method = RequestMethod.GET)
    public String exportReportByInterval() {
        return null;
    }

}

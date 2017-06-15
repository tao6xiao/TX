package com.trs.gov.kpi.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.trs.gov.kpi.entity.exception.BizException;
import com.trs.gov.kpi.entity.exception.RemoteException;
import com.trs.gov.kpi.entity.responsedata.DepDocMultiCounterResponse;
import com.trs.gov.kpi.entity.responsedata.DocTypeCounterResponse;
import com.trs.gov.kpi.entity.responsedata.SiteDocMultiCounterResponse;
import com.trs.gov.kpi.entity.responsedata.UserDocMultiCounterResponse;
import com.trs.gov.kpi.service.outer.ReportApiService;
import com.trs.gov.kpi.utils.DateUtil;
import com.trs.gov.kpi.utils.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 稿件统计处理器
 *
 * Created by linwei on 2017/6/15.
 */
@Slf4j
@RestController
@RequestMapping(value = "/gov/kpi/doc/report")
public class DocReportController {

    @Resource
    private ReportApiService reportApiService;

    @RequestMapping(value = "/curmonth/bytype", method = RequestMethod.GET)
    @ResponseBody
    public List<DocTypeCounterResponse> getCurMonthCountByType() throws RemoteException {
        ReportApiService.ReportApiParam param = ReportApiService.ReportApiParamBuilder.newBuilder()
                .setReportName("editcenter_doctype_new_bymonth")
                .setDimensionFields("DocType")
                .setBeginDate(DateUtil.curMonth())
                .build();
        String reportData = reportApiService.getReport(param);
        if (StringUtil.isEmpty(reportData)) {
            return new ArrayList<>();
        } else {
            final ArrayList<DocTypeCounterResponse> responseList = new ArrayList<>();
            final JSONArray objects = JSON.parseArray(reportData);
            for (int index = 0; index < objects.size(); index++) {
                JSONObject data = objects.getJSONObject(index);
                responseList.add(new DocTypeCounterResponse(data.getInteger("DocType"), data.getLong("Count")));
            }
            return responseList;
        }
    }

    @RequestMapping(value = "/bydepartment", method = RequestMethod.GET)
    @ResponseBody
    public List<DepDocMultiCounterResponse> getCounterByDep(@ModelAttribute String beginDateTime, @ModelAttribute String endDateTime) {

        return new ArrayList<>();
    }

    @RequestMapping(value = "/bysite", method = RequestMethod.GET)
    @ResponseBody
    public List<SiteDocMultiCounterResponse> getCounterBySite(@ModelAttribute String beginDateTime, @ModelAttribute String endDateTime) {

        return new ArrayList<>();
    }

    @RequestMapping(value = "/byuser", method = RequestMethod.GET)
    @ResponseBody
    public List<UserDocMultiCounterResponse> getCounterByUser(@ModelAttribute String beginDateTime, @ModelAttribute String endDateTime) {

        return new ArrayList<>();
    }

    @RequestMapping(value = "/curmonth/byday", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Long> getCurMonthCounterByDay(String month) {

        return new LinkedHashMap<>();
    }

}

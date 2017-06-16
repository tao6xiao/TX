package com.trs.gov.kpi.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.trs.gov.kpi.constant.Constants;
import com.trs.gov.kpi.entity.exception.BizException;
import com.trs.gov.kpi.entity.exception.RemoteException;
import com.trs.gov.kpi.entity.responsedata.*;
import com.trs.gov.kpi.service.outer.ReportApiService;
import com.trs.gov.kpi.utils.DateUtil;
import com.trs.gov.kpi.utils.StringUtil;
import javafx.util.Pair;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

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

    private static final String PREX_EDIT_CENTER_REPORT = "editcenter_";

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
    public List<DepDocMultiCounterResponse> getCounterByDep(@ModelAttribute String beginDateTime, @ModelAttribute String endDateTime) throws RemoteException, InstantiationException, IllegalAccessException {
        List<Pair<String, SetFunc<DepDocMultiCounterResponse, String>>> reports = getMultiReportList("department");
        SetFunc<DepDocMultiCounterResponse, String> setDepIdFunc = (counter, value) ->  counter.setDepartmentId(Long.valueOf(value));
        return getMultiCounterReport(reports, "Department", beginDateTime, endDateTime, DepDocMultiCounterResponse.class, setDepIdFunc);
    }

    @RequestMapping(value = "/bysite", method = RequestMethod.GET)
    @ResponseBody
    public List<SiteDocMultiCounterResponse> getCounterBySite(@ModelAttribute String beginDateTime, @ModelAttribute String endDateTime) throws RemoteException, InstantiationException, IllegalAccessException {
        List<Pair<String, SetFunc<SiteDocMultiCounterResponse, String>>> reports = getMultiReportList("site");
        SetFunc<SiteDocMultiCounterResponse, String> setSiteIdFunc = (counter, value) ->  counter.setSiteId(Long.valueOf(value));
        return getMultiCounterReport(reports, "Site", beginDateTime, endDateTime, SiteDocMultiCounterResponse.class, setSiteIdFunc);
    }

    @RequestMapping(value = "/byuser", method = RequestMethod.GET)
    @ResponseBody
    public List<UserDocMultiCounterResponse> getCounterByUser(@ModelAttribute String beginDateTime, @ModelAttribute String endDateTime) throws RemoteException, InstantiationException, IllegalAccessException {
        List<Pair<String, SetFunc<UserDocMultiCounterResponse, String>>> reports  = getMultiReportList("user");
        SetFunc<UserDocMultiCounterResponse, String> setUserIdFunc = (counter, value) ->  counter.setUserId(Long.valueOf(value));
        return getMultiCounterReport(reports, "User", beginDateTime, endDateTime, UserDocMultiCounterResponse.class, setUserIdFunc);
    }

    @RequestMapping(value = "/curmonth/byday", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, String> getCurMonthCounterByDay() throws RemoteException {
        Calendar now = Calendar.getInstance();// 当前起始日期
        Date curDate = new Date();
        now.setTime(curDate);

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
        String monthPrefix = format.format(curDate);
        final int curDay = now.get(Calendar.DAY_OF_MONTH);
        Map<String, String> allMonthReport = new LinkedHashMap<>();
        for (int index = 1; index <= curDay; index++) {
            allMonthReport.put(monthPrefix +  String.format("-%02d", index), "0");
        }

        final Map<String, String> reportData = getDocReport(PREX_EDIT_CENTER_REPORT + "site_yifa_doc_byday", "CRDay", null, null);
        allMonthReport.putAll(reportData);
        return allMonthReport;
    }

    @RequestMapping(value = "/multi/onemonth", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Long> getMultiOfOneMonth(String month) throws RemoteException, ParseException, BizException {
        if (StringUtil.isEmpty(month)) {
            throw new BizException(Constants.INVALID_PARAMETER);
        }
        if (!DateUtil.isValidMonth(month)) {
            throw new BizException(Constants.INVALID_PARAMETER);
        }

        Calendar nextMonthCalendar = Calendar.getInstance();// 当前起始日期

        String beginDay = month + "-01 00:00:00";
        nextMonthCalendar.setTime(DateUtil.toDate(beginDay));
        nextMonthCalendar.set(Calendar.MONTH, nextMonthCalendar.get(Calendar.MONTH) + 1);
        String endDay = DateUtil.toString(new Date(nextMonthCalendar.getTime().getTime() - 1000));

        Map<String, Long> result = new HashMap<>();
        final Map<String, String> newDocReportData = getDocReport(PREX_EDIT_CENTER_REPORT + "site_new_doc_byday", "Site", beginDay, endDay);
        result.put("newDoc", countMap(newDocReportData));
        final Map<String, String> yifaReportData = getDocReport(PREX_EDIT_CENTER_REPORT + "site_yifa_doc_byday", "Site", beginDay, endDay);
        result.put("yifa", countMap(yifaReportData));
        final Map<String, String> pushReportData = getDocReport(PREX_EDIT_CENTER_REPORT + "site_push_doc_byday", "Site", beginDay, endDay);
        result.put("push", countMap(pushReportData));
        final Map<String, String> distributeReportData = getDocReport(PREX_EDIT_CENTER_REPORT + "site_distribute_doc_byday", "Site", beginDay, endDay);
        result.put("distribute", countMap(distributeReportData));

        return result;
    }

    private Long countMap(Map<String, String> countMap) {
        Long result = 0L;
        for (String value : countMap.values()) {
            result += Long.valueOf(value);
        }
        return result;
    }


    private <T extends DocMultiCounterResponse> List<Pair<String, SetFunc<T, String>>> getMultiReportList(String byName) {
        List<Pair<String, SetFunc<T, String>>> reports = new ArrayList<>();
        reports.add(new Pair<>(PREX_EDIT_CENTER_REPORT + byName + "_new_doc_byday", (counter, value) ->  counter.setNewDocCount(Long.valueOf(value))));
        reports.add(new Pair<>(PREX_EDIT_CENTER_REPORT + byName + "_copy_doc_byday", (counter, value) ->  counter.setCopyDocCount(Long.valueOf(value))));
        reports.add(new Pair<>(PREX_EDIT_CENTER_REPORT + byName + "_quote_doc_byday", (counter, value) ->  counter.setQuoteDocCount(Long.valueOf(value))));
        reports.add(new Pair<>(PREX_EDIT_CENTER_REPORT + byName + "_mirror_doc_byday", (counter, value) ->  counter.setMirrorDocCount(Long.valueOf(value))));
        reports.add(new Pair<>(PREX_EDIT_CENTER_REPORT + byName + "_daibian_doc_byday", (counter, value) ->  counter.setDaibianDocCount(Long.valueOf(value))));
        reports.add(new Pair<>(PREX_EDIT_CENTER_REPORT + byName + "_daishen_doc_byday", (counter, value) ->  counter.setDaishenDocCount(Long.valueOf(value))));
        reports.add(new Pair<>(PREX_EDIT_CENTER_REPORT + byName + "_daiqian_doc_byday", (counter, value) ->  counter.setDaiqianDocCount(Long.valueOf(value))));
        reports.add(new Pair<>(PREX_EDIT_CENTER_REPORT + byName + "_yifa_doc_byday", (counter, value) ->  counter.setYifaDocCount(Long.valueOf(value))));
        reports.add(new Pair<>(PREX_EDIT_CENTER_REPORT + byName + "_push_doc_byday", (counter, value) ->  counter.setPushDocCount(Long.valueOf(value))));
        reports.add(new Pair<>(PREX_EDIT_CENTER_REPORT + byName + "_distribute_doc_byday", (counter, value) ->  counter.setDistributeDocCount(Long.valueOf(value))));
        return reports;
    }

    private <T> List<T> getMultiCounterReport(
            List<Pair<String, SetFunc<T, String>>> reports,
            String dimensionFields, String beginDateTime, String endDateTime,
            Class<T> counterClass, SetFunc<T, String> setIdFunc) throws RemoteException, InstantiationException, IllegalAccessException {
        Map<String, T> counterMap = new HashMap<>();
        for (Pair<String, SetFunc<T, String>> report : reports) {
            Map<String, String> reportData = getDocReport(report.getKey(), dimensionFields, beginDateTime, endDateTime);
            setCounter(counterMap, reportData, counterClass, setIdFunc, report.getValue());
        }

        final List<T> counterList = new ArrayList<>();
        final Collection<T> counters = counterMap.values();
        for (T counter : counters) {
            counterList.add(counter);
        }
        return counterList;
    }

    private Map<String, String> getDocReport(String reportName, String dimensionFields, String beginDateTime, String endDateTime) throws RemoteException {
        ReportApiService.ReportApiParam param = ReportApiService.ReportApiParamBuilder.newBuilder()
                .setReportName(reportName)
                .setDimensionFields(dimensionFields)
                .setBeginDate(beginDateTime)
                .setEndDate(endDateTime)
                .build();

        String reportData = reportApiService.getReport(param);
        if (StringUtil.isEmpty(reportData)) {
            return new HashMap<>();
        } else {
            Map<String, String> result = new HashMap<>();
            JSONArray datas = JSON.parseArray(reportData);
            for (int index = 0; index < datas.size(); index++) {
                JSONObject data = datas.getJSONObject(index);
                result.put(data.getString(dimensionFields), data.getString("Count"));
            }
            return result;
        }
    }

    private <T> void setCounter(Map<String, T> counterMap, Map<String, String> newDocReport, Class<T> counterClass, SetFunc<T, String> setIdFunc, SetFunc<T, String> setCounterFunc) throws IllegalAccessException, InstantiationException {
        final Iterator<Map.Entry<String, String>> newDocIterator = newDocReport.entrySet().iterator();
        while (newDocIterator.hasNext()) {
            final Map.Entry<String, String> newDocEntry = newDocIterator.next();
            String key = newDocEntry.getKey();
            T counter = counterMap.get(key);
            if (counter == null) {
                counter = counterClass.newInstance();
                setIdFunc.apply(counter, key);
                counterMap.put(key, counter);
            }
            setCounterFunc.apply(counter, newDocEntry.getValue());
        }
    }

    @FunctionalInterface
    interface SetFunc<A, B> {
        void apply (A a, B b);
    }
}

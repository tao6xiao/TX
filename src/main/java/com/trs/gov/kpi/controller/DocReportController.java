package com.trs.gov.kpi.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.trs.gov.kpi.entity.exception.RemoteException;
import com.trs.gov.kpi.entity.responsedata.DepDocMultiCounterResponse;
import com.trs.gov.kpi.entity.responsedata.DocTypeCounterResponse;
import com.trs.gov.kpi.entity.responsedata.SiteDocMultiCounterResponse;
import com.trs.gov.kpi.entity.responsedata.UserDocMultiCounterResponse;
import com.trs.gov.kpi.service.outer.ReportApiService;
import com.trs.gov.kpi.utils.DateUtil;
import com.trs.gov.kpi.utils.StringUtil;
import javafx.util.Pair;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
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
        List<Pair<String, SetFunc<DepDocMultiCounterResponse, String>>> reports = new ArrayList<>();
        reports.add(new Pair<>("editcenter_department_new_doc_byday", (counter, value) ->  counter.setNewDocCount(Long.valueOf(value))));
        reports.add(new Pair<>("editcenter_department_copy_doc_byday", (counter, value) ->  counter.setCopyDocCount(Long.valueOf(value))));
        reports.add(new Pair<>("editcenter_department_quote_doc_byday", (counter, value) ->  counter.setQuoteDocCount(Long.valueOf(value))));
        reports.add(new Pair<>("editcenter_department_mirror_doc_byday", (counter, value) ->  counter.setMirrorDocCount(Long.valueOf(value))));
        reports.add(new Pair<>("editcenter_department_daibian_doc_byday", (counter, value) ->  counter.setDaibianDocCount(Long.valueOf(value))));
        reports.add(new Pair<>("editcenter_department_daishen_doc_byday", (counter, value) ->  counter.setDaishenDocCount(Long.valueOf(value))));
        reports.add(new Pair<>("editcenter_department_daiqian_doc_byday", (counter, value) ->  counter.setDaiqianDocCount(Long.valueOf(value))));
        reports.add(new Pair<>("editcenter_department_yifa_doc_byday", (counter, value) ->  counter.setYifaDocCount(Long.valueOf(value))));
        reports.add(new Pair<>("editcenter_department_push_doc_byday", (counter, value) ->  counter.setPushDocCount(Long.valueOf(value))));
        reports.add(new Pair<>("editcenter_department_distribute_doc_byday", (counter, value) ->  counter.setDistributeDocCount(Long.valueOf(value))));

        SetFunc<DepDocMultiCounterResponse, String> setDepIdFunc = (counter, value) ->  counter.setDepartmentId(Long.valueOf(value));
        return getMultiCounterReport(reports, "Department", beginDateTime, endDateTime, DepDocMultiCounterResponse.class, setDepIdFunc);
    }

    @RequestMapping(value = "/bysite", method = RequestMethod.GET)
    @ResponseBody
    public List<SiteDocMultiCounterResponse> getCounterBySite(@ModelAttribute String beginDateTime, @ModelAttribute String endDateTime) throws RemoteException, InstantiationException, IllegalAccessException {
        List<Pair<String, SetFunc<SiteDocMultiCounterResponse, String>>> reports = new ArrayList<>();
        reports.add(new Pair<>("editcenter_site_new_doc_byday", (counter, value) ->  counter.setNewDocCount(Long.valueOf(value))));
        reports.add(new Pair<>("editcenter_site_copy_doc_byday", (counter, value) ->  counter.setCopyDocCount(Long.valueOf(value))));
        reports.add(new Pair<>("editcenter_site_quote_doc_byday", (counter, value) ->  counter.setQuoteDocCount(Long.valueOf(value))));
        reports.add(new Pair<>("editcenter_site_mirror_doc_byday", (counter, value) ->  counter.setMirrorDocCount(Long.valueOf(value))));
        reports.add(new Pair<>("editcenter_site_daibian_doc_byday", (counter, value) ->  counter.setDaibianDocCount(Long.valueOf(value))));
        reports.add(new Pair<>("editcenter_site_daishen_doc_byday", (counter, value) ->  counter.setDaishenDocCount(Long.valueOf(value))));
        reports.add(new Pair<>("editcenter_site_daiqian_doc_byday", (counter, value) ->  counter.setDaiqianDocCount(Long.valueOf(value))));
        reports.add(new Pair<>("editcenter_site_yifa_doc_byday", (counter, value) ->  counter.setYifaDocCount(Long.valueOf(value))));
        reports.add(new Pair<>("editcenter_site_push_doc_byday", (counter, value) ->  counter.setPushDocCount(Long.valueOf(value))));
        reports.add(new Pair<>("editcenter_site_distribute_doc_byday", (counter, value) ->  counter.setDistributeDocCount(Long.valueOf(value))));

        SetFunc<SiteDocMultiCounterResponse, String> setSiteIdFunc = (counter, value) ->  counter.setSiteId(Long.valueOf(value));
        return getMultiCounterReport(reports, "Site", beginDateTime, endDateTime, SiteDocMultiCounterResponse.class, setSiteIdFunc);
    }

    @RequestMapping(value = "/byuser", method = RequestMethod.GET)
    @ResponseBody
    public List<UserDocMultiCounterResponse> getCounterByUser(@ModelAttribute String beginDateTime, @ModelAttribute String endDateTime) throws RemoteException, InstantiationException, IllegalAccessException {
        List<Pair<String, SetFunc<UserDocMultiCounterResponse, String>>> reports = new ArrayList<>();
        reports.add(new Pair<>("editcenter_user_new_doc_byday", (counter, value) ->  counter.setNewDocCount(Long.valueOf(value))));
        reports.add(new Pair<>("editcenter_user_copy_doc_byday", (counter, value) ->  counter.setCopyDocCount(Long.valueOf(value))));
        reports.add(new Pair<>("editcenter_user_quote_doc_byday", (counter, value) ->  counter.setQuoteDocCount(Long.valueOf(value))));
        reports.add(new Pair<>("editcenter_user_mirror_doc_byday", (counter, value) ->  counter.setMirrorDocCount(Long.valueOf(value))));
        reports.add(new Pair<>("editcenter_user_daibian_doc_byday", (counter, value) ->  counter.setDaibianDocCount(Long.valueOf(value))));
        reports.add(new Pair<>("editcenter_user_daishen_doc_byday", (counter, value) ->  counter.setDaishenDocCount(Long.valueOf(value))));
        reports.add(new Pair<>("editcenter_user_daiqian_doc_byday", (counter, value) ->  counter.setDaiqianDocCount(Long.valueOf(value))));
        reports.add(new Pair<>("editcenter_user_yifa_doc_byday", (counter, value) ->  counter.setYifaDocCount(Long.valueOf(value))));
        reports.add(new Pair<>("editcenter_user_push_doc_byday", (counter, value) ->  counter.setPushDocCount(Long.valueOf(value))));
        reports.add(new Pair<>("editcenter_user_distribute_doc_byday", (counter, value) ->  counter.setDistributeDocCount(Long.valueOf(value))));

        SetFunc<UserDocMultiCounterResponse, String> setUserIdFunc = (counter, value) ->  counter.setUserId(Long.valueOf(value));
        return getMultiCounterReport(reports, "User", beginDateTime, endDateTime, UserDocMultiCounterResponse.class, setUserIdFunc);
    }

    @RequestMapping(value = "/curmonth/byday", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Long> getCurMonthCounterByDay(String month) {

        return new LinkedHashMap<>();
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

package com.trs.gov.kpi.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.trs.gov.kpi.constant.Authority;
import com.trs.gov.kpi.constant.Constants;
import com.trs.gov.kpi.constant.OperationType;
import com.trs.gov.kpi.entity.exception.BizException;
import com.trs.gov.kpi.entity.exception.RemoteException;
import com.trs.gov.kpi.entity.responsedata.*;
import com.trs.gov.kpi.service.outer.AuthorityService;
import com.trs.gov.kpi.service.outer.ReportApiService;
import com.trs.gov.kpi.service.outer.SiteApiService;
import com.trs.gov.kpi.utils.DateUtil;
import com.trs.gov.kpi.utils.LogUtil;
import com.trs.gov.kpi.utils.StringUtil;
import javafx.util.Pair;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 稿件统计处理器
 * <p>
 * Created by linwei on 2017/6/15.
 */
@Slf4j
@RestController
@RequestMapping(value = "/gov/kpi/doc/report")
public class DocReportController {

    public static final String KEY_TOTAL_COUNT = "totalCount";
    public static final String KEY_DATA = "data";
    public static final String SITE_YIFA_DOC_BYDAY = "site_yifa_doc_byday";
    @Resource
    private ReportApiService reportApiService;

    @Resource
    private AuthorityService authorityService;

    @Resource
    private ApplicationContext applicationContext;

    @Resource
    private SiteApiService siteApiService;

    private static final String PREX_EDIT_CENTER_REPORT = "editcenter_";

    /**
     * 本月新增文档分类型统计
     *
     * @return
     * @throws RemoteException
     * @throws BizException
     */
    @RequestMapping(value = "/curmonth/bytype", method = RequestMethod.GET)
    @ResponseBody
    public List<DocTypeCounterResponse> getCurMonthCountByType(Integer siteId) throws RemoteException, BizException {
        Date startTime = new Date();
        String logDesc = "本月新增文档分类型统计查询";
        authorityService.checkRight(Authority.KPIWEB_STATISTICS_DOCUMENT, siteId);
        try {
            ReportApiService.ReportApiParam param = ReportApiService.ReportApiParamBuilder.newBuilder()
                    .setReportName("editcenter_doctype_new_bymonth")
                    .setDimensionFields("DocType")
                    .setBeginDate(DateUtil.curMonth())
                    .build();

            String reportData = reportApiService.getReport(param);
            if (StringUtil.isEmpty(reportData)) {
                Date endTime = new Date();
                LogUtil.addOperationLog(OperationType.QUERY, logDesc, "");
                LogUtil.addElapseLog(OperationType.QUERY, LogUtil.buildElapseLogDesc(siteApiService, siteId, logDesc), endTime.getTime() - startTime.getTime());
                return new ArrayList<>();
            } else {
                final ArrayList<DocTypeCounterResponse> responseList = new ArrayList<>();
                final JSONArray objects = JSON.parseArray(reportData);
                for (int index = 0; index < objects.size(); index++) {
                    JSONObject data = objects.getJSONObject(index);
                    responseList.add(new DocTypeCounterResponse(data.getInteger("DocType"), data.getLong("Count")));
                }
                Date endTime = new Date();
                LogUtil.addOperationLog(OperationType.QUERY, logDesc, LogUtil.getSiteNameForLog(siteApiService, siteId));
                LogUtil.addElapseLog(OperationType.QUERY, LogUtil.buildElapseLogDesc(siteApiService, siteId, logDesc), endTime.getTime() - startTime.getTime());
                return responseList;
            }
        } catch (Exception e) {
            LogUtil.addOperationLog(OperationType.QUERY, LogUtil.buildFailOperationLogDesc(logDesc), LogUtil.getSiteNameForLog(siteApiService, siteId));
            throw e;
        }
    }

    /**
     * 按部门统计稿件
     *
     * @param beginDateTime
     * @param endDateTime
     * @return
     * @throws RemoteException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws BizException
     */
    @RequestMapping(value = "/bydepartment", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> getCounterByDep(Integer siteId, String beginDateTime, String endDateTime) throws RemoteException, InstantiationException, IllegalAccessException,
            BizException {
        Date startTime = new Date();
        String logDesc = "查询按部门统计的稿件信息";
        authorityService.checkRight(Authority.KPIWEB_STATISTICS_DOCUMENT, siteId);
        try {
            List<Pair<String, SetFunc<DepDocMultiCounterResponse, String>>> reports = getMultiReportList("department");
            SetFunc<DepDocMultiCounterResponse, String> setDepIdFunc = (counter, value) -> counter.setDepartmentId(Long.valueOf(value));
            List<DepDocMultiCounterResponse> allReports = getMultiCounterReport(reports, "Department", beginDateTime, endDateTime, DepDocMultiCounterResponse.class, setDepIdFunc);
            Map<String, Object> result = getResult(allReports);
            Date endTime = new Date();
            LogUtil.addOperationLog(OperationType.QUERY, logDesc, LogUtil.getSiteNameForLog(siteApiService, siteId));
            LogUtil.addElapseLog(OperationType.QUERY, LogUtil.buildElapseLogDesc(siteApiService, siteId, logDesc), endTime.getTime() - startTime.getTime());
            return result;
        } catch (Exception e) {
            LogUtil.addOperationLog(OperationType.QUERY, LogUtil.buildFailOperationLogDesc(logDesc), LogUtil.getSiteNameForLog(siteApiService, siteId));
            throw e;
        }
    }

    private Map<String, Object> getResult(List allReports) {
        Map<String, Object> result = new HashMap<>();
        result.put(KEY_TOTAL_COUNT, countAll(allReports));
        result.put(KEY_DATA, allReports);
        return result;
    }

    /**
     * 按站点统计稿件
     *
     * @param beginDateTime
     * @param endDateTime
     * @return
     * @throws RemoteException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws BizException
     */
    @RequestMapping(value = "/bysite", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> getCounterBySite(Integer siteId, String beginDateTime, String endDateTime) throws RemoteException, InstantiationException, IllegalAccessException,
            BizException {
        Date startTime = new Date();
        String logDesc = "查询按站点统计的稿件信息";
        authorityService.checkRight(Authority.KPIWEB_STATISTICS_DOCUMENT, siteId);
        try {
            List<Pair<String, SetFunc<SiteDocMultiCounterResponse, String>>> reports = getMultiReportList("site");
            SetFunc<SiteDocMultiCounterResponse, String> setSiteIdFunc = (counter, value) -> counter.setSiteId(Long.valueOf(value));
            final java.util.List<SiteDocMultiCounterResponse> allReports = getMultiCounterReport(reports, "Site", beginDateTime, endDateTime, SiteDocMultiCounterResponse.class,
                    setSiteIdFunc);
            Map<String, Object> result = getResult(allReports);
            Date endTime = new Date();
            LogUtil.addOperationLog(OperationType.QUERY, logDesc, LogUtil.getSiteNameForLog(siteApiService, siteId));
            LogUtil.addElapseLog(OperationType.QUERY, LogUtil.buildElapseLogDesc(siteApiService, siteId, logDesc), endTime.getTime() - startTime.getTime());
            return result;
        } catch (Exception e) {
            LogUtil.addOperationLog(OperationType.QUERY, LogUtil.buildFailOperationLogDesc(logDesc), LogUtil.getSiteNameForLog(siteApiService, siteId));
            throw e;
        }
    }

    /**
     * 按个人统计稿件
     *
     * @param beginDateTime
     * @param endDateTime
     * @return
     * @throws RemoteException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws BizException
     */
    @RequestMapping(value = "/byuser", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> getCounterByUser(Integer siteId, String beginDateTime, String endDateTime) throws RemoteException, InstantiationException, IllegalAccessException,
            BizException {
        Date startTime = new Date();
        String logDesc = "查询按个人统计的稿件信息";
        authorityService.checkRight(Authority.KPIWEB_STATISTICS_DOCUMENT, siteId);

        try {
            List<Pair<String, SetFunc<UserDocMultiCounterResponse, String>>> reports = getMultiReportList("user");
            SetFunc<UserDocMultiCounterResponse, String> setUserIdFunc = (counter, value) -> counter.setUserName(value);
            final List<UserDocMultiCounterResponse> allReports = getMultiCounterReport(reports, "User", beginDateTime, endDateTime, UserDocMultiCounterResponse.class, setUserIdFunc);
            Map<String, Object> result = getResult(allReports);
            Date endTime = new Date();
            LogUtil.addOperationLog(OperationType.QUERY, logDesc, LogUtil.getSiteNameForLog(siteApiService, siteId));
            LogUtil.addElapseLog(OperationType.QUERY, LogUtil.buildElapseLogDesc(siteApiService, siteId, logDesc), endTime.getTime() - startTime.getTime());
            return result;
        } catch (Exception e) {
            LogUtil.addOperationLog(OperationType.QUERY, LogUtil.buildFailOperationLogDesc(logDesc), LogUtil.getSiteNameForLog(siteApiService, siteId));
            throw e;
        }
    }

    /**
     * 本月每天发稿量统计
     *
     * @return
     * @throws RemoteException
     * @throws BizException
     */
    @RequestMapping(value = "/curmonth/byday", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, String> getCurMonthCounterByDay(Integer siteId) throws RemoteException, BizException {
        Date startTime = new Date();
        String logDesc = "查询本月每天发稿量统计信息";
        authorityService.checkRight(Authority.KPIWEB_STATISTICS_DOCUMENT, siteId);
        try {
            Calendar now = Calendar.getInstance();// 当前起始日期
            Date curDate = new Date();
            now.setTime(curDate);

            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
            String monthPrefix = format.format(curDate);
            final int curDay = now.get(Calendar.DAY_OF_MONTH);
            Map<String, String> allMonthReport = new LinkedHashMap<>();
            for (int index = 1; index <= curDay; index++) {
                allMonthReport.put(monthPrefix + String.format("-%02d", index), "0");
            }
            now.add(Calendar.DAY_OF_MONTH, 1);
            String endDay = DateUtil.toString(now.getTime());
            now.set(Calendar.DAY_OF_MONTH, 1);
            String beginDay = DateUtil.toString(now.getTime());
            final Map<String, String> reportData = getDocReport(PREX_EDIT_CENTER_REPORT + SITE_YIFA_DOC_BYDAY, "CRDay", beginDay, endDay);
            allMonthReport.putAll(reportData);
            Date endTime = new Date();
            LogUtil.addOperationLog(OperationType.QUERY, logDesc, LogUtil.getSiteNameForLog(siteApiService, siteId));
            LogUtil.addElapseLog(OperationType.QUERY, LogUtil.buildElapseLogDesc(siteApiService, siteId, logDesc), endTime.getTime() - startTime.getTime());
            return allMonthReport;
        } catch (Exception e) {
            LogUtil.addOperationLog(OperationType.QUERY, LogUtil.buildFailOperationLogDesc(logDesc), LogUtil.getSiteNameForLog(siteApiService, siteId));
            throw e;
        }
    }

    /**
     * 原稿，已发，上报，下达历史数据量统计查询
     *
     * @param month
     * @return
     * @throws RemoteException
     * @throws ParseException
     * @throws BizException
     */
    @RequestMapping(value = "/multi/onemonth", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Long> getMultiOfOneMonth(Integer siteId, String month) throws RemoteException, ParseException, BizException {
        Date startTime = new Date();
        String logDesc = "原稿，已发，上报，下达历史数据量统计查询";
        authorityService.checkRight(Authority.KPIWEB_STATISTICS_DOCUMENT, siteId);
        if (StringUtil.isEmpty(month)) {
            LogUtil.addOperationLog(OperationType.QUERY, LogUtil.buildFailOperationLogDesc(logDesc), LogUtil.getSiteNameForLog(siteApiService, siteId));
            throw new BizException(Constants.INVALID_PARAMETER);
        }
        if (!DateUtil.isValidMonth(month)) {
            LogUtil.addOperationLog(OperationType.QUERY, LogUtil.buildFailOperationLogDesc(logDesc), LogUtil.getSiteNameForLog(siteApiService, siteId));
            throw new BizException(Constants.INVALID_PARAMETER);
        }
        try {
            Calendar nextMonthCalendar = Calendar.getInstance();// 当前起始日期

            String beginDay = month + "-01 00:00:00";
            nextMonthCalendar.setTime(DateUtil.toDate(beginDay));
            nextMonthCalendar.set(Calendar.MONTH, nextMonthCalendar.get(Calendar.MONTH) + 1);
            String endDay = DateUtil.toString(new Date(nextMonthCalendar.getTime().getTime() - 1000));

            Map<String, Long> result = new HashMap<>();
            final Map<String, String> newDocReportData = getDocReport(PREX_EDIT_CENTER_REPORT + "site_new_doc_byday", "Site", beginDay, endDay);
            result.put("newDoc", countMap(newDocReportData));
            final Map<String, String> yifaReportData = getDocReport(PREX_EDIT_CENTER_REPORT + SITE_YIFA_DOC_BYDAY, "Site", beginDay, endDay);
            result.put("yifa", countMap(yifaReportData));
            final Map<String, String> pushReportData = getDocReport(PREX_EDIT_CENTER_REPORT + "site_push_doc_byday", "Site", beginDay, endDay);
            result.put("push", countMap(pushReportData));
            final Map<String, String> distributeReportData = getDocReport(PREX_EDIT_CENTER_REPORT + "site_distribute_doc_byday", "Site", beginDay, endDay);
            result.put("distribute", countMap(distributeReportData));
            Date endTime = new Date();
            LogUtil.addOperationLog(OperationType.QUERY, logDesc, LogUtil.getSiteNameForLog(siteApiService, siteId));
            LogUtil.addElapseLog(OperationType.QUERY, LogUtil.buildElapseLogDesc(siteApiService, siteId, logDesc), endTime.getTime() - startTime.getTime());
            return result;
        } catch (Exception e) {
            LogUtil.addOperationLog(OperationType.QUERY, LogUtil.buildFailOperationLogDesc(logDesc), LogUtil.getSiteNameForLog(siteApiService, siteId));
            throw e;
        }
    }

    /**
     * 本月新增文档状态统计
     *
     * @return
     * @throws RemoteException
     * @throws BizException
     */
    @RequestMapping(value = "/curmonth/bystatus", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Long> getCurMonthDocStatusReport(Integer siteId) throws RemoteException, BizException {
        Date startTime = new Date();
        String logDesc = "本月新增文档状态统计";
        authorityService.checkRight(Authority.KPIWEB_STATISTICS_DOCUMENT, siteId);
        try {
            String beginDay = DateUtil.curMonth();
            Map<String, Long> result = new HashMap<>();
            final Map<String, String> daibianReportData = getDocReport(PREX_EDIT_CENTER_REPORT + "site_daibian_doc_byday", "Site", beginDay, null);
            result.put("daibian", countMap(daibianReportData));
            final Map<String, String> daishenReportData = getDocReport(PREX_EDIT_CENTER_REPORT + "site_daishen_doc_byday", "Site", beginDay, null);
            result.put("daishen", countMap(daishenReportData));
            final Map<String, String> daiqianReportData = getDocReport(PREX_EDIT_CENTER_REPORT + "site_daiqian_doc_byday", "Site", beginDay, null);
            result.put("daiqian", countMap(daiqianReportData));
            final Map<String, String> yifaReportData = getDocReport(PREX_EDIT_CENTER_REPORT + SITE_YIFA_DOC_BYDAY, "Site", beginDay, null);
            result.put("yifa", countMap(yifaReportData));
            Date endTime = new Date();
            LogUtil.addOperationLog(OperationType.QUERY, logDesc, LogUtil.getSiteNameForLog(siteApiService, siteId));
            LogUtil.addElapseLog(OperationType.QUERY, LogUtil.buildElapseLogDesc(siteApiService, siteId, logDesc), endTime.getTime() - startTime.getTime());

            return result;
        } catch (Exception e) {
            LogUtil.addOperationLog(OperationType.QUERY, LogUtil.buildFailOperationLogDesc(logDesc), LogUtil.getSiteNameForLog(siteApiService, siteId));
            throw e;
        }
    }

    private <T extends DocMultiCounterResponse> Long countAll(List<T> counters) {
        Long result = 0L;
        for (T counter : counters) {
            result += counter.getNewDocCount();
            result += counter.getCopyDocCount();
            result += counter.getQuoteDocCount();
            result += counter.getMirrorDocCount();
        }
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
        reports.add(new Pair<>(PREX_EDIT_CENTER_REPORT + byName + "_new_doc_byday", (counter, value) -> counter.setNewDocCount(Long.valueOf(value))));
        reports.add(new Pair<>(PREX_EDIT_CENTER_REPORT + byName + "_copy_doc_byday", (counter, value) -> counter.setCopyDocCount(Long.valueOf(value))));
        reports.add(new Pair<>(PREX_EDIT_CENTER_REPORT + byName + "_quote_doc_byday", (counter, value) -> counter.setQuoteDocCount(Long.valueOf(value))));
        reports.add(new Pair<>(PREX_EDIT_CENTER_REPORT + byName + "_mirror_doc_byday", (counter, value) -> counter.setMirrorDocCount(Long.valueOf(value))));
        reports.add(new Pair<>(PREX_EDIT_CENTER_REPORT + byName + "_daibian_doc_byday", (counter, value) -> counter.setDaibianDocCount(Long.valueOf(value))));
        reports.add(new Pair<>(PREX_EDIT_CENTER_REPORT + byName + "_daishen_doc_byday", (counter, value) -> counter.setDaishenDocCount(Long.valueOf(value))));
        reports.add(new Pair<>(PREX_EDIT_CENTER_REPORT + byName + "_daiqian_doc_byday", (counter, value) -> counter.setDaiqianDocCount(Long.valueOf(value))));
        reports.add(new Pair<>(PREX_EDIT_CENTER_REPORT + byName + "_yifa_doc_byday", (counter, value) -> counter.setYifaDocCount(Long.valueOf(value))));
        reports.add(new Pair<>(PREX_EDIT_CENTER_REPORT + byName + "_push_doc_byday", (counter, value) -> counter.setPushDocCount(Long.valueOf(value))));
        reports.add(new Pair<>(PREX_EDIT_CENTER_REPORT + byName + "_distribute_doc_byday", (counter, value) -> counter.setDistributeDocCount(Long.valueOf(value))));
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

    private <T> void setCounter(Map<String, T> counterMap, Map<String, String> newDocReport, Class<T> counterClass, SetFunc<T, String> setIdFunc, SetFunc<T, String> setCounterFunc) throws
            IllegalAccessException, InstantiationException {
        final Iterator<Map.Entry<String, String>> newDocIterator = newDocReport.entrySet().iterator();
        while (newDocIterator.hasNext()) {
            final Map.Entry<String, String> newDocEntry = newDocIterator.next();
            String key = newDocEntry.getKey();
            T counter = counterMap.get(key);
            if (counter == null) {
                counter = applicationContext.getBean(counterClass);
//                counter = counterClass.newInstance();
                setIdFunc.apply(counter, key);
                counterMap.put(key, counter);
            }
            setCounterFunc.apply(counter, newDocEntry.getValue());
        }
    }

    @FunctionalInterface
    interface SetFunc<A, B> {
        void apply(A a, B b);
    }
}

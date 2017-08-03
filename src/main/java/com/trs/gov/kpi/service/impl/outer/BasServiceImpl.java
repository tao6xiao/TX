package com.trs.gov.kpi.service.impl.outer;

import com.alibaba.fastjson.JSON;
import com.squareup.okhttp.*;
import com.trs.gov.kpi.constant.ErrorType;
import com.trs.gov.kpi.constant.Granularity;
import com.trs.gov.kpi.constant.OperationType;
import com.trs.gov.kpi.entity.HistoryDate;
import com.trs.gov.kpi.entity.MonitorSiteDeal;
import com.trs.gov.kpi.entity.exception.BizException;
import com.trs.gov.kpi.entity.exception.RemoteException;
import com.trs.gov.kpi.entity.outerapi.bas.BasPVResponse;
import com.trs.gov.kpi.entity.outerapi.bas.SiteSummary;
import com.trs.gov.kpi.entity.outerapi.bas.SummaryResponse;
import com.trs.gov.kpi.entity.requestdata.BasRequest;
import com.trs.gov.kpi.entity.responsedata.History;
import com.trs.gov.kpi.entity.responsedata.HistoryStatistics;
import com.trs.gov.kpi.service.MonitorSiteService;
import com.trs.gov.kpi.service.outer.BasService;
import com.trs.gov.kpi.service.outer.SiteApiService;
import com.trs.gov.kpi.utils.DateUtil;
import com.trs.gov.kpi.utils.LogUtil;
import com.trs.gov.kpi.utils.OuterApiServiceUtil;
import com.trs.gov.kpi.utils.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by ranwei on 2017/6/13.
 */
@Slf4j
@Service
public class BasServiceImpl implements BasService {

    @Value("${service.outer.bas.url}")
    private String basServiceUrl;

    @Resource
    private MonitorSiteService monitorSiteService;

    @Resource
    private SiteApiService siteApiService;

    private static final String MPIDS = "mpIds";
    private static final String DAY = "day";

    @Override
    public Integer getVisits(BasRequest basRequest) throws RemoteException, ParseException {
        MonitorSiteDeal monitorSiteDeal = monitorSiteService.getMonitorSiteDealBySiteId(basRequest.getSiteId());
        String siteIndexPage = "";
        if (monitorSiteDeal != null) {
            siteIndexPage = monitorSiteDeal.getIndexUrl();
        }
        String beginDay = initTime(getPreviousMonthDate());
        String endDay = initTime(DateUtil.toString(new Date()));
        String url = basServiceUrl + "/api/retrieveWebUV";

        return requestBasPV(url, beginDay, endDay, siteIndexPage);
    }


    private String getPreviousMonthDate() {

        Calendar date = Calendar.getInstance();
        date.setTime(new Date());
        date.add(Calendar.DAY_OF_MONTH, -31);

        return DateUtil.toString(date.getTime());
    }

    @Override
    public History getHistoryVisits(BasRequest basRequest) throws RemoteException, ParseException {
        MonitorSiteDeal monitorSiteDeal = monitorSiteService.getMonitorSiteDealBySiteId(basRequest.getSiteId());
        String siteIndexPage = "";
        if (monitorSiteDeal != null) {
            siteIndexPage = monitorSiteDeal.getIndexUrl();
        }
        String url = basServiceUrl + "/api/retrieveWebUV";

        DateUtil.setDefaultDate(basRequest);

        List<HistoryDate> dateList = DateUtil.splitDate(basRequest.getBeginDateTime(), basRequest.getEndDateTime(), basRequest.getGranularity());
        List<HistoryStatistics> list = new ArrayList<>();
        for (Iterator<HistoryDate> iterator = dateList.iterator(); iterator.hasNext(); ) {
            HistoryDate historyDate = iterator.next();
            //不返回当前周期的数据，因为当前周期还未结束
            if (!iterator.hasNext()) {
                return new History(new Date(), list);
            }
            HistoryStatistics historyStatistics = new HistoryStatistics();
            historyStatistics.setTime(historyDate.getDate());

            if (Granularity.YEAR.equals(basRequest.getGranularity())) {//粒度为年时，需要逐月请求并累加,单独处理
                List<HistoryDate> dates = DateUtil.splitDate(historyDate.getBeginDate(), historyDate.getEndDate(), null);
                historyStatistics.setValue(getVisitsSum(dates, url, siteIndexPage));
                list.add(historyStatistics);
                continue;
            }

            Integer pv = requestBasPV(url, initTime(historyDate.getBeginDate()), initTime(historyDate.getEndDate()), siteIndexPage);
            historyStatistics.setValue(pv);

            list.add(historyStatistics);
        }

        return new History(new Date(), list);
    }

    private int getVisitsSum(List<HistoryDate> dates, String url, String siteIndexPage) throws ParseException, RemoteException {
        int sum = 0;
        for (HistoryDate date : dates) {
            Integer pv = requestBasPV(url, initTime(date.getBeginDate()), initTime(date.getEndDate()), siteIndexPage);
            if (pv != null) {
                sum += pv;
            }
        }
        return sum;
    }

    private Integer requestBasPV(String url, String beginDay, String endDay, String siteIndexPage) throws RemoteException {

        OkHttpClient client = new OkHttpClient();
        String json = "{" +
                "\"beginDay\": \"" + beginDay + "\"," +
                "\"endDay\": \"" + endDay + "\"," +
                "\"urls\": [" +
                "\"" + siteIndexPage + "\"" +
                "]" +
                "}";
        RequestBody body = new FormEncodingBuilder()
                .add("json", json)
                .build();
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        BasPVResponse basPVResponse;
        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                String jsonResult = response.body().string();
                if (StringUtil.isEmpty(jsonResult)) {
                    return null;
                }
                basPVResponse = JSON.parseObject(jsonResult, BasPVResponse.class);
            } else {
                log.error("failed to getVisits, error: " + response);
                throw new RemoteException("获取访问量失败！");
            }
        } catch (Exception e) {
            log.error("getVisits failed ", e);
            LogUtil.addErrorLog(OperationType.REQUEST, ErrorType.REQUEST_FAILED, "getVisits failed ", e);
            throw new RemoteException("获取访问量失败！", e);
        }
        if (basPVResponse.getRecords() == null || basPVResponse.getRecords().isEmpty()) {
            return 0;
        }
        return basPVResponse.getRecords().get(0).getPv();
    }

    @Override
    public Integer getStayTime(BasRequest basRequest) throws RemoteException, BizException {

        Map<String, String> params = new HashMap<>();
        String mpId = siteApiService.getSiteById(basRequest.getSiteId(), null).getMpId();
        if (StringUtil.isEmpty(mpId)) {
            throw new BizException("当前站点没有对应的mpId，无法获取数据！");
        } else {
            params.put(MPIDS, mpId);
        }

        SiteSummary siteSummary = requestBasSummary(params);
        if (siteSummary == null) {
            return 0;
        } else {
            return siteSummary.getAvgDuration30();
        }
    }

    @Override
    public History getHistoryStayTime(BasRequest basRequest) throws ParseException, RemoteException, BizException {

        DateUtil.setDefaultDate(basRequest);

        List<HistoryDate> dateList = DateUtil.splitDate(basRequest.getBeginDateTime(), basRequest.getEndDateTime(), basRequest.getGranularity());
        List<HistoryStatistics> list = new ArrayList<>();

        for (Iterator<HistoryDate> iterator = dateList.iterator(); iterator.hasNext(); ) {
            HistoryDate historyDate = iterator.next();
            //不返回当前周期的数据，因为当前周期还未结束
            if (!iterator.hasNext()) {
                return new History(new Date(), list);
            }
            HistoryStatistics historyStatistics = new HistoryStatistics();
            historyStatistics.setTime(historyDate.getDate());

            Map<String, String> params = new HashMap<>();
            String mpId = siteApiService.getSiteById(basRequest.getSiteId(), null).getMpId();
            if (StringUtil.isEmpty(mpId)) {
                throw new BizException("当前站点没有对应的mpId，无法获取数据！");
            } else {
                params.put(MPIDS, mpId);
            }

            if (Granularity.YEAR.equals(basRequest.getGranularity())) {//粒度为年时，需要逐月请求并累加,单独处理
                List<HistoryDate> dates = DateUtil.splitDate(historyDate.getBeginDate(), historyDate.getEndDate(), null);
                historyStatistics.setValue(getTimeSum(dates, params));
                list.add(historyStatistics);
                continue;
            }

            if (Granularity.DAY.equals(basRequest.getGranularity())){//粒度为天时，查询的是当天的数据，需要传入开始时间
                params.put(DAY, initTime(historyDate.getBeginDate()));
            }else {
                params.put(DAY, initTime(historyDate.getEndDate()));
            }
            SiteSummary siteSummary = requestBasSummary(params);

            if (siteSummary != null) {
                historyStatistics.setValue(getVisitsByGranularity(basRequest.getGranularity(), siteSummary));
            } else {
                historyStatistics.setValue(0);
            }

            list.add(historyStatistics);
        }
        return new History(new Date(), list);
    }

    private int getTimeSum(List<HistoryDate> dates, Map<String, String> params) throws ParseException, RemoteException {
        int sum = 0;
        for (HistoryDate date : dates) {
            params.put(DAY, initTime(date.getEndDate()));
            SiteSummary siteSummary = requestBasSummary(params);
            if (siteSummary != null) {
                sum += siteSummary.getAvgDuration30();
            }
        }
        return sum;
    }

    /**
     * 根据粒度拿到对应的停留时间
     *
     * @param granularity
     * @param siteSummary
     * @return
     */
    private int getVisitsByGranularity(Integer granularity, SiteSummary siteSummary) {
        if (Granularity.DAY.equals(granularity)) {
            return siteSummary.getAvgDuration();
        } else if (Granularity.WEEK.equals(granularity)) {
            return siteSummary.getAvgDuration7();
        } else {//不设置，默认为月
            return siteSummary.getAvgDuration30();
        }
    }

    private SiteSummary requestBasSummary(Map<String, String> params) throws RemoteException {

        OkHttpClient client = new OkHttpClient();
        Request request = OuterApiServiceUtil.buildRequest(basServiceUrl, "/api/mpSummary", params);
        SummaryResponse summaryResponse;
        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                String jsonResult = response.body().string();
                if (StringUtil.isEmpty(jsonResult)) {
                    return null;
                }
                summaryResponse = JSON.parseObject(jsonResult, SummaryResponse.class);
            } else {
                log.error("failed to getStayTime, error: " + response);
                throw new RemoteException("获取停留时间失败！");
            }
        } catch (Exception e) {
            log.error("getStayTime failed ", e);
            LogUtil.addErrorLog(OperationType.REQUEST, ErrorType.REQUEST_FAILED, "getStayTime failed ", e);
            throw new RemoteException("获取停留时间失败！", e);
        }
        if (summaryResponse.getRecords() == null || summaryResponse.getRecords().isEmpty()) {
            return null;
        }
        return summaryResponse.getRecords().get(0);
    }

    /**
     * 将绩效考核接受的日期格式转化为网脉接受的日期格式
     *
     * @param time
     * @return
     * @throws ParseException
     */
    private String initTime(String time) throws ParseException {

        SimpleDateFormat sdfIn = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdfOut = new SimpleDateFormat("yyyyMMdd");
        Date data = sdfIn.parse(time);

        return sdfOut.format(data);
    }

}

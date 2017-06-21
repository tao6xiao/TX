package com.trs.gov.kpi.service.impl.outer;

import com.alibaba.fastjson.JSON;
import com.squareup.okhttp.*;
import com.trs.gov.kpi.entity.HistoryDate;
import com.trs.gov.kpi.entity.MonitorSiteDeal;
import com.trs.gov.kpi.entity.exception.RemoteException;
import com.trs.gov.kpi.entity.outerapi.bas.BasPVResponse;
import com.trs.gov.kpi.entity.outerapi.bas.SiteSummary;
import com.trs.gov.kpi.entity.outerapi.bas.SummaryResponse;
import com.trs.gov.kpi.entity.requestdata.BasRequest;
import com.trs.gov.kpi.entity.responsedata.HistoryStatistics;
import com.trs.gov.kpi.service.MonitorSiteService;
import com.trs.gov.kpi.service.outer.BasService;
import com.trs.gov.kpi.utils.DateUtil;
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

    private static final String SITE_IDS = "mpIds";
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
    public List<HistoryStatistics> getHistoryVisits(BasRequest basRequest) throws RemoteException, ParseException {
        MonitorSiteDeal monitorSiteDeal = monitorSiteService.getMonitorSiteDealBySiteId(basRequest.getSiteId());
        String siteIndexPage = "";
        if (monitorSiteDeal != null) {
            siteIndexPage = monitorSiteDeal.getIndexUrl();
        }
        String url = basServiceUrl + "/api/retrieveWebUV";
        setDefaultDate(basRequest);

        List<HistoryDate> dateList = DateUtil.splitDateByMonth(basRequest.getBeginDateTime(), basRequest.getEndDateTime());
        List<HistoryStatistics> list = new ArrayList<>();
        for (HistoryDate historyDate : dateList) {
            HistoryStatistics historyStatistics = new HistoryStatistics();
            Integer pv = requestBasPV(url, initTime(historyDate.getBeginDate()), initTime(historyDate.getEndDate()), siteIndexPage);
            historyStatistics.setValue(pv);
            historyStatistics.setTime(historyDate.getMonth());
            list.add(historyStatistics);
        }

        return list;
    }

    /**
     * 设置默认起止日期
     *
     * @param basRequest
     * @throws ParseException
     */
    private void setDefaultDate(BasRequest basRequest) throws ParseException {
        if (StringUtil.isEmpty(basRequest.getEndDateTime())) {
            basRequest.setEndDateTime(DateUtil.toString(new Date()));
        }
        if (StringUtil.isEmpty(basRequest.getBeginDateTime())) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(DateUtil.toDayDate(basRequest.getEndDateTime()));
            calendar.add(Calendar.MONTH, -6);
            basRequest.setBeginDateTime(DateUtil.toDayString(calendar.getTime()));
        }
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
            throw new RemoteException("获取访问量失败！", e);
        }
        if (basPVResponse.getRecords() == null || basPVResponse.getRecords().isEmpty()) {
            return 0;
        }
        return basPVResponse.getRecords().get(0).getPv();
    }

    @Override
    public Integer getStayTime(BasRequest basRequest) throws RemoteException {

        Map<String, String> params = new HashMap<>();
        params.put(SITE_IDS, Integer.toString(basRequest.getSiteId()));

        StringBuilder url = new StringBuilder(basServiceUrl + "/api/mpSummary");
        SiteSummary siteSummary = requestBasSummary(url, params);
        if (siteSummary == null) {
            return 0;
        } else {
            return siteSummary.getAvgDuration30();
        }
    }

    @Override
    public List<HistoryStatistics> geHistoryStayTime(BasRequest basRequest) throws ParseException, RemoteException {

        StringBuilder url = new StringBuilder(basServiceUrl + "/api/mpSummary");
        setDefaultDate(basRequest);

        List<HistoryDate> dateList = DateUtil.splitDateByMonth(basRequest.getBeginDateTime(), basRequest.getEndDateTime());
        List<HistoryStatistics> list = new ArrayList<>();

        for (Iterator<HistoryDate> iterator = dateList.iterator(); iterator.hasNext(); ) {
            HistoryDate historyDate = iterator.next();
            //处理最后一个月的数据，如果这月还没结束，就不返回该月的数据
            if (!iterator.hasNext()) {
                Calendar lastDate = Calendar.getInstance();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                lastDate.setTime(sdf.parse(historyDate.getEndDate()));
                if (lastDate.get(Calendar.DAY_OF_MONTH) != 1) {//判断是否为当月第一天
                    break;
                }
            }
            HistoryStatistics historyStatistics = new HistoryStatistics();
            Map<String, String> params = new HashMap<>();
            params.put(SITE_IDS, Integer.toString(basRequest.getSiteId()));
            params.put(DAY, initTime(historyDate.getEndDate()));
            SiteSummary siteSummary = requestBasSummary(url, params);
            historyStatistics.setTime(historyDate.getMonth());
            if (siteSummary != null) {
                historyStatistics.setValue(siteSummary.getAvgDuration30());
            } else {
                historyStatistics.setValue(0);
            }

            list.add(historyStatistics);
        }
        return list;
    }

    private SiteSummary requestBasSummary(StringBuilder url, Map<String, String> params) throws RemoteException {
        if (!params.isEmpty()) {
            url.append("?");
            for (Iterator<Map.Entry<String, String>> it = params.entrySet().iterator(); it.hasNext(); ) {
                Map.Entry<String, String> entry = it.next();
                url.append(entry.getKey()).append("=").append(entry.getValue());
                if (it.hasNext()) {
                    url.append("&");
                }
            }
        }

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url.toString()).build();
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
            throw new RemoteException("获取停留时间失败！", e);
        }
        if (summaryResponse.getRecords() == null || summaryResponse.getRecords().isEmpty()) {
            return null;
        }
        return summaryResponse.getRecords().get(0);
    }

    private String initTime(String time) throws ParseException {

        SimpleDateFormat sdfIn = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdfOut = new SimpleDateFormat("yyyyMMdd");

        Date data = sdfIn.parse(time);

        return sdfOut.format(data);
    }

}

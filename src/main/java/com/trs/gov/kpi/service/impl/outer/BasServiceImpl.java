package com.trs.gov.kpi.service.impl.outer;

import com.alibaba.fastjson.JSON;
import com.squareup.okhttp.*;
import com.trs.gov.kpi.entity.HistoryDate;
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
import java.io.IOException;
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

    private static final MediaType JSON_TYPE = MediaType.parse("application/json; charset=utf-8");

    private static final String DEFAULT_BEGIN_TIME = "20000505";

    private static final String SITE_IDS = "mpIds";
    private static final String DAY = "day";

    @Override
    public Integer getVisits(BasRequest basRequest) throws RemoteException, ParseException {

        String siteIndexPage = monitorSiteService.getMonitorSiteDealBySiteId(basRequest.getSiteId()).getIndexUrl();

        String beginDay;
        String endDay;
        if (StringUtil.isEmpty(basRequest.getBeginDateTime())) {
            beginDay = DEFAULT_BEGIN_TIME;
        } else {
            beginDay = initTime(basRequest.getBeginDateTime());
        }
        if (StringUtil.isEmpty(basRequest.getEndDateTime())) {
            endDay = initTime(DateUtil.toString(new Date()));
        } else {
            endDay = initTime(basRequest.getEndDateTime());
        }
        String url = basServiceUrl + "/api/retrieveWebUV";

        return requestBasPV(url, beginDay, endDay, siteIndexPage);
    }

    @Override
    public List<HistoryStatistics> getHistoryVisits(BasRequest basRequest) throws RemoteException {
        String siteIndexPage = monitorSiteService.getMonitorSiteDealBySiteId(basRequest.getSiteId()).getIndexUrl();
        String url = basServiceUrl + "/api/retrieveWebUV";
        if (StringUtil.isEmpty(basRequest.getBeginDateTime())) {
            basRequest.setBeginDateTime("2017-05-01");
        }
        if (StringUtil.isEmpty(basRequest.getEndDateTime())) {
            basRequest.setEndDateTime(DateUtil.toString(new Date()));
        }
        List<HistoryDate> dateList = DateUtil.splitDateByMonth(basRequest.getBeginDateTime(), basRequest.getEndDateTime());
        List<HistoryStatistics> list = new ArrayList<>();
        for (HistoryDate historyDate : dateList) {
            HistoryStatistics historyStatistics = new HistoryStatistics();
            int pv = requestBasPV(url, historyDate.getBeginDate(), historyDate.getEndDate(), siteIndexPage);
            historyStatistics.setValue(pv);
            historyStatistics.setTime(historyDate.getMonth());
            list.add(historyStatistics);
        }

        return list;
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
        RequestBody body = RequestBody.create(JSON_TYPE, json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        BasPVResponse basPVResponse;
        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                String jsonResult = response.body().toString();
                if (StringUtil.isEmpty(jsonResult)) {
                    return null;
                }
                basPVResponse = JSON.parseObject(jsonResult, BasPVResponse.class);
            } else {
                log.error("failed to getVisits, error: " + response);
                throw new RemoteException("获取访问量失败！");
            }
        } catch (IOException e) {
            log.error("getVisits failed ", e);
            throw new RemoteException("获取访问量失败！", e);
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
            return null;
        } else {
            return siteSummary.getAvgDuration30();
        }
    }

    @Override
    public List<HistoryStatistics> geHistoryStayTime(BasRequest basRequest) throws ParseException, RemoteException {

        StringBuilder url = new StringBuilder(basServiceUrl + "/api/mpSummary");
        if (StringUtil.isEmpty(basRequest.getBeginDateTime())) {
            basRequest.setBeginDateTime("2017-05-01");
        }
        if (StringUtil.isEmpty(basRequest.getEndDateTime())) {
            basRequest.setEndDateTime(DateUtil.toString(new Date()));
        }
        List<HistoryDate> dateList = DateUtil.splitDateByMonth(basRequest.getBeginDateTime(), basRequest.getEndDateTime());
        List<HistoryStatistics> list = new ArrayList<>();
        for (HistoryDate historyDate : dateList) {
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
            for (Map.Entry<String, String> entry : params.entrySet()) {
                url.append(entry.getKey()).append("=").append(entry.getValue());
            }
        }

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url.toString()).build();
        SummaryResponse summaryResponse;
        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                String jsonResult = response.body().toString();
                if (StringUtil.isEmpty(jsonResult)) {
                    return null;
                }
                summaryResponse = JSON.parseObject(jsonResult, SummaryResponse.class);
            } else {
                log.error("failed to getStayTime, error: " + response);
                throw new RemoteException("获取停留时间失败！");
            }
        } catch (IOException e) {
            log.error("getStayTime failed ", e);
            throw new RemoteException("获取停留时间失败！", e);
        }
        return summaryResponse.getRecords().get(0);
    }

    private String initTime(String time) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Date data = DateUtil.toDate(time);

        return sdf.format(data);
    }

}

package com.trs.gov.kpi.service.impl.outer;

import com.alibaba.fastjson.JSON;
import com.squareup.okhttp.*;
import com.trs.gov.kpi.constant.ErrorType;
import com.trs.gov.kpi.constant.Granularity;
import com.trs.gov.kpi.constant.OperationType;
import com.trs.gov.kpi.entity.HistoryDate;
import com.trs.gov.kpi.entity.exception.BizException;
import com.trs.gov.kpi.entity.exception.RemoteException;
import com.trs.gov.kpi.entity.outerapi.Site;
import com.trs.gov.kpi.entity.outerapi.bas.BasPVResponse;
import com.trs.gov.kpi.entity.outerapi.bas.SiteSummary;
import com.trs.gov.kpi.entity.outerapi.bas.SummaryResponse;
import com.trs.gov.kpi.entity.requestdata.BasRequest;
import com.trs.gov.kpi.entity.responsedata.HistoryStatisticsResp;
import com.trs.gov.kpi.entity.responsedata.HistoryStatistics;
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
    private SiteApiService siteApiService;

    private static final String MPIDS = "mpIds";
    private static final String DAY = "day";

    @Override
    public Integer getVisits(BasRequest basRequest) throws RemoteException, ParseException {
        Site site = siteApiService.getSiteById(basRequest.getSiteId(), null);
        String siteIndexPage = "";
        if (site != null) {
            siteIndexPage = site.getWebHttp();
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
    public HistoryStatisticsResp getHistoryVisits(BasRequest basRequest) throws RemoteException, ParseException {
        Site site = siteApiService.getSiteById(basRequest.getSiteId(), null);
        String siteIndexPage = "";
        if (site != null) {
            siteIndexPage = site.getWebHttp();
        }
        String url = basServiceUrl + "/api/retrieveWebUV";

        basRequest.setDefaultDate();

        List<HistoryDate> dateList = DateUtil.splitDate(basRequest.getBeginDateTime(), basRequest.getEndDateTime(), basRequest.getGranularity());
        List<HistoryStatistics> list = new ArrayList<>();
        for (Iterator<HistoryDate> iterator = dateList.iterator(); iterator.hasNext(); ) {
            HistoryDate historyDate = iterator.next();
            HistoryStatistics historyStatistics = new HistoryStatistics();
            historyStatistics.setTime(historyDate.getDate());
            subOneDay(historyDate);
            Integer pv = requestBasPV(url, initTime(historyDate.getBeginDate()), initTime(historyDate.getEndDate()), siteIndexPage);
            historyStatistics.setValue(pv);

            list.add(historyStatistics);
        }

        return new HistoryStatisticsResp(new Date(), list);
    }

    /**
     * DateUtil.splitDate方法是把结束日期往后加了一天，需要减去一天，与网脉统一
     *
     * @param date
     */
    private void subOneDay(HistoryDate date) throws ParseException {
        Date end = DateUtil.toDayDate(date.getEndDate());
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(end);
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        date.setEndDate(DateUtil.toDayString(calendar.getTime()));
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
                throw new RemoteException("获取访问量失败！返回：" + response);
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
    public HistoryStatisticsResp getHistoryStayTime(BasRequest basRequest) throws ParseException, RemoteException, BizException {

        basRequest.setDefaultDate();

        List<HistoryDate> dateList = DateUtil.splitDate(basRequest.getBeginDateTime(), basRequest.getEndDateTime(), basRequest.getGranularity());
        List<HistoryStatistics> list = new ArrayList<>();

        String mpId = siteApiService.getSiteById(basRequest.getSiteId(), null).getMpId();
        if (StringUtil.isEmpty(mpId)) {
            throw new BizException("当前站点没有对应的mpId，无法获取数据！");
        }

        for (Iterator<HistoryDate> iterator = dateList.iterator(); iterator.hasNext(); ) {
            HistoryDate historyDate = iterator.next();
            subOneDay(historyDate);
            Map<String, String> params = new HashMap<>();
            params.put(MPIDS, mpId);
            if (Granularity.DAY.equals(basRequest.getGranularity())) {//粒度为天时，查询的是当天的数据，需要传入开始时间
                params.put(DAY, initTime(historyDate.getBeginDate()));
            } else {
                params.put(DAY, initTime(historyDate.getEndDate()));
            }
            HistoryStatistics historyStatistics = new HistoryStatistics();
            historyStatistics.setTime(historyDate.getDate());
            //查询最后一个周期数据时 需要逐天累加
            if (!iterator.hasNext() && basRequest.getGranularity() != Granularity.DAY) {
                updateLastStatistics(historyDate, historyStatistics, mpId);
                list.add(historyStatistics);
                return new HistoryStatisticsResp(new Date(), list);
            }

            SiteSummary siteSummary = requestBasSummary(params);

            if (siteSummary != null) {
                historyStatistics.setValue(getVisitsByGranularity(basRequest.getGranularity(), siteSummary));
            } else {
                historyStatistics.setValue(0);
            }
            list.add(historyStatistics);
        }
        return new HistoryStatisticsResp(new Date(), list);
    }

    private void updateLastStatistics(HistoryDate historyDate, HistoryStatistics historyStatistics, String mpId) throws ParseException, RemoteException {
        int sum = 0;
        int count = 0;
        //当天的数据查不到，需要去除
        subOneDay(historyDate);
        List<HistoryDate> dateList = DateUtil.splitDate(historyDate.getBeginDate(), historyDate.getEndDate(), Granularity.DAY);
        for (HistoryDate date : dateList) {
            Map<String, String> params = new HashMap<>();
            params.put(MPIDS, mpId);
            params.put(DAY, initTime(date.getBeginDate()));
            SiteSummary siteSummary = requestBasSummary(params);
            if (siteSummary != null) {
                sum += getVisitsByGranularity(Granularity.DAY, siteSummary);
            } else {
                sum += 0;
            }
            count++;
        }
        if (count == 0) {
            historyStatistics.setValue(0);
        } else {
            historyStatistics.setValue(sum / count);
        }
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
                throw new RemoteException("获取停留时间失败！返回：" + response);
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

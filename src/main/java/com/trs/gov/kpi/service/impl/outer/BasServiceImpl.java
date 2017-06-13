package com.trs.gov.kpi.service.impl.outer;

import com.alibaba.fastjson.JSON;
import com.squareup.okhttp.*;
import com.trs.gov.kpi.entity.exception.RemoteException;
import com.trs.gov.kpi.entity.outerapi.bas.BasPVResponse;
import com.trs.gov.kpi.entity.requestdata.BasRequest;
import com.trs.gov.kpi.entity.responsedata.History;
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
import java.util.Date;

/**
 * Created by ranwei on 2017/6/13.
 */
@Slf4j
@Service
public class BasServiceImpl implements BasService {

    @Value("${service.outer.tapi.url}")
    private String tapiServiceUrl;

    @Resource
    private MonitorSiteService monitorSiteService;

    private static final MediaType JSONTYPE = MediaType.parse("application/json; charset=utf-8");

    private static final String DEFAULT_BEGIN_TIME = "20000505";

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

        OkHttpClient client = new OkHttpClient();
        String json = "{" +
                "\"beginDay\": \"" + beginDay + "\"," +
                "\"endDay\": \"" + endDay + "\"," +
                "\"urls\": [" +
                "\"" + siteIndexPage + "\"" +
                "]" +
                "}";
        String url = tapiServiceUrl + "/api/retrieveWebUV";
        RequestBody body = RequestBody.create(JSONTYPE, json);
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
    public History geHistoryVisits(BasRequest request) {
        return null;
    }

    @Override
    public Integer getStaytime(BasRequest request) {
        return 0;
    }

    @Override
    public History geHistoryStaytime(BasRequest request) {
        return null;
    }

    private String initTime(String time) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Date data = DateUtil.toDate(time);

        return sdf.format(data);
    }

}

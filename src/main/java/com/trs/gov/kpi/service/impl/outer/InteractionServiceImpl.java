package com.trs.gov.kpi.service.impl.outer;

import com.alibaba.fastjson.JSON;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.trs.gov.kpi.constant.ErrorType;
import com.trs.gov.kpi.constant.Granularity;
import com.trs.gov.kpi.constant.OperationType;
import com.trs.gov.kpi.entity.HistoryDate;
import com.trs.gov.kpi.entity.exception.RemoteException;
import com.trs.gov.kpi.entity.outerapi.nbhd.*;
import com.trs.gov.kpi.entity.responsedata.HistoryStatistics;
import com.trs.gov.kpi.service.outer.InteractionService;
import com.trs.gov.kpi.utils.DateUtil;
import com.trs.gov.kpi.utils.LogUtil;
import com.trs.gov.kpi.utils.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.trs.gov.kpi.utils.OuterApiServiceUtil.newServiceRequestBuilder;

/**
 * Created by ranwei on 2017/6/9.
 */
@Slf4j
@Service
public class InteractionServiceImpl implements InteractionService {

    @Value("${service.outer.nbhd.url}")
    private String nbhdServiceUrl;

    private static final String GOVMSGBOX_SERVICE_NAME = "openGovmsgbox";

    private static final String SITE_ID = "siteId";

    @Override
    public NBHDPageDataResult getGovMsgBoxes(NBHDRequestParam param) throws RemoteException {

        Map<String, String> params = new HashMap<>();
        params.put(SITE_ID, String.valueOf(param.getSiteId()));
        if (param.getPageIndex() != null) {
            params.put("pageIndex", String.valueOf(param.getPageIndex()));
        }
        if (param.getPageSize() != null) {
            params.put("pageSize", String.valueOf(param.getPageSize()));
        }
        if (param.getSearchField() != null) {
            params.put("searchField", param.getSearchField());
        }
        if (param.getSearchText() != null) {
            params.put("searchText", param.getSearchText());
        }
        addCond(param, params);

        OkHttpClient client = new OkHttpClient();
        try {
            Response response = client.newCall(
                    buildRequest("listGovmsgboxs", GOVMSGBOX_SERVICE_NAME, null, params)).execute();

            if (response.isSuccessful()) {
                String jsonResult = response.body().string();
                if (StringUtil.isEmpty(jsonResult)) {
                    return null;
                }
                return JSON.parseObject(jsonResult, NBHDPageDataResult.class);
            } else {
                String errorInfo = MessageFormat.format("failed to listGovmsgboxs, [siteId={0}], error:{1}", param.getSiteId(), response);
                log.error(errorInfo);
                throw new RemoteException("获取咨询列表失败！[siteId=" + param.getSiteId() + "]，返回：" + response);
            }
        } catch (IOException e) {
            log.error("failed listGovmsgboxs, [siteId=" + param.getSiteId() + "]", e);
            LogUtil.addErrorLog(OperationType.REQUEST, ErrorType.REQUEST_FAILED, "failed listGovmsgboxs,  [siteId=" + param.getSiteId() + "]", e);
            throw new RemoteException("获取咨询列表失败！[siteId=" + param.getSiteId() + "]", e);
        }

    }

    @Override
    public NBHDStatisticsRes getGovMsgBoxesCount(NBHDRequestParam param) throws RemoteException {


        Map<String, String> params = new HashMap<>();
        params.put(SITE_ID, String.valueOf(param.getSiteId()));
        addCond(param, params);

        OkHttpClient client = new OkHttpClient();
        try {
            Response response = client.newCall(
                    buildRequest("countGovmsgboxs", GOVMSGBOX_SERVICE_NAME, null, params)).execute();

            if (response.isSuccessful()) {
                String jsonResult = response.body().string();
                if (StringUtil.isEmpty(jsonResult)) {
                    return null;
                }
                return JSON.parseObject(jsonResult, NBHDStatisticsRes.class);
            } else {
                log.error("failed to countGovmsgboxs,  [siteId=" + param.getSiteId() + "], error: " + response);
                throw new RemoteException("获取咨询统计失败！ [siteId=" + param.getSiteId() + "], 返回：" + response);
            }
        } catch (IOException e) {
            log.error("failed countGovmsgboxs,  [siteId=" + param.getSiteId() + "]", e);
            LogUtil.addErrorLog(OperationType.REQUEST, ErrorType.REQUEST_FAILED, "failed countGovmsgboxs, [siteId=" + param.getSiteId() + "]", e);
            throw new RemoteException("获取咨询统计失败！[siteId=" + param.getSiteId() + "]", e);
        }
    }

    @Override
    public List<HistoryStatistics> getGovMsgHistoryCount(NBHDRequestParam param) throws RemoteException {

        Map<String, String> params = new HashMap<>();
        params.put(SITE_ID, String.valueOf(param.getSiteId()));
        param.setDefaultDate();
        addCond(param, params);
        if (param.getGranularity() != null) {
            params.put("granularity", String.valueOf(param.getGranularity()));
        }

        OkHttpClient client = new OkHttpClient();
        NBHDHistoryRes nbhdHistoryRes;
        try {
            Response response = client.newCall(
                    buildRequest("countDetailGovmsgboxs", GOVMSGBOX_SERVICE_NAME, null, params)).execute();

            if (response.isSuccessful()) {
                String jsonResult = response.body().string();
                if (StringUtil.isEmpty(jsonResult)) {
                    return new ArrayList<>();
                }
                nbhdHistoryRes = JSON.parseObject(jsonResult, NBHDHistoryRes.class);
            } else {
                log.error("failed to countDetailGovmsgboxs, [siteId=" + param.getSiteId() + "], error: " + response);
                throw new RemoteException("获取咨询统计历史记录失败！ [siteId=" + param.getSiteId() + "]");
            }
        } catch (IOException e) {
            log.error("failed countDetailGovmsgboxs, [siteId=" + param.getSiteId() + "]", e);
            LogUtil.addErrorLog(OperationType.REQUEST, ErrorType.REQUEST_FAILED, "failed countDetailGovmsgboxs, [siteId=" + param.getSiteId() + "]", e);
            throw new RemoteException("获取咨询统计历史记录失败！[siteId=" + param.getSiteId() + "]", e);
        }
        List<NBHDHistory> datas = nbhdHistoryRes.getDatas();

        List<HistoryStatistics> historyStatisticsList = new ArrayList<>();
        List<HistoryDate> dateList = DateUtil.splitDate(param.getBeginDateTime(), param.getEndDateTime(), param.getGranularity());
        for (HistoryDate historyDate : dateList) {
            Boolean isNotExits = true;
            String kpiDate = historyDate.getDate();

            HistoryStatistics historyStatistics = new HistoryStatistics();
            historyStatistics.setTime(kpiDate);

            for (NBHDHistory nbhdHistory : datas) {
                String nbhdDate = nbhdHistory.getGranularity();
                if (isEqual(kpiDate, nbhdDate, param.getGranularity())) {
                    historyStatistics.setValue(nbhdHistory.getCount());
                    isNotExits = false;
                    break;
                }
            }
            if (isNotExits || datas.isEmpty()) {
                historyStatistics.setValue(0);
            }
            historyStatisticsList.add(historyStatistics);
        }

        return historyStatisticsList;
    }

    private Boolean isEqual(String kpiDate, String nbhdDate, Integer granularity) {

        if (Granularity.WEEK.equals(granularity)) {
            String[] week = kpiDate.split("-");
            return week[1].equals(nbhdDate);
        } else if (Granularity.YEAR.equals(granularity)) {
            return kpiDate.equals(nbhdDate);
        } else {
            String date = kpiDate.replaceAll("-", "");
            return date.equals(nbhdDate);
        }
    }

    private Request buildRequest(String methodName, String serviceName, String userName, Map<String, String> params) {
        addUserNameParam(userName, params);
        return newServiceRequestBuilder()
                .setUrlFormat("%s/fdy/%s.do?method=%s")
                .setServiceUrl(nbhdServiceUrl)
                .setServiceName(serviceName)
                .setMethodName(methodName)
                .setParams(params).build();
    }

    private void addUserNameParam(String userName, Map<String, String> params) {
        if (StringUtil.isEmpty(userName)) {
            params.put("userName", "admin");
        } else {
            params.put("userName", userName);
        }
    }

    private void addCond(NBHDRequestParam param, Map<String, String> params) {
        if (param.getBeginDateTime() != null) {
            params.put("beginDateTime", param.getBeginDateTime());
        }
        if (param.getEndDateTime() != null) {
            params.put("endDateTime", param.getEndDateTime());
        }
    }

}

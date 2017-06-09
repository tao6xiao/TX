package com.trs.gov.kpi.service.impl.outer;

import com.alibaba.fastjson.JSON;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.trs.gov.kpi.entity.exception.RemoteException;
import com.trs.gov.kpi.entity.outerapi.nbhd.NBHDHistoryRes;
import com.trs.gov.kpi.entity.outerapi.nbhd.NBHDPageDataResult;
import com.trs.gov.kpi.entity.outerapi.nbhd.NBHDRequestParam;
import com.trs.gov.kpi.entity.outerapi.nbhd.NBHDStatisticsRes;
import com.trs.gov.kpi.service.outer.InteractionService;
import com.trs.gov.kpi.utils.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
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

    private final static String GOVMSGBOX_SERVICE_NAME = "openGovmsgbox";

    private final static String SITE_ID = "siteId";

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
        if (param.getSolveStatus() != null) {
            params.put("solveStatus", String.valueOf(param.getSolveStatus()));
        }
        if (param.getIsDeadLine() != null) {
            params.put("isDeadLine", String.valueOf(param.getIsDeadLine()));
        }
        addCond(param, params);

        OkHttpClient client = new OkHttpClient();
        try {
            Response response = client.newCall(
                    buildRequest("listGovmsgboxs", GOVMSGBOX_SERVICE_NAME, param.getUserName(), params)).execute();

            if (response.isSuccessful()) {
                String jsonResult = response.body().toString();
                if (StringUtil.isEmpty(jsonResult)) {
                    return null;
                }
                return JSON.parseObject(jsonResult, NBHDPageDataResult.class);
            } else {
                log.error("failed to listGovmsgboxs, error: " + response);
                throw new RemoteException("获取咨询列表失败！");
            }
        } catch (IOException e) {
            log.error("failed listGovmsgboxs", e);
            throw new RemoteException("获取咨询列表失败！", e);
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
                    buildRequest("countGovmsgboxs", GOVMSGBOX_SERVICE_NAME, param.getUserName(), params)).execute();

            if (response.isSuccessful()) {
                String jsonResult = response.body().toString();
                if (StringUtil.isEmpty(jsonResult)) {
                    return null;
                }
                return JSON.parseObject(jsonResult, NBHDStatisticsRes.class);
            } else {
                log.error("failed to countGovmsgboxs, error: " + response);
                throw new RemoteException("获取咨询统计失败！");
            }
        } catch (IOException e) {
            log.error("failed countGovmsgboxs", e);
            throw new RemoteException("获取咨询统计失败！", e);
        }
    }

    @Override
    public NBHDHistoryRes getGovMsgHistoryCount(NBHDRequestParam param) throws RemoteException {
        Map<String, String> params = new HashMap<>();

        params.put(SITE_ID, String.valueOf(param.getSiteId()));
        params.put("solveStatus", String.valueOf(param.getSolveStatus()));
        addCond(param, params);
        if (param.getGranularity() != null) {
            params.put("granularity", String.valueOf(param.getGranularity()));
        }

        OkHttpClient client = new OkHttpClient();
        try {
            Response response = client.newCall(
                    buildRequest("countDetailGovmsgboxs", GOVMSGBOX_SERVICE_NAME, param.getUserName(), params)).execute();

            if (response.isSuccessful()) {
                String jsonResult = response.body().toString();
                if (StringUtil.isEmpty(jsonResult)) {
                    return null;
                }
                return JSON.parseObject(jsonResult, NBHDHistoryRes.class);
            } else {
                log.error("failed to countDetailGovmsgboxs, error: " + response);
                throw new RemoteException("获取咨询统计历史记录失败！");
            }
        } catch (IOException e) {
            log.error("failed countDetailGovmsgboxs", e);
            throw new RemoteException("获取咨询统计历史记录失败！", e);
        }
    }

    private Request buildRequest(String methodName, String serviceName, String userName, Map<String, String> params) {
        return newServiceRequestBuilder()
                .setUrlFormat("%s/fdy/%s.do?method=%s&userName=%s")
                .setServiceUrl(nbhdServiceUrl)
                .setServiceName(serviceName)
                .setMethodName(methodName)
                .setUserName(userName)
                .setParams(params).build();
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

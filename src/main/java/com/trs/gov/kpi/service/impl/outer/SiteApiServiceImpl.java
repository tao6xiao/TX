package com.trs.gov.kpi.service.impl.outer;

import com.alibaba.fastjson.JSON;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.trs.gov.kpi.entity.exception.RemoteException;
import com.trs.gov.kpi.entity.outerapi.ApiPageData;
import com.trs.gov.kpi.entity.outerapi.ApiResult;
import com.trs.gov.kpi.entity.outerapi.Channel;
import com.trs.gov.kpi.entity.outerapi.Site;
import com.trs.gov.kpi.service.outer.SiteApiService;
import com.trs.gov.kpi.utils.OuterApiUtil;
import com.trs.gov.kpi.utils.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

/**
 * Created by linwei on 2017/5/18.
 */
@Slf4j
@Service
public class SiteApiServiceImpl implements SiteApiService {

    private static final String EDIT_CENTER_URL = "http://dev3.trs.org.cn";

    private static final String SERVICE_NAME = "gov_site";

    @Override
    public Site getSiteById(int siteId, String userName) throws RemoteException {

        try {
            Map<String, String> params = Collections.singletonMap("SiteId", String.valueOf(siteId));
            OkHttpClient client = new OkHttpClient();
            Response response = client.newCall(
                    buildRequest("findSiteById", userName, params)).execute();

            if (response.isSuccessful()) {
                ApiResult result = OuterApiUtil.toResultObj(response.body().string());
                if (result == null) {
                    log.error("invalid result: " + response.body().string());
                    throw new RemoteException("获取站点失败！");
                }

                return JSON.parseObject(result.getData(), Site.class);
            } else {
                log.error("failed to get site, error: " + response.body().toString());
                throw new RemoteException("获取站点失败！");
            }
        } catch (IOException e) {
            log.error("failed getSiteById", e);
            throw new RemoteException("获取站点失败！", e);
        }
    }

    @Override
    public List<Channel> getChildChannel(int siteId, int parentId, String userName) throws RemoteException {

        try {
            Map<String, String> params = new HashMap<>();
            if (parentId <= 0) {
                // 获取站点一级的栏目
                params.put("SiteId", String.valueOf(siteId));
            } else {
                params.put("ParentChannelId", String.valueOf(parentId));
            }
            OkHttpClient client = new OkHttpClient();
            Response response = client.newCall(
                    buildRequest("queryChildrenChannelsOnEditorCenter", userName, params)).execute();

            if (response.isSuccessful()) {
                ApiResult result = OuterApiUtil.toResultObj(response.body().string());
                if (result == null || StringUtil.isEmpty(result.getData())) {
                    log.error("invalid result: " + response.body().string());
                    throw new RemoteException("获取子栏目失败！");
                }

                ApiPageData pageData = JSON.parseObject(result.getData(), ApiPageData.class);
                if (StringUtil.isEmpty(result.getData())) {
                    log.error("invalid page data: " + response.body().string());
                    throw new RemoteException("获取子栏目失败！");
                }

                return JSON.parseArray(pageData.getData(), Channel.class);
            } else {
                log.error("error: " + response.body().toString());
                throw new RemoteException("获取子栏目失败！");
            }
        } catch (IOException e) {
            log.error("", e);
            throw new RemoteException("获取子栏目失败！", e);
        }
    }

    @Override
    public Channel getChannelById(int channelId, String userName) throws RemoteException {

        try {
            Map<String, String> params = Collections.singletonMap("ChannelId", String.valueOf(channelId));
            OkHttpClient client = new OkHttpClient();
            Response response = client.newCall(buildRequest("findChannelById", userName, params)).execute();

            if (response.isSuccessful()) {
                ApiResult result = OuterApiUtil.toResultObj(response.body().string());
                if (result == null) {
                    log.error("invalid result: " + response.body().string());
                    throw new RemoteException("获取栏目失败！");
                }
                return JSON.parseObject(result.getData(), Channel.class);
            } else {
                log.error("failed to get site, error: " + response.body().toString());
                throw new RemoteException("获取站点失败！");
            }
        } catch (IOException e) {
            log.error("failed getSiteById", e);
            throw new RemoteException("获取站点失败！", e);
        }
    }


    public String getRequestUrl(String methodName, String userName, Map<String, String> params) {
        if (userName == null || userName.trim().isEmpty()) {
            userName = "admin";
        }
        StringBuilder url = new StringBuilder(
                String.format("%s/gov/opendata.do?serviceId=%s&methodname=%s&CurrUserName=%s",
                        EDIT_CENTER_URL, SERVICE_NAME, methodName, userName));
        if (params == null) {
            return url.toString();
        }
        Iterator<String> iter = params.keySet().iterator();
        while (iter.hasNext()) {
            String paramKey = iter.next();
            url.append("&").append(paramKey).append("=").append(params.get(paramKey));
        }
        return url.toString();
    }

    private Request buildRequest(String methodName, String userName, Map<String, String> params) {
        return new Request.Builder()
                .url(getRequestUrl(methodName, userName, params))
                .addHeader("Accept", "application/json; charset=utf-8")
                .build();
    }
}

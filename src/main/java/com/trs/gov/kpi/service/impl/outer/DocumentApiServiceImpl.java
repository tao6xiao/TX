package com.trs.gov.kpi.service.impl.outer;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.trs.gov.kpi.entity.exception.RemoteException;
import com.trs.gov.kpi.entity.outerapi.ApiResult;
import com.trs.gov.kpi.service.outer.DocumentApiService;
import com.trs.gov.kpi.utils.OuterApiServiceUtil;
import com.trs.gov.kpi.utils.OuterApiUtil;
import com.trs.gov.kpi.utils.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

/**
 * Created by linwei on 2017/5/23.
 */
@Slf4j
@Service
public class DocumentApiServiceImpl implements DocumentApiService {

    private static final String SERVICE_NAME = "gov_commondocument";

    @Override
    public List<Integer> getPublishDocIds(String useName, int siteId, int channelId, Date beginTime) throws RemoteException {
        try {
            Map<String, String> params = new HashMap<>();
            params.put("SiteId", String.valueOf(siteId));
            params.put("ChannelId", String.valueOf(channelId));
            OkHttpClient client = new OkHttpClient();
            Response response = client.newCall(
                    buildRequest("queryAllPublishedDocIds", useName, params)).execute();

            if (response.isSuccessful()) {
                ApiResult result = OuterApiUtil.toResultObj(response.body().string());
                if (result == null || StringUtil.isEmpty(result.getData())) {
                    log.error("invalid result: " + response);
                    throw new RemoteException("获取发布文档ID失败！");
                }

                List<Integer> ids = new ArrayList<>();
                if (result.getData() != null && !result.getData().trim().isEmpty()) {
                    String[] idArray = result.getData().trim().split(",");
                    for (String id : idArray) {
                        ids.add(Integer.valueOf(id));
                    }
                }

                return ids;
            } else {
                log.error("error: " + response);
                throw new RemoteException("获取发布文档ID失败！");
            }
        } catch (IOException e) {
            log.error("", e);
            throw new RemoteException("获取发布文档ID失败！", e);
        }

    }


    private Request buildRequest(String methodName, String userName, Map<String, String> params) {
        return new OuterApiServiceUtil.ServiceRequestBuilder()
                .setUrlFormat("%s/gov/opendata.do?serviceId=%s&methodname=%s&CurrUserName=%s")
                .setServiceName(SERVICE_NAME)
                .setMethodName(methodName)
                .setUserName(userName)
                .setParams(params).build();
    }
}

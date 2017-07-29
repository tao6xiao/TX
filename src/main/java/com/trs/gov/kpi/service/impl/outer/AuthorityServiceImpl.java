package com.trs.gov.kpi.service.impl.outer;

import com.alibaba.fastjson.JSON;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.trs.gov.kpi.entity.exception.RemoteException;
import com.trs.gov.kpi.entity.outerapi.ApiResult;
import com.trs.gov.kpi.service.outer.AuthorityService;
import com.trs.gov.kpi.utils.LogUtil;
import com.trs.gov.kpi.utils.OuterApiServiceUtil;
import com.trs.gov.kpi.utils.OuterApiUtil;
import com.trs.gov.kpi.utils.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static com.trs.gov.kpi.utils.OuterApiServiceUtil.newServiceRequestBuilder;

/**
 * Created by ranwei on 2017/7/4.
 */
@Slf4j
@Service
public class AuthorityServiceImpl implements AuthorityService {

    @Value("${service.outer.editcenter.url}")
    private String editCenterServiceUrl;

    private static final String MODULE_ID = "40";//绩效考核模块ID


    @Override
    public boolean hasRight(String currUserName, Integer siteId, Integer channelId, String oprkeys) throws RemoteException {

        Map<String, String> params = new HashMap<>();
        if (siteId != null) {
            params.put("siteId", String.valueOf(siteId));
        }
        if (channelId != null) {
            params.put("channelId", String.valueOf(channelId));
        }
        params.put("oprkeys", oprkeys);
        //判断平台级权限需要用到
        params.put("MODULEID", MODULE_ID);

        try {
            OkHttpClient client = new OkHttpClient();
            Response response = client.newCall(buildRequest("hasRight", params, "gov_right", currUserName)).execute();

            if (response.isSuccessful()) {
                ApiResult result = OuterApiUtil.getValidResult(response, "查找权限");
                if (StringUtil.isEmpty(result.getData())) {
                    return false;
                }
                return JSON.parseObject(result.getData(), Boolean.class);
            } else {
                log.error("failed to findRight, error: " + response);
                throw new RemoteException("查找指定oprkeys的权限失败！");
            }
        } catch (IOException e) {
            log.error("failed findRight", e);
            LogUtil.addSystemLog("failed findRight", e);
            throw new RemoteException("查找指定oprkeys的权限失败！", e);
        }
    }

    private Request buildRequest(String methodName, Map<String, String> params, String serviceName, String userName) {
        OuterApiServiceUtil.addUserNameParam(userName, params);
        return newServiceRequestBuilder()
                .setUrlFormat("%s/gov/opendata.do?serviceId=%s&methodname=%s")
                .setServiceUrl(editCenterServiceUrl)
                .setServiceName(serviceName)
                .setMethodName(methodName)
                .setParams(params).build();
    }
}

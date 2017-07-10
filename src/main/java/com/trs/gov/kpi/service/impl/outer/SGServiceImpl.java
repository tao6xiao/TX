package com.trs.gov.kpi.service.impl.outer;

import com.alibaba.fastjson.JSON;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.trs.gov.kpi.entity.exception.RemoteException;
import com.trs.gov.kpi.entity.outerapi.sp.SGPageDataRes;
import com.trs.gov.kpi.entity.outerapi.sp.SGStatistics;
import com.trs.gov.kpi.entity.requestdata.PageDataRequestParam;
import com.trs.gov.kpi.service.outer.SGService;
import com.trs.gov.kpi.utils.OuterApiServiceUtil;
import com.trs.gov.kpi.utils.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ranwei on 2017/6/12.
 */
@Slf4j
@Service
public class SGServiceImpl implements SGService {

    @Value("${service.outer.sg.url}")
    private String sgServiceUrl;

    private static final String SITE_ID = "siteId";

    @Override
    public SGPageDataRes getSGList(PageDataRequestParam param) throws RemoteException {
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put(SITE_ID, Integer.toString(param.getSiteId()));
        initParamMap(param, paramMap);

        OkHttpClient client = new OkHttpClient();
        Request request = OuterApiServiceUtil.buildRequest(sgServiceUrl, "/questionBszn.jsp", paramMap);
        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                String jsonResult = response.body().string();
                if (StringUtil.isEmpty(jsonResult)) {
                    return null;
                }
                return JSON.parseObject(jsonResult, SGPageDataRes.class);
            } else {
                log.error("failed to getSGList, error: " + response);
                throw new RemoteException("获取服务指南失败！");
            }
        } catch (Exception e) {
            log.error("getSGList failed ", e);
            throw new RemoteException("获取服务指南失败！", e);
        }
    }

    @Override
    public SGStatistics getSGCount(PageDataRequestParam param) throws RemoteException {
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put(SITE_ID, Integer.toString(param.getSiteId()));
        initParamMap(param, paramMap);

        OkHttpClient client = new OkHttpClient();
        Request request = OuterApiServiceUtil.buildRequest(sgServiceUrl, "/bsznCount.jsp", paramMap);
        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                String jsonResult = response.body().string();
                if (StringUtil.isEmpty(jsonResult)) {
                    return null;
                }
                return JSON.parseObject(jsonResult, SGStatistics.class);
            } else {
                log.error("failed to getSGCount, error: " + response);
                throw new RemoteException("获取服务指南统计失败！");
            }
        } catch (Exception e) {
            log.error("getSGCount failed ", e);
            throw new RemoteException("获取服务指南统计失败！", e);
        }

    }

    @Override
    public List getSGHistoryCount(PageDataRequestParam param) throws RemoteException {
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put(SITE_ID, Integer.toString(param.getSiteId()));
        initParamMap(param, paramMap);

        OkHttpClient client = new OkHttpClient();
        Request request = OuterApiServiceUtil.buildRequest(sgServiceUrl, "/bsznCountByGranularity.jsp", paramMap);
        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                String jsonResult = response.body().string();
                if (StringUtil.isEmpty(jsonResult)) {
                    return null;
                }
                return JSON.parseObject(jsonResult, List.class);
            } else {
                log.error("failed to getSGHistoryCount, error: " + response);
                throw new RemoteException("获取服务指南历史统计失败！");
            }
        } catch (Exception e) {
            log.error("getSGHistoryCount failed ", e);
            throw new RemoteException("获取服务指南历史统计失败！", e);
        }
    }

    @Override
    public SGPageDataRes getAllService(PageDataRequestParam param) throws RemoteException {
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put(SITE_ID, Integer.toString(param.getSiteId()));
        initParamMap(param, paramMap);

        OkHttpClient client = new OkHttpClient();
        Request request = OuterApiServiceUtil.buildRequest(sgServiceUrl, "/allBsznUrl.jsp", paramMap);
        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                String jsonResult = response.body().string();
                if (StringUtil.isEmpty(jsonResult)) {
                    return null;
                }
                return JSON.parseObject(jsonResult, SGPageDataRes.class);
            } else {
                log.error("failed to getAllService, error: " + response);
                throw new RemoteException("获取服务链接失败！");
            }
        } catch (Exception e) {
            log.error("getAllService failed ", e);
            throw new RemoteException("获取服务链接失败！", e);
        }
    }

    private void initParamMap(PageDataRequestParam param, Map<String, String> paramMap) {
        if (param.getPageIndex() != null) {
            paramMap.put("pageIndex", Integer.toString(param.getPageIndex()));
        }
        if (param.getPageSize() != null) {
            paramMap.put("pageSize", Integer.toString(param.getPageSize()));
        }
        if (!StringUtil.isEmpty(param.getBeginDateTime())) {
            paramMap.put("beginDateTime", param.getBeginDateTime());
        }
        if (!StringUtil.isEmpty(param.getEndDateTime())) {
            paramMap.put("endDateTime", param.getEndDateTime());
        }
        if (!StringUtil.isEmpty(param.getSearchField())) {
            paramMap.put("searchField", param.getSearchField());
        }
        if (!StringUtil.isEmpty(param.getSearchText())) {
            paramMap.put("searchText", param.getSearchText());
        }
        if (!StringUtil.isEmpty(param.getSortFields())) {
            paramMap.put("sortFields", param.getSortFields());
        }
        if (param.getGranularity() != null) {
            paramMap.put("granularity", Integer.toString(param.getGranularity()));
        }
    }
}

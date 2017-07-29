package com.trs.gov.kpi.service.impl.outer;

import com.alibaba.fastjson.JSON;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.trs.gov.kpi.entity.exception.RemoteException;
import com.trs.gov.kpi.entity.outerapi.ApiResult;
import com.trs.gov.kpi.entity.outerapi.sp.SGPageDataRes;
import com.trs.gov.kpi.entity.outerapi.sp.SGStatistics;
import com.trs.gov.kpi.entity.requestdata.PageDataRequestParam;
import com.trs.gov.kpi.entity.responsedata.History;
import com.trs.gov.kpi.service.outer.SGService;
import com.trs.gov.kpi.utils.LogUtil;
import com.trs.gov.kpi.utils.OuterApiServiceUtil;
import com.trs.gov.kpi.utils.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
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


    @Override
    public SGPageDataRes getSGList(PageDataRequestParam param) throws RemoteException {
        Map<String, String> paramMap = initParamMap(param);
        OkHttpClient client = new OkHttpClient();
        Request request = OuterApiServiceUtil.buildRequest(sgServiceUrl, "/questionBszn.jsp", paramMap);
        return (SGPageDataRes) getResult(client, request, "获取服务指南失败！", SGPageDataRes.class);
    }


    @Override
    public SGStatistics getSGCount(PageDataRequestParam param) throws RemoteException {
        Map<String, String> paramMap = initParamMap(param);
        OkHttpClient client = new OkHttpClient();
        Request request = OuterApiServiceUtil.buildRequest(sgServiceUrl, "/bsznCount.jsp", paramMap);
        return (SGStatistics) getResult(client, request, "获取服务指南统计失败！", SGStatistics.class);
    }

    @Override
    public History getSGHistoryCount(PageDataRequestParam param) throws RemoteException {
        Map<String, String> paramMap = initParamMap(param);
        OkHttpClient client = new OkHttpClient();
        Request request = OuterApiServiceUtil.buildRequest(sgServiceUrl, "/bsznCountByGranularity.jsp", paramMap);
        List list = (List) getResult(client, request, "获取服务指南历史统计失败！", List.class);
        return new History(new Date(), list);
    }

    @Override
    public SGPageDataRes getAllService(Integer siteId) throws RemoteException {
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("siteId", Integer.toString(siteId));
        OkHttpClient client = new OkHttpClient();
        Request request = OuterApiServiceUtil.buildRequest(sgServiceUrl, "/allBsznUrl.jsp", paramMap);
        return (SGPageDataRes) getResult(client, request, "获取服务链接失败！", SGPageDataRes.class);
    }

    private Map<String, String> initParamMap(PageDataRequestParam param) {
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("siteId", Integer.toString(param.getSiteId()));
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
        return paramMap;
    }

    private Object getResult(OkHttpClient client, Request request, String msg, Class clazz) throws RemoteException {
        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                String jsonResult = response.body().string();
                if (StringUtil.isEmpty(jsonResult)) {
                    throw new RemoteException(msg);
                }
                ApiResult result = JSON.parseObject(jsonResult, ApiResult.class);
                return JSON.parseObject(result.getData(), clazz);
            } else {
                log.error("failed to getSGService, error: " + response);
                throw new RemoteException(msg);
            }
        } catch (Exception e) {
            log.error("getSGService failed ", e);
            LogUtil.addSystemLog("getSGService failed ", e);
            throw new RemoteException(msg, e);
        }
    }
}

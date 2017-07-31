package com.trs.gov.kpi.service.impl.outer;

import com.alibaba.fastjson.JSON;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.trs.gov.kpi.entity.exception.RemoteException;
import com.trs.gov.kpi.entity.outerapi.ApiResult;
import com.trs.gov.kpi.entity.outerapi.ChnlDoc;
import com.trs.gov.kpi.entity.outerapi.Document;
import com.trs.gov.kpi.service.outer.DocumentApiService;
import com.trs.gov.kpi.service.outer.SiteApiService;
import com.trs.gov.kpi.utils.LogUtil;
import com.trs.gov.kpi.utils.OuterApiServiceUtil;
import com.trs.gov.kpi.utils.OuterApiUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.*;

import static com.trs.gov.kpi.utils.OuterApiServiceUtil.newServiceRequestBuilder;

/**
 * Created by linwei on 2017/5/23.
 */
@Slf4j
@Service
public class DocumentApiServiceImpl implements DocumentApiService {

    @Value("${service.outer.editcenter.url}")
    private String editCenterServiceUrl;

    @Resource
    private SiteApiService siteApiService;

    private static final String SERVICE_NAME = "gov_documentapi";

    @Override
    public List<Integer> getPublishDocIds(String userName, int siteId, int channelId, String beginTime) throws RemoteException {
        try {
            Map<String, String> params = new HashMap<>();
            params.put("SiteId", String.valueOf(siteId));
            params.put("ChannelId", String.valueOf(channelId));
            if (beginTime != null && !beginTime.trim().isEmpty()) {
                params.put("OperTimeStart", beginTime);
            }
            OkHttpClient client = new OkHttpClient();
            Response response = client.newCall(
                    buildRequest("queryAllPublishedDocIds", userName, params)).execute();

            if (response.isSuccessful()) {
                ApiResult result = getValidResult(response, "获取发布文档ID");
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
            LogUtil.addSystemLog("", e);
            throw new RemoteException("获取发布文档ID失败！", e);
        }

    }

    @Override
    public Document getDocument(String userName, int channelId, int documentId) throws RemoteException {
        try {
            Map<String, String> params = new HashMap<>();
            params.put("ChannelId", String.valueOf(channelId));
            params.put("DocId", String.valueOf(documentId));
            OkHttpClient client = new OkHttpClient();
            Response response = client.newCall(
                    buildRequest("findDocumentById", userName, params)).execute();

            return responseManger("findDocumentById", response);
        } catch (IOException e) {
            log.error("failed to getDocument, error: ", e);
            LogUtil.addSystemLog("failed to getDocument, error: ", e);
            throw new RemoteException("获取发布文档失败！", e);
        }
    }

    @Override
    public List<Document> getPublishDocuments(int siteId) throws RemoteException {
        Set<Integer> allChnlIds = new HashSet<>();
        allChnlIds = siteApiService.getAllChildChnlIds(null, siteId, 0, allChnlIds);
        if (allChnlIds == null || allChnlIds.isEmpty()) {
            return new ArrayList<>();
        }

        List<Document> documentList = new ArrayList<>();
        for (Integer chnlId : allChnlIds) {
            if (chnlId == null) {
                continue;
            }
            // TODO: 2017/5/24  set userName
            List<Integer> publishDocIds = getPublishDocIds(null, siteId, chnlId, null);
            for (Integer publishDocId : publishDocIds) {
                if (publishDocId == null) {
                    continue;
                }
                // TODO: 2017/5/24  set userName
                Document document = getDocument(null, chnlId, publishDocId);
                document.setSiteId(siteId);
                documentList.add(document);
            }
        }
        return documentList;
    }

    @Override
    public ChnlDoc findDocumentByUrl(String userName, String url) throws RemoteException {
        try {
            Map<String, String> params = new HashMap<>();
            params.put("URL", url);
            OkHttpClient client = new OkHttpClient();
            Response response = client.newCall(
                    buildRequest("findDocumentByUrl", userName, params)).execute();

            if (response.isSuccessful()) {
                ApiResult result = getValidResult(response, "获取文档");
                if (result.getData() != null && !result.getData().trim().isEmpty()) {
                    return JSON.parseObject(result.getData(), ChnlDoc.class);
                }
                return null;
            } else {
                log.error("failed to findDocumentByUrl, error: " + response);
                throw new RemoteException("获取文档失败！");
            }
        } catch (IOException e) {
            log.error("failed to findDocumentByUrl, error", e);
            LogUtil.addSystemLog("failed to findDocumentByUrl, error", e);
            throw new RemoteException("通过url获取文档失败！", e);
        }
    }

    private Document responseManger(String method, Response response) throws IOException, RemoteException {
        if (response.isSuccessful()) {
            ApiResult result = getValidResult(response, "获取文档");
            if (result.getData() != null && !result.getData().trim().isEmpty()) {
                return JSON.parseObject(result.getData(), Document.class);
            }
            return null;
        } else {
            log.error("failed to " + method + ", error: " + response);
            throw new RemoteException("获取文档失败！");
        }
    }

    private ApiResult getValidResult(Response response, String errMsg) throws RemoteException,
            IOException {
        ApiResult result = OuterApiUtil.toResultObj(response.body().string());
        if (result == null) {
            log.error("invalid result: " + response);
            throw new RemoteException(errMsg + "失败！");
        }
        if (!result.isOk()) {
            log.error("fail result: " + result.getMsg());
            throw new RemoteException(errMsg + "失败！[" + result.getMsg() + "]");
        }
        return result;
    }

    private Request buildRequest(String methodName, String userName, Map<String, String> params) {
        OuterApiServiceUtil.addUserNameParam(userName, params);
        return newServiceRequestBuilder()
                .setUrlFormat("%s/gov/opendata.do?serviceId=%s&methodname=%s")
                .setServiceUrl(editCenterServiceUrl)
                .setServiceName(SERVICE_NAME)
                .setMethodName(methodName)
                .setParams(params).build();
    }
}

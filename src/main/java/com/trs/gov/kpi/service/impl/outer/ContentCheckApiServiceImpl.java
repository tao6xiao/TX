package com.trs.gov.kpi.service.impl.outer;

import com.alibaba.fastjson.JSON;
import com.trs.gov.kpi.entity.exception.RemoteException;
import com.trs.gov.kpi.entity.outerapi.ContentCheckResult;
import com.trs.gov.kpi.service.outer.ContentCheckApiService;
import com.trs.gov.kpi.utils.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by he.lang on 2017/5/24.
 */
@Slf4j
@Service
public class ContentCheckApiServiceImpl implements ContentCheckApiService {

    @Value("${service.outer.ckm.url}")
    private String cmkUrl;

    @Override
    public ContentCheckResult check(String text, String type) throws RemoteException {
        long beginTime = new Date().getTime();
        CloseableHttpClient httpClient = null;
        try {
            httpClient = HttpClients.createDefault();
            HttpPost httpPost = new HttpPost(cmkUrl);
            List<NameValuePair> parameters = new ArrayList<>();
            parameters.add(new BasicNameValuePair("text", text));
            parameters.add(new BasicNameValuePair("type", type));
            HttpEntity entity1 = new UrlEncodedFormEntity(parameters, "utf-8");
            httpPost.setEntity(entity1);
            byte[] base = new Base64().encode("admin:trsadmin".getBytes());
            String authorization = "Basic " + new String(base);
            httpPost.setHeader("Authorization", authorization);
            HttpResponse response = httpClient.execute(httpPost);
            long endTime = new Date().getTime();

            if (endTime - beginTime > 1000) {
                log.info("ckm check time use: " + (endTime - beginTime));
            }

            StatusLine statusLine = response.getStatusLine();
            int code = statusLine.getStatusCode();
            if (code == 200) {
                HttpEntity entity2 = response.getEntity();
                String resultText = EntityUtils.toString(entity2, "utf-8");
                if (StringUtil.isEmpty(resultText)) {
                    throw new RemoteException("invalid result, it is empty. ");
                }
                ContentCheckResult checkResult = JSON.parseObject(resultText, ContentCheckResult.class);
                if (!checkResult.isOk()) {
                    log.warn("CKM check return error: " + resultText);
                }
                return checkResult;
            } else {
                throw new RemoteException("bad code: " + code);
            }
        } catch (IOException e) {
            throw new RemoteException("fail to check!", e);
        } finally {
            if (httpClient != null) {
                try {
                    httpClient.close();
                } catch (IOException e) {
                    log.error("", e);
                }
            }
        }
    }
}

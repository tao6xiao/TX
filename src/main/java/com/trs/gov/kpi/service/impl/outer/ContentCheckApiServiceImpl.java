package com.trs.gov.kpi.service.impl.outer;

import com.alibaba.fastjson.JSON;
import com.trs.gov.kpi.entity.exception.RemoteException;
import com.trs.gov.kpi.entity.outerapi.ContentCheckResult;
import com.trs.gov.kpi.service.outer.ContentCheckApiService;
import com.trs.gov.kpi.utils.StringUtil;
import lombok.extern.slf4j.Slf4j;
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
import sun.misc.BASE64Encoder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by he.lang on 2017/5/24.
 */
@Slf4j
@Service
public class ContentCheckApiServiceImpl implements ContentCheckApiService {

    @Value("${service.outer.ckm.url}")
    private String CKMUrl;

    @Override
    public ContentCheckResult check(String text, String type) throws RemoteException {
        try {
            CloseableHttpClient httpClient = HttpClients.createDefault();
            HttpPost httpPost = new HttpPost(CKMUrl);
            List<NameValuePair> parameters = new ArrayList<>();
            parameters.add(new BasicNameValuePair("text", text));
            parameters.add(new BasicNameValuePair("type", type));
            HttpEntity entity1 = new UrlEncodedFormEntity(parameters, "utf-8");
            httpPost.setEntity(entity1);
            String base = new BASE64Encoder().encode("admin:trsadmin".getBytes());
            String authorization = "Basic " + base;
            httpPost.setHeader("Authorization", authorization);
            HttpResponse response = httpClient.execute(httpPost);
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
                throw new RemoteException("bad code: " + String.valueOf(code));
            }
        } catch (IOException e) {
            throw new RemoteException("fail to check!", e);
        }
    }
}

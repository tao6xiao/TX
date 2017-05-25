package com.trs.gov.kpi.service.impl.outer;

import com.trs.gov.kpi.dao.MonitorSiteMapper;
import com.trs.gov.kpi.entity.exception.RemoteException;
import com.trs.gov.kpi.entity.outerapi.Channel;
import com.trs.gov.kpi.entity.outerapi.Document;
import com.trs.gov.kpi.service.outer.ContentCheckApiService;
import com.trs.gov.kpi.service.outer.DocumentApiService;
import com.trs.gov.kpi.service.outer.SiteApiService;
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

import javax.annotation.Resource;
import java.io.IOException;
import java.text.ParseException;
import java.util.*;

/**
 * Created by he.lang on 2017/5/24.
 */
@Service
public class ContentCheckApiServiceImpl implements ContentCheckApiService {

    @Value("${service.outer.ckm.url}")
    private String CKMUrl;

    @Override
    public String check(String text, String type) throws RemoteException {
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
                return EntityUtils.toString(entity2, "utf-8");
            } else {
                throw new RemoteException("bad code: " + String.valueOf(code));
            }
        } catch (IOException e) {
            throw new RemoteException("fail to check!", e);
        }
    }
}

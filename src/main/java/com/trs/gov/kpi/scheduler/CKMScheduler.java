package com.trs.gov.kpi.scheduler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.trs.gov.kpi.dao.IssueMapper;
import com.trs.gov.kpi.dao.MonitorSiteMapper;
import com.trs.gov.kpi.service.outer.ContentCheckApiService;
import lombok.Getter;
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
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by he.lang on 2017/5/24.
 */
@Slf4j
@Component
@Scope("prototype")
public class CKMScheduler{

    @Value("${service.outer.ckm.url}")
    private String CKMUrl;

    @Resource
    private IssueMapper issueMapper;

    @Resource
    private ContentCheckApiService contentCheckApiService;

    @Getter
    private final Runnable task = new Runnable() {
        @Override
        public void run() {
            //get document
            String result = check("", "");
            JSONObject resultObj =  JSON.parseObject(result);
//            resultObj.getInteger("code") == 1;
            JSONObject checkResult = resultObj.getJSONObject("result");
            checkResult.keySet();

        }
    };


    private String check(String text,String type){

        try{

            CloseableHttpClient httpClient = HttpClients.createDefault();

            HttpPost httpPost = new HttpPost(CKMUrl);


            List<NameValuePair> parameters = new ArrayList<NameValuePair>();

            parameters.add(new BasicNameValuePair("text", text));

            parameters.add(new BasicNameValuePair("type", type));

            HttpEntity entity1 = new UrlEncodedFormEntity(parameters, "utf-8");

            httpPost.setEntity(entity1);

            HttpResponse response = httpClient.execute(httpPost);

            StatusLine statusLine = response.getStatusLine();

            int code = statusLine.getStatusCode();

            if (code == 200) {

                HttpEntity entity2 = response.getEntity();

//                System.out.println(EntityUtils.toString(entity2, "utf-8"));

                return EntityUtils.toString(entity2, "utf-8");//直接返回检查的结果
            }

        }catch(IOException e){

            e.printStackTrace();

        }
        return null;
    }
}

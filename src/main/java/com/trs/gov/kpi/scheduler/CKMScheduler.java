package com.trs.gov.kpi.scheduler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.trs.gov.kpi.constant.InfoErrorType;
import com.trs.gov.kpi.constant.IsDelType;
import com.trs.gov.kpi.constant.IsResolvedType;
import com.trs.gov.kpi.constant.IssueType;
import com.trs.gov.kpi.dao.IssueMapper;
import com.trs.gov.kpi.dao.MonitorSiteMapper;
import com.trs.gov.kpi.entity.Issue;
import com.trs.gov.kpi.entity.exception.RemoteException;
import com.trs.gov.kpi.entity.outerapi.Document;
import com.trs.gov.kpi.service.outer.ContentCheckApiService;
import com.trs.gov.kpi.utils.InitTime;
import lombok.Getter;
import lombok.Setter;
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
import java.text.ParseException;
import java.util.*;

/**
 * Created by he.lang on 2017/5/24.
 */
@Slf4j
@Component
@Scope("prototype")
public class CKMScheduler extends AbstractScheduler {

    @Value("${service.outer.ckm.url}")
    private String CKMUrl;

    @Setter
    @Getter
    private String baseUrl;

    @Setter @Getter
    private Integer siteId;

    @Resource
    private IssueMapper issueMapper;

    @Resource
    private ContentCheckApiService contentCheckApiService;

    @Getter
    private final Runnable task = new Runnable() {
        @Override
        public void run() {
            log.info("CKMScheduler start...");
            List<Issue> issueList = new ArrayList<>();
            try {
                List<Document> documentList = contentCheckApiService.getPublishDocuments(getSiteId());
                for (Document document : documentList) {
                    if("".equals(document.getDocTitle()) || "".equals(document.getDocContent()) || document.getDocPubUrl() == null){
                        continue;
                    }
                    String checkText = document.getDocTitle() + "。" + document.getDocContent();
                    String result = check(checkText, "字词,敏感词");
                    JSONObject resultObj = JSON.parseObject(result);
                    if (resultObj.getInteger("code") == 1) {
                        JSONObject checkResult = resultObj.getJSONObject("result");
                        Set<String> keySet = checkResult.keySet();
                        for (String key : keySet) {
                            if(key == null){
                                continue;
                            }
                            String value = checkResult.getString(key);
                            if ("字词".equals(key)) {
                                key = "错别字";
                            }
                            Integer subTypeId = InfoErrorType.getSubTypeIdByName(key).value;
                            if (subTypeId > 0) {
                                Issue issue = new Issue();
                                issue.setSiteId(document.getSiteId());
                                issue.setTypeId(IssueType.INFO_ISSUE.getCode());
                                issue.setSubTypeId(subTypeId);
                                issue.setDetail(document.getDocPubUrl());
                                Date nowTime = new Date();
                                String nowTimeStr = InitTime.getNowTimeFormat(nowTime);
                                nowTime = InitTime.getNowTimeFormat(nowTimeStr);
                                issue.setIssueTime(nowTime);
                                issue.setIsResolved(IsResolvedType.IS_NOT_RESOLVED.getCode());
                                issue.setIsDel(IsDelType.IS_NOT_DEL.getCode());
                                issue.setCustomer1(value);
                                issueList.add(issue);
                            }
                        }
                    }
                }
            } catch (RemoteException e) {
                log.info("");
            } catch (ParseException e) {
                e.printStackTrace();
            } finally {
                log.info("CKMScheduler end...");
            }

        }
    };


    private String check(String text, String type) {

        try {

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

        } catch (IOException e) {

            e.printStackTrace();

        }
        return null;
    }

}

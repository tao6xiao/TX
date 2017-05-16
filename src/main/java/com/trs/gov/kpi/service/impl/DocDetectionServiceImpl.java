package com.trs.gov.kpi.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.trs.gov.kpi.constant.DocumentErrorType;
import com.trs.gov.kpi.constant.IssueType;
import com.trs.gov.kpi.dao.IssueMapper;
import com.trs.gov.kpi.entity.Issue;
import com.trs.gov.kpi.entity.exception.RemoteException;
import com.trs.gov.kpi.service.DocDetectionService;
import com.trs.gov.kpi.utils.HttpUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.*;

/**
 * Created by wangxuan on 2017/5/15.
 */
@Service
@Slf4j
public class DocDetectionServiceImpl implements DocDetectionService {

    @Resource
    private IssueMapper issueMapper;

    @Override
    public Set<String> detectText(String text) throws RemoteException {

        Map<String, String> detectionParams = new HashMap<>();
        detectionParams.put(DETECTION_KEY_TYPE, StringUtils.join(DETECTION_TYPE, ";"));
        detectionParams.put(DETECTION_KEY_TEXT, text);

        try {

            String response = HttpUtils.postHttpInfo(DETECTION_URL, detectionParams);
            JSONObject detection = JSON.parseObject(response);
            Assert.isTrue(detection.getInteger("code") == 1, String.format("远程调用失败，返回为:%s", JSON.toJSONString(response)));

            JSONObject result = detection.getJSONObject("result");
            return result.keySet();
        } catch (Exception e) {

            log.error("detection text error!", e);
            throw new RemoteException();
        }
    }

    @Override
    public List<String> getDocumentIdsBySiteId(Integer siteId) {

        return Collections.emptyList();
    }

    @Override
    public Document getDocumentById(String documentId) {

        return new Document();
    }

    public List<Integer> getSiteIds() {

        return Collections.emptyList();
    }

    public void DetectInfo() throws RemoteException {

        List<Integer> siteIds = getSiteIds();
        for(Integer siteId: siteIds) {

            List<String> documentIds = getDocumentIdsBySiteId(siteId);
            for(String documentId: documentIds) {

                Document document = getDocumentById(documentId);
                Set<String> resultSet = detectText(String.format("%s\\r\\n%s", document.getTitle(), document.getText()));
                if(CollectionUtils.isNotEmpty(resultSet)) {

                    for(String result: resultSet) {

                        Issue issue = new Issue();
                        issue.setSiteId(siteId);
                        issue.setIssueTime(new Date());
                        issue.setTypeId(IssueType.INFO_ISSUE.getCode());
                        issue.setSubTypeId(DocumentErrorType.getTypeByKey(result).getCode());
                        issue.setDetail(document.getLink());
                        issueMapper.insert(issue);
                    }
                }
            }
        }
    }
}

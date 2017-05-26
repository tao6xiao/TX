package com.trs.gov.kpi.scheduler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.trs.gov.kpi.constant.Types;
import com.trs.gov.kpi.dao.IssueMapper;
import com.trs.gov.kpi.entity.Issue;
import com.trs.gov.kpi.entity.exception.RemoteException;
import com.trs.gov.kpi.entity.outerapi.Document;
import com.trs.gov.kpi.service.outer.ContentCheckApiService;
import com.trs.gov.kpi.service.outer.DocumentApiService;
import com.trs.gov.kpi.utils.CollectionUtil;
import com.trs.gov.kpi.utils.StringUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.text.ParseException;
import java.util.*;

/**
 * Created by he.lang on 2017/5/24.
 */
@Slf4j
@Component
@Scope("prototype")
public class CKMScheduler extends AbstractScheduler {

    @Setter
    @Getter
    private String baseUrl;

    @Setter
    @Getter
    private Integer siteId;

    @Resource
    private IssueMapper issueMapper;

    @Resource
    private DocumentApiService documentApiService;

    @Resource
    private ContentCheckApiService contentCheckApiService;

    @Getter
    private final Runnable task = new Runnable() {
        @Override
        public void run() {
            log.info("CKMScheduler " + String.valueOf(siteId) + " start...");
            try {
                List<Issue> issueList = new ArrayList<>();
                List<Document> documentList = documentApiService.getPublishDocuments(getSiteId());
//                documentList = null;
//                documentList = new ArrayList<>();
//                Document document1 = new Document();
//                document1.setSiteId(11);
//                document1.setChannelId(144);
//                document1.setDocContent("我们化夏子孙，建成的小别野");
//                document1.setDocPubUrl("www.112.com");
//                document1.setDocTitle("检测文档");
//                documentList.add(document1);

                List<String> checkTypeList = Types.InfoErrorIssueType.getAllCheckTypes();
                if (checkTypeList == null || checkTypeList.isEmpty()) {
                    return;
                }

                String checkTypes = CollectionUtil.join(checkTypeList, ";");
                for (Document document : documentList) {

                    StringBuilder checkContent = new StringBuilder();
                    if (document.getDocTitle() != null &&  !document.getDocTitle().trim().isEmpty()) {
                        checkContent.append(document.getDocTitle());
                        checkContent.append("。");
                    }

                    if (document.getDocContent() != null && !document.getDocContent().trim().isEmpty()) {
                        checkContent.append(document.getDocContent());
                    }

                    if (checkContent.length() <= 0) {
                        continue;
                    }

                    String checkText = checkContent.toString();
                    String result = contentCheckApiService.check(checkText, checkTypes);

                    if (StringUtil.isEmpty(result)) {
                        log.error("invalid return value,  failed to check document " + document.getChannelId() + "->" + document.getMetaDataId());
                        continue;
                    }
                    JSONObject resultObj = JSON.parseObject(result);
                    Integer code = resultObj.getInteger("code");
                    if (code == null || code != 1) {
                        log.error("invalid code,  failed to check document "
                                + document.getChannelId() + "->" + document.getMetaDataId() + ", result = " + result);
                        continue;
                    }

                    JSONObject checkResult = resultObj.getJSONObject("result");
                    if (checkResult != null) {
                        Set<String> keySet = checkResult.keySet();
                        for (String key : keySet) {
                            if (key == null) {
                                continue;
                            }
                            String value = checkResult.getString(key);
                            Integer subTypeId = Types.InfoErrorIssueType.valueOfCheckType(key).value;
                            if (subTypeId > 0) {
                                Issue issue = new Issue();
                                issue.setSiteId(document.getSiteId());
                                issue.setTypeId(Types.IssueType.INFO_ERROR_ISSUE.value);
                                issue.setSubTypeId(subTypeId);
                                issue.setDetail(document.getDocPubUrl());
                                issue.setIssueTime(new Date());
                                issue.setCustomer1(value);
                                issueList.add(issue);
                            }
                        }
                    }
                }
                //插入监测出的信息错误数据
                for (Issue issue : issueList) {
                    issueMapper.insert(issue);
                }
                log.info("add content error count: " + issueList.size());
            } catch (RemoteException e) {
                log.error("", e);
            } catch (ParseException e) {
                log.error("", e);
            } finally {
                log.info("CKMScheduler " + String.valueOf(siteId) + " end...");
            }
        }
    };


}

package com.trs.gov.kpi.scheduler;

import com.trs.gov.kpi.constant.Types;
import com.trs.gov.kpi.dao.IssueMapper;
import com.trs.gov.kpi.entity.Issue;
import com.trs.gov.kpi.entity.exception.RemoteException;
import com.trs.gov.kpi.entity.outerapi.ContentCheckResult;
import com.trs.gov.kpi.entity.outerapi.Document;
import com.trs.gov.kpi.service.outer.ContentCheckApiService;
import com.trs.gov.kpi.service.outer.DocumentApiService;
import com.trs.gov.kpi.utils.CollectionUtil;
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
public class CKMScheduler implements SchedulerTask {

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
            log.info("CKMScheduler " + siteId + " start...");
            try {
                List<Issue> issueList = new ArrayList<>();
                List<Document> documentList = documentApiService.getPublishDocuments(getSiteId());
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

                    ContentCheckResult result = null;
                    try {
                        result = contentCheckApiService.check(checkContent.toString(), checkTypes);
                    } catch (Exception e) {
                        log.error("failed to check document " + document.getChannelId() + "->" + document.getMetaDataId(), e);
                        continue;
                    }

                    if (!result.isOk()) {
                        log.error("return error: " + result.getMessage() + ", document: " + document.getChannelId() + "->" + document.getMetaDataId());
                        continue;
                    }

                    if (result.getResult() != null) {
                        for (String checkType : checkTypeList) {
                            Types.InfoErrorIssueType subIssueType = Types.InfoErrorIssueType.valueOfCheckType(checkType);
                            String errorContent = result.getResultOfType(subIssueType);
                            if (errorContent == null) {
                                continue;
                            }
                            Issue issue = new Issue();
                            issue.setSiteId(document.getSiteId());
                            issue.setTypeId(Types.IssueType.INFO_ERROR_ISSUE.value);
                            issue.setSubTypeId(subIssueType.value);
                            issue.setDetail(document.getDocPubUrl());
                            issue.setIssueTime(new Date());
                            issue.setCustomer1(errorContent);
                            issueList.add(issue);
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
                log.info("CKMScheduler " + siteId + " end...");
            }
        }
    };


}

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
import com.trs.gov.kpi.utils.DBUtil;
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

    @Override
    public void run() {
        log.info("CKMScheduler " + siteId + " start...");
        List<String> checkTypeList = Types.InfoErrorIssueType.getAllCheckTypes();
        try {
            List<Document> documentList = documentApiService.getPublishDocuments(getSiteId());
            for (Document document : documentList) {
                insert(buildList(document, checkTypeList));
            }
        } catch (RemoteException | ParseException e) {
            log.error("", e);
        } finally {
            log.info("CKMScheduler " + siteId + " end...");
        }
    }

    private List<Issue> buildList(Document document, List<String> checkTypeList) {
        List<Issue> issueList = new ArrayList<>();

        StringBuilder checkContent = buildCheckContent(document);
        if (checkContent.length() <= 0) {
            return issueList;
        }

        ContentCheckResult result = null;
        try {
            result = contentCheckApiService.check(checkContent.toString(), CollectionUtil.join(checkTypeList, ";"));
        } catch (Exception e) {
            log.error("failed to check document " + document.getChannelId() + "->" + document.getMetaDataId(), e);
            return issueList;
        }

        if (!result.isOk()) {
            log.error("return error: " + result.getMessage() + ", document: " + document.getChannelId() + "->" + document.getMetaDataId());
            return issueList;
        }

        if (result.getResult() != null) {
            issueList = toIssueList(document, checkTypeList,result);
        }
        return issueList;
    }

    private List<Issue> toIssueList(Document document, List<String> checkTypeList, ContentCheckResult result) {
        List<Issue> issueList = new ArrayList<>();
        for (String checkType : checkTypeList) {
            Types.InfoErrorIssueType subIssueType = Types.InfoErrorIssueType.valueOfCheckType(checkType);
            String errorContent = result.getResultOfType(subIssueType);
            if (StringUtil.isEmpty(errorContent)) {
                continue;
            }
            Issue issue = new Issue();
            issue.setSiteId(document.getSiteId());
            issue.setCustomer2(Integer.toString(document.getChannelId()));
            issue.setTypeId(Types.IssueType.INFO_ERROR_ISSUE.value);
            issue.setSubTypeId(subIssueType.value);
            issue.setDetail(document.getDocPubUrl());
            issue.setIssueTime(new Date());
            issue.setCustomer1(errorContent);
            issueList.add(issue);
        }
        return issueList;
    }

    private void insert(List<Issue> issueList){
        //插入监测出的信息错误数据
        for (Issue issue : issueList) {
            issueMapper.insert(DBUtil.toRow(issue));
        }
        log.info("buildCheckContent insert error count: " + issueList.size());
    }

    private StringBuilder buildCheckContent(Document document){
        StringBuilder checkContent = new StringBuilder();
        if (document.getDocTitle() != null &&  !document.getDocTitle().trim().isEmpty()) {
            checkContent.append(document.getDocTitle());
            checkContent.append("。");
        }

        if (document.getDocContent() != null && !document.getDocContent().trim().isEmpty()) {
            checkContent.append(document.getDocContent());
        }
        return checkContent;
    }
}

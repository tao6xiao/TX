package com.trs.gov.kpi.processor;

import com.alibaba.fastjson.JSONObject;
import com.trs.gov.kpi.constant.IssueTableField;
import com.trs.gov.kpi.constant.Status;
import com.trs.gov.kpi.constant.Types;
import com.trs.gov.kpi.dao.IssueMapper;
import com.trs.gov.kpi.entity.InfoError;
import com.trs.gov.kpi.entity.Issue;
import com.trs.gov.kpi.entity.dao.QueryFilter;
import com.trs.gov.kpi.entity.msg.PageInfoMsg;
import com.trs.gov.kpi.entity.outerapi.ContentCheckResult;
import com.trs.gov.kpi.entity.outerapi.Document;
import com.trs.gov.kpi.entity.requestdata.PageDataRequestParam;
import com.trs.gov.kpi.service.helper.QueryFilterHelper;
import com.trs.gov.kpi.service.outer.ContentCheckApiService;
import com.trs.gov.kpi.utils.CollectionUtil;
import com.trs.gov.kpi.utils.DBUtil;
import com.trs.gov.kpi.utils.StringUtil;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.*;

/**
 * Created by li.hao on 2017/7/11.
 */
@Slf4j
@Component
@Scope("prototype")
public class CKMProcessWorker implements Runnable {

    @Resource
    private ContentCheckApiService contentCheckApiService;

    @Resource
    private IssueMapper issueMapper;

    @Setter
    private PageInfoMsg content;

    @Override
    public void run() {
        buildList();
    }

    private List<Issue> buildList() {
        List<Issue> issueList = new ArrayList<>();
        List<String> checkTypeList = Types.InfoErrorIssueType.getAllCheckTypes();

        String cleanText = Jsoup.clean(content.getContent(), Whitelist.none());
        cleanText = cleanText.replaceAll("&nbsp", " ");

        ContentCheckResult result = null;
        try {
            result = contentCheckApiService.check(cleanText, CollectionUtil.join(checkTypeList, ";"));
        } catch (Exception e) {
            log.error("failed to check content of url [" + content.getUrl() + "]", e);
            return issueList;
        }

        if (!result.isOk()) {
            log.error("return error: " + result.getMessage() + ", url [" + content.getUrl() + "]");
            return issueList;
        }

        if (result.getResult() != null) {
            issueList = toIssueList(checkTypeList, result);
        }
        return issueList;
    }

    private List<Issue> toIssueList(List<String> checkTypeList, ContentCheckResult result) {
        List<Issue> issueList = new ArrayList<>();
        for (String checkType : checkTypeList) {
            Types.InfoErrorIssueType subIssueType = Types.InfoErrorIssueType.valueOfCheckType(checkType);
            String errorContent = result.getResultOfType(subIssueType);
            if (StringUtil.isEmpty(errorContent)) {
                continue;
            } else {
                final Set<Map.Entry<String, Object>> entries = JSONObject.parseObject(errorContent).entrySet();
                for (Map.Entry<String, Object> entry : entries) {
                    String errorInfo = entry.getKey();
                    final String[] infos = errorInfo.split(":");
                    String word = infos[0];
                    String correct = infos[1];
                    

                }
            }
        }
        return issueList;
    }

    public void insert(List<Issue> issueList) {
        //插入监测出的信息错误数据
        for (Issue issue : issueList) {

            PageDataRequestParam param = new PageDataRequestParam();
            param.setSiteId(content.getSiteId());
            QueryFilter queryFilter = QueryFilterHelper.toFilter(param);
            queryFilter.addCond(IssueTableField.TYPE_ID, Types.IssueType.INFO_ERROR_ISSUE.value);
            queryFilter.addCond(IssueTableField.IS_RESOLVED, Status.Resolve.UN_RESOLVED.value);
            queryFilter.addCond(IssueTableField.IS_DEL, Status.Delete.UN_DELETE.value);
            queryFilter.addCond(IssueTableField.CUSTOMER1, issue.getCustomer1());
            queryFilter.addCond(IssueTableField.SUBTYPE_ID, issue.getSubTypeId());
            queryFilter.addCond(IssueTableField.DETAIL, issue.getDetail());

            List<InfoError> infoErrors = issueMapper.selectInfoError(queryFilter);
            if(infoErrors.size() == 0){
                issueMapper.insert(DBUtil.toRow(issue));
            }
        }
        log.info("buildCheckContent insert error count: " + issueList.size());
    }
}

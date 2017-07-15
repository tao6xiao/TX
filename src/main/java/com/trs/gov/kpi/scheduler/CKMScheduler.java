package com.trs.gov.kpi.scheduler;

import com.trs.gov.kpi.constant.IssueTableField;
import com.trs.gov.kpi.constant.Status;
import com.trs.gov.kpi.constant.Types;
import com.trs.gov.kpi.dao.IssueMapper;
import com.trs.gov.kpi.entity.InfoError;
import com.trs.gov.kpi.entity.Issue;
import com.trs.gov.kpi.entity.dao.QueryFilter;
import com.trs.gov.kpi.entity.exception.RemoteException;
import com.trs.gov.kpi.entity.outerapi.Channel;
import com.trs.gov.kpi.entity.outerapi.ContentCheckResult;
import com.trs.gov.kpi.entity.requestdata.PageDataRequestParam;
import com.trs.gov.kpi.service.helper.QueryFilterHelper;
import com.trs.gov.kpi.service.outer.ContentCheckApiService;
import com.trs.gov.kpi.service.outer.SiteApiService;
import com.trs.gov.kpi.utils.CollectionUtil;
import com.trs.gov.kpi.utils.DBUtil;
import com.trs.gov.kpi.utils.PageCKMSpiderUtil;
import com.trs.gov.kpi.utils.StringUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

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

    @Setter
    @Getter
    private Boolean isTimeNode;

    @Resource
    private ContentCheckApiService contentCheckApiService;

    @Resource
    private IssueMapper issueMapper;

    @Resource
    PageCKMSpiderUtil spider;

    @Resource
    SiteApiService siteApiService;

    @Override
    public void run() throws RemoteException {
        log.info("CKMScheduler " + siteId + " start...");
        List<String> checkTypeList = Types.InfoErrorIssueType.getAllCheckTypes();
        Set<PageCKMSpiderUtil.CKMPage> ckmPages = spider.fetchPages(5, baseUrl);
        for (PageCKMSpiderUtil.CKMPage page : ckmPages) {
            insert(buildList(page, checkTypeList));
        }
        log.info("CKMScheduler " + siteId + " end...");
    }

    private List<Issue> buildList(PageCKMSpiderUtil.CKMPage page, List<String> checkTypeList) throws RemoteException {
        List<Issue> issueList = new ArrayList<>();

        String checkContent = page.getContent();
        if (checkContent.length() <= 0) {
            return issueList;
        }

        ContentCheckResult result = null;
        try {
            result = contentCheckApiService.check(checkContent, CollectionUtil.join(checkTypeList, ";"));
        } catch (Exception e) {
            log.error("failed to check content " + checkContent, e);
            return issueList;
        }

        if (!result.isOk()) {
            log.error("return error: " + result.getMessage());
            return issueList;
        }

        if (result.getResult() != null) {
            issueList = toIssueList(page, checkTypeList, result);
        }
        return issueList;
    }

    private List<Issue> toIssueList(PageCKMSpiderUtil.CKMPage page, List<String> checkTypeList, ContentCheckResult result) throws RemoteException {
        List<Issue> issueList = new ArrayList<>();
        for (String checkType : checkTypeList) {
            Types.InfoErrorIssueType subIssueType = Types.InfoErrorIssueType.valueOfCheckType(checkType);
            String errorContent = result.getResultOfType(subIssueType);
            if (StringUtil.isEmpty(errorContent)) {
                continue;
            }
            Issue issue = new Issue();
            issue.setSiteId(siteId);
            Channel channel = siteApiService.findChannelByUrl("", page.getUrl());
            if(channel != null){
                issue.setCustomer2(String.valueOf(channel.getChannelId()));
            }
            issue.setTypeId(Types.IssueType.INFO_ERROR_ISSUE.value);
            issue.setSubTypeId(subIssueType.value);
            issue.setDetail(page.getUrl());
            Date nowTime = new Date();
            issue.setIssueTime(nowTime);
            issue.setCheckTime(nowTime);
            issue.setCustomer1(errorContent);
            issueList.add(issue);
        }
        return issueList;
    }

    private void insert(List<Issue> issueList) {
        //插入监测出的信息错误数据
        for (Issue issue : issueList) {

            PageDataRequestParam param = new PageDataRequestParam();
            param.setSiteId(siteId);
            QueryFilter queryFilter = QueryFilterHelper.toFilter(param);
            queryFilter.addCond(IssueTableField.TYPE_ID, Types.IssueType.INFO_ERROR_ISSUE.value);
            queryFilter.addCond(IssueTableField.IS_RESOLVED, Status.Resolve.UN_RESOLVED.value);
            queryFilter.addCond(IssueTableField.IS_DEL, Status.Delete.UN_DELETE.value);
            queryFilter.addCond(IssueTableField.DETAIL, issue.getDetail());
            queryFilter.addCond(IssueTableField.CUSTOMER1, issue.getCustomer1());
            queryFilter.addCond(IssueTableField.SUBTYPE_ID, issue.getSubTypeId());
            if(issue.getCustomer2() != null) {
                queryFilter.addCond(IssueTableField.CUSTOMER2, issue.getCustomer2());
            }

            List<InfoError> infoErrors = issueMapper.selectInfoError(queryFilter);
            if(infoErrors.isEmpty()){
                issueMapper.insert(DBUtil.toRow(issue));
            }
        }
        log.info("buildCheckContent insert error count: " + issueList.size());
    }
}

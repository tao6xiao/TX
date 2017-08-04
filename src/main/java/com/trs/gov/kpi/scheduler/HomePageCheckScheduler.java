package com.trs.gov.kpi.scheduler;

import com.trs.gov.kpi.constant.EnumCheckJobType;
import com.trs.gov.kpi.constant.IssueTableField;
import com.trs.gov.kpi.constant.Status;
import com.trs.gov.kpi.constant.Types;
import com.trs.gov.kpi.dao.IssueMapper;
import com.trs.gov.kpi.entity.Issue;
import com.trs.gov.kpi.entity.MonitorRecord;
import com.trs.gov.kpi.entity.dao.QueryFilter;
import com.trs.gov.kpi.entity.dao.Table;
import com.trs.gov.kpi.entity.outerapi.Site;
import com.trs.gov.kpi.service.MonitorRecordService;
import com.trs.gov.kpi.service.outer.SiteApiService;
import com.trs.gov.kpi.utils.DBUtil;
import com.trs.gov.kpi.utils.LogUtil;
import com.trs.gov.kpi.utils.SpiderUtils;
import com.trs.gov.kpi.utils.StringUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * Created by wangxuan on 2017/5/11.
 */
@Slf4j
@Component
@Scope("prototype")
public class HomePageCheckScheduler implements SchedulerTask {

    @Resource
    SpiderUtils spider;

    @Resource
    private SiteApiService siteApiService;

    @Setter
    @Getter
    private String baseUrl;

    @Setter
    @Getter
    private Boolean isTimeNode;

    @Setter
    @Getter
    private Integer siteId;

    @Resource
    private IssueMapper issueMapper;

    @Resource
    private MonitorRecordService monitorRecordService;

    @Override
    public void run() {

        log.info("HomePageCheckScheduler " + siteId + " start...");
        Date startTime = new Date();
        try {

            final Site checkSite = siteApiService.getSiteById(siteId, null);
            if (checkSite == null) {
                log.error("site[" + siteId + "] is not exsit!");
                LogUtil.addSystemLog("site[" + siteId + "] is not exsit!");
                return;
            }

            baseUrl = checkSite.getWebHttp();
            if (StringUtil.isEmpty(baseUrl)) {
                log.warn("site[" + siteId + "]'s web http is empty!");
                return;
            }

            List<String> unavailableUrls = spider.homePageCheck(siteId, baseUrl);
            if (unavailableUrls.contains(baseUrl)) {
                QueryFilter queryFilter = new QueryFilter(Table.ISSUE);
                queryFilter.addCond(IssueTableField.SITE_ID, siteId);
                queryFilter.addCond(IssueTableField.TYPE_ID, Types.IssueType.LINK_AVAILABLE_ISSUE.value);
                queryFilter.addCond(IssueTableField.SUBTYPE_ID, Types.LinkAvailableIssueType.INVALID_HOME_PAGE.value);
                queryFilter.addCond(IssueTableField.DETAIL, baseUrl);
                queryFilter.addCond(IssueTableField.IS_DEL, Status.Delete.UN_DELETE.value);
                queryFilter.addCond(IssueTableField.IS_RESOLVED, Status.Resolve.UN_RESOLVED.value);
                int count = issueMapper.count(queryFilter);
                if (count < 1) {
                    Issue issue = new Issue();
                    issue.setSiteId(siteId);
                    issue.setSubTypeId(Types.LinkAvailableIssueType.INVALID_HOME_PAGE.value);
                    issue.setTypeId(Types.IssueType.LINK_AVAILABLE_ISSUE.value);
                    issue.setDetail(baseUrl);
                    issue.setCustomer1(baseUrl);
                    issue.setIssueTime(new Date());
                    issueMapper.insert(DBUtil.toRow(issue));
                }
            }

            Date endTime = new Date();
            MonitorRecord monitorRecord = new MonitorRecord();
            monitorRecord.setSiteId(siteId);
            monitorRecord.setTaskStatus(EnumCheckJobType.CHECK_CONTENT.value);
            monitorRecord.setBeginTime(startTime);
            monitorRecord.setEndTime(endTime);
            monitorRecordService.insertMonitorRecord(monitorRecord);
        } catch (Exception e) {
            log.error("", e);
            LogUtil.addSystemLog("", e);
        } finally {
            log.info("HomePageCheckScheduler " + siteId + " end...");
        }

    }
}

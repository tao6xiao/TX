package com.trs.gov.kpi.scheduler;

import com.trs.gov.kpi.constant.*;
import com.trs.gov.kpi.dao.CommonMapper;
import com.trs.gov.kpi.dao.IssueMapper;
import com.trs.gov.kpi.entity.Issue;
import com.trs.gov.kpi.entity.MonitorRecord;
import com.trs.gov.kpi.entity.dao.DBUpdater;
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

    @Resource
    private CommonMapper commonMapper;

    //站点监测状态（0：自动监测；1：手动监测）
    @Setter
    private Integer monitorType;

    @Override
    public void run() {

        log.info(SchedulerRelated.getStartMessage(SchedulerType.HOMEPAGE_CHECK_SCHEDULER.toString(), siteId));
        LogUtil.addDebugLog(OperationType.TASK_SCHEDULE, DebugType.MONITOR_START, SchedulerRelated.getStartMessage(SchedulerType.HOMEPAGE_CHECK_SCHEDULER.toString(), siteId));

        try {
            final Site checkSite = siteApiService.getSiteById(siteId, null);
            if (checkSite == null) {
                log.error("site[" + siteId + "] is not exsit!");
                return;
            }

            baseUrl = checkSite.getWebHttp();
            if (StringUtil.isEmpty(baseUrl)) {
                log.warn("site[" + siteId + "]'s web http is empty!");
                return;
            }

            //监测开始(添加基本信息)
            final LogUtil.PerformanceLogRecorder performanceLogRecorder = new LogUtil.PerformanceLogRecorder(OperationType.TASK_SCHEDULE, SchedulerType.HOMEPAGE_CHECK_SCHEDULER + "[siteId=" + siteId + "]");
            Date startTime = new Date();
            insertStartMonitorRecord(startTime);

            //首页可用性(状态记录；0可用，1不可用)
            int isAvailable = 0;
            List<String> unavailableUrls = spider.homePageCheck(siteId, baseUrl);
            if (unavailableUrls.contains(baseUrl)) {
                isAvailable = 1;
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
                }else {
                    DBUpdater updater = new DBUpdater(Table.ISSUE.getTableName());
                    updater.addField(IssueTableField.CHECK_TIME, new Date());
                    commonMapper.update(updater, queryFilter);
                }
            }

            //监测完成(修改结果、结束时间、状态)
            insertEndMonitorRecord(startTime, isAvailable);


            // TODO REVIEW LINWEI DO_he.lang  性能日志的operationType有没有规范？要不然会冲突。 SchedulerRelated.HOMEPAGE_CHECK_SCHEDULER.intern() intern不需要
            performanceLogRecorder.recordAlways();
        } catch (Exception e) {
            String errorInfo = "首页可用性检测失败！[siteId=" + siteId + "]";
            log.error(errorInfo, e);
            LogUtil.addErrorLog(OperationType.TASK_SCHEDULE, ErrorType.REQUEST_FAILED, errorInfo, e);
        } finally {
            // TODO REVIEW LINWEI DONE_he.lang FIXED SchedulerRelated.getEndMessage(SchedulerRelated.HOMEPAGE_CHECK_SCHEDULER, siteId) 代码重复了
            String info = SchedulerRelated.getEndMessage(SchedulerType.HOMEPAGE_CHECK_SCHEDULER.toString(), siteId);
            log.info(info);
            LogUtil.addDebugLog(OperationType.TASK_SCHEDULE, DebugType.MONITOR_END, info);
        }

    }

    /**
     * 插入检测记录
     * @param startTime
     */
    private void insertStartMonitorRecord(Date startTime) {
        MonitorRecord monitorRecord = new MonitorRecord();
        monitorRecord.setSiteId(siteId);
        monitorRecord.setTypeId(monitorType);
        monitorRecord.setTaskId(EnumCheckJobType.CHECK_HOME_PAGE.value);
        monitorRecord.setBeginTime(startTime);
        monitorRecord.setTaskStatus(Status.MonitorStatusType.DOING.value);
        monitorRecordService.insertMonitorRecord(monitorRecord);

    }

    /**
     * 检测结束，记录入库
     * @param startTime
     */
    private void insertEndMonitorRecord(Date startTime, Integer isAvailable) {
        Date endTime = new Date();
        QueryFilter filter = new QueryFilter(Table.MONITOR_RECORD);
        filter.addCond(MonitorRecordTableField.SITE_ID, siteId);
        filter.addCond(MonitorRecordTableField.TASK_ID, EnumCheckJobType.CHECK_HOME_PAGE.value);
        filter.addCond(MonitorRecordTableField.BEGIN_TIME, startTime);

        DBUpdater updater = new DBUpdater(Table.MONITOR_RECORD.getTableName());
        updater.addField(MonitorRecordTableField.RESULT, isAvailable);
        updater.addField(MonitorRecordTableField.END_TIME, endTime);
        updater.addField(MonitorRecordTableField.TASK_STATUS, Status.MonitorStatusType.DONE.value);
        commonMapper.update(updater, filter);
    }
}

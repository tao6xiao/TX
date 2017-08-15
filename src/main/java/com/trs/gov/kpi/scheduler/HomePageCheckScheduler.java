package com.trs.gov.kpi.scheduler;

import com.trs.gov.kpi.constant.*;
import com.trs.gov.kpi.dao.CommonMapper;
import com.trs.gov.kpi.dao.IssueMapper;
import com.trs.gov.kpi.entity.Issue;
import com.trs.gov.kpi.entity.MonitorRecord;
import com.trs.gov.kpi.entity.dao.DBUpdater;
import com.trs.gov.kpi.entity.dao.QueryFilter;
import com.trs.gov.kpi.entity.dao.Table;
import com.trs.gov.kpi.entity.exception.RemoteException;
import com.trs.gov.kpi.entity.outerapi.Site;
import com.trs.gov.kpi.service.MonitorRecordService;
import com.trs.gov.kpi.service.outer.OuterApiService;
import com.trs.gov.kpi.service.outer.SiteApiService;
import com.trs.gov.kpi.utils.*;
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

    //首页可用性(状态记录；0可用，1不可用)
    private Integer homePageisAvailable = 0;

    private Date startTime;//开始时间记录

    @Override
    public void run() throws RemoteException {

        final Site checkSite = siteApiService.getSiteById(siteId, null);
        baseUrl = OuterApiServiceUtil.checkSiteAndGetUrl(siteId, checkSite);
        if(StringUtil.isEmpty(baseUrl))
        {
            return ;
        }


        //监测开始(添加基本信息)
            startTime = new Date();
        insertStartMonitorRecord(startTime);

            //首页可用性(状态记录；0可用，1不可用)
        List<String> unavailableUrls = spider.homePageCheck(siteId, baseUrl);
        if (unavailableUrls.contains(baseUrl)) {
                homePageisAvailable = 1;
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
            } else {
                DBUpdater updater = new DBUpdater(Table.ISSUE.getTableName());
                updater.addField(IssueTableField.CHECK_TIME, new Date());
                commonMapper.update(updater, queryFilter);
            }
        }
        //监测完成(修改结果、结束时间、状态)
            insertEndMonitorRecord(startTime, homePageisAvailable, Status.MonitorStatusType.CHECK_DONE.value);
        // TODO REVIEW LINWEI DO_he.lang  性能日志的operationType有没有规范？要不然会冲突。 SchedulerUtil.HOMEPAGE_CHECK_SCHEDULER.intern() intern不需要
            //监测失败
            insertEndMonitorRecord(startTime, homePageisAvailable, Status.MonitorStatusType.CHECK_ERROR.value);

    }

    @Override
    public String getName() {
        return SchedulerType.HOMEPAGE_CHECK_SCHEDULER.toString();
    }

    /**
     * 插入检测记录
     *
     * @param startTime
     */
    private void insertStartMonitorRecord(Date startTime) {
        MonitorRecord monitorRecord = new MonitorRecord();
        monitorRecord.setSiteId(siteId);
        monitorRecord.setTypeId(monitorType);
        monitorRecord.setTaskId(EnumCheckJobType.CHECK_HOME_PAGE.value);
        monitorRecord.setBeginTime(startTime);
        monitorRecord.setTaskStatus(Status.MonitorStatusType.DOING_CHECK.value);
        monitorRecordService.insertMonitorRecord(monitorRecord);

    }

    /**
     * 检测结束，记录入库
     *
     * @param startTime
     */
    private void insertEndMonitorRecord(Date startTime, Integer isAvailable, Integer taskStatus) {
        Date endTime = new Date();
        QueryFilter filter = new QueryFilter(Table.MONITOR_RECORD);
        filter.addCond(MonitorRecordTableField.SITE_ID, siteId);
        filter.addCond(MonitorRecordTableField.TASK_ID, EnumCheckJobType.CHECK_HOME_PAGE.value);
        filter.addCond(MonitorRecordTableField.BEGIN_TIME, startTime);

        DBUpdater updater = new DBUpdater(Table.MONITOR_RECORD.getTableName());
        updater.addField(MonitorRecordTableField.RESULT, isAvailable);
        updater.addField(MonitorRecordTableField.END_TIME, endTime);
        updater.addField(MonitorRecordTableField.TASK_STATUS, taskStatus);
        commonMapper.update(updater, filter);
    }
}

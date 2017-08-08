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
import com.trs.gov.kpi.entity.outerapi.sp.ServiceGuide;
import com.trs.gov.kpi.service.MonitorRecordService;
import com.trs.gov.kpi.service.outer.SGService;
import com.trs.gov.kpi.utils.DBUtil;
import com.trs.gov.kpi.utils.LogUtil;
import com.trs.gov.kpi.utils.ServiceLinkSpiderUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;

/**
 * 服务链接是否可用
 * Created by he.lang on 2017/7/12.
 */
@Slf4j
@Component
@Scope("prototype")
public class ServiceLinkScheduler implements SchedulerTask {

    @Setter
    @Getter
    private Integer siteId;

    @Setter
    @Getter
    private Boolean isTimeNode;

    @Setter
    @Getter
    private String baseUrl;

    @Resource
    IssueMapper issueMapper;

    @Resource
    ServiceLinkSpiderUtil spider;

    @Resource
    SGService sgService;

    @Resource
    private MonitorRecordService monitorRecordService;

    @Resource
    private CommonMapper commonMapper;

    //失效的服务链接计数
    int count = 0;

    @Override
    public void run() {

        log.info(SchedulerType.startScheduler(SchedulerType.SERVICE_LINK_SCHEDULER, siteId));
        LogUtil.addDebugLog(OperationType.TASK_SCHEDULE, DebugType.MONITOR_START, SchedulerType.startScheduler(SchedulerType.SERVICE_LINK_SCHEDULER, siteId));

        //监测开始(添加基本信息)
        Date startTime = new Date();
        MonitorRecord monitorRecord = new MonitorRecord();
        monitorRecord.setSiteId(siteId);
        monitorRecord.setTaskId(EnumCheckJobType.SERVICE_LINK.value);
        monitorRecord.setBeginTime(startTime);
        monitorRecord.setTaskStatus(Status.MonitorStatusType.DOING.value);
        monitorRecordService.insertMonitorRecord(monitorRecord);

        try {
            for (ServiceGuide guide : sgService.getAllService(siteId).getData()) {
                if (spider.linkCheck(guide.getItemLink()) == Types.ServiceLinkIssueType.INVALID_LINK) {
                    Issue issue = new Issue();
                    issue.setSiteId(siteId);
                    issue.setSubTypeId(Types.ServiceLinkIssueType.INVALID_LINK.value);
                    issue.setTypeId(Types.IssueType.SERVICE_LINK_AVAILABLE.value);
                    issue.setDetail(guide.getItemLink());
                    issue.setCustomer1(guide.getItemLink());
                    Date nowTime = new Date();
                    issue.setIssueTime(nowTime);
                    issue.setCheckTime(nowTime);
                    issueMapper.insert(DBUtil.toRow(issue));
                    count ++;
                }
            }

            //监测完成(修改结果、结束时间、状态)
            Date endTime = new Date();
            QueryFilter filter = new QueryFilter(Table.MONITOR_RECORD);
            filter.addCond(MonitorRecordTableField.SITEID, siteId);
            filter.addCond(MonitorRecordTableField.TASKID, EnumCheckJobType.SERVICE_LINK.value);
            filter.addCond(MonitorRecordTableField.BEGINTIME,startTime);

            DBUpdater updater = new DBUpdater(Table.MONITOR_RECORD.getTableName());
            updater.addField(MonitorRecordTableField.RESULT,count);
            updater.addField(MonitorRecordTableField.ENDTIME, endTime);
            updater.addField(MonitorRecordTableField.TASKSTATUS, Status.MonitorStatusType.DONE.value);
            commonMapper.update(updater, filter);

            LogUtil.addElapseLog(OperationType.TASK_SCHEDULE, SchedulerType.SERVICE_LINK_SCHEDULER.intern(), endTime.getTime()-startTime.getTime());
        } catch (RemoteException e) {
            log.error("", e);
            LogUtil.addErrorLog(OperationType.REMOTE, ErrorType.REMOTE_FAILED, "", e);
        } finally {
            log.info(SchedulerType.endScheduler(SchedulerType.SERVICE_LINK_SCHEDULER, siteId));
            LogUtil.addDebugLog(OperationType.TASK_SCHEDULE, DebugType.MONITOR_END, SchedulerType.endScheduler(SchedulerType.SERVICE_LINK_SCHEDULER, siteId));
        }
    }

}


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

    //站点监测状态（0：自动监测；1：手动监测）
    @Setter
    private Integer monitorType;

    @Override
    public void run() {
        try {
            log.info(SchedulerRelated.getStartMessage(SchedulerRelated.SchedulerType.SERVICE_LINK_SCHEDULER.toString(), siteId));
            LogUtil.addDebugLog(OperationType.TASK_SCHEDULE, DebugType.MONITOR_START, SchedulerRelated.getStartMessage(SchedulerRelated.SchedulerType.SERVICE_LINK_SCHEDULER.toString(), siteId));

            //监测开始(添加基本信息)
            final LogUtil.PerformanceLogRecorder performanceLogRecorder = new LogUtil.PerformanceLogRecorder(OperationType.TASK_SCHEDULE, SchedulerRelated.SchedulerType.SERVICE_LINK_SCHEDULER + "[siteId=" + siteId + "]");
            Date startTime = new Date();
            insertStartMonitorRecord(startTime);

            //失效的服务链接计数
            int count = 0;
            for (ServiceGuide guide : sgService.getAllService(siteId).getData()) {
                if (spider.linkCheck(guide.getItemLink()) == Types.ServiceLinkIssueType.INVALID_LINK) {
                    QueryFilter queryFilter = new QueryFilter(Table.ISSUE);
                    queryFilter.addCond(IssueTableField.SITE_ID,siteId);
                    queryFilter.addCond(IssueTableField.TYPE_ID,Types.IssueType.SERVICE_LINK_AVAILABLE);
                    queryFilter.addCond(IssueTableField.DETAIL,guide.getItemLink());
                    int issueCount = issueMapper.count(queryFilter);
                    if(issueCount<1){
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
                    }else {
                        DBUpdater updater = new DBUpdater(Table.ISSUE.getTableName());
                        updater.addField(IssueTableField.CHECK_TIME, new Date());
                        commonMapper.update(updater, queryFilter);
                    }
                    count++;
                }
            }

            //监测完成(修改结果、结束时间、状态)
            insertEndMonitorRecord(startTime, count);

            performanceLogRecorder.recordAlways();
        } catch (RemoteException e) {
            String errorInfo = "服务链接可用性检测失败！[siteId=" + siteId + "]";
            log.error(errorInfo, e);
            LogUtil.addErrorLog(OperationType.REMOTE, ErrorType.REMOTE_FAILED, errorInfo, e);
        } finally {
            String info = SchedulerRelated.getEndMessage(SchedulerRelated.SchedulerType.SERVICE_LINK_SCHEDULER.toString(), siteId);
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
        monitorRecord.setTaskId(EnumCheckJobType.SERVICE_LINK.value);
        monitorRecord.setBeginTime(startTime);
        monitorRecord.setTaskStatus(Status.MonitorStatusType.DOING.value);
        monitorRecordService.insertMonitorRecord(monitorRecord);

    }

    /**
     * 检测结束，记录入库
     * @param startTime
     */
    private void insertEndMonitorRecord(Date startTime, Integer count) {
        Date endTime = new Date();
        QueryFilter filter = new QueryFilter(Table.MONITOR_RECORD);
        filter.addCond(MonitorRecordTableField.SITE_ID, siteId);
        filter.addCond(MonitorRecordTableField.TASK_ID, EnumCheckJobType.SERVICE_LINK.value);
        filter.addCond(MonitorRecordTableField.BEGIN_TIME, startTime);

        DBUpdater updater = new DBUpdater(Table.MONITOR_RECORD.getTableName());
        updater.addField(MonitorRecordTableField.RESULT, count);
        updater.addField(MonitorRecordTableField.END_TIME, endTime);
        updater.addField(MonitorRecordTableField.TASK_STATUS, Status.MonitorStatusType.DONE.value);
        commonMapper.update(updater, filter);
    }

}


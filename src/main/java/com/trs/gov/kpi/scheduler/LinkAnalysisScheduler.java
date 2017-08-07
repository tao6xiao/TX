package com.trs.gov.kpi.scheduler;

import com.trs.gov.kpi.constant.*;
import com.trs.gov.kpi.dao.CommonMapper;
import com.trs.gov.kpi.dao.WebPageMapper;
import com.trs.gov.kpi.entity.MonitorRecord;
import com.trs.gov.kpi.entity.dao.DBUpdater;
import com.trs.gov.kpi.entity.dao.QueryFilter;
import com.trs.gov.kpi.entity.dao.Table;
import com.trs.gov.kpi.entity.outerapi.Site;
import com.trs.gov.kpi.service.LinkAvailabilityService;
import com.trs.gov.kpi.service.MonitorRecordService;
import com.trs.gov.kpi.service.WebPageService;
import com.trs.gov.kpi.service.outer.SiteApiService;
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

/**
 * Created by wangxuan on 2017/5/10.
 * 定时任务，抓取网站链接是否可用
 */
@Slf4j
@Component
@Scope("prototype")
public class LinkAnalysisScheduler implements SchedulerTask{

    @Resource
    LinkAvailabilityService linkAvailabilityService;

    @Resource
    private SiteApiService siteApiService;

    @Resource
    SpiderUtils spider;

    @Resource
    WebPageService webPageService;

    @Resource
    WebPageMapper webPageMapper;

    @Resource
    private MonitorRecordService monitorRecordService;

    @Resource
    private CommonMapper commonMapper;

    @Setter
    @Getter
    private Integer siteId;

    @Setter
    @Getter
    private String baseUrl;

    @Setter
    @Getter
    private Boolean isTimeNode;

    @Setter
    Integer count = 0;

    @Override
    public void run() {

        log.info(SchedulerType.startScheduler(SchedulerType.LINK_ANALYSIS_SCHEDULER, siteId));
        LogUtil.addDebugLog(OperationType.TASK_SCHEDULE, DebugType.MONITOR_START, SchedulerType.startScheduler(SchedulerType.LINK_ANALYSIS_SCHEDULER, siteId));

        //监测开始(添加基本信息)
        Date startTime = new Date();
        MonitorRecord monitorRecord = new MonitorRecord();
        monitorRecord.setSiteId(siteId);
        monitorRecord.setTaskId(EnumCheckJobType.CHECK_LINK.value);
        monitorRecord.setBeginTime(startTime);
        monitorRecord.setTaskStatus(Status.MonitorStatusType.DOING.value);
        monitorRecordService.insertMonitorRecord(monitorRecord);

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

            spider.linkCheck(3, siteId, baseUrl);//测试url：http://tunchang.hainan.gov.cn/tcgov/

            //监测完成(修改结果、结束时间、状态)
            Date endTime = new Date();
            QueryFilter filter = new QueryFilter(Table.MONITOR_RECORD);
            filter.addCond(MonitorRecordTableField.SITEID, siteId);
            filter.addCond(MonitorRecordTableField.TASKID, EnumCheckJobType.CHECK_LINK.value);
            filter.addCond(MonitorRecordTableField.BEGINTIME,startTime);

            DBUpdater updater = new DBUpdater(Table.MONITOR_RECORD.getTableName());
            updater.addField(MonitorRecordTableField.RESULT,spider.getCount());
            updater.addField(MonitorRecordTableField.ENDTIME, endTime);
            updater.addField(MonitorRecordTableField.TASKSTATUS, Status.MonitorStatusType.DONE.value);
            commonMapper.update(updater, filter);

            LogUtil.addElapseLog(OperationType.TASK_SCHEDULE, SchedulerType.LINK_ANALYSIS_SCHEDULER.intern(), endTime.getTime()-startTime.getTime());
        } catch (Exception e) {
            log.error("check link:{}, siteId:{} availability error!", baseUrl, siteId, e);
            LogUtil.addErrorLog(OperationType.TASK_SCHEDULE, ErrorType.REQUEST_FAILED, "check link:{" + baseUrl + "}, siteId:{" + siteId + "} availability error!", e);
        } finally {
            log.info(SchedulerType.endScheduler(SchedulerType.LINK_ANALYSIS_SCHEDULER, siteId));
            LogUtil.addDebugLog(OperationType.TASK_SCHEDULE, DebugType.MONITOR_END, SchedulerType.endScheduler(SchedulerType.LINK_ANALYSIS_SCHEDULER, siteId));

        }
    }


}

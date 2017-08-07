package com.trs.gov.kpi.scheduler;

import com.trs.gov.kpi.constant.*;
import com.trs.gov.kpi.dao.WebPageMapper;
import com.trs.gov.kpi.entity.MonitorRecord;
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
    int count = 0;

    @Override
    public void run() {

        log.info(SchedulerType.schedulerStart(SchedulerType.LINK_ANALYSIS_SCHEDULER, siteId));
        LogUtil.addDebugLog(OperationType.TASK_SCHEDULE, DebugType.MONITOR_START, SchedulerType.schedulerStart(SchedulerType.LINK_ANALYSIS_SCHEDULER, siteId));
        Date startTime = new Date();
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

            Date endTime = new Date();
            MonitorRecord monitorRecord = new MonitorRecord();
            monitorRecord.setSiteId(siteId);
            monitorRecord.setTaskId(EnumCheckJobType.CHECK_CONTENT.value);
            monitorRecord.setBeginTime(startTime);
            monitorRecord.setEndTime(endTime);
            monitorRecord.setResult(1);
            monitorRecordService.insertMonitorRecord(monitorRecord);

            LogUtil.addElapseLog(OperationType.TASK_SCHEDULE, SchedulerType.LINK_ANALYSIS_SCHEDULER.intern(), endTime.getTime()-startTime.getTime());
        } catch (Exception e) {
            log.error("check link:{}, siteId:{} availability error!", baseUrl, siteId, e);
            LogUtil.addErrorLog(OperationType.TASK_SCHEDULE, ErrorType.REQUEST_FAILED, "check link:{" + baseUrl + "}, siteId:{" + siteId + "} availability error!", e);
        } finally {
            log.info(SchedulerType.schedulerEnd(SchedulerType.LINK_ANALYSIS_SCHEDULER, siteId));
            LogUtil.addDebugLog(OperationType.TASK_SCHEDULE, DebugType.MONITOR_END, SchedulerType.schedulerEnd(SchedulerType.LINK_ANALYSIS_SCHEDULER, siteId));

        }
    }


}

package com.trs.gov.kpi.scheduler;

import com.trs.gov.kpi.constant.EnumCheckJobType;
import com.trs.gov.kpi.constant.Types;
import com.trs.gov.kpi.dao.IssueMapper;
import com.trs.gov.kpi.entity.Issue;
import com.trs.gov.kpi.entity.MonitorRecord;
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

    @Override
    public void run() {

        log.info("ServiceLinkScheduler " + siteId + " start...");
        Date startTime = new Date();
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
                }
            }

            Date endTime = new Date();
            MonitorRecord monitorRecord = new MonitorRecord();
            monitorRecord.setSiteId(siteId);
            monitorRecord.setTaskStatus(EnumCheckJobType.CHECK_CONTENT.value);
            monitorRecord.setBeginTime(startTime);
            monitorRecord.setEndTime(endTime);
            monitorRecordService.insertMonitorRecord(monitorRecord);
        } catch (RemoteException e) {
            log.error("", e);
            LogUtil.addSystemLog("", e);
        } finally {
            log.info("ServiceLinkScheduler " + siteId + " end...");
        }
    }

}


package com.trs.gov.kpi.scheduler;

import com.trs.gov.kpi.constant.EnumCheckJobType;
import com.trs.gov.kpi.constant.MonitorRecordTableField;
import com.trs.gov.kpi.constant.SchedulerType;
import com.trs.gov.kpi.constant.Status;
import com.trs.gov.kpi.dao.CommonMapper;
import com.trs.gov.kpi.dao.WebPageMapper;
import com.trs.gov.kpi.entity.MonitorRecord;
import com.trs.gov.kpi.entity.dao.DBUpdater;
import com.trs.gov.kpi.entity.dao.QueryFilter;
import com.trs.gov.kpi.entity.dao.Table;
import com.trs.gov.kpi.entity.exception.RemoteException;
import com.trs.gov.kpi.entity.outerapi.Site;
import com.trs.gov.kpi.service.LinkAvailabilityService;
import com.trs.gov.kpi.service.MonitorRecordService;
import com.trs.gov.kpi.service.WebPageService;
import com.trs.gov.kpi.service.outer.SiteApiService;
import com.trs.gov.kpi.utils.OuterApiServiceUtil;
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
public class LinkAnalysisScheduler implements SchedulerTask {

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

    //站点监测状态（0：自动监测；1：手动监测）
    @Setter
    private Integer monitorType;

    @Override
    public void run() throws RemoteException {
        final Site checkSite = siteApiService.getSiteById(siteId, null);
        baseUrl = OuterApiServiceUtil.checkSiteAndGetUrl(siteId, checkSite);
        if(StringUtil.isEmpty(baseUrl))
        {
            return ;
        }

        //监测开始(添加基本信息)

        // 插入检测记录
        Date startTime = new Date();
        insertStartMonitorRecord(startTime);

        spider.linkCheck(3, siteId, baseUrl);//测试url：http://tunchang.hainan.gov.cn/tcgov/

        //监测完成(修改结果、结束时间、状态)
        insertEndMonitorRecord(startTime);
    }

    @Override
    public String getName() {
        return SchedulerType.LINK_ANALYSIS_SCHEDULER.toString();
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
        monitorRecord.setTaskId(EnumCheckJobType.CHECK_LINK.value);
        monitorRecord.setBeginTime(startTime);
        monitorRecord.setTaskStatus(Status.MonitorStatusType.DOING.value);
        monitorRecordService.insertMonitorRecord(monitorRecord);
    }

    /**
     * 检测结束，记录入库
     *
     * @param startTime
     */
    private void insertEndMonitorRecord(Date startTime) {
        Date endTime = new Date();
        QueryFilter filter = new QueryFilter(Table.MONITOR_RECORD);
        filter.addCond(MonitorRecordTableField.SITE_ID, siteId);
        filter.addCond(MonitorRecordTableField.TASK_ID, EnumCheckJobType.CHECK_LINK.value);
        filter.addCond(MonitorRecordTableField.BEGIN_TIME, startTime);

        DBUpdater updater = new DBUpdater(Table.MONITOR_RECORD.getTableName());
        updater.addField(MonitorRecordTableField.RESULT, spider.getCount());
        updater.addField(MonitorRecordTableField.END_TIME, endTime);
        updater.addField(MonitorRecordTableField.TASK_STATUS, Status.MonitorStatusType.DONE.value);
        commonMapper.update(updater, filter);
    }


}

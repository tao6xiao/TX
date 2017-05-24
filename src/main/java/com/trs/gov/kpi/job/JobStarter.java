package com.trs.gov.kpi.job;

import com.trs.gov.kpi.constant.FreqUnit;
import com.trs.gov.kpi.constant.FrequencyType;
import com.trs.gov.kpi.dao.MonitorFrequencyMapper;
import com.trs.gov.kpi.entity.MonitorFrequency;
import com.trs.gov.kpi.entity.MonitorSite;
import com.trs.gov.kpi.scheduler.HomePageCheckScheduler;
import com.trs.gov.kpi.scheduler.InfoUpdateCheckScheduler;
import com.trs.gov.kpi.scheduler.LinkAnalysisScheduler;
import com.trs.gov.kpi.scheduler.SchedulerTask;
import com.trs.gov.kpi.service.MonitorSiteService;
import com.trs.gov.kpi.utils.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

/**
 * Created by linwei on 2017/5/24.
 */
@Slf4j
@Service
public class JobStarter implements ApplicationListener<ContextRefreshedEvent> {

    private static final int CHECK_HOMEPAGE_TYPE = 1;
    private static final int CHECK_LINK_TYPE = 2;
    private static final int CHECK_INFO_UPDATE_TYPE = 3;

    @Resource
    ApplicationContext applicationContext;

    @Resource
    MonitorSiteService monitorSiteService;

    @Resource
    MonitorFrequencyMapper monitorFrequencyMapper;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {

        if (contextRefreshedEvent.getApplicationContext().getParent() == null) {

            // 启动完成后，就开始执行
            try {
                Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
                scheduler.start();

                // 栏目更新检查
                initInfoUpdateCheckJob(scheduler);

                // 首页有效性检查
                initHomepageCheckJob(scheduler);

                // 全站链接有效性检查
                initLinkCheckJob(scheduler);

            } catch (SchedulerException e) {
                log.error("", e);
            }
        }
    }

    /**
     * 启动栏目更新检查任务
     *
     * @param scheduler
     */
    private void initInfoUpdateCheckJob(Scheduler scheduler) {

        // 查询数据库里面的所有站点
        final List<MonitorSite> allMonitorSites = monitorSiteService.getAllMonitorSites();
        if (allMonitorSites == null || allMonitorSites.isEmpty()) {
            return;
        }

        // 每一个站点一个job
        for (MonitorSite site : allMonitorSites) {
            scheduleJob(scheduler, CHECK_INFO_UPDATE_TYPE, site, 24 * 60 * 60);
        }
    }

    /**
     * 首页有效性检测
     *
     * @param scheduler
     */
    private void initHomepageCheckJob(Scheduler scheduler) {
        // 查询数据库里面的所有站点
        final List<MonitorSite> allMonitorSites = monitorSiteService.getAllMonitorSites();
        if (allMonitorSites == null || allMonitorSites.isEmpty()) {
            return;
        }

        for (MonitorSite site : allMonitorSites) {

            final List<MonitorFrequency> monitorFrequencies = monitorFrequencyMapper
                    .queryBySiteId(site.getSiteId());
            if (StringUtil.isEmpty(site.getIndexUrl())
                    || monitorFrequencies == null || monitorFrequencies.isEmpty()) {
                continue;
            }

            for (MonitorFrequency freq : monitorFrequencies) {
                if (freq != null && freq.getTypeId() == FrequencyType.HOMEPAGE_AVAILABILITY.getTypeId()) {

                    int interval = 0;
                    if (FrequencyType.HOMEPAGE_AVAILABILITY.getFreqUnit() == FreqUnit
                            .DAYS_PER_TIME) {
                        // 计算间隔的时间，秒
                        interval = 24 * 60 * 60 * freq.getValue();
                    } else if (FrequencyType.HOMEPAGE_AVAILABILITY.getFreqUnit() == FreqUnit.TIMES_PER_DAY) {
                        // 计算间隔的时间，秒
                        interval = 24 * 60 * 60 / freq.getValue();
                    }

                    scheduleJob(scheduler, CHECK_HOMEPAGE_TYPE, site, interval);
                }
            }
        }
    }

    /**
     * 链接有效性检测
     *
     * @param scheduler
     */
    private void initLinkCheckJob(Scheduler scheduler) {
        // 查询数据库里面的所有站点
        final List<MonitorSite> allMonitorSites = monitorSiteService.getAllMonitorSites();
        if (allMonitorSites == null || allMonitorSites.isEmpty()) {
            return;
        }

        for (MonitorSite site : allMonitorSites) {

            final List<MonitorFrequency> monitorFrequencies = monitorFrequencyMapper
                    .queryBySiteId(site.getSiteId());
            if (StringUtil.isEmpty(site.getIndexUrl())
                    || monitorFrequencies == null || monitorFrequencies.isEmpty()) {
                continue;
            }

            for (MonitorFrequency freq : monitorFrequencies) {
                if (freq != null && freq.getTypeId() == FrequencyType.TOTAL_BROKEN_LINKS.getTypeId()) {

                    int interval = 0;
                    if (FrequencyType.TOTAL_BROKEN_LINKS.getFreqUnit() == FreqUnit
                            .DAYS_PER_TIME) {
                        // 计算间隔的时间，秒
                        interval = 24 * 60 * 60 * freq.getValue();
                    } else if (FrequencyType.TOTAL_BROKEN_LINKS.getFreqUnit() == FreqUnit.TIMES_PER_DAY) {
                        // 计算间隔的时间，秒
                        interval = 24 * 60 * 60 / freq.getValue();
                    }
                    scheduleJob(scheduler, CHECK_LINK_TYPE, site, interval);
                }
            }
        }
    }

    /**
     * 注册调度任务
     *
     * @param scheduler
     * @param checkType
     * @param site
     * @param interval
     */
    private void scheduleJob(Scheduler scheduler, int checkType, MonitorSite site, int interval) {

        String name = "";
        SchedulerTask task = null;
        switch (checkType) {
            case CHECK_HOMEPAGE_TYPE:
                name = "homePage";
                task = applicationContext.getBean
                        (HomePageCheckScheduler.class);
                break;
            case CHECK_INFO_UPDATE_TYPE:
                name = "infoUpdate";
                task = applicationContext.getBean
                        (InfoUpdateCheckScheduler.class);
                break;
            case CHECK_LINK_TYPE:
                name = "link";
                task = applicationContext.getBean
                        (LinkAnalysisScheduler.class);
                break;
            default:
                return;
        }

        JobDetail job = newJob(CheckJob.class)
                .withIdentity(name + "CheckJob" + String.valueOf(site.getSiteId()), "group-" + name + "-check")
                .build();

        // 真正的执行任务
        task.setSiteId(site.getSiteId());
        task.setBaseUrl(site.getIndexUrl());
        job.getJobDataMap().put("task", task);

        // 每天执行一次
        Trigger trigger = newTrigger()
                .withIdentity(name + "CheckJobTrigger" + String.valueOf(site.getSiteId()), "group-" + name +  "-check")
                .startNow()
                .withSchedule(simpleSchedule()
                        .withIntervalInSeconds(interval)
                        .repeatForever())
                .build();
        try {
            scheduler.scheduleJob(job, trigger);
        } catch (SchedulerException e) {
            log.error("failed to schedule " + name + " check of site " + String.valueOf(site.getSiteId()), e);
        }
    }
}

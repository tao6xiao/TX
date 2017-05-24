package com.trs.gov.kpi.job;

import com.trs.gov.kpi.constant.FreqUnit;
import com.trs.gov.kpi.constant.FrequencyType;
import com.trs.gov.kpi.dao.MonitorFrequencyMapper;
import com.trs.gov.kpi.entity.MonitorFrequency;
import com.trs.gov.kpi.entity.MonitorSite;
import com.trs.gov.kpi.scheduler.HomePageCheckScheduler;
import com.trs.gov.kpi.scheduler.InfoUpdateCheckScheduler;
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
            JobDetail job = newJob(CheckJob.class)
                    .withIdentity("infoUpdateCheckJob" + String.valueOf(site.getSiteId()), "group-check")
                    .build();

            // 真正的执行任务
            InfoUpdateCheckScheduler checkScheduler = applicationContext.getBean
                    (InfoUpdateCheckScheduler.class);
            checkScheduler.setSiteId(site.getSiteId());
            checkScheduler.setBaseUrl(site.getIndexUrl());
            job.getJobDataMap().put("task", checkScheduler);

            // 每天执行一次
            Trigger trigger = newTrigger()
                    .withIdentity("infoUpdateCheckJobTrigger" + String.valueOf(site.getSiteId()), "group-check")
                    .startNow()
                    .withSchedule(simpleSchedule()
                            .withIntervalInSeconds(24 * 60 * 60)
                            .repeatForever())
                    .build();

            try {
                scheduler.scheduleJob(job, trigger);
            } catch (SchedulerException e) {
                log.error("failed to schedule info update check of site " + String.valueOf(site.getSiteId()), e);
            }
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

                    JobDetail job = newJob(CheckJob.class)
                            .withIdentity("homePageCheckJob" + String.valueOf(site.getSiteId()), "group-check")
                            .build();

                    // 真正的执行任务
                    HomePageCheckScheduler checkScheduler = applicationContext.getBean
                            (HomePageCheckScheduler.class);
                    checkScheduler.setSiteId(site.getSiteId());
                    checkScheduler.setBaseUrl(site.getIndexUrl());
                    job.getJobDataMap().put("task", checkScheduler);

                    // 每天执行一次
                    Trigger trigger = newTrigger()
                            .withIdentity("homePageCheckJobTrigger" + String.valueOf(site.getSiteId()), "group-check")
                            .startNow()
                            .withSchedule(simpleSchedule()
                                    .withIntervalInSeconds(interval)
                                    .repeatForever())
                            .build();
                    try {
                        scheduler.scheduleJob(job, trigger);
                    } catch (SchedulerException e) {
                        log.error("failed to schedule home page check of site " + String.valueOf(site.getSiteId()), e);
                    }
                }
            }
        }
    }
}

package com.trs.gov.kpi.job;

import com.trs.gov.kpi.entity.MonitorSite;
import com.trs.gov.kpi.scheduler.InfoUpdateCheckScheduler;
import com.trs.gov.kpi.service.MonitorSiteService;
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

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {

        if (contextRefreshedEvent.getApplicationContext().getParent() == null) {

            // 启动完成后，就开始执行
            try {
                Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
                scheduler.start();

                // 栏目更新检查
                initInfoUpdateCheckJob(scheduler);

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
}

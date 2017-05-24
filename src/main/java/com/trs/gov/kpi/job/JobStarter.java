package com.trs.gov.kpi.job;

import com.trs.gov.kpi.scheduler.InfoUpdateCheckScheduler;
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

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {

        if (contextRefreshedEvent.getApplicationContext().getParent() == null) {

            // 启动完成后，就开始执行
            try {
                Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
                scheduler.start();

                JobDetail job = newJob(CheckJob.class)
                        .withIdentity("infoUpdateCheckJob", "group-check")
                        .build();

                // 真正的执行任务
                InfoUpdateCheckScheduler checkScheduler = applicationContext.getBean
                        (InfoUpdateCheckScheduler.class);
                checkScheduler.setSiteId(11);
                checkScheduler.setBaseUrl("www.baidu.com");
                job.getJobDataMap().put("task", checkScheduler);

                Trigger trigger = newTrigger()
                        .withIdentity("infoUpdateCheckJobTrigger", "group-check")
                        .startNow()
                        .withSchedule(simpleSchedule()
                                .withIntervalInSeconds(24 * 60 * 60)
                                .repeatForever())
                        .build();

                scheduler.scheduleJob(job, trigger);
            } catch (SchedulerException e) {
                log.error("", e);
            }
        }
    }
}

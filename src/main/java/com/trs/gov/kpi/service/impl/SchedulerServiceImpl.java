package com.trs.gov.kpi.service.impl;

import com.trs.gov.kpi.constant.FreqUnit;
import com.trs.gov.kpi.constant.FrequencyType;
import com.trs.gov.kpi.dao.MonitorFrequencyMapper;
import com.trs.gov.kpi.entity.MonitorFrequency;
import com.trs.gov.kpi.entity.MonitorSite;
import com.trs.gov.kpi.job.CheckJob;
import com.trs.gov.kpi.scheduler.*;
import com.trs.gov.kpi.service.MonitorSiteService;
import com.trs.gov.kpi.service.SchedulerService;
import com.trs.gov.kpi.utils.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

/**
 * Created by wangxuan on 2017/5/11.
 */
@Slf4j
@Service
public class SchedulerServiceImpl implements SchedulerService, ApplicationListener<ContextRefreshedEvent> {

    private static final int CHECK_HOMEPAGE_TYPE = 1;
    private static final int CHECK_LINK_TYPE = 2;
    private static final int CHECK_INFO_UPDATE_TYPE = 3;
    private static final int CHECK_CONTENT_TYPE = 4;

    @Resource
    SchedulerTask[] schedulerTasks;

    @Resource
    SchedulerManager schedulerManager;

    @Resource
    MonitorSiteService monitorSiteService;

    @Resource
    MonitorFrequencyMapper monitorFrequencyMapper;

    @Resource
    ApplicationContext applicationContext;

    /**
     * 初始化scheduler
     * TODO:按照数据库配置进行装配
     */
    @PostConstruct
    public void init() {

        try {

            log.info("init scheduler model...");

//            List<MonitorSite> monitorSites = monitorSiteService.getAllMonitorSites();
//            for(MonitorSite monitorSite: monitorSites) {
//
//                if(monitorSite.getSiteId() != null) {
//
//                    List<MonitorFrequency> monitorFrequencies = monitorFrequencyMapper.queryBySiteId(monitorSite.getSiteId());
//                    for(MonitorFrequency monitorFrequency: monitorFrequencies) {
//
//                        try {
//
//                            registerScheduler(
//                                    monitorSite.getIndexUrl(),
//                                    monitorSite.getSiteId(),
//                                    FrequencyType.getFrequencyTypeByTypeId(monitorFrequency.getTypeId()),
//                                    FrequencyType.getFrequencyTypeByTypeId(monitorFrequency.getTypeId()).getFreqUnit(),
//                                    monitorFrequency.getValue().intValue());
//                        } catch (Exception e) {
//
//                            log.error("register scheduler {} error!", JSON.toJSONString(monitorFrequency));
//                        }
//                    }
//                }
//            }

            log.info("init scheduler model completed!");
        } catch (Exception e) {

            log.error("init scheduler model error!", e);
        }
    }

    public void registerScheduler(String baseUrl, Integer siteId, FrequencyType frequencyType, FreqUnit freqUnit, Integer freq) {

        SchedulerTask schedulerTask = applicationContext.getBean(frequencyType.getScheduler());
        schedulerTask.setBaseUrl(baseUrl);
        schedulerTask.setSiteId(siteId);
        addFreqUnitAndFreq(freqUnit, freq, schedulerTask);
        schedulerManager.registerScheduler(schedulerTask);
    }

    @Override
    public void addHomePageCheckJob(int siteId, String indexUrl) {

        try {
            Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
            final MonitorSite site = monitorSiteService.getMonitorSiteBySiteId
                    (siteId);

            scheduleHomePageCheckJob(scheduler, site);

        } catch (SchedulerException e) {
            log.error("", e);
        }
    }

    @Override
    public void removeHomePageCheckJob(int siteId, String indexUrl) {
        try {
            Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
            JobKey key = new JobKey(getJobName(siteId, CHECK_HOMEPAGE_TYPE), getJobGroupName(CHECK_HOMEPAGE_TYPE));
            scheduler.deleteJob(key);
        } catch (SchedulerException e) {
            log.error("", e);
        }
    }

    private void addFreqUnitAndFreq(FreqUnit freqUnit, Integer freq, SchedulerTask schedulerTask) {

        if(FreqUnit.DAYS_PER_TIME == freqUnit) {

            schedulerTask.setDelayAndTimeUnit(freq.longValue(), TimeUnit.DAYS);
        } else if(FreqUnit.TIMES_PER_DAY == freqUnit) {

            Long totalMinutesPerDay = TimeUnit.DAYS.toMinutes(1);
            schedulerTask.setDelayAndTimeUnit(totalMinutesPerDay / freq, TimeUnit.MINUTES);
        }
    }

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

                // 文档内容错误检测
                initContentCheckJob(scheduler);

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
            scheduleHomePageCheckJob(scheduler, site);
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
                    int interval = getInterval(FrequencyType.TOTAL_BROKEN_LINKS.getFreqUnit(), freq.getValue());
                    scheduleJob(scheduler, CHECK_CONTENT_TYPE, site, interval);
                }
            }
        }
    }

    /**
     * 文档内容有效性检测
     *
     * @param scheduler
     */
    private void initContentCheckJob(Scheduler scheduler) {
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
                if (freq != null && freq.getTypeId() == FrequencyType.WRONG_INFORMATION.getTypeId()) {
                    int interval = getInterval(FrequencyType.WRONG_INFORMATION.getFreqUnit(), freq.getValue());
                    scheduleJob(scheduler, CHECK_CONTENT_TYPE, site, interval);
                }
            }
        }
    }

    private void scheduleHomePageCheckJob(Scheduler scheduler, MonitorSite site) {
        final List<MonitorFrequency> monitorFrequencies = monitorFrequencyMapper
                .queryBySiteId(site.getSiteId());
        if (StringUtil.isEmpty(site.getIndexUrl())
                || monitorFrequencies == null || monitorFrequencies.isEmpty()) {
            return;
        }

        for (MonitorFrequency freq : monitorFrequencies) {
            if (freq != null && freq.getTypeId() == FrequencyType.HOMEPAGE_AVAILABILITY.getTypeId()) {
                int interval = getInterval(FrequencyType.HOMEPAGE_AVAILABILITY.getFreqUnit(), freq.getValue());
                scheduleJob(scheduler, CHECK_HOMEPAGE_TYPE, site, interval);
            }
        }
    }

    /**
     * 获取间隔时间
     *
     * @param unit
     * @param value
     * @return
     */
    private int getInterval(FreqUnit unit, Short value) {
        int interval = 0;
        if (unit == FreqUnit.DAYS_PER_TIME) {
            // 计算间隔的时间，秒
            interval = 24 * 60 * 60 * value;
        } else if (unit == FreqUnit.TIMES_PER_DAY) {
            // 计算间隔的时间，秒
            interval = 24 * 60 * 60 / value;
        }
        return interval;
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

        String name = getTypeName(checkType);
        SchedulerTask task = newTask(checkType);
        if (task == null) {
            return;
        }

        JobDetail job = newJob(CheckJob.class)
                .withIdentity(getJobName(site.getSiteId(), checkType), getJobGroupName(checkType))
                .build();

        // 真正的执行任务
        task.setSiteId(site.getSiteId());
        task.setBaseUrl(site.getIndexUrl());
        job.getJobDataMap().put("task", task);

        // 每天执行一次
        Trigger trigger = newTrigger()
                .withIdentity(getJobTrigger(site.getSiteId(), checkType), getJobGroupName(checkType))
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

    private String getJobName(int siteId, int checkType) {
        return getTypeName(checkType) + "CheckJob" + String.valueOf(siteId);
    }

    private String getJobGroupName(int checkType) {
        return "group-" + getTypeName(checkType) + "-check";
    }

    private String getJobTrigger(int siteId, int checkType) {
        return getTypeName(checkType) + "CheckJobTrigger" + String.valueOf(siteId);
    }

    private String getTypeName(int checkType) {
        switch (checkType) {
            case CHECK_HOMEPAGE_TYPE:
                return "homePage";
            case CHECK_INFO_UPDATE_TYPE:
                return "infoUpdate";
            case CHECK_LINK_TYPE:
                return "link";
			case CHECK_CONTENT_TYPE:
                return "content";
            default:
                return null;
        }
    }

    private SchedulerTask newTask(int checkType) {
        switch (checkType) {
            case CHECK_HOMEPAGE_TYPE:
                return applicationContext.getBean
                        (HomePageCheckScheduler.class);
            case CHECK_INFO_UPDATE_TYPE:
                return applicationContext.getBean
                        (InfoUpdateCheckScheduler.class);
            case CHECK_LINK_TYPE:
                return applicationContext.getBean
                        (LinkAnalysisScheduler.class);
			case CHECK_CONTENT_TYPE:
                return applicationContext.getBean
                        (CKMScheduler.class);
            default:
                return null;
        }
    }
}

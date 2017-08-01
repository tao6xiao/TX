package com.trs.gov.kpi.service.impl;

import com.trs.gov.kpi.constant.Constants;
import com.trs.gov.kpi.constant.EnumCheckJobType;
import com.trs.gov.kpi.constant.FreqUnit;
import com.trs.gov.kpi.constant.FrequencyType;
import com.trs.gov.kpi.dao.MonitorFrequencyMapper;
import com.trs.gov.kpi.entity.MonitorFrequency;
import com.trs.gov.kpi.entity.MonitorSite;
import com.trs.gov.kpi.entity.exception.BizException;
import com.trs.gov.kpi.job.CheckJob;
import com.trs.gov.kpi.scheduler.*;
import com.trs.gov.kpi.service.MonitorSiteService;
import com.trs.gov.kpi.service.SchedulerService;
import com.trs.gov.kpi.utils.DateUtil;
import com.trs.gov.kpi.utils.LogUtil;
import com.trs.gov.kpi.utils.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.*;
import java.util.List;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

/**
 * Created by wangxuan on 2017/5/11.
 */
@Slf4j
@Service
public class SchedulerServiceImpl implements SchedulerService {

    @Value("${issue.location.dir}")
    private String locationDir;

    @Resource
    SchedulerTask[] schedulerTasks;

    @Resource
    MonitorSiteService monitorSiteService;

    @Resource
    MonitorFrequencyMapper monitorFrequencyMapper;

    @Resource
    ApplicationContext applicationContext;


    @Override
    public void addCheckJob(int siteId, EnumCheckJobType checkType) throws BizException {

        try {
            Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
            final MonitorSite site = monitorSiteService.getMonitorSiteBySiteId
                    (siteId);
            if (site == null) {
                log.error("Invalid parameter: 当前站点不是监测中的站点");
                throw new BizException(Constants.INVALID_PARAMETER);
            }

            switch (checkType) {
                case CHECK_HOME_PAGE:
                    scheduleCheckJob(scheduler, site, FrequencyType.HOMEPAGE_AVAILABILITY, EnumCheckJobType.CHECK_HOME_PAGE);
                    break;
                case CHECK_CONTENT:
                    scheduleCheckJob(scheduler, site, FrequencyType.WRONG_INFORMATION, EnumCheckJobType.CHECK_CONTENT);
                    break;
                case CHECK_INFO_UPDATE:
                    scheduleJob(scheduler, EnumCheckJobType.CHECK_INFO_UPDATE, site, DateUtil.SECOND_ONE_DAY);
                    break;
                case CHECK_LINK:
                    scheduleCheckJob(scheduler, site, FrequencyType.TOTAL_BROKEN_LINKS, EnumCheckJobType.CHECK_LINK);
                    break;
                default:
            }

        } catch (SchedulerException e) {
            log.error("", e);
            LogUtil.addSystemLog("", e);
        }
    }

    @Override
    public void removeCheckJob(int siteId, EnumCheckJobType checkType) {
        try {
            Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
            JobKey jobKey = JobKey.jobKey(getJobName(siteId, checkType), getJobGroupName(checkType));
            TriggerKey triggerKey = TriggerKey.triggerKey(getJobTrigger(siteId, checkType), getJobGroupName(checkType));
            while (scheduler.checkExists(triggerKey)) {
                scheduler.unscheduleJob(triggerKey);
            }
            while (scheduler.checkExists(jobKey)) {
                if (!scheduler.deleteJob(jobKey)) {
                    log.warn("failed delete job " + jobKey);
                }
            }
        } catch (SchedulerException e) {
            log.error("", e);
            LogUtil.addSystemLog("", e);
        }
    }

    @PostConstruct
    public void startService() {
        // 启动完成后，就开始执行
        try {
            // 分发样式文件
            distStyleFile();

            Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
            scheduler.start();

            // 栏目更新检查
            initInfoUpdateCheckJob(scheduler);

            // 首页有效性检查
            initHomepageCheckJob(scheduler);

            // 全站链接有效性检查
            initLinkCheckJob(scheduler);

            //文档内容错误检测
            initContentCheckJob(scheduler);

            //计算绩效指数
            initPerformanceCheckJob(scheduler);

            //按时间节点生成报表
            initTimeNodeCheckJob(scheduler);

            //按时间区间生成报表
            initTimeIntervalCheckJob(scheduler);

            //服务链接可用性监测
            initServiceLinkCheckJob(scheduler);

        } catch (SchedulerException e) {
            log.error("", e);
            LogUtil.addSystemLog("", e);
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
            scheduleJob(scheduler, EnumCheckJobType.CHECK_INFO_UPDATE, site, DateUtil.SECOND_ONE_DAY);
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
            scheduleCheckJob(scheduler, site, FrequencyType.HOMEPAGE_AVAILABILITY, EnumCheckJobType.CHECK_HOME_PAGE);
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
            scheduleCheckJob(scheduler, site, FrequencyType.TOTAL_BROKEN_LINKS, EnumCheckJobType.CHECK_LINK);
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
            scheduleCheckJob(scheduler, site, FrequencyType.WRONG_INFORMATION, EnumCheckJobType.CHECK_CONTENT);
        }
    }

    /**
     * 绩效指数计算
     *
     * @param scheduler
     */
    private void initPerformanceCheckJob(Scheduler scheduler) {
        // 查询数据库里面的所有站点
        final List<MonitorSite> allMonitorSites = monitorSiteService.getAllMonitorSites();
        if (allMonitorSites == null || allMonitorSites.isEmpty()) {
            return;
        }

        for (MonitorSite site : allMonitorSites) {
            // 每个月1日凌晨0点执行一次
            scheduleJob(scheduler, EnumCheckJobType.CALCULATE_PERFORMANCE, site, "0 0 0 1 * ?");
        }
    }

    /**
     * 按时间节点生成报表
     *
     * @param scheduler
     */
    private void initTimeNodeCheckJob(Scheduler scheduler) {
        // 查询数据库里面的所有站点
        final List<MonitorSite> allMonitorSites = monitorSiteService.getAllMonitorSites();
        if (allMonitorSites == null || allMonitorSites.isEmpty()) {
            return;
        }

        for (MonitorSite site : allMonitorSites) {
            // 每天凌晨0点执行一次
            scheduleJob(scheduler, EnumCheckJobType.TIMENODE_REPORT_GENERATE, site, "0 0 0 * * ?");
        }
    }

    /**
     * 服务链接监测
     *
     * @param scheduler
     */
    private void initServiceLinkCheckJob(Scheduler scheduler) {
        // 查询数据库里面的所有站点
        final List<MonitorSite> allMonitorSites = monitorSiteService.getAllMonitorSites();
        if (allMonitorSites == null || allMonitorSites.isEmpty()) {
            return;
        }

        for (MonitorSite site : allMonitorSites) {
            scheduleJob(scheduler, EnumCheckJobType.SERVICE_LINK, site, DateUtil.SECOND_ONE_DAY);
        }
    }

    /**
     * 按时间区间生成报表
     *
     * @param scheduler
     */
    private void initTimeIntervalCheckJob(Scheduler scheduler) {
        // 查询数据库里面的所有站点
        final List<MonitorSite> allMonitorSites = monitorSiteService.getAllMonitorSites();
        if (allMonitorSites == null || allMonitorSites.isEmpty()) {
            return;
        }

        for (MonitorSite site : allMonitorSites) {
            // 每个月1日凌晨0点执行一次
            scheduleJob(scheduler, EnumCheckJobType.TIMEINTERVAL_REPORT_GENERATE, site, "0 0 0 2 * ?");
        }
    }

    private void scheduleCheckJob(Scheduler scheduler, MonitorSite site, FrequencyType freqType, EnumCheckJobType jobType) {
        final List<MonitorFrequency> monitorFrequencies = monitorFrequencyMapper
                .queryBySiteId(site.getSiteId());
        if (StringUtil.isEmpty(site.getIndexUrl())
                || monitorFrequencies == null || monitorFrequencies.isEmpty()) {
            return;
        }

        for (MonitorFrequency freq : monitorFrequencies) {
            if (freq != null && freq.getTypeId() == freqType.getTypeId()) {
                int interval = getInterval(freqType.getFreqUnit(), freq.getValue());
                scheduleJob(scheduler, jobType, site, interval);
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
        } else if (unit == FreqUnit.TIMES_PER_MONTH) {
            // 计算间隔的时间，秒
            interval = 30 * 24 * 60 * 60 / value;
        }
        return interval;
    }

    /**
     * 注册调度任务
     *
     * @param scheduler
     * @param jobType
     * @param site
     * @param cronExpress cron 表达式
     */
    private void scheduleJob(Scheduler scheduler, EnumCheckJobType jobType, MonitorSite site, String cronExpress) {
        scheduleJob(scheduler, jobType, site, cronSchedule(cronExpress));
    }

    /**
     * 注册调度任务
     *
     * @param scheduler
     * @param jobType
     * @param site
     * @param interval  间隔时间
     */
    private void scheduleJob(Scheduler scheduler, EnumCheckJobType jobType, MonitorSite site, int interval) {
        scheduleJob(scheduler, jobType, site, simpleSchedule()
                .withIntervalInSeconds(interval)
                .repeatForever());
    }

    /**
     * 注册调度任务
     *
     * @param scheduler
     * @param jobType
     * @param site
     * @param builder
     */
    private <T extends Trigger> void scheduleJob(Scheduler scheduler, EnumCheckJobType jobType, MonitorSite site, ScheduleBuilder<T> builder) {

        SchedulerTask task = newTask(jobType);
        if (task == null) {
            return;
        }

        JobDetail job = newJob(CheckJob.class)
                .withIdentity(getJobName(site.getSiteId(), jobType), getJobGroupName(jobType))
                .build();

        // 真正的执行任务
        task.setSiteId(site.getSiteId());
        task.setBaseUrl(site.getIndexUrl());
        if (jobType == EnumCheckJobType.TIMENODE_REPORT_GENERATE) {
            task.setIsTimeNode(true);
        } else if (jobType == EnumCheckJobType.TIMEINTERVAL_REPORT_GENERATE) {
            task.setIsTimeNode(false);
        }
        job.getJobDataMap().put("task", task);


        // 每天执行一次
        Trigger trigger = newTrigger()
                .withIdentity(getJobTrigger(site.getSiteId(), jobType), getJobGroupName(jobType))
                .startNow()
                .forJob(job.getKey())
                .withSchedule(builder)
                .build();
        try {
            scheduler.scheduleJob(job, trigger);
        } catch (SchedulerException e) {
            log.error("failed to schedule " + jobType.name() + " check of site " + site.getSiteId(), e);
            LogUtil.addSystemLog("failed to schedule " + jobType.name() + " check of site " + site.getSiteId(), e);
        }
    }

    private String getJobName(int siteId, EnumCheckJobType checkType) {
        return checkType.name() + "CheckJob" + siteId;
    }

    private String getJobGroupName(EnumCheckJobType checkType) {
        return "group-" + checkType.name() + "-check";
    }

    private String getJobTrigger(int siteId, EnumCheckJobType jobType) {
        return jobType.name() + "CheckJobTrigger" + siteId;
    }

    private SchedulerTask newTask(EnumCheckJobType jobType) {
        switch (jobType) {
            case CHECK_HOME_PAGE:
                return applicationContext.getBean
                        (HomePageCheckScheduler.class);
            case CHECK_INFO_UPDATE:
                return applicationContext.getBean
                        (InfoUpdateCheckScheduler.class);
            case CHECK_LINK:
                return applicationContext.getBean
                        (LinkAnalysisScheduler.class);
            case CHECK_CONTENT:
                return applicationContext.getBean
                        (CKMScheduler.class);
            case CALCULATE_PERFORMANCE:
                return applicationContext.getBean
                        (PerformanceScheduler.class);
            case TIMENODE_REPORT_GENERATE:
                return applicationContext.getBean
                        (ReportGenerateScheduler.class);
            case TIMEINTERVAL_REPORT_GENERATE:
                return applicationContext.getBean
                        (ReportGenerateScheduler.class);
            case SERVICE_LINK:
                return applicationContext.getBean
                        (ServiceLinkScheduler.class);
            default:
                return null;
        }
    }

    /**
     * 写入问题定位文件所需的样式文件
     */
    private void distStyleFile() {
        final InputStream resourceAsStream = getClass().getResourceAsStream("/style/css.css");
        BufferedReader reader = new BufferedReader(new InputStreamReader(resourceAsStream));
        StringBuilder sb = new StringBuilder();
        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + System.getProperty("line.separator"));
            }
        } catch (IOException e) {
            log.error("", e);
            LogUtil.addSystemLog("", e);
        } finally {
            try {
                resourceAsStream.close();
            } catch (IOException e) {
                log.error("", e);
                LogUtil.addSystemLog("", e);
            }
        }

        try {
            FileUtils.forceMkdir(new File(locationDir + File.separator + "style"));
            FileUtils.writeStringToFile(new File(locationDir + File.separator + "style" + File.separator + "css.css"), sb.toString());
        } catch (IOException e) {
            log.error("", e);
            LogUtil.addSystemLog("", e);
        }
    }
}

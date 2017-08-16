package com.trs.gov.kpi.service.impl;

import com.trs.gov.kpi.constant.*;
import com.trs.gov.kpi.dao.MonitorFrequencyMapper;
import com.trs.gov.kpi.entity.MonitorFrequency;
import com.trs.gov.kpi.entity.MonitorSite;
import com.trs.gov.kpi.entity.exception.BizException;
import com.trs.gov.kpi.entity.exception.RemoteException;
import com.trs.gov.kpi.job.CheckJob;
import com.trs.gov.kpi.scheduler.*;
import com.trs.gov.kpi.service.MonitorSiteService;
import com.trs.gov.kpi.service.SchedulerService;
import com.trs.gov.kpi.utils.DateUtil;
import com.trs.gov.kpi.utils.LogUtil;
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
import java.text.MessageFormat;
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

    private static final String FIRST_DAY_OF_MONTH = "0 0 0 1 * ?";

    @Value("${issue.location.dir}")
    private String locationDir;

    @Resource
    MonitorSiteService monitorSiteService;

    @Resource
    MonitorFrequencyMapper monitorFrequencyMapper;

    @Resource
    ApplicationContext applicationContext;

    @Override
    public void addCheckJob(int siteId, EnumCheckJobType checkType) throws BizException {

        final MonitorSite site = monitorSiteService.getMonitorSiteBySiteId(siteId);
        if (site == null) {
            log.error("Invalid parameter: 当前站点不是监测中的站点");
            throw new BizException(Constants.INVALID_PARAMETER);
        }
        Scheduler scheduler;
        try {
            scheduler = StdSchedulerFactory.getDefaultScheduler();
        } catch (SchedulerException e) {
            String errorInfo = MessageFormat.format("添加监测任务失败！[siteId={0}, checkType={1}]", siteId, checkType);
            log.error(errorInfo, e);
            throw new BizException(errorInfo, e);
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
            case CALCULATE_PERFORMANCE:
                scheduleJob(scheduler, EnumCheckJobType.CALCULATE_PERFORMANCE, site, FIRST_DAY_OF_MONTH);
                break;
            case TIMENODE_REPORT_GENERATE:
                scheduleJob(scheduler, EnumCheckJobType.TIMENODE_REPORT_GENERATE, site, "0 0 0 * * ?");
                break;
            case TIMEINTERVAL_REPORT_GENERATE:
                scheduleJob(scheduler, EnumCheckJobType.TIMEINTERVAL_REPORT_GENERATE, site, FIRST_DAY_OF_MONTH);
                break;
            case SERVICE_LINK:
                scheduleJob(scheduler, EnumCheckJobType.SERVICE_LINK, site, DateUtil.SECOND_ONE_DAY);
                break;
            default:
                log.warn("invalid checkType: " + checkType);
        }

    }

    @Override
    public void removeCheckJob(int siteId, EnumCheckJobType checkType) throws BizException {
        String paramInfo = MessageFormat.format("[siteId={0}, checkType={1}]", siteId, checkType);
        try {
            Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
            JobKey jobKey = JobKey.jobKey(getJobName(siteId, checkType), getJobGroupName(checkType));
            TriggerKey triggerKey = TriggerKey.triggerKey(getJobTriggerName(siteId, checkType), getJobGroupName(checkType));
            while (scheduler.checkExists(triggerKey)) {
                scheduler.unscheduleJob(triggerKey);
            }
            while (scheduler.checkExists(jobKey)) {
                if (!scheduler.deleteJob(jobKey)) {
                    log.warn("failed delete job " + jobKey);
                }
            }
            LogUtil.addDebugLog(OperationType.TASK_SCHEDULE, DebugType.REMOVE_SCHEDULE, "移除调度任务" + paramInfo);
        } catch (SchedulerException e) {
            String errorInfo = "移除调度任务失败！" + paramInfo;
            log.error(errorInfo, e);
            LogUtil.addErrorLog(OperationType.TASK_SCHEDULE, ErrorType.TASK_SCHEDULE_FAILED, errorInfo, e);
            throw new BizException(errorInfo, e);
        }
    }

    @Override
    public void doCheckJobOnce(int siteId, EnumCheckJobType checkJobType) throws BizException, RemoteException {
        String paramInfo = MessageFormat.format("[siteId={0}, checkJobType={1}]", siteId, checkJobType);
        MonitorSite monitorSite = monitorSiteService.getMonitorSiteBySiteId(siteId);
        if (monitorSite == null) {
            log.error("Invalid parameter: 当前站点不是监测中的站点。" + paramInfo);
            throw new BizException(Constants.INVALID_PARAMETER);
        }
        Scheduler scheduler;
        try {
            scheduler = StdSchedulerFactory.getDefaultScheduler();
        } catch (SchedulerException e) {
            String errorInfo = "执行任务失败！" + paramInfo;
            log.error(errorInfo, e);
            throw new BizException(errorInfo, e);
        }
        switch (checkJobType) {
            case CHECK_HOME_PAGE://首页可用性检测
                doCheckJobNow(scheduler, siteId, EnumCheckJobType.CHECK_HOME_PAGE);
                break;
            case CHECK_CONTENT://内容检测
                doCheckJobNow(scheduler, siteId, EnumCheckJobType.CHECK_CONTENT);
                break;
            case CHECK_INFO_UPDATE://信息更新检测
                doCheckJobNow(scheduler, siteId, EnumCheckJobType.CHECK_INFO_UPDATE);
                break;
            case CHECK_LINK://链接可用性检测
                doCheckJobNow(scheduler, siteId, EnumCheckJobType.CHECK_LINK);
                break;
            case SERVICE_LINK://服务链接可用性检测
                doCheckJobNow(scheduler, siteId, EnumCheckJobType.SERVICE_LINK);
                break;
            default:
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
            log.error("failed to start scheduler service!", e);
            LogUtil.addErrorLog(OperationType.TASK_SCHEDULE, ErrorType.TASK_SCHEDULE_FAILED, "调度服务启动失败！", e);
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

        LogUtil.addDebugLog(OperationType.TASK_SCHEDULE, DebugType.REGISTER_SCHEDULE, "启动栏目更新检查任务成功");
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

        LogUtil.addDebugLog(OperationType.TASK_SCHEDULE, DebugType.REGISTER_SCHEDULE, "启动首页监测任务成功");
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
        LogUtil.addDebugLog(OperationType.TASK_SCHEDULE, DebugType.REGISTER_SCHEDULE, "启动全站链接监测任务成功");
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
        LogUtil.addDebugLog(OperationType.TASK_SCHEDULE, DebugType.REGISTER_SCHEDULE, "启动信息错误监测任务成功");
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
            scheduleJob(scheduler, EnumCheckJobType.CALCULATE_PERFORMANCE, site, FIRST_DAY_OF_MONTH);
        }
        LogUtil.addDebugLog(OperationType.TASK_SCHEDULE, DebugType.REGISTER_SCHEDULE, "启动绩效指数计算任务成功");
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
        LogUtil.addDebugLog(OperationType.TASK_SCHEDULE, DebugType.REGISTER_SCHEDULE, "启动时间节点报表生成任务成功");
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
        LogUtil.addDebugLog(OperationType.TASK_SCHEDULE, DebugType.REGISTER_SCHEDULE, "启动服务链接监测任务成功");
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
            scheduleJob(scheduler, EnumCheckJobType.TIMEINTERVAL_REPORT_GENERATE, site, FIRST_DAY_OF_MONTH);
        }
        LogUtil.addDebugLog(OperationType.TASK_SCHEDULE, DebugType.REGISTER_SCHEDULE, "启动时间区间生成报表任务成功");
    }

    private void scheduleCheckJob(Scheduler scheduler, MonitorSite site, FrequencyType freqType, EnumCheckJobType jobType) {
        final List<MonitorFrequency> monitorFrequencies = monitorFrequencyMapper
                .queryBySiteId(site.getSiteId());

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
        try {
            scheduleJob(scheduler, jobType, site, cronSchedule(cronExpress));
            LogUtil.addDebugLog(OperationType.TASK_SCHEDULE, DebugType.REGISTER_SCHEDULE, "站点Id=" + site.getSiteId() + ", 注册调度任务成功：任务类型:" + jobType.name() + "，间隔时间：cornExpress = " + cronExpress);
        } catch (SchedulerException e) {
            addSchedulerExceptionLog(jobType, site, e);
        }
    }

    private void addSchedulerExceptionLog(EnumCheckJobType jobType, MonitorSite site, SchedulerException e) {
        log.error("failed to schedule " + jobType.name() + " check of site " + site.getSiteId(), e);
        LogUtil.addErrorLog(OperationType.TASK_SCHEDULE, ErrorType.TASK_SCHEDULE_FAILED, "注册调度任务失败：failed to schedule " + jobType.name() + " check of site " + site.getSiteId(), e);
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
        try {
            scheduleJob(scheduler, jobType, site, simpleSchedule()
                    .withIntervalInSeconds(interval)
                    .repeatForever());
            LogUtil.addDebugLog(OperationType.TASK_SCHEDULE, DebugType.REGISTER_SCHEDULE, "注册调度任务成功：站点Id: " + site.getSiteId() + ", 任务类型:" + jobType.name() + "，间隔时间：" + interval);
        } catch (SchedulerException e) {
            addSchedulerExceptionLog(jobType, site, e);
        }
    }

    /**
     * 注册调度任务
     *
     * @param scheduler
     * @param jobType
     * @param site
     * @param builder
     */
    private <T extends Trigger> void scheduleJob(Scheduler scheduler, EnumCheckJobType jobType, MonitorSite site, ScheduleBuilder<T> builder) throws SchedulerException {

        SchedulerTask task = newTask(jobType);
        if (task == null) {
            throw new SchedulerException();
        }

        JobDetail job = newJob(CheckJob.class)
                .withIdentity(getJobName(site.getSiteId(), jobType), getJobGroupName(jobType))
                .build();

        // 真正的执行任务
        task.setSiteId(site.getSiteId());
        if (jobType == EnumCheckJobType.TIMENODE_REPORT_GENERATE) {
            task.setIsTimeNode(true);
        } else if (jobType == EnumCheckJobType.TIMEINTERVAL_REPORT_GENERATE) {
            task.setIsTimeNode(false);
        }
        job.getJobDataMap().put("task", task);

        // TODO: 2017/8/14 he.lang 手动检测实现后，startNow()将删除
        // 每天执行一次
        Trigger trigger = newTrigger()
                .withIdentity(getJobTriggerName(site.getSiteId(), jobType), getJobGroupName(jobType))
                .startNow()
                .forJob(job.getKey())
                .withSchedule(builder)
                .build();

        scheduler.scheduleJob(job, trigger);

    }

    /**
     * 执行立即检测
     *
     * @param siteId
     * @param checkJobType
     * @throws RemoteException
     */
    private void doCheckJobNow(Scheduler scheduler, Integer siteId, EnumCheckJobType checkJobType) throws RemoteException, BizException {

        String paramInfo = MessageFormat.format("[siteId={0}, jobType={1}]", siteId, checkJobType);
        SchedulerTask task = newTask(checkJobType);
        if (task == null) {
            throw new BizException("手动检测失败! 无法创建监测任务。" + paramInfo);
        }

        JobDetail job = newJob(CheckJob.class)
                .withIdentity(getOnceJobName(siteId, checkJobType), getOnceJobGroupName(checkJobType))
                .build();

        task.setSiteId(siteId);
        task.setMonitorType(Status.MonitorType.MANUAL_MONITOR.value);
        job.getJobDataMap().put("task", task);

        Trigger trigger = newTrigger()
                .withIdentity(getOnceJobTriggerName(siteId, checkJobType), getOnceJobGroupName(checkJobType))
                .startNow()
                .forJob(job.getKey())
                .build();

        try {
            scheduler.scheduleJob(job, trigger);
        } catch (SchedulerException e) {
            log.error("failed to doCheckJobNow!" + paramInfo, e);
            throw new BizException("手动检测失败! 调度任务失败。" + paramInfo, e);
        }
    }

    private String getJobName(int siteId, EnumCheckJobType checkType) {
        return checkType.name() + "CheckJob" + siteId;
    }

    private String getOnceJobName(int siteId, EnumCheckJobType checkType) {
        return checkType.name() + "OnceCheckJob" + siteId;
    }

    private String getJobGroupName(EnumCheckJobType checkType) {
        return "group-" + checkType.name() + "-check";
    }

    private String getOnceJobGroupName(EnumCheckJobType checkType) {
        return "group-once-" + checkType.name() + "-check";
    }

    private String getJobTriggerName(int siteId, EnumCheckJobType jobType) {
        return jobType.name() + "CheckJobTrigger" + siteId;
    }

    private String getOnceJobTriggerName(int siteId, EnumCheckJobType jobType) {
        return jobType.name() + "OnceCheckJobTrigger" + siteId;
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
                log.error("Invalid job type = " + jobType);
                return null;
        }
    }

    /**
     * 写入问题定位文件所需的样式文件
     */
    private void distStyleFile() {
        String message = "写入问题定位文件所需的样式文件";
        String styleFile = "/style/css.css";
        final InputStream resourceAsStream = getClass().getResourceAsStream(styleFile);
        BufferedReader reader = new BufferedReader(new InputStreamReader(resourceAsStream));
        StringBuilder sb = new StringBuilder();
        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + System.getProperty("line.separator"));
            }
        } catch (IOException e) {
            log.error("failed read style file: " + styleFile, e);
            LogUtil.addErrorLog(OperationType.REQUEST, ErrorType.REQUEST_FAILED, message, e);
        } finally {
            try {
                resourceAsStream.close();
            } catch (IOException e) {
                log.error("failed close style file: " + styleFile, e);
                LogUtil.addErrorLog(OperationType.REQUEST, ErrorType.REQUEST_FAILED, message, e);
            }
        }

        try {
            FileUtils.forceMkdir(new File(locationDir + File.separator + "style"));
            FileUtils.writeStringToFile(new File(locationDir + File.separator + "style" + File.separator + "css.css"), sb.toString());
        } catch (IOException e) {
            log.error("failed to copy style file: " + styleFile, e);
            LogUtil.addErrorLog(OperationType.REQUEST, ErrorType.REQUEST_FAILED, message, e);
        }
    }
}

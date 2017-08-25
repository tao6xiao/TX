package com.trs.gov.kpi.job;

import com.trs.gov.kpi.config.TestConfigConst;
import com.trs.gov.kpi.constant.MonitorRecordTableField;
import com.trs.gov.kpi.dao.CommonMapper;
import com.trs.gov.kpi.entity.dao.DBUpdater;
import com.trs.gov.kpi.entity.dao.QueryFilter;
import com.trs.gov.kpi.entity.dao.Table;
import com.trs.gov.kpi.scheduler.HomePageCheckScheduler;
import com.trs.gov.kpi.scheduler.SchedulerTask;
import com.trs.gov.kpi.service.impl.MonitorRecordServiceImpl;
import com.trs.gov.kpi.service.impl.outer.SiteApiServiceImpl;
import com.trs.gov.kpi.utils.ReflectionUtils;
import com.trs.gov.kpi.utils.SpringContextUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.Date;

/**
 * Created by linwei on 2017/8/18.
 */
@RunWith(SpringRunner.class)
@MybatisTest(includeFilters = {@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = {MonitorRecordServiceImpl.class, SiteApiServiceImpl.class})})
@TestPropertySource(properties = {TestConfigConst.LOCAL_DB_URL_PROP, TestConfigConst.LOCAL_DB_USER_NAME_PROP, TestConfigConst.LOCAL_DB_PWD_PROP})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class CheckJobTest {

    @Resource
    private ApplicationContext applicationContext;

    @Resource
    private CommonMapper commonMapper;

    @Before
    public void setup() {
        SpringContextUtil contextUtil = new SpringContextUtil();
        contextUtil.setApplicationContext(applicationContext);
    }


    @Test
    @Rollback
    public void insertBeginMonitorRecord() {
        CheckJob job = new CheckJob();

        final HomePageCheckScheduler homePageCheckScheduler = new HomePageCheckScheduler();
        homePageCheckScheduler.setSiteId(11111111);

        final Date date = new Date();

        ReflectionUtils.invokeMethod(job, "insertBeginMonitorRecord",
                new Class<?>[]{SchedulerTask.class, Date.class}, new Object[]{homePageCheckScheduler, date});


    }

    @Test
    public void updateMonitorRecord() {
        QueryFilter filter = new QueryFilter(Table.MONITOR_RECORD);
        filter.addCond(MonitorRecordTableField.SITE_ID, 499);
        filter.addCond(MonitorRecordTableField.TASK_ID, 2);
        filter.addCond(MonitorRecordTableField.BEGIN_TIME, null);

        DBUpdater updater = new DBUpdater(Table.MONITOR_RECORD.getTableName());
        updater.addField(MonitorRecordTableField.RESULT, 12);
        updater.addField(MonitorRecordTableField.END_TIME, new Date());
        updater.addField(MonitorRecordTableField.TASK_STATUS, 5);
        commonMapper.update(updater, filter);
    }
}
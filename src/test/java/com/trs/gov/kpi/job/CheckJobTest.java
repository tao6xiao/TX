package com.trs.gov.kpi.job;

import com.trs.gov.kpi.config.TestConfigConst;
import com.trs.gov.kpi.scheduler.HomePageCheckScheduler;
import com.trs.gov.kpi.scheduler.SchedulerTask;
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
@MybatisTest(includeFilters = {@ComponentScan.Filter(type = FilterType.REGEX, pattern="com.trs.*.MonitorRecordServiceImpl")})
@TestPropertySource(properties = {TestConfigConst.TEST_DB_URL_PROP, TestConfigConst.TEST_DB_USER_NAME_PROP, TestConfigConst.TEST_DB_PWD_PROP})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class CheckJobTest {

    @Resource
    private ApplicationContext applicationContext;

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
}
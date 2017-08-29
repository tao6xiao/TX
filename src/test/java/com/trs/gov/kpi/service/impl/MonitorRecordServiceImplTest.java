package com.trs.gov.kpi.service.impl;

import com.trs.gov.kpi.config.TestConfigConst;
import com.trs.gov.kpi.constant.Status;
import com.trs.gov.kpi.constant.Types;
import com.trs.gov.kpi.dao.CommonMapper;
import com.trs.gov.kpi.dao.MonitorRecordMapper;
import com.trs.gov.kpi.entity.MonitorRecord;
import com.trs.gov.kpi.entity.requestdata.PageDataRequestParam;
import com.trs.gov.kpi.entity.responsedata.ApiPageData;
import com.trs.gov.kpi.entity.responsedata.MonitorOnceResponse;
import com.trs.gov.kpi.service.MonitorRecordService;
import com.trs.gov.kpi.service.impl.outer.DeptApiServiceImpl;
import com.trs.gov.kpi.utils.DBUtil;
import com.trs.gov.kpi.utils.DateUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by tao.xiao on 2017/8/29.
 */
@MybatisTest(includeFilters = {@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = {MonitorRecordServiceImpl.class})})
@TestPropertySource(properties = {TestConfigConst.TEST_DB_URL_PROP, TestConfigConst.TEST_DB_USER_NAME_PROP, TestConfigConst.TEST_DB_PWD_PROP})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@RunWith(SpringRunner.class)
public class MonitorRecordServiceImplTest {

    @Resource
    MonitorRecordService monitorRecordService;

    @Resource
    MonitorRecordMapper monitorRecordMapper;

    @Resource
    CommonMapper commonMapper;

    @Before
    @Rollback
    public void addTestData() throws Exception {
        MonitorRecord monitorRecord = new MonitorRecord();
        monitorRecord.setSiteId(TestConfigConst.testSiteId);

        monitorRecord.setTaskId(Types.MonitorRecordNameType.TASK_CHECK_HOME_PAGE.value);
        monitorRecord.setTaskStatus(Status.MonitorStatusType.CHECK_DONE.value);
        monitorRecord.setBeginTime(DateUtil.toDate("2017-08-26 00:00:00"));
        monitorRecord.setEndTime(DateUtil.toDate("2017-08-26 00:00:03"));
        commonMapper.insert(DBUtil.toRow(monitorRecord));

        monitorRecord.setBeginTime(DateUtil.toDate("2017-08-26 00:00:01"));
        monitorRecord.setEndTime(DateUtil.toDate("2017-08-26 00:00:02"));
        commonMapper.insert(DBUtil.toRow(monitorRecord));

        monitorRecord.setTaskId(Types.MonitorRecordNameType.TASK_CHECK_INFO_UPDATE.value);
        monitorRecord.setTaskStatus(Status.MonitorStatusType.CHECK_ERROR.value);
        monitorRecord.setBeginTime(DateUtil.toDate("2017-08-26 00:00:00"));
        monitorRecord.setEndTime(DateUtil.toDate("2017-08-26 00:00:02"));
        commonMapper.insert(DBUtil.toRow(monitorRecord));

        monitorRecord.setTaskStatus(Status.MonitorStatusType.DOING_CHECK.value);
        monitorRecord.setBeginTime(DateUtil.toDate("2017-08-26 00:00:01"));
        monitorRecord.setEndTime(DateUtil.toDate("2017-08-26 00:00:02"));
        commonMapper.insert(DBUtil.toRow(monitorRecord));

        monitorRecord.setTaskId(Types.MonitorRecordNameType.TASK_CHECK_LINK.value);
        monitorRecord.setTaskStatus(Status.MonitorStatusType.CHECK_ERROR.value);
        monitorRecord.setBeginTime(DateUtil.toDate("2017-08-26 00:00:00"));
        monitorRecord.setEndTime(DateUtil.toDate("2017-08-26 00:00:02"));
        commonMapper.insert(DBUtil.toRow(monitorRecord));

        monitorRecord.setTaskStatus(Status.MonitorStatusType.CHECK_ERROR.value);
        monitorRecord.setBeginTime(DateUtil.toDate("2017-08-26 00:00:02"));
        monitorRecord.setEndTime(DateUtil.toDate("2017-08-26 00:00:03"));
        commonMapper.insert(DBUtil.toRow(monitorRecord));

    }

    @Test
    public void selectMonitorRecordList() throws Exception {
        PageDataRequestParam param = new PageDataRequestParam();
        param.setSiteId(TestConfigConst.testSiteId);

        //无时间范围
        ApiPageData result = monitorRecordService.selectMonitorRecordList(param);
        assertEquals(6, result.getData().size());

        //设置时间范围
        param.setBeginDateTime("2017-08-26 00:00:01");
        result = monitorRecordService.selectMonitorRecordList(param);
        assertEquals(3, result.getData().size());

    }

    @Test
    public void selectMonitorResulrOnce() throws Exception {
        List<Integer> checkJobValues = new ArrayList<>(Arrays.asList(1, 4, 2, 3));
        List<MonitorOnceResponse> result = monitorRecordService
                .selectMonitorResulrOnce(TestConfigConst.testSiteId, checkJobValues);

        //首页性检测
        assertEquals(DateUtil.toDate("2017-08-26 00:00:01"), result.get(0).getBeginDateTime());

        //信息更新检测
        assertEquals(Types.MonitorRecordNameType.TASK_CHECK_INFO_UPDATE.value, result.get(1).getTaskId().intValue());
        assertEquals(DateUtil.toDate("2017-08-26 00:00:01"), result.get(1).getBeginDateTime());
        assertEquals(Status.MonitorStatusType.DOING_CHECK.getName(), result.get(1).getTaskStatusName());

        //失效链接检测
        assertEquals(DateUtil.toDate("2017-08-26 00:00:02"), result.get(2).getBeginDateTime());
        assertEquals(null, result.get(2).getResult());

        //信息错误检测
        assertEquals(Status.MonitorStatusType.NO_CHECK.getName(), result.get(3).getTaskStatusName());

    }

    @Test
    public void updateLastServerAbnormalShutdownTaskMonitorState() throws Exception {

        monitorRecordService.updateLastServerAbnormalShutdownTaskMonitorState(TestConfigConst.testSiteId,
                Types.MonitorRecordNameType.TASK_CHECK_HOME_PAGE.value);

        monitorRecordService.updateLastServerAbnormalShutdownTaskMonitorState(TestConfigConst.testSiteId,
                Types.MonitorRecordNameType.TASK_CHECK_INFO_UPDATE.value);

        PageDataRequestParam param = new PageDataRequestParam();
        param.setSiteId(TestConfigConst.testSiteId);

        assertNotEquals(Status.MonitorStatusType.CHECK_ERROR,
                monitorRecordMapper.selectNewestMonitorRecord(TestConfigConst.testSiteId,
                Types.MonitorRecordNameType.TASK_CHECK_HOME_PAGE.value)
                .get(0).getTaskStatus().intValue());
        assertEquals(Status.MonitorStatusType.CHECK_ERROR.value,
                monitorRecordMapper.selectNewestMonitorRecord(TestConfigConst.testSiteId,
                Types.MonitorRecordNameType.TASK_CHECK_INFO_UPDATE.value)
                        .get(0).getTaskStatus().intValue());


    }

}
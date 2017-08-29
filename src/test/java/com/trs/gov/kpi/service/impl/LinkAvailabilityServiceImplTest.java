package com.trs.gov.kpi.service.impl;

import com.trs.gov.kpi.config.TestConfigConst;
import com.trs.gov.kpi.constant.Types;
import com.trs.gov.kpi.dao.IssueMapper;
import com.trs.gov.kpi.entity.Issue;
import com.trs.gov.kpi.entity.MonitorRecord;
import com.trs.gov.kpi.entity.outerapi.Site;
import com.trs.gov.kpi.entity.requestdata.PageDataRequestParam;
import com.trs.gov.kpi.entity.responsedata.ApiPageData;
import com.trs.gov.kpi.entity.responsedata.IndexPage;
import com.trs.gov.kpi.service.LinkAvailabilityService;
import com.trs.gov.kpi.service.MonitorRecordService;
import com.trs.gov.kpi.service.impl.outer.DeptApiServiceImpl;
import com.trs.gov.kpi.service.outer.SiteApiService;
import com.trs.gov.kpi.utils.DBUtil;
import com.trs.gov.kpi.utils.DateUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

import static org.junit.Assert.*;
import static org.mockito.BDDMockito.given;

/**
 * Created by tao.xiao on 2017/8/28.
 */
@MybatisTest(includeFilters = {@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = {LinkAvailabilityServiceImpl.class, MonitorRecordServiceImpl.class, DeptApiServiceImpl.class})})
@TestPropertySource(properties = {TestConfigConst.TEST_DB_URL_PROP, TestConfigConst.TEST_DB_USER_NAME_PROP, TestConfigConst.TEST_DB_PWD_PROP})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@RunWith(SpringRunner.class)
public class LinkAvailabilityServiceImplTest {

    @Resource
    private LinkAvailabilityService linkAvailabilityService;

    @Resource
    private MonitorRecordService monitorRecordService;

    @MockBean
    private SiteApiService siteApiService;

    @Resource
    IssueMapper issueMapper;

    @Test
    @Rollback
    public void getIssueList() throws Exception {
        PageDataRequestParam param = new PageDataRequestParam();
        param.setSiteId(8269426);
        param.setPageIndex(1);
        param.setPageSize(10);

        Issue issue = new Issue();
        issue.setSiteId(param.getSiteId());
        issue.setTypeId(Types.IssueType.LINK_AVAILABLE_ISSUE.value);
        issue.setSubTypeId(Types.LinkAvailableIssueType.INVALID_LINK.value);
        issue.setIssueTime(DateUtil.toDate("2018-08-26 00:00:00"));
        issueMapper.insert(DBUtil.toRow(issue));

        Issue issue1 = new Issue();
        issue1.setSiteId(param.getSiteId());
        issue1.setTypeId(Types.IssueType.SERVICE_LINK_AVAILABLE.value);
        issue1.setSubTypeId(Types.ServiceLinkIssueType.INVALID_LINK.value);
        issue1.setIssueTime(DateUtil.toDate("2018-08-26 00:00:00"));
        issueMapper.insert(DBUtil.toRow(issue1));

        ApiPageData expectedResult = linkAvailabilityService.getIssueList(param);
        assertEquals(1, expectedResult.getData().size());
    }

    @Test
    @Rollback
    public void showIndexAvailability() throws Exception {
        PageDataRequestParam param = new PageDataRequestParam();
        IndexPage expectedResult = new IndexPage();
        param.setSiteId(8269426);
        Site site = new Site();
        site.setWebHttp("www.testindex.com");
        given(this.siteApiService.getSiteById(param.getSiteId(), null)).willReturn(site);

        // 存在监测记录
        MonitorRecord monitorRecord = new MonitorRecord();
        monitorRecord.setSiteId(param.getSiteId());
        monitorRecord.setTaskId(1);
        monitorRecord.setTaskStatus(2);
        monitorRecord.setEndTime(DateUtil.toDate("2017-08-26 00:00:00"));
        monitorRecordService.insertMonitorRecord(monitorRecord);
        expectedResult = linkAvailabilityService.showIndexAvailability(param);
        assertNotEquals(null ,expectedResult.getMonitorTime());

        // 首页可用
        monitorRecord.setResult(0);
        monitorRecord.setEndTime(DateUtil.toDate("2017-08-26 00:00:01"));
        monitorRecordService.insertMonitorRecord(monitorRecord);
        expectedResult = linkAvailabilityService.showIndexAvailability(param);
        assertEquals(true, expectedResult.getIndexAvailable());

        // 首页不可用
        monitorRecord.setResult(1);
        monitorRecord.setEndTime(DateUtil.toDate("2017-08-26 00:00:02"));
        monitorRecordService.insertMonitorRecord(monitorRecord);
        expectedResult = linkAvailabilityService.showIndexAvailability(param);
        assertEquals(false, expectedResult.getIndexAvailable());

        monitorRecord.setResult(11);
        monitorRecord.setEndTime(DateUtil.toDate("2017-08-26 00:00:03"));
        monitorRecordService.insertMonitorRecord(monitorRecord);
        expectedResult = linkAvailabilityService.showIndexAvailability(param);
        assertEquals(false, expectedResult.getIndexAvailable());

        // 不存在监测记录
        param.setSiteId(8269427);
        given(this.siteApiService.getSiteById(param.getSiteId(), null)).willReturn(site);
        monitorRecordService.insertMonitorRecord(monitorRecord);
        expectedResult = linkAvailabilityService.showIndexAvailability(param);
        assertEquals(null, expectedResult.getMonitorTime());
    }
}
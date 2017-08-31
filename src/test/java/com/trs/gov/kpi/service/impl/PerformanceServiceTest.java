package com.trs.gov.kpi.service.impl;

import com.sun.tools.internal.xjc.reader.xmlschema.bindinfo.BIConversion;
import com.trs.gov.kpi.config.TestConfigConst;
import com.trs.gov.kpi.constant.EnumIndexUpdateType;
import com.trs.gov.kpi.entity.InfoUpdate;
import com.trs.gov.kpi.entity.outerapi.sp.SGStatistics;
import com.trs.gov.kpi.entity.requestdata.PageDataRequestParam;
import com.trs.gov.kpi.entity.responsedata.IndexPage;
import com.trs.gov.kpi.entity.responsedata.Statistics;
import com.trs.gov.kpi.service.InfoUpdateService;
import com.trs.gov.kpi.service.LinkAvailabilityService;
import com.trs.gov.kpi.service.MonitorRecordService;
import com.trs.gov.kpi.service.outer.SGService;
import com.trs.gov.kpi.service.outer.SiteApiService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.BDDMockito.given;

/**
 * Created by tao.xiao on 2017/8/31.
 */
@MybatisTest(includeFilters = {@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = {PerformanceService.class})})
@TestPropertySource(properties = {TestConfigConst.TEST_DB_URL_PROP, TestConfigConst.TEST_DB_USER_NAME_PROP, TestConfigConst.TEST_DB_PWD_PROP})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@RunWith(SpringRunner.class)
public class PerformanceServiceTest {

    @MockBean
    LinkAvailabilityService linkAvailabilityService;

    @MockBean
    SGService sgService;

    @MockBean
    InfoUpdateService infoUpdateService;

    @Resource
    private PerformanceService performanceService;

    @Test
    public void calPerformanceIndex() throws Exception {
        PageDataRequestParam param = new PageDataRequestParam();
        param.setSiteId(TestConfigConst.testSiteId);
        // 首页不可用
        IndexPage indexPage = null;
        given(this.linkAvailabilityService.showIndexAvailability(param)).willReturn(indexPage);
        assertEquals(Double.valueOf(62.5), performanceService.calPerformanceIndex(TestConfigConst.testSiteId));
        indexPage = new IndexPage();
        given(this.linkAvailabilityService.showIndexAvailability(param)).willReturn(indexPage);
        indexPage.setIndexAvailable(Boolean.FALSE);
        assertEquals((Double)Double.parseDouble(String.format("%.2f", 62.5)), performanceService.calPerformanceIndex(TestConfigConst.testSiteId));

        // 首页可用
        indexPage.setIndexAvailable(Boolean.TRUE);
        given(this.linkAvailabilityService.showIndexAvailability(param)).willReturn(indexPage);
        given(this.linkAvailabilityService.getUnhandledIssueCount(param)).willReturn(10);
        assertEquals((Double)Double.parseDouble(String.format("%.2f", 100 - 0.375)), performanceService.calPerformanceIndex(TestConfigConst.testSiteId));

        given(this.linkAvailabilityService.getUnhandledIssueCount(param)).willReturn(1000);
        assertEquals((Double)Double.parseDouble(String.format("%.2f", 100 - 37.5)), performanceService.calPerformanceIndex(TestConfigConst.testSiteId));

        // 办事指南得分
        given(this.linkAvailabilityService.getUnhandledIssueCount(param)).willReturn(0);
        SGStatistics sgStatistics = new SGStatistics();
        sgStatistics.setAbandonedCounts(5);
        given(this.sgService.getSGCount(param)).willReturn(sgStatistics);
        assertEquals((Double)Double.parseDouble(String.format("%.2f", 100 - 1.25*5)), performanceService.calPerformanceIndex(TestConfigConst.testSiteId));

        sgStatistics.setAbandonedCounts(500);
        given(this.sgService.getSGCount(param)).willReturn(sgStatistics);
        assertEquals((Double)Double.parseDouble(String.format("%.2f", 100 - 12.5)), performanceService.calPerformanceIndex(TestConfigConst.testSiteId));

        sgStatistics = null;
        given(this.sgService.getSGCount(param)).willReturn(sgStatistics);
        assertEquals((Double)Double.parseDouble(String.format("%.2f", 100.00)), performanceService.calPerformanceIndex(TestConfigConst.testSiteId));

        // 网站信息更新得分
        List<Statistics> statisticsList = new ArrayList<>();
        Statistics statistics1 = new Statistics();
        statistics1.setCount(1);
        statistics1.setType(EnumIndexUpdateType.ALL.getCode());
        statisticsList.add(statistics1);
        Statistics statistics2 = new Statistics();
        statistics2.setCount(1);
        statistics2.setType(EnumIndexUpdateType.NULL_CHANNEL.getCode());
        statisticsList.add(statistics2);
        given(this.infoUpdateService.getUpdateNotInTimeCountList(param)).willReturn(statisticsList);
        assertEquals((Double)Double.parseDouble(String.format("%.2f", 100 - 37.5*0.3)), performanceService.calPerformanceIndex(TestConfigConst.testSiteId));

        statistics2.setCount(10);
        statistics2.setType(EnumIndexUpdateType.NULL_CHANNEL.getCode());
        statisticsList.add(statistics2);
        given(this.infoUpdateService.getUpdateNotInTimeCountList(param)).willReturn(statisticsList);
        assertEquals((Double)Double.parseDouble(String.format("%.2f", 100 - 37.5)), performanceService.calPerformanceIndex(TestConfigConst.testSiteId));
    }
}
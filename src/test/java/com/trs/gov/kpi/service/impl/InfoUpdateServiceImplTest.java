package com.trs.gov.kpi.service.impl;

import com.trs.gov.kpi.config.TestConfigConst;

import com.trs.gov.kpi.entity.outerapi.Dept;
import com.trs.gov.kpi.entity.requestdata.PageDataRequestParam;
import com.trs.gov.kpi.entity.responsedata.*;
import com.trs.gov.kpi.service.DutyDeptService;
import com.trs.gov.kpi.service.InfoUpdateService;
import com.trs.gov.kpi.service.MonitorRecordService;
import com.trs.gov.kpi.service.impl.outer.DocumentApiServiceImpl;
import com.trs.gov.kpi.service.outer.DeptApiService;
import com.trs.gov.kpi.service.outer.SiteApiService;
import com.trs.gov.kpi.service.outer.SiteChannelServiceHelper;
import com.trs.gov.kpi.utils.SpringContextUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.BDDMockito.given;

/**
 * Created by tao.xiao on 2017/8/22.
 */
@MybatisTest(includeFilters = {@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = {InfoUpdateServiceImpl.class, SiteChannelServiceHelper.class, DocumentApiServiceImpl.class})})
@TestPropertySource(properties = {TestConfigConst.TEST_DB_URL_PROP, TestConfigConst.TEST_DB_USER_NAME_PROP, TestConfigConst.TEST_DB_PWD_PROP})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@RunWith(SpringRunner.class)
public class InfoUpdateServiceImplTest {

    @MockBean
    private SiteApiService siteApiService;

    @MockBean
    private DeptApiService deptApiService;

    @MockBean
    private DutyDeptService dutyDeptService;

    @MockBean
    private MonitorRecordService monitorRecordService;

    @Resource
    private InfoUpdateService infoUpdateService;

    @Resource
    private ApplicationContext applicationContext;

    @Before
    public void setup() {
        SpringContextUtil contextUtil = new SpringContextUtil();
        contextUtil.setApplicationContext(applicationContext);
    }

    @Test
    public void getIssueCount() throws Exception {
        PageDataRequestParam param = new PageDataRequestParam();
        param.setSiteId(11);
        List<Statistics> statisticsList = infoUpdateService.getIssueCount(param);
        //已解决问题和预警
        assertEquals(2, statisticsList.get(0).getType());
        assertEquals(24, statisticsList.get(0).getCount());
        //待解决问题
        assertEquals(12, statisticsList.get(1).getType());
        assertEquals(57, statisticsList.get(1).getCount());
        //待解决预警
        assertEquals(21, statisticsList.get(2).getType());
        assertEquals(150, statisticsList.get(2).getCount());

        param.setBeginDateTime("2017-07-01 00:00:00");
        statisticsList = infoUpdateService.getIssueCount(param);
        assertEquals(0, statisticsList.get(0).getCount());

    }

    @Test
    public void getIssueHistoryCount() throws Exception {
        PageDataRequestParam param = new PageDataRequestParam();
        param.setSiteId(11);
        HistoryStatisticsResp historyStatisticsResp = infoUpdateService.getIssueHistoryCount(param);
        //测试七月份统计数量
        HistoryStatistics historyStatistics = (HistoryStatistics) historyStatisticsResp.getData().get(6);
        assertEquals("2017-07", historyStatistics.getTime());
        assertEquals(Integer.valueOf(11), historyStatistics.getValue());
    }

    @Test
    public void getUpdateNotInTimeCountList() throws Exception {
        PageDataRequestParam param = new PageDataRequestParam();
        param.setSiteId(11);
        //模拟递归调用外部接口添加子栏目
        given(this.siteApiService.getAllChildChnlIds(null, param.getSiteId(),
                124, new HashSet<>(Arrays.asList(124))))
                .willReturn(new HashSet<>(Arrays.asList(124)));
        given(this.siteApiService.getAllChildChnlIds(null, param.getSiteId(),
                134, new HashSet<>(Arrays.asList(124, 134))))
                .willReturn(new HashSet<>(Arrays.asList(124, 134)));
        given(this.siteApiService.getAllChildChnlIds(null, param.getSiteId(),
                143, new HashSet<>(Arrays.asList(124, 134, 143))))
                .willReturn(new HashSet<>(Arrays.asList(124, 134, 143)));
        given(this.siteApiService.getAllChildChnlIds(null, param.getSiteId(),
                153, new HashSet<>(Arrays.asList(124, 134, 143, 153))))
                .willReturn(new HashSet<>(Arrays.asList(124, 134, 143, 153)));
        given(this.siteApiService.getAllChildChnlIds(null, param.getSiteId(),
                154, new HashSet<>(Arrays.asList(124, 134, 143, 153, 154))))
                .willReturn(new HashSet<>(Arrays.asList(124, 134, 143, 153, 154)));
        given(this.siteApiService.getAllChildChnlIds(null, param.getSiteId(),
                155, new HashSet<>(Arrays.asList(124, 134, 143, 153, 154, 155))))
                .willReturn(new HashSet<>(Arrays.asList(124, 134, 143, 153, 154, 155)));

        //测试A类
        Statistics statistics = infoUpdateService.getUpdateNotInTimeCountList(param).get(1);
        assertEquals(2, statistics.getType());
        assertEquals(2, statistics.getCount());
    }

    @Test
    public void get() throws Exception {
        given(this.deptApiService.findDeptById("", 1)).willReturn(new Dept());
        PageDataRequestParam param = new PageDataRequestParam();
        param.setSiteId(11);
        param.setPageSize(20);
        param.setPageIndex(1);
        param.setBeginDateTime("2017-05-01 00:00:00");
        param.setEndDateTime("2017-05-10 00:00:00");
        assertEquals(1, infoUpdateService.get(param).getData().size());
    }

    @Test
    public void getNotInTimeCountMonth() throws Exception {
        PageDataRequestParam param = new PageDataRequestParam();
        param.setSiteId(11);
        param.setBeginDateTime("2017-05-01 00:00:00");
        param.setEndDateTime("2017-05-10 00:00:00");

        MonthUpdateResponse monthUpdateResponse = infoUpdateService.getNotInTimeCountMonth(param);
        assertEquals(1, monthUpdateResponse.getUpdateNotInTimeChnl().size());
        assertEquals(1, monthUpdateResponse.getEmptyChnl().size());
    }
}
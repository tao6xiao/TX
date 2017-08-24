package com.trs.gov.kpi.service.impl;

import com.trs.gov.kpi.config.TestConfigConst;

import com.trs.gov.kpi.entity.outerapi.Dept;
import com.trs.gov.kpi.entity.requestdata.PageDataRequestParam;
import com.trs.gov.kpi.entity.responsedata.*;
import com.trs.gov.kpi.service.InfoUpdateService;
import com.trs.gov.kpi.service.outer.DeptApiService;
import com.trs.gov.kpi.service.outer.SiteApiService;
import com.trs.gov.kpi.utils.SpringContextUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.BDDMockito.given;

/**
 * Created by tao.xiao on 2017/8/22.
 */
@SpringBootTest
@TestPropertySource(properties = {TestConfigConst.TEST_DB_URL_PROP, TestConfigConst.TEST_DB_USER_NAME_PROP, TestConfigConst.TEST_DB_PWD_PROP})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@RunWith(SpringRunner.class)
public class InfoUpdateServiceImplTest {

    @MockBean
    private SiteApiService siteApiService;

    @MockBean
    private DeptApiService deptApiService;

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
        assertEquals(statisticsList.get(0).getType(), 2);
        assertEquals(statisticsList.get(0).getCount(), 24);
        //待解决问题
        assertEquals(statisticsList.get(1).getType(), 12);
        assertEquals(statisticsList.get(1).getCount(), 57);
        //待解决预警
        assertEquals(statisticsList.get(2).getType(), 21);
        assertEquals(statisticsList.get(2).getCount(), 150);

        param.setBeginDateTime("2017-07-01 00:00:00");
        statisticsList = infoUpdateService.getIssueCount(param);
        assertEquals(statisticsList.get(0).getCount(), 0);

    }

    @Test
    public void getIssueHistoryCount() throws Exception {
        PageDataRequestParam param = new PageDataRequestParam();
        param.setSiteId(11);
        HistoryStatisticsResp historyStatisticsResp = infoUpdateService.getIssueHistoryCount(param);
        //测试七月份统计数量
        HistoryStatistics historyStatistics = (HistoryStatistics) historyStatisticsResp.getData().get(6);
        assertEquals(historyStatistics.getTime(), "2017-07");
        assertEquals(historyStatistics.getValue(), Integer.valueOf(11));
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
        assertEquals(statistics.getType(), 2);
        assertEquals(statistics.getCount(), 2);
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
        assertEquals(infoUpdateService.get(param).getData().size(), 1);
    }

    @Test
    public void getNotInTimeCountMonth() throws Exception {
        PageDataRequestParam param = new PageDataRequestParam();
        param.setSiteId(11);
        param.setBeginDateTime("2017-06-24 00:00:00");
        param.setEndDateTime("2017-06-24 23:59:00");
        MonthUpdateResponse monthUpdateResponse = infoUpdateService.getNotInTimeCountMonth(param);
        assertEquals(monthUpdateResponse.getUpdateNotInTimeChnl().size(), 7);
        assertEquals(monthUpdateResponse.getEmptyChnl().size(), 0);

        param.setBeginDateTime("2017-05-01 00:00:00");
        param.setEndDateTime("2017-05-10 00:00:00");
        monthUpdateResponse = infoUpdateService.getNotInTimeCountMonth(param);
        assertEquals(monthUpdateResponse.getUpdateNotInTimeChnl().size(), 1);
        assertEquals(monthUpdateResponse.getEmptyChnl().size(), 1);
    }
}
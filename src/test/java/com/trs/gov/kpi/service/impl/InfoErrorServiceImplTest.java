package com.trs.gov.kpi.service.impl;

import com.trs.gov.kpi.config.TestConfigConst;
import com.trs.gov.kpi.entity.outerapi.Dept;
import com.trs.gov.kpi.entity.requestdata.PageDataRequestParam;
import com.trs.gov.kpi.entity.responsedata.HistoryStatistics;
import com.trs.gov.kpi.entity.responsedata.HistoryStatisticsResp;
import com.trs.gov.kpi.entity.responsedata.Statistics;
import com.trs.gov.kpi.service.InfoErrorService;
import com.trs.gov.kpi.service.outer.DeptApiService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.BDDMockito.given;

/**
 * Created by tao.xiao on 2017/8/23.
 */
@SpringBootTest
@TestPropertySource(properties = {TestConfigConst.TEST_DB_URL_PROP, TestConfigConst.TEST_DB_USER_NAME_PROP, TestConfigConst.TEST_DB_PWD_PROP})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@RunWith(SpringRunner.class)

public class InfoErrorServiceImplTest {

    @Resource
    private InfoErrorService infoErrorService;

    @MockBean
    private DeptApiService deptApiService;

    @Test
    public void getIssueCount() throws Exception {
        PageDataRequestParam param = new PageDataRequestParam();
        param.setSiteId(11);
        List<Statistics> testList = infoErrorService.getIssueCount(param);
        //已处理测试
        assertEquals(testList.get(0).getCount(), 16);
        //未处理测试
        assertEquals(testList.get(1).getCount(), 76);

    }

    @Test
    public void getIssueHistoryCount() throws Exception {
        PageDataRequestParam param = new PageDataRequestParam();
        param.setSiteId(11);
        HistoryStatisticsResp historyStatisticsResp = infoErrorService.getIssueHistoryCount(param);
        //五月数据测试
        HistoryStatistics historyStatisticsTest = (HistoryStatistics) historyStatisticsResp.getData().get(4);
        assertEquals(historyStatisticsTest.getValue(), Integer.valueOf(10));
        //六月数据测试
        historyStatisticsTest = (HistoryStatistics) historyStatisticsResp.getData().get(5);
        assertEquals(historyStatisticsTest.getValue(), Integer.valueOf(82));
    }

    @Test
    public void getInfoErrorList() throws Exception {
        PageDataRequestParam param = new PageDataRequestParam();
        param.setSiteId(11);

        //模拟空数据，防止报空指针错误
        given(this.deptApiService.findDeptById("", 201)).willReturn(new Dept());

        assertEquals(infoErrorService.getInfoErrorList(param).getPager().getItemCount(), Integer.valueOf(76));
;
    }

}
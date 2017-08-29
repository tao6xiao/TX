package com.trs.gov.kpi.service.impl;

import com.trs.gov.kpi.config.TestConfigConst;
import com.trs.gov.kpi.constant.Status;
import com.trs.gov.kpi.constant.Types;
import com.trs.gov.kpi.dao.IssueMapper;
import com.trs.gov.kpi.entity.Issue;
import com.trs.gov.kpi.entity.outerapi.Dept;
import com.trs.gov.kpi.entity.requestdata.PageDataRequestParam;
import com.trs.gov.kpi.entity.responsedata.HistoryStatistics;
import com.trs.gov.kpi.entity.responsedata.HistoryStatisticsResp;
import com.trs.gov.kpi.entity.responsedata.Statistics;
import com.trs.gov.kpi.service.InfoErrorService;
import com.trs.gov.kpi.service.impl.outer.SiteApiServiceImpl;
import com.trs.gov.kpi.service.outer.DeptApiService;
import com.trs.gov.kpi.utils.DBUtil;
import com.trs.gov.kpi.utils.DateUtil;
import org.junit.Before;
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

import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.BDDMockito.given;

/**
 * Created by tao.xiao on 2017/8/23.
 */
@MybatisTest(includeFilters = {@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = {InfoErrorServiceImpl.class, SiteApiServiceImpl.class, MonitorRecordServiceImpl.class})})
@TestPropertySource(properties = {TestConfigConst.TEST_DB_URL_PROP, TestConfigConst.TEST_DB_USER_NAME_PROP, TestConfigConst.TEST_DB_PWD_PROP})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@RunWith(SpringRunner.class)

public class InfoErrorServiceImplTest {

    @Resource
    private InfoErrorService infoErrorService;

    @MockBean
    private DeptApiService deptApiService;

    @Resource
    IssueMapper issueMapper;

    @Rollback
    @Before
    public void addTestData() throws Exception{

        Issue issue = new Issue();
        issue.setSiteId(TestConfigConst.testSiteId);

        issue.setTypeId(Types.IssueType.INFO_ERROR_ISSUE.value);
        issue.setSubTypeId(Types.InfoErrorIssueType.SENSITIVE_WORDS.value);
        issue.setIsResolved(Status.Resolve.RESOLVED.value);
        issue.setIssueTime(DateUtil.toDate("2017-08-28 00:00:00"));
        issueMapper.insert(DBUtil.toRow(issue));

        issue.setIsResolved(Status.Resolve.IGNORED.value);
        issue.setIssueTime(DateUtil.toDate("2017-08-26 00:00:00"));
        issueMapper.insert(DBUtil.toRow(issue));

        issue.setIsResolved(Status.Resolve.UN_RESOLVED.value);
        issueMapper.insert(DBUtil.toRow(issue));

        issue.setIssueTime(DateUtil.toDate("2017-07-26 00:00:00"));
        issueMapper.insert(DBUtil.toRow(issue));

    }

    @Test
    public void getIssueCount() throws Exception {
        PageDataRequestParam param = new PageDataRequestParam();
        param.setSiteId(TestConfigConst.testSiteId);
        List<Statistics> testList = infoErrorService.getIssueCount(param);
        //已处理测试
        assertEquals(2, testList.get(0).getCount());
        //未处理测试
        assertEquals(2, testList.get(1).getCount());

    }

    @Test
    public void getIssueHistoryCount() throws Exception {
        PageDataRequestParam param = new PageDataRequestParam();
        //param.setBeginDateTime("2017-01-01");
        param.setSiteId(TestConfigConst.testSiteId);
        HistoryStatisticsResp historyStatisticsResp = infoErrorService.getIssueHistoryCount(param);
        //七月数据测试
        HistoryStatistics historyStatisticsTest = (HistoryStatistics) historyStatisticsResp.getData().get(6);
        assertEquals(Integer.valueOf(1), historyStatisticsTest.getValue());
        //八月数据测试
        historyStatisticsTest = (HistoryStatistics) historyStatisticsResp.getData().get(7);
        assertEquals(Integer.valueOf(3), historyStatisticsTest.getValue());
    }

    @Test
    public void getInfoErrorList() throws Exception {
        PageDataRequestParam param = new PageDataRequestParam();
        param.setSiteId(TestConfigConst.testSiteId);

        //模拟空数据，防止报空指针错误
        given(this.deptApiService.findDeptById("", 1)).willReturn(new Dept());

        assertEquals(Integer.valueOf(2), infoErrorService.getInfoErrorList(param).getPager().getItemCount());

    }
}
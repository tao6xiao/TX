package com.trs.gov.kpi.service.impl;

import com.trs.gov.kpi.config.TestConfigConst;
import com.trs.gov.kpi.dao.DutyDeptMapper;
import com.trs.gov.kpi.entity.DutyDept;
import com.trs.gov.kpi.entity.outerapi.Channel;
import com.trs.gov.kpi.entity.outerapi.Dept;
import com.trs.gov.kpi.entity.requestdata.DutyDeptRequest;
import com.trs.gov.kpi.entity.requestdata.PageDataRequestParam;
import com.trs.gov.kpi.entity.responsedata.ApiPageData;
import com.trs.gov.kpi.service.DutyDeptService;
import com.trs.gov.kpi.service.outer.DeptApiService;
import com.trs.gov.kpi.service.outer.SiteApiService;
import com.trs.gov.kpi.utils.DBUtil;
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
 * Created by tao.xiao on 2017/8/23.
 */
@MybatisTest(includeFilters = {@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = {DutyDeptServiceImpl.class})})
@TestPropertySource(properties = {TestConfigConst.TEST_DB_URL_PROP, TestConfigConst.TEST_DB_USER_NAME_PROP, TestConfigConst.TEST_DB_PWD_PROP})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@RunWith(SpringRunner.class)
public class DutyDeptServiceImplTest {

//    @InjectMocks
//    private DutyDeptServiceImpl dutyDeptService;

//    @Mock
//    private IssueMapper issueMapper;

    @Resource
    private DutyDeptMapper deptMapper;

    @Resource
    private DutyDeptService dutyDeptService;

    @MockBean
    private SiteApiService siteApiService;

    @MockBean
    private DeptApiService deptApiService;

    @Test
    @Rollback
    public void getByChnlId_empty() throws Exception {
        // 测试不存在的情况
        int chnlId = 1111111111;
        assertEquals(null, dutyDeptService.getByChnlId(chnlId, DutyDept.ALL_CONTAIN_COND));
        assertEquals(null, dutyDeptService.getByChnlId(chnlId, DutyDept.NOT_CONTAIN_CHILD));
        assertEquals(null, dutyDeptService.getByChnlId(chnlId, DutyDept.CONTAIN_CHILD));
        assertEquals(null, dutyDeptService.getByChnlId(chnlId, (byte)3));
    }

    @Test
    @Rollback
    public void getByChnlId_containChild() throws Exception {
        int chnlId = 1111111111;
        // 测试包含子栏目，且存在
        DutyDept dept = new DutyDept();
        dept.setChnlId(chnlId);
        dept.setSiteId(22222222);
        dept.setContain(DutyDept.CONTAIN_CHILD);
        dept.setDeptId(33);
        deptMapper.insert(DBUtil.toRow(dept));
        assertEquals(dept, dutyDeptService.getByChnlId(chnlId, DutyDept.ALL_CONTAIN_COND));
        assertEquals(null, dutyDeptService.getByChnlId(chnlId, DutyDept.NOT_CONTAIN_CHILD));
        assertEquals(dept, dutyDeptService.getByChnlId(chnlId, DutyDept.CONTAIN_CHILD));
        assertEquals(null, dutyDeptService.getByChnlId(chnlId, (byte)3));
    }

    @Test
    @Rollback
    public void getByChnlId_notContainChild() throws Exception {
        int chnlId = 1111111111;
        // 测试包含子栏目，且存在
        DutyDept dept = new DutyDept();
        dept.setChnlId(chnlId);
        dept.setSiteId(22222222);
        dept.setContain(DutyDept.NOT_CONTAIN_CHILD);
        dept.setDeptId(33);
        deptMapper.insert(DBUtil.toRow(dept));
        assertEquals(dept, dutyDeptService.getByChnlId(chnlId, DutyDept.ALL_CONTAIN_COND));
        assertEquals(dept, dutyDeptService.getByChnlId(chnlId, DutyDept.NOT_CONTAIN_CHILD));
        assertEquals(null, dutyDeptService.getByChnlId(chnlId, DutyDept.CONTAIN_CHILD));
        assertEquals(null, dutyDeptService.getByChnlId(chnlId, (byte)3));
    }

    @Test
    @Rollback
    public void getByChnlId_allContain() throws Exception {
        int chnlId = 1111111111;
        // 测试包含子栏目，且存在
        DutyDept dept = new DutyDept();
        dept.setChnlId(chnlId);
        dept.setSiteId(22222222);
        dept.setContain(DutyDept.CONTAIN_CHILD);
        dept.setDeptId(33);
        deptMapper.insert(DBUtil.toRow(dept));

        DutyDept dept2 = new DutyDept();
        dept2.setChnlId(chnlId+1);
        dept2.setSiteId(22222222);
        dept2.setContain(DutyDept.NOT_CONTAIN_CHILD);
        dept2.setDeptId(33);
        deptMapper.insert(DBUtil.toRow(dept2));

        assertEquals(dept, dutyDeptService.getByChnlId(chnlId, DutyDept.ALL_CONTAIN_COND));
        assertEquals(dept2, dutyDeptService.getByChnlId(chnlId+1, DutyDept.ALL_CONTAIN_COND));
        assertEquals(null, dutyDeptService.getByChnlId(chnlId, (byte)3));
    }


    @Test
    public void get() throws Exception {

        DutyDept dutyDept = new DutyDept();
        dutyDept.setChnlId(124);
        dutyDept.setDeptId(169);

        given(this.siteApiService.getChannelById(dutyDept.getChnlId(), "")).willReturn(new Channel());
        given(this.deptApiService.findDeptById("", dutyDept.getDeptId())).willReturn(new Dept());

        PageDataRequestParam param = new PageDataRequestParam();
        param.setSiteId(11);
        param.setPageIndex(1);
        param.setPageSize(5);

        ApiPageData apiPageData = dutyDeptService.get(param);

        DutyDept dutyDeptTest = (DutyDept)apiPageData.getData().get(0);
        assertEquals(dutyDeptTest.getChnlId(), Integer.valueOf(124));
        assertEquals(dutyDeptTest.getDeptId(), Integer.valueOf(169));
        DutyDept nullDutyDeptTest = (DutyDept)apiPageData.getData().get(1);
        assertEquals(nullDutyDeptTest.getDeptId(), null);

        assertEquals(apiPageData.getData().size(), 3);
    }

}
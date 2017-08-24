package com.trs.gov.kpi.service.impl;

import com.trs.gov.kpi.config.TestConfigConst;
import com.trs.gov.kpi.entity.DutyDept;
import com.trs.gov.kpi.entity.outerapi.Channel;
import com.trs.gov.kpi.entity.outerapi.Dept;
import com.trs.gov.kpi.entity.requestdata.DutyDeptRequest;
import com.trs.gov.kpi.entity.requestdata.PageDataRequestParam;
import com.trs.gov.kpi.entity.responsedata.ApiPageData;
import com.trs.gov.kpi.service.DutyDeptService;
import com.trs.gov.kpi.service.outer.DeptApiService;
import com.trs.gov.kpi.service.outer.SiteApiService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

import java.util.ArrayList;
import java.util.Arrays;
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
public class DutyDeptServiceImplTest {

    @Resource
    private DutyDeptService dutyDeptService;

    @MockBean
    private SiteApiService siteApiService;

    @MockBean
    private DeptApiService deptApiService;

    @Test
    public void getByChnlId() throws Exception {
        DutyDeptRequest deptRequest = new DutyDeptRequest();
        deptRequest.setDeptId(2);
        assertNotEquals(dutyDeptService.getByChnlId(deptRequest.getDeptId(), DutyDept.ALL_CONTAIN_COND), null);
        deptRequest.setDeptId(2);
        assertEquals(dutyDeptService.getByChnlId(deptRequest.getDeptId(), DutyDept.NOT_CONTAIN_CHILD), null);
        deptRequest.setDeptId(2);
        assertNotEquals(dutyDeptService.getByChnlId(deptRequest.getDeptId(), DutyDept.CONTAIN_CHILD), null);
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
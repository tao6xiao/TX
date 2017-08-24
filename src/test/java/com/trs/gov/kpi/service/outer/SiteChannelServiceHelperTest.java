package com.trs.gov.kpi.service.outer;

import com.trs.gov.kpi.config.TestConfigConst;
import com.trs.gov.kpi.service.impl.DutyDeptServiceImpl;
import com.trs.gov.kpi.service.impl.InfoUpdateServiceImpl;
import com.trs.gov.kpi.service.impl.outer.DeptApiServiceImpl;
import com.trs.gov.kpi.service.impl.outer.DocumentApiServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.Arrays;

import static org.junit.Assert.*;
import static org.mockito.BDDMockito.given;

/**
 * Created by tao.xiao on 2017/8/23.
 */
@MybatisTest(includeFilters = {@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = {DutyDeptServiceImpl.class, SiteChannelServiceHelper.class, DeptApiServiceImpl.class, DocumentApiServiceImpl.class})})
@TestPropertySource(properties = {TestConfigConst.TEST_DB_URL_PROP, TestConfigConst.TEST_DB_USER_NAME_PROP, TestConfigConst.TEST_DB_PWD_PROP})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@RunWith(SpringRunner.class)
public class SiteChannelServiceHelperTest {

    @MockBean
    private SiteApiService siteApiService;

    @Resource
    SiteChannelServiceHelper siteChannelServiceHelper;

    @Test
    public void findRelatedDept() throws Exception {
        int chnlId = 1274;
        int parentChnlId = 1244;
        //183为父栏目部门ID
        given(this.siteApiService.findChannelPath(chnlId, "")).willReturn(Arrays.asList(chnlId, parentChnlId));
        assertEquals(siteChannelServiceHelper.findRelatedDept(parentChnlId, ""), Integer.valueOf(183));
    }

}
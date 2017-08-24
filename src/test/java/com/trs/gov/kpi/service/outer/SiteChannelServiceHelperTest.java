package com.trs.gov.kpi.service.outer;

import com.trs.gov.kpi.config.TestConfigConst;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.Arrays;

import static org.junit.Assert.*;
import static org.mockito.BDDMockito.given;

/**
 * Created by tao.xiao on 2017/8/23.
 */
@SpringBootTest
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
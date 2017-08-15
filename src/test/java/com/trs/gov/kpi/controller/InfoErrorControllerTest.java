package com.trs.gov.kpi.controller;

import com.trs.gov.kpi.constant.Authority;
import com.trs.gov.kpi.constant.IssueIndicator;
import com.trs.gov.kpi.entity.LocalUser;
import com.trs.gov.kpi.entity.requestdata.PageDataRequestParam;
import com.trs.gov.kpi.entity.responsedata.Statistics;
import com.trs.gov.kpi.service.InfoErrorService;
import com.trs.gov.kpi.service.IssueService;
import com.trs.gov.kpi.service.outer.AuthorityService;
import com.trs.gov.kpi.service.outer.SiteApiService;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by ranwei on 2017/6/8.
 */
@RunWith(SpringRunner.class)
@WebMvcTest(controllers = InfoErrorController.class,
        excludeFilters = {@ComponentScan.Filter(type = FilterType.REGEX, pattern="com.trs.*.TLFilter|com.trs.*.RedisSessionConfig")})
@TestPropertySource(properties = {
        "spring.datasource.url=jdbc:mysql://59.110.25.145:3306/kpidb?useUnicode=true&characterEncoding=UTF-8&useSSL=false&allowMultiQueries=true",
        "spring.datasource.username=admin",
        "spring.datasource.password=trs@mlf.028"})
@Transactional
public class InfoErrorControllerTest {

    @MockBean
    private IssueService issueService;

    @MockBean
    private SiteApiService siteApiService;

    @MockBean
    private InfoErrorService infoErrorService;

    @MockBean
    private AuthorityService authorityService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @Rollback
    public void getIssueCount() throws Exception {
        PageDataRequestParam param = new PageDataRequestParam();
        param.setSiteId(11);

        Mockito.doNothing().when(this.authorityService).checkRight(Authority.KPIWEB_INFOERROR_SEARCH, 11);

        List<Statistics> expectedResult = new ArrayList<>();
        Statistics handledIssueStatistics = new Statistics();
        handledIssueStatistics.setCount(10);
        handledIssueStatistics.setType(IssueIndicator.SOLVED.value);
        handledIssueStatistics.setName(IssueIndicator.SOLVED.getName());
        expectedResult.add(handledIssueStatistics);
        given(this.infoErrorService.getIssueCount(param)).willReturn(expectedResult);

        mockMvc.perform(get("/gov/kpi/content/issue/bytype/count").param("siteId", "11"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(content().json("{\"data\":[{\"count\":10,\"name\":\"已解决\",\"type\":1}],\"isSuccess\":true,\"msg\":\"操作成功\"}"));
    }

    @Test
    public void getIssueHistoryCount() throws Exception {

    }

    @Test
    public void getIssueList() throws Exception {

    }

    @Test
    public void handIssuesByIds() throws Exception {

    }

    @Test
    public void ignoreIssuesByIds() throws Exception {

    }

    @Test
    public void delIssueByIds() throws Exception {

    }

}
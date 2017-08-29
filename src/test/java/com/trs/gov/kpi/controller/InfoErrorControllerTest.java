package com.trs.gov.kpi.controller;

import com.trs.gov.kpi.constant.Authority;
import com.trs.gov.kpi.constant.IssueIndicator;
import com.trs.gov.kpi.entity.requestdata.PageDataRequestParam;
import com.trs.gov.kpi.entity.responsedata.*;
import com.trs.gov.kpi.service.InfoErrorService;
import com.trs.gov.kpi.service.IssueService;
import com.trs.gov.kpi.service.outer.AuthorityService;
import com.trs.gov.kpi.service.outer.SiteApiService;
import com.trs.gov.kpi.utils.DateUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Date;
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
        PageDataRequestParam param = new PageDataRequestParam();
        param.setSiteId(11);

        Mockito.doNothing().when(this.authorityService).checkRight(Authority.KPIWEB_INFOERROR_SEARCH, 11);

        List<HistoryStatistics> historyStatisticsList = new ArrayList<>();
        HistoryStatistics monStatistics = new HistoryStatistics();
        monStatistics.setTime("2017-01");
        monStatistics.setValue(10);
        historyStatisticsList.add(monStatistics);
        HistoryStatistics febthStatistics = new HistoryStatistics();
        febthStatistics.setTime("2017-02");
        febthStatistics.setValue(11);
        historyStatisticsList.add(febthStatistics);
        Date checkTime = DateUtil.toDate("2017-02-25 00:00:00");
        HistoryStatisticsResp expectedResult = new HistoryStatisticsResp(checkTime, historyStatisticsList);

        given(this.infoErrorService.getIssueHistoryCount(param)).willReturn(expectedResult);

        mockMvc.perform(get("/gov/kpi/content/issue/all/count/history").contentType("application/json;charset=UTF-8").param("siteId", "11"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(content().json("{\"data\":{\"checkTime\":1487952000000," +
                        "\"data\":[{\"time\":\"2017-01\",\"value\":10}," +
                        "{\"time\":\"2017-02\",\"value\":11}]}," +
                        "\"isSuccess\":true,\"msg\":\"操作成功\"}"));
    }
}
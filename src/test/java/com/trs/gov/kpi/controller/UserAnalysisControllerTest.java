package com.trs.gov.kpi.controller;

import com.trs.gov.kpi.entity.requestdata.BasRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.Date;

/**
 * Created by li.hao on 2017/6/20.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class UserAnalysisControllerTest {

    @Resource
    UserAnalysisController userAnalysisController;

    @Test
    public void getVisits() throws Exception {

        BasRequest basRequest = new BasRequest();
        basRequest.setSiteId(2);
        basRequest.setDateTime(new Date());
        userAnalysisController.getVisits(basRequest);
    }

    @Test
    public void getHistoryVisits() throws Exception {

        BasRequest basRequest = new BasRequest();
        basRequest.setSiteId(2);
        basRequest.setDateTime(new Date());
        userAnalysisController.getHistoryVisits(basRequest);
    }

    @Test
    public void getStayTime() throws Exception {

        BasRequest basRequest = new BasRequest();
        basRequest.setSiteId(2);
        basRequest.setDateTime(new Date());
        userAnalysisController.getStayTime(basRequest);
    }

    @Test
    public void geHistoryStayTime() throws Exception {

        BasRequest basRequest = new BasRequest();
        basRequest.setSiteId(2);
        basRequest.setDateTime(new Date());
//        userAnalysisController.geHistoryStayTime(basRequest);
    }

}
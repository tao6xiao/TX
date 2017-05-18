package com.trs.gov.kpi.service.impl.outer;

import org.junit.Test;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Created by linwei on 2017/5/18.
 */
public class SiteApiServiceImplTest {
    @Test
    public void getRequestUrl() throws Exception {

        SiteApiServiceImpl apiService = new SiteApiServiceImpl();
        Map<String, String> params = new HashMap<>();
        params.put("param1", "1");
        params.put("param2", "2");
        String url = apiService.getRequestUrl("test_func", "", params);
        String expectedUrl = "http://dev3.trs.org.cn/gov/opendata.do?serviceId=gov_site&methodname=test_func&CurrUserName=admin&param1=1&param2=2";
        assertEquals(expectedUrl, url);
    }

}
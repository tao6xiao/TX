package com.trs.gov.kpi.service.impl.outer;

import com.squareup.okhttp.Request;
import com.trs.gov.kpi.service.outer.OuterApiService;
import com.trs.gov.kpi.utils.OuterApiServiceUtil;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Value;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Created by linwei on 2017/5/18.
 */
public class SiteApiServiceImplTest {

    @Value("${service.outer.editcenter.url}")
    private String editCenterServiceUrl;

    @Test
    public void getRequestUrl() throws Exception {

        SiteApiServiceImpl apiService = new SiteApiServiceImpl();
        Map<String, String> params = new HashMap<>();
        params.put("param1", "1");
        params.put("param2", "2");

        // TODO fix test
//        Request req = new OuterApiServiceUtil.ServiceRequestBuilder()
//                .setParams(params)
//                .setMethodName("test_func")
//                .setServiceName("gov_site")
//                .setUrlFormat("%s/gov/opendata.do?serviceId=%s&methodname=%s&CurrUserName=%s")
//                .setUserName("").build();
//
//        String expectedUrl = editCenterServiceUrl + "/gov/opendata.do?serviceId=gov_site&methodname=test_func&CurrUserName=admin&param1=1&param2=2";
//        assertEquals(expectedUrl, req.urlString());
    }

}
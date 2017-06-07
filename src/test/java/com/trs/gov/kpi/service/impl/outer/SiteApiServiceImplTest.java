package com.trs.gov.kpi.service.impl.outer;

import com.squareup.okhttp.Request;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static com.trs.gov.kpi.utils.OuterApiServiceUtil.newServiceRequestBuilder;
import static org.junit.Assert.assertEquals;

/**
 * Created by linwei on 2017/5/18.
 */
public class SiteApiServiceImplTest {

    @Test
    public void getRequestUrl() throws Exception {

        Map<String, String> params = new HashMap<>();
        params.put("param1", "1");
        params.put("param2", "2");

        Request req = newServiceRequestBuilder()
                .setUrlFormat("%s/gov/opendata.do?serviceId=%s&methodname=%s&CurrUserName=%s")
                .setServiceUrl("http://www.baidu.com")
                .setServiceName("testService")
                .setMethodName("testMethod")
                .setUserName("testUser")
                .setParams(params).build();
        String expectedUrl = "http://www.baidu.com/gov/opendata.do?serviceId=testService&methodname=testMethod&CurrUserName=testUser&param1=1&param2=2";
        assertEquals(expectedUrl, req.urlString());
    }

}
package com.trs.gov.kpi.entity.outerapi;

import com.alibaba.fastjson.JSON;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by linwei on 2017/5/18.
 */
public class ApiResultTest {

    private String testJson = "{\n" +
            "    \"ISSUCCESS\":\"true\",\n" +
            "    \"MSG\":\"操作成功\",\n" +
            "    \"DATA\":{\n" +
            "        \"SITEDESC\" : \"TRS演示站点\",\n" +
            "        \"CRUSER\" : \"admin\",\n" +
            "        \"HASCHILDREN\" : \"true\",\n" +
            "        \"SITEORDER\" : \"3\",\n" +
            "        \"MEDIATYPE\" : \"1\",\n" +
            "        \"SCHEDULE\" : \"0\",\n" +
            "        \"OPERTIME\" : \"\",\n" +
            "        \"DATAPATH\" : \"wcmdemo1\",\n" +
            "        \"STATUS\" : \"0\",\n" +
            "        \"ATTRIBUTE\" : \"\",\n" +
            "        \"CRTIME\" : \"2009-07-21 11:53:29\",\n" +
            "        \"ISSUBSCRIBE\" : \"1\",\n" +
            "        \"LASTMODIFYTIME\" : \"2017-05-12 16:51:57\",\n" +
            "        \"SITENAME\" : \"TRS演示站点\",\n" +
            "        \"TRUENAME\" : \"系统管理员\",\n" +
            "        \"SITEID\" : \"1\",\n" +
            "        \"PUBLISHPRO\" : \"1\",\n" +
            "        \"PARENTID\" : \"0\",\n" +
            "        \"ISMOBILE\" : \"0\",\n" +
            "        \"SITETYPE\" : \"0\"\n" +
            "    }\n" +
            "}";

    @Test
    public void testParseObj() throws Exception {
        ApiResult resultObj = JSON.parseObject(testJson, ApiResult.class);
        assertTrue(resultObj.isOk());
    }

    @Test
    public void testParseEmptyData() throws Exception {
        ApiResult resultObj = JSON.parseObject("{\n" +
                "    \"ISSUCCESS\":\"true\",\n" +
                "    \"MSG\":\"操作成功\"}", ApiResult.class);
        assertEquals(null, resultObj.getData());
    }

    @Test
    public void testParseSiteData() throws Exception {
        ApiResult resultObj = JSON.parseObject(testJson, ApiResult.class);
        Site site = JSON.parseObject(resultObj.getData(), Site.class);
        assertEquals(1, site.getSiteId());
    }

}
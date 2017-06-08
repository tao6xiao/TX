package com.trs.gov.kpi.utils;

import com.trs.gov.kpi.entity.requestdata.IssueCountRequest;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by he.lang on 2017/6/8.
 */
public class ParamCheckUtilTest {
    @Test
    public void paramCheck() throws Exception {
        IssueCountRequest request = new IssueCountRequest();
        request.setBeginDateTime("2017-06-07 00:00:00");
        request.setEndDateTime("2017-06-08 00:00:00");
        request.setSiteIds("1,2,3");
        ParamCheckUtil.paramCheck(request);
        assertTrue(true);
    }

}
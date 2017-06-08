package com.trs.gov.kpi.utils;

import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.assertEquals;

/**
 * Created by ranwei on 2017/6/8.
 */
public class InitTimeTest {
    @Test
    public void initBeginDateTime_Not_Null() throws Exception {
        String beginDateTime = "2017-06-08 00:00:00";
        Date earliestTime = DateUtil.toDate("2017-05-05 00:00:00");
        beginDateTime = InitTime.initBeginDateTime(beginDateTime, earliestTime);
        assertEquals("2017-06-08 00:00:00", beginDateTime);
    }

    @Test
    public void initBeginDateTime_Null() throws Exception {
        String beginDateTime = null;
        Date earliestTime = DateUtil.toDate("2017-05-05 00:00:00");
        beginDateTime = InitTime.initBeginDateTime(beginDateTime, earliestTime);
        assertEquals("2017-05-05 00:00:00", beginDateTime);
    }

    @Test
    public void initEndDateTime_Not_Null() throws Exception {
        String endDateTime = "2017-05-05 00:00:00";
        endDateTime = InitTime.initEndDateTime(endDateTime);
        assertEquals("2017-05-05 00:00:00", endDateTime);
    }

    @Test
    public void initEndDateTime_Null() throws Exception {
        String endDateTime = null;
        endDateTime = InitTime.initEndDateTime(endDateTime);
        assertEquals(DateUtil.toString(new Date()), endDateTime);
    }

}
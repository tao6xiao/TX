package com.trs.gov.kpi.utils;

import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.*;

/**
 * Created by yuanyuan on 2017/6/3.
 */
public class DateUtilTest {
    @Test
    public void nearestPeriodBeginDate() throws Exception {
        Date testDate = DateUtil.toDate("2017-05-23 00:00:00");
        Date result = DateUtil.nearestPeriodBeginDate(testDate, "2017-05-01 00:00:00", 1);
        assertEquals("2017-05-23 00:00:00", DateUtil.toString(result));

        result = DateUtil.nearestPeriodBeginDate(testDate, "2017-05-22 00:00:00", 1);
        assertEquals("2017-05-23 00:00:00", DateUtil.toString(result));

        result = DateUtil.nearestPeriodBeginDate(testDate, "2017-05-23 00:00:00", 2);
        assertEquals("2017-05-23 00:00:00", DateUtil.toString(result));

        result = DateUtil.nearestPeriodBeginDate(testDate, "2017-05-22 00:00:00", 2);
        assertEquals("2017-05-22 00:00:00", DateUtil.toString(result));

        result = DateUtil.nearestPeriodBeginDate(testDate, "2017-05-21 00:00:00", 3);
        assertEquals("2017-05-21 00:00:00", DateUtil.toString(result));

        result = DateUtil.nearestPeriodBeginDate(testDate, "2017-05-19 00:00:00", 3);
        assertEquals("2017-05-22 00:00:00", DateUtil.toString(result));

        // 不是整天的情况
        result = DateUtil.nearestPeriodBeginDate(DateUtil.toDate("2017-05-23 00:00:00"), "2017-05-01 03:10:05", 1);
        assertEquals("2017-05-22 03:10:05", DateUtil.toString(result));

        result = DateUtil.nearestPeriodBeginDate(DateUtil.toDate("2017-05-23 00:00:00"), "2017-05-01 03:10:05", 2);
        assertEquals("2017-05-21 03:10:05", DateUtil.toString(result));
    }

}
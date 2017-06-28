package com.trs.gov.kpi.utils;

import com.trs.gov.kpi.entity.HistoryDate;
import org.junit.Test;

import java.util.Date;
import java.util.List;

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

    @Test
    public void isValidMonth() {
        assertTrue(DateUtil.isValidMonth("2017-02"));
        assertFalse(DateUtil.isValidMonth("2017-13"));
    }


    @Test
    public void splitDate() throws Exception {

        //开始时间大于结束时间
        List<HistoryDate> result = DateUtil.splitDate("2017-06-01 00:00:00", "2017-05-19 00:00:00", null);
        assertEquals(0, result.size());

        //默认粒度
        result = DateUtil.splitDate("2016-11-30 12:05:00", "2017-02-28 19:05:00", null);
        assertEquals(4, result.size());

        //开始时间和结束时间都处于第一个周期
        result = DateUtil.splitDate("2017-06-06 00:00:00", "2017-06-09 00:00:00", 2);
        assertEquals(1, result.size());
        result = DateUtil.splitDate("2017-06-06 00:00:00", "2017-06-09 00:00:00", 3);
        assertEquals(1, result.size());
        result = DateUtil.splitDate("2017-06-06 00:00:00", "2017-06-09 00:00:00", 4);
        assertEquals(1, result.size());

        //跨多个周期
        result = DateUtil.splitDate("2017-06-01 12:05:00", "2017-06-20 08:00:00", 1);
        assertEquals(20, result.size());
        result = DateUtil.splitDate("2017-06-01 12:05:00", "2017-07-30 08:00:00", 2);
        assertEquals(10, result.size());
        result = DateUtil.splitDate("2017-04-30 12:05:00", "2017-07-30 08:00:00", 3);
        assertEquals(4, result.size());
        result = DateUtil.splitDate("2015-04-30 12:05:00", "2017-07-30 08:00:00", 4);
        assertEquals(3, result.size());

        //起止时间在同一天
        result = DateUtil.splitDate("2017-04-30 12:05:00", "2017-04-30 19:05:00", 1);
        assertEquals(1, result.size());
        result = DateUtil.splitDate("2017-04-30 12:05:00", "2017-04-30 19:05:00", 2);
        assertEquals(1, result.size());
        result = DateUtil.splitDate("2017-04-30 12:05:00", "2017-04-30 19:05:00", 3);
        assertEquals(1, result.size());
        result = DateUtil.splitDate("2017-04-30 12:05:00", "2017-04-30 19:05:00", 4);
        assertEquals(1, result.size());

        //临界点
        result = DateUtil.splitDate("2017-06-03 12:05:00", "2017-06-10 19:05:00", 2);
        assertEquals(2, result.size());
        result = DateUtil.splitDate("2017-05-30 12:05:00", "2017-06-01 19:05:00", 3);
        assertEquals(2, result.size());
        result = DateUtil.splitDate("2016-12-31 12:05:00", "2017-01-01 19:05:00", 4);
        assertEquals(2, result.size());

    }

    @Test
    public void formatOrParseDate() throws Exception {

        String strDate = "2017-05-05 12:00:00";
        Date date = DateUtil.toDate(strDate);
        String result = DateUtil.toString(date);
        assertEquals(strDate, result);

        strDate = "2017-05-05";
        date = DateUtil.toDayDate(strDate);
        result = DateUtil.toDayString(date);
        assertEquals(strDate, result);

    }
}
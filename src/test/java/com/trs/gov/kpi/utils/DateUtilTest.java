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
    public void splitDateByMonth() throws Exception {

        //开始时间大于结束时间
        List<HistoryDate> result = DateUtil.splitDateByMonth("2017-06-01 00:00:00", "2017-05-19 00:00:00");
        assertEquals(0, result.size());

        //开始时间和结束时间都处于临界点
        result = DateUtil.splitDateByMonth("2017-05-31 00:00:00", "2017-06-01 00:00:00");
        assertEquals(2, result.size());

        //跨3个月
        result = DateUtil.splitDateByMonth("2017-04-30 12:05:00", "2017-07-30 08:00:00");
        assertEquals(4, result.size());

        //起止时间在同一天
        result = DateUtil.splitDateByMonth("2017-04-31 12:05:00", "2017-04-31 19:05:00");
        assertEquals(1, result.size());

        //跨年
        result = DateUtil.splitDateByMonth("2016-11-30 12:05:00", "2017-02-28 19:05:00");
        assertEquals(4, result.size());
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
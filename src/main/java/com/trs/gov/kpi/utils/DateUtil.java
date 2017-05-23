package com.trs.gov.kpi.utils;

import lombok.extern.slf4j.Slf4j;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by linwei on 2017/5/23.
 */
@Slf4j
public class DateUtil {
    private static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

    // 一天的毫秒数
    private static final long MS_ONE_DAY = 24 * 60 * 60 * 1000;

    /**
     * 日期字符串转换为日期
     * @param dateString
     * @return
     */
    public static Date toDate(String dateString) {
        if (dateString == null) {
            return null;
        }
        try {
            return format.parse(dateString);
        } catch (ParseException e) {
            log.error("failed to format date: " + dateString, e);
        }
        return null;
    }

    /**
     * 格式化日期为字符串
     * @param date
     * @return
     */
    public static String toString(Date date) {
        if (date == null) {
            return "";
        }
        return format.format(date);
    }

    /**
     * 计算两个日期之间相差的天数
     * @param date1
     * @param date2
     * @return
     */
    public static float diffDay(Date date1, Date date2) {
        return (date1.getTime() - date2.getTime()) / MS_ONE_DAY;
    }

    /**
     * 添加天数
     * @param date
     * @param day
     * @return
     */
    public static Date addDay(Date date, int day) {
        return new Date(date.getTime() + day * MS_ONE_DAY);
    }

}

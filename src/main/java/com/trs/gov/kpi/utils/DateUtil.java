package com.trs.gov.kpi.utils;

import com.trs.gov.kpi.entity.HistoryDate;
import lombok.extern.slf4j.Slf4j;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by linwei on 2017/5/23.
 */
@Slf4j
public class DateUtil {

    // 一天的秒数
    public static final int SECOND_ONE_DAY = 24 * 60 * 60;

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
            final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
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
        final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
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

    /**
     * 把时间按月拆分
     * @param start
     * @param end
     * @return 返回拆分后的每个月
     */
    public static List<HistoryDate> splitDateByMonth(String start, String end) {
        List<HistoryDate> list = new ArrayList<>();
        try {

            Date beginDate = new SimpleDateFormat("yyyy-MM-dd").parse(start);// 定义起始日期
            Date endDate = new SimpleDateFormat("yyyy-MM-dd").parse(end);// 定义结束日期

            Calendar currBegin = Calendar.getInstance();// 当前起始日期
            currBegin.setTime(beginDate);

            Calendar initCalender = Calendar.getInstance();//用于初始化起止时间段

            Calendar endCalendar = Calendar.getInstance();//结束日期
            endCalendar.setTime(endDate);


            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");


            while (currBegin.getTime().before(endDate)) {// 判断是否到结束日期
                HistoryDate historyDate = new HistoryDate();
                initCalender.setTime(currBegin.getTime());

                String firstDay;
                String lastDay;

                if (currBegin.getTime().equals(beginDate)) {
                    initCalender.set(Calendar.DAY_OF_MONTH, currBegin
                            .getActualMaximum(Calendar.DAY_OF_MONTH));

                    firstDay = sdf.format(beginDate);
                    initCalender.add(Calendar.DAY_OF_MONTH, 1);//增加一天，让结束日期为下个月的第一天，避免查询时最后一天查不到的情况
                    lastDay = sdf.format(initCalender.getTime());

                    historyDate.setBeginDate(firstDay);
                    historyDate.setEndDate(lastDay);


                } else if (currBegin.get(Calendar.MONTH) == endCalendar.get(Calendar.MONTH) && currBegin.get(Calendar.YEAR) == endCalendar.get(Calendar.YEAR)) {
                    initCalender.set(Calendar.DAY_OF_MONTH, 1);//取当月的第一天
                    firstDay = sdf.format(initCalender.getTime());

                    endCalendar.add(Calendar.DAY_OF_MONTH, 1);//取下个月的第一天
                    lastDay = sdf.format(endCalendar.getTime());

                    historyDate.setBeginDate(firstDay);
                    historyDate.setEndDate(lastDay);

                } else {
                    initCalender.set(Calendar.DAY_OF_MONTH, 1);//取当月的第一天
                    firstDay = sdf.format(initCalender.getTime());

                    initCalender.set(Calendar.DAY_OF_MONTH, currBegin
                            .getActualMaximum(Calendar.DAY_OF_MONTH));
                    initCalender.add(Calendar.DAY_OF_MONTH, 1);
                    lastDay = sdf.format(initCalender.getTime());

                    historyDate.setBeginDate(firstDay);
                    historyDate.setEndDate(lastDay);

                }
                int month = currBegin.get(Calendar.MONTH);
                historyDate.setMonth(currBegin.get(Calendar.YEAR) + "-" + formatDate(month));
                list.add(historyDate);
                currBegin.add(Calendar.MONTH, 1);// 进行当前日期月份加1
                currBegin.set(Calendar.DAY_OF_MONTH, 1);//取当月的第一天

            }

        } catch (ParseException e) {
            return null;
        }

        return list;
    }

    private static String formatDate(int month) {
        return (month + 1) < 10 ? "0" + (month + 1) : "" + (month + 1);
    }


}

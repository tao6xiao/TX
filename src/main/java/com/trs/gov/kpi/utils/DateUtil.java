package com.trs.gov.kpi.utils;

import com.trs.gov.kpi.entity.HistoryDate;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by linwei on 2017/5/23.
 */
@Slf4j
public class DateUtil {

    private DateUtil() {

    }

    // 一天的秒数
    public static final int SECOND_ONE_DAY = 24 * 60 * 60;

    // 一天的毫秒数
    private static final long MS_ONE_DAY = 24 * 60 * 60 * 1000L;

    private static final String MONTH_FORMAT = "yyyy-MM";

    private static final String DAY_FORMAT = "yyyy-MM-dd";

    // 惯用时间格式
    private static final String COMMON_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    /**
     * 日期字符串转换为日期
     *
     * @param dateString
     * @return
     */
    public static Date toDate(@NonNull String dateString) throws ParseException {
        final SimpleDateFormat format = new SimpleDateFormat(COMMON_DATE_FORMAT);
        return format.parse(dateString);
    }

    /**
     * 格式化日期为字符串
     *
     * @param date
     * @return
     */
    public static String toString(Date date) {
        if (date == null) {
            return "";
        }
        final SimpleDateFormat format = new SimpleDateFormat(COMMON_DATE_FORMAT);
        return format.format(date);
    }


    /**
     * 将yyyy-MM-dd格式日期字符串转换为日期
     *
     * @param dateString
     * @return
     */
    public static Date toDayDate(@NonNull String dateString) throws ParseException {
        final SimpleDateFormat format = new SimpleDateFormat(DAY_FORMAT);
        return format.parse(dateString);
    }

    /**
     * 格式化 yyyy-MM-dd格式日期为字符串
     *
     * @param date
     * @return
     */
    public static String toDayString(Date date) {
        if (date == null) {
            return "";
        }
        final SimpleDateFormat format = new SimpleDateFormat(DAY_FORMAT);
        return format.format(date);
    }

    /**
     * 计算两个日期之间相差的天数
     *
     * @param date1
     * @param date2
     * @return
     */
    public static float diffDay(Date date1, Date date2) {
        return (date1.getTime() - date2.getTime()) * 1.0f / MS_ONE_DAY;
    }

    /**
     * 添加天数
     *
     * @param date
     * @param day
     * @return
     */
    public static Date addDay(Date date, int day) {
        return new Date(date.getTime() + day * MS_ONE_DAY);
    }

    /**
     * 把时间按月拆分
     *
     * @param start
     * @param end
     * @return 返回拆分后的每个月
     */
    public static List<HistoryDate> splitDateByMonth(String start, String end) {
        List<HistoryDate> list = new ArrayList<>();
        try {

            Date beginDate = new SimpleDateFormat(DAY_FORMAT).parse(start);// 定义起始日期
            Date endDate = new SimpleDateFormat(DAY_FORMAT).parse(end);// 定义结束日期

            Calendar currBegin = Calendar.getInstance();// 当前起始日期
            currBegin.setTime(beginDate);

            Calendar initCalender = Calendar.getInstance();//用于初始化起止时间段

            Calendar endCalendar = Calendar.getInstance();//结束日期
            endCalendar.setTime(endDate);


            SimpleDateFormat sdf = new SimpleDateFormat(DAY_FORMAT);

            //起止日期相同时
            if (currBegin.getTime().equals(endCalendar.getTime())) {
                HistoryDate historyDate = new HistoryDate();
                historyDate.setMonth(currBegin.get(Calendar.YEAR) + "-" + formatMonth(currBegin.get(Calendar.MONTH)));
                historyDate.setBeginDate(sdf.format(currBegin.getTime()));
                endCalendar.add(Calendar.DAY_OF_MONTH, 1);
                historyDate.setEndDate(sdf.format(endCalendar.getTime()));
                list.add(historyDate);
                currBegin.add(Calendar.MONTH, 1);//使得开始日期大于等于结束时间，跳过后两次判断
            }


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

                    endCalendar.add(Calendar.DAY_OF_MONTH, 1);//增加一天，避免查询时最后一天查不到的情况
                    lastDay = sdf.format(endCalendar.getTime());
                    endCalendar.add(Calendar.DAY_OF_MONTH, -1);//减去一天，还原结束日期

                    historyDate.setBeginDate(firstDay);
                    historyDate.setEndDate(lastDay);

                } else {
                    initCalender.set(Calendar.DAY_OF_MONTH, 1);//取当月的第一天
                    firstDay = sdf.format(initCalender.getTime());

                    initCalender.set(Calendar.DAY_OF_MONTH, currBegin
                            .getActualMaximum(Calendar.DAY_OF_MONTH));
                    initCalender.add(Calendar.DAY_OF_MONTH, 1);//增加一天，让结束日期为下个月的第一天，避免查询时最后一天查不到的情况
                    lastDay = sdf.format(initCalender.getTime());

                    historyDate.setBeginDate(firstDay);
                    historyDate.setEndDate(lastDay);

                }
                int month = currBegin.get(Calendar.MONTH);
                historyDate.setMonth(currBegin.get(Calendar.YEAR) + "-" + formatMonth(month));
                list.add(historyDate);
                currBegin.add(Calendar.MONTH, 1);// 进行当前日期月份加1
                currBegin.set(Calendar.DAY_OF_MONTH, 1);//取当月的第一天

            }

            //处理结束日期为当月第一天的情况
            if (currBegin.getTime().equals(endCalendar.getTime())) {
                HistoryDate historyDate = new HistoryDate();
                historyDate.setMonth(currBegin.get(Calendar.YEAR) + "-" + formatMonth(currBegin.get(Calendar.MONTH)));
                historyDate.setBeginDate(sdf.format(currBegin.getTime()));
                endCalendar.add(Calendar.DAY_OF_MONTH, 1);
                historyDate.setEndDate(sdf.format(endCalendar.getTime()));
                list.add(historyDate);
            }

        } catch (ParseException e) {
            return list;
        }

        return list;
    }

    private static String formatMonth(int month) {
        return (month + 1) < 10 ? "0" + (month + 1) : String.valueOf(month + 1);
    }

    /**
     * 获取离指定时间最近的这个周期的起始时间
     *
     * @param date        指定的时间
     * @param beginDate   周期计算的起始时间
     * @param intervalDay 周期间隔时间，天为单位
     * @return
     */
    public static Date nearestPeriodBeginDate(@NonNull Date date, @NonNull String beginDate, int intervalDay) throws ParseException {
        long beginTime = toDate(beginDate).getTime();
        long endTime = date.getTime();
        int periods = (int) ((endTime - beginTime) * 1.0f / MS_ONE_DAY / intervalDay);
        return new Date(beginTime + intervalDay * periods * MS_ONE_DAY);
    }

    /**
     * 获取当前月， yyyy-MM-dd HH:mm:ss 格式，比如 2017-06-01 00:00:00
     *
     * @return
     */
    public static String curMonth() {
        final SimpleDateFormat format = new SimpleDateFormat(MONTH_FORMAT);
        return format.format(new Date()) + "-01 00:00:00";
    }

    /**
     * 判定是否为合法的月份，格式为 yyyy-MM, 比如 2017-01
     *
     * @param month
     * @return
     */
    public static boolean isValidMonth(String month) {
        // 1-9月
        String pattern = "^\\d{4}\\-0[1-9]$";
        // 11,12月
        String pattern2 = "^\\d{4}\\-1[1-2]$";
        return Pattern.matches(pattern, month) || Pattern.matches(pattern2, month);
    }

}

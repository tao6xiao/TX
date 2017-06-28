package com.trs.gov.kpi.utils;

import com.trs.gov.kpi.constant.Granularity;
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
     * 格式化月份为两位有效数字
     *
     * @param month
     * @return
     */
    private static String formatMonth(int month) {
        return (month + 1) < 10 ? "0" + (month + 1) : String.valueOf(month + 1);
    }

    /**
     * 格式化日期为两位有效数字
     *
     * @param day
     * @return
     */
    private static String formatDay(int day) {
        return day < 10 ? "0" + day : String.valueOf(day);
    }

    /**
     * 根据粒度获取周期,用以返回数据中各个数据对应日期的显示
     *
     * @param calendar
     * @param granularity
     * @return
     */
    private static String getUnitByGranularity(Calendar calendar, Integer granularity) {
        if (Granularity.DAY.equals(granularity)) {
            return calendar.get(Calendar.YEAR) + "-" + formatMonth(calendar.get(Calendar.MONTH)) + "-" + formatDay(calendar.get(Calendar.DAY_OF_MONTH));
        } else if (Granularity.WEEK.equals(granularity)) {
            return calendar.get(Calendar.YEAR) + "-" + calendar.get(Calendar.WEEK_OF_YEAR);
        } else if (Granularity.YEAR.equals(granularity)) {
            return Integer.toString(calendar.get(Calendar.YEAR));
        } else {//不设置，默认为月
            return calendar.get(Calendar.YEAR) + "-" + formatMonth(calendar.get(Calendar.MONTH));
        }
    }

    /**
     * 根据粒度获取默认的开始时间
     *
     * @param beginDateTime
     * @param granularity
     * @return
     * @throws ParseException
     */
    public static String getDefaultBeginDate(String beginDateTime, Integer granularity) {

        String endDateTime;
        Calendar calendar = Calendar.getInstance();
        try {
            calendar.setTime(DateUtil.toDate(beginDateTime));
        } catch (ParseException e) {
            log.error("解析开始日期失败！", e);
        }

        if (Granularity.DAY.equals(granularity)) {
            calendar.set(Calendar.DAY_OF_MONTH, 1);
            endDateTime = DateUtil.toString(calendar.getTime());
        } else if (Granularity.WEEK.equals(granularity)) {
            calendar.add(Calendar.WEEK_OF_YEAR, -11);
            calendar.set(Calendar.DAY_OF_WEEK, 1);
            endDateTime = DateUtil.toString(calendar.getTime());
        } else if (Granularity.YEAR.equals(granularity)) {
            calendar.add(Calendar.YEAR, -5);
            calendar.set(Calendar.MONTH, 0);
            calendar.set(Calendar.DAY_OF_MONTH, 1);
            endDateTime = DateUtil.toString(calendar.getTime());
        } else {//不设置，默认为月
            calendar.set(Calendar.MONTH, 0);
            calendar.set(Calendar.DAY_OF_MONTH, 1);
            endDateTime = DateUtil.toString(calendar.getTime());
        }
        return endDateTime;
    }

    /**
     * 分割日期
     *
     * @param start
     * @param end
     * @param granularity
     * @return
     */
    public static List<HistoryDate> splitDate(String start, String end, Integer granularity) {

        int cycle;//日期分割周期
        int unit;//周期的最小单位
        if (Granularity.DAY.equals(granularity)) {
            cycle = Calendar.DATE;
            unit = Calendar.DATE;
        } else if (Granularity.WEEK.equals(granularity)) {
            cycle = Calendar.WEEK_OF_YEAR;
            unit = Calendar.DAY_OF_WEEK;
        } else if (Granularity.YEAR.equals(granularity)) {
            cycle = Calendar.YEAR;
            unit = Calendar.DAY_OF_YEAR;
        } else {//不设置，默认为月
            cycle = Calendar.MONTH;
            unit = Calendar.DAY_OF_MONTH;
        }

        List<HistoryDate> list = new ArrayList<>();
        try {
            Date beginDate = new SimpleDateFormat(DAY_FORMAT).parse(start);// 定义开始时间
            Date endDate = new SimpleDateFormat(DAY_FORMAT).parse(end);// 定义结束时间

            Calendar currBegin = Calendar.getInstance();// 定义当前开始日期
            currBegin.setTime(beginDate);

            Calendar initCalender = Calendar.getInstance();//用于给起止日期赋值

            Calendar endCalendar = Calendar.getInstance();//结束日期
            endCalendar.setTime(endDate);

            SimpleDateFormat sdf = new SimpleDateFormat(DAY_FORMAT);


            //起止日期相同时
            if (currBegin.getTime().equals(endCalendar.getTime())) {
                HistoryDate historyDate = new HistoryDate();
                historyDate.setDate(getUnitByGranularity(currBegin, granularity));
                historyDate.setBeginDate(sdf.format(currBegin.getTime()));
                endCalendar.add(unit, 1);
                historyDate.setEndDate(sdf.format(endCalendar.getTime()));
                endCalendar.add(unit, -1);//减去一天，还原结束日期
                list.add(historyDate);
                currBegin.add(unit, 1);//使得开始日期大于等于结束时间，跳过后两次判断
            }

            //粒度为天，没有其他粒度那么复杂，单独处理
            if (Granularity.DAY.equals(granularity)) {
                initDayList(currBegin, endDate, list, unit, granularity, sdf);
                return list;
            }

            while (currBegin.getTime().before(endDate)) {// 判断是否到结束日期
                HistoryDate historyDate = new HistoryDate();
                initCalender.setTime(currBegin.getTime());

                String firstDay;
                String lastDay;

                if (currBegin.getTime().equals(beginDate)) {//开始日期等于当前开始日期
                    initCalender.set(unit, currBegin.getActualMaximum(unit));

                    lastDay = getLastDay(initCalender, endCalendar, sdf, unit);
                    firstDay = sdf.format(beginDate);

                    historyDate.setBeginDate(firstDay);
                    historyDate.setEndDate(lastDay);

                } else if (currBegin.get(cycle) == endCalendar.get(cycle) && currBegin.get(Calendar.YEAR) == endCalendar.get(Calendar.YEAR)) {
                    //当前开始日期与结束日期在同一个周期内
                    initCalender.set(unit, 1);//取当月的第一天
                    firstDay = sdf.format(initCalender.getTime());

                    endCalendar.add(unit, 1);//增加一天，避免查询时最后一天查不到的情况
                    lastDay = sdf.format(endCalendar.getTime());
                    endCalendar.add(unit, -1);//减去一天，还原结束日期

                    historyDate.setBeginDate(firstDay);
                    historyDate.setEndDate(lastDay);
                } else {
                    initCalender.set(unit, 1);//取当周的第一天
                    firstDay = sdf.format(initCalender.getTime());

                    initCalender.set(unit, currBegin
                            .getActualMaximum(unit));
                    initCalender.add(unit, 1);//增加一天，让结束日期为下个月的第一天，避免查询时最后一天查不到的情况
                    lastDay = sdf.format(initCalender.getTime());

                    historyDate.setBeginDate(firstDay);
                    historyDate.setEndDate(lastDay);

                }
                historyDate.setDate(getUnitByGranularity(currBegin, granularity));
                list.add(historyDate);
                currBegin.add(cycle, 1);// 周期加1
                currBegin.set(unit, 1);//取当当前周期的第一天
            }

            //处理结束日期为周期第一天的情况
            if (currBegin.getTime().equals(endCalendar.getTime())) {
                HistoryDate historyDate = new HistoryDate();
                historyDate.setDate(getUnitByGranularity(currBegin, granularity));
                historyDate.setBeginDate(sdf.format(currBegin.getTime()));
                endCalendar.add(unit, 1);
                historyDate.setEndDate(sdf.format(endCalendar.getTime()));
                list.add(historyDate);
            }

        } catch (ParseException e) {
            return list;
        }

        return list;
    }

    /**
     * 粒度为天时，往日期集合里循环添加日期
     *
     * @param currBegin
     * @param endDate
     * @param list
     * @param unit
     * @param granularity
     * @param sdf
     */
    private static void initDayList(Calendar currBegin, Date endDate, List<HistoryDate> list, int unit, Integer granularity, SimpleDateFormat sdf) {
        while (currBegin.getTime().before(endDate)) {
            addNextDay(list, currBegin, unit, granularity, sdf);
            if (currBegin.getTime().equals(endDate)) {
                addNextDay(list, currBegin, unit, granularity, sdf);
            }
        }
    }

    /**
     * 获取结束日期前，判断结束日期是否在第一个周期内
     *
     * @param initCalender
     * @param endCalendar
     * @param sdf
     * @param unit
     * @return
     */
    private static String getLastDay(Calendar initCalender, Calendar endCalendar, SimpleDateFormat sdf, int unit) {
        if (initCalender.getTime().before(endCalendar.getTime())) {
            initCalender.add(unit, 1);//增加一天，让结束日期为下个月的第一天，避免查询时最后一天查不到的情况
            return sdf.format(initCalender.getTime());
        } else {//结束日期在第一个周期内
            endCalendar.add(unit, 1);
            return sdf.format(endCalendar.getTime());
        }
    }

    /**
     * 粒度为天时，用于往日期集合里添加下一天的起止时间
     *
     * @param list
     * @param currBegin
     * @param unit
     * @param granularity
     * @param sdf
     */
    private static void addNextDay(List<HistoryDate> list, Calendar currBegin, int unit, Integer granularity, SimpleDateFormat sdf) {
        HistoryDate historyDate = new HistoryDate();
        historyDate.setDate(getUnitByGranularity(currBegin, granularity));
        String firstDay = sdf.format(currBegin.getTime());
        currBegin.add(unit, 1);
        String lastDay = sdf.format(currBegin.getTime());
        historyDate.setBeginDate(firstDay);
        historyDate.setEndDate(lastDay);
        list.add(historyDate);
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

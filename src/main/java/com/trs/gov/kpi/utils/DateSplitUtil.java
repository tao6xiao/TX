package com.trs.gov.kpi.utils;

import com.trs.gov.kpi.entity.HistoryDate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by ranwei on 2017/5/16.
 */
public class DateSplitUtil {

    public static List<HistoryDate> getHistoryDateList(String start, String end) {
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

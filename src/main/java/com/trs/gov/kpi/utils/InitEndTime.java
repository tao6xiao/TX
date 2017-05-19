package com.trs.gov.kpi.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by ranwei on 2017/5/16.
 */
public class InitEndTime {

    public static String initTime(String endTime) {
        String time = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date date = sdf.parse(endTime);
            Calendar calender = Calendar.getInstance();
            calender.setTime(date);
            calender.add(Calendar.DAY_OF_MONTH, 1);
            time = sdf.format(calender.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return time;
    }

    public static String getStringTime(Date date){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(date);
    }
}

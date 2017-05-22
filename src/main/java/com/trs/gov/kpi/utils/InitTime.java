package com.trs.gov.kpi.utils;

import com.trs.gov.kpi.service.OperationService;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by ranwei on 2017/5/16.
 */
public class InitTime {
    @Resource
    private static OperationService operationService;

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

    public static String getNowTimeFormat(Date date){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(date);
    }

    public static Date getNowTimeFormat(String dateTime) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.parse(dateTime);
    }

    /**
     * 获取起始时间，如果不设置则从系统启动开始算起
     * @param beginDateTime
     * @return
     * @throws ParseException
     */
    public static Date CheckBeginDateTime(String beginDateTime) throws ParseException {
        if(beginDateTime == null){
            Date ealiestTime = operationService.getEarliestIssueTime();
            return ealiestTime;
        }else {
            Date setTime = getNowTimeFormat(beginDateTime);
            return setTime;
        }
    }

    /**
     * 结束时间，如果不设置，则从当前时间作为结束时间
     * @param endDateTime
     * @return
     * @throws ParseException
     */
    public static Date CheckEndDateTime(String endDateTime) throws ParseException {
        if(endDateTime == null){
            Date nowTime = new Date();
            String nowTimeStr = getNowTimeFormat(nowTime);
            nowTime = getNowTimeFormat(nowTimeStr);
            return nowTime;
        }else {
            Date setTime = getNowTimeFormat(endDateTime);
            return setTime;
        }
    }

}

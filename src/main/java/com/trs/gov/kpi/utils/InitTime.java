package com.trs.gov.kpi.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by ranwei on 2017/5/16.
 */
public class InitTime {

    /**
     * 获取起始时间，如果不设置则从系统启动开始算起
     * @param beginDateTime
     * @return
     * @throws ParseException
     */
    public static String checkBeginDateTime(String beginDateTime, Date earliestTime)  {
        if(StringUtil.isEmpty(beginDateTime)){
            return DateUtil.toString(earliestTime);
        }else {
            return beginDateTime;
        }
    }

    /**
     * 结束时间，如果不设置，则从当前时间作为结束时间
     * @param endDateTime
     * @return
     * @throws ParseException
     */
    public static String checkEndDateTime(String endDateTime) {
        if(StringUtil.isEmpty(endDateTime)){
            return DateUtil.toString(new Date());
        }else {
            return endDateTime;
        }
    }

}

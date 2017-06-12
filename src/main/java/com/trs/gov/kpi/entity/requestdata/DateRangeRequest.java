package com.trs.gov.kpi.entity.requestdata;

/**
 * Created by linwei on 2017/6/12.
 */
public class DateRangeRequest {

    private String beginDateTime;

    private String endDateTime;

    public String getBeginDateTime() {
        return beginDateTime;
    }

    public void setBeginDateTime(String beginDateTime) {
        if (beginDateTime != null && beginDateTime.trim().isEmpty()) {
            return;
        }
        this.beginDateTime = beginDateTime;
    }

    public String getEndDateTime() {
        return endDateTime;
    }

    public void setEndDateTime(String endDateTime) {
        if (endDateTime != null && endDateTime.trim().isEmpty()) {
            return;
        }
        this.endDateTime = endDateTime;
    }
}

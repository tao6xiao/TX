package com.trs.gov.kpi.entity;

import java.util.Date;

public class DefaultUpdateFreq {
    private Integer siteId;

    private Integer value;

    private Date setTime;

    public Integer getSiteId() {
        return siteId;
    }

    public void setSiteId(Integer siteId) {
        this.siteId = siteId;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public Date getSetTime() {
        return setTime;
    }

    public void setSetTime(Date setTime) {
        this.setTime = setTime;
    }

    @Override
    public String toString() {
        return "{" +
                "siteId=" + siteId +
                ", value=" + value +
                '}';
    }
}
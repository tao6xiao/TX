package com.trs.gov.kpi.entity;

public class FrequencyPreset {
    private Integer id;

    private Integer siteId;

    private Integer updateFreq;

    private Integer alertFreq;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getSiteId() {
        return siteId;
    }

    public void setSiteId(Integer siteId) {
        this.siteId = siteId;
    }

    public Integer getUpdateFreq() {
        return updateFreq;
    }

    public void setUpdateFreq(Integer updateFreq) {
        this.updateFreq = updateFreq;
    }

    public Integer getAlertFreq() {
        return alertFreq;
    }

    public void setAlertFreq(Integer alertFreq) {
        this.alertFreq = alertFreq;
    }
}
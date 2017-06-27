package com.trs.gov.kpi.entity;

public class MonitorSite {
    private Integer siteId;

    private String departmentName;

    private Integer guarderId;

    private String indexUrl;

    public Integer getSiteId() {
        return siteId;
    }

    public void setSiteId(Integer siteId) {
        this.siteId = siteId;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName == null ? null : departmentName.trim();
    }

    public Integer getGuarderId() {
        return guarderId;
    }

    public void setGuarderId(Integer guarderId) {
        this.guarderId = guarderId;
    }

    public String getIndexUrl() {
        return indexUrl;
    }

    public void setIndexUrl(String indexUrl) {
        this.indexUrl = indexUrl == null ? null : indexUrl.trim();
    }

}
package com.trs.gov.kpi.entity;

public class MonitorSite {
    private Integer siteId;

    private String departmentName;

    private String guarderName;

    private String guarderAccount;

    private String guarderPhone;

    private String indexUrl;

    private String siteIds;

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

    public String getGuarderName() {
        return guarderName;
    }

    public void setGuarderName(String guarderName) {
        this.guarderName = guarderName == null ? null : guarderName.trim();
    }

    public String getGuarderAccount() {
        return guarderAccount;
    }

    public void setGuarderAccount(String guarderAccount) {
        this.guarderAccount = guarderAccount == null ? null : guarderAccount.trim();
    }

    public String getGuarderPhone() {
        return guarderPhone;
    }

    public void setGuarderPhone(String guarderPhone) {
        this.guarderPhone = guarderPhone == null ? null : guarderPhone.trim();
    }

    public String getIndexUrl() {
        return indexUrl;
    }

    public void setIndexUrl(String indexUrl) {
        this.indexUrl = indexUrl == null ? null : indexUrl.trim();
    }

    public String getSiteIds() {
        return siteIds;
    }

    public void setSiteIds(String siteIds) {
        this.siteIds = siteIds == null ? null : siteIds.trim();
    }
}
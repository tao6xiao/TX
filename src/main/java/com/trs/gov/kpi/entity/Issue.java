package com.trs.gov.kpi.entity;

import lombok.Data;

import java.util.Date;

public class Issue {

    /**
     * 问题编号
     */
    private String id;

    /**
     * 站点编号
     */
    private Integer siteId;

    private Integer typeId;

    private Integer subTypeId;

    private String subTypeName;

    private String detail = "";

    private Date issueTime;

    private Integer isResolved = 0;

    private Integer isDel = 0;

    private String customer1;

    private String customer2;

    private String customer3;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getSiteId() {
        return siteId;
    }

    public void setSiteId(Integer siteId) {
        this.siteId = siteId;
    }

    public Integer getTypeId() {
        return typeId;
    }

    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
    }

    public Integer getSubTypeId() {
        return subTypeId;
    }

    public void setSubTypeId(Integer subTypeId) {
        this.subTypeId = subTypeId;
    }

    public String getSubTypeName() {
        return subTypeName;
    }

    public void setSubTypeName(String subTypeName) {
        this.subTypeName = subTypeName;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        if (detail != null) {
            this.detail = detail;
        }
    }

    public Date getIssueTime() {
        return issueTime;
    }

    public void setIssueTime(Date issueTime) {
        this.issueTime = issueTime;
    }

    public Integer getIsResolved() {
        return isResolved;
    }

    public void setIsResolved(Integer isResolved) {
        this.isResolved = isResolved;
    }

    public Integer getIsDel() {
        return isDel;
    }

    public void setIsDel(Integer isDel) {
        this.isDel = isDel;
    }

    public String getCustomer1() {
        return customer1;
    }

    public void setCustomer1(String customer1) {
        this.customer1 = customer1;
    }

    public String getCustomer2() {
        return customer2;
    }

    public void setCustomer2(String customer2) {
        this.customer2 = customer2;
    }

    public String getCustomer3() {
        return customer3;
    }

    public void setCustomer3(String customer3) {
        this.customer3 = customer3;
    }
}
package com.trs.gov.kpi.entity;

import com.trs.gov.kpi.annotation.DBField;
import com.trs.gov.kpi.annotation.DBTable;

import java.util.Date;

/**
 * issue表对应的实体类
 */
@DBTable
public class Issue {

    /**
     * 问题编号
     */
    @DBField(autoInc = true)
    private String id;

    /**
     * 站点编号
     */
    @DBField
    private Integer siteId;

    @DBField
    private Integer typeId;//问题类型编号

    @DBField
    private Integer subTypeId;//问题子类型编号

    private String subTypeName;//问题子类型

    @DBField
    private String detail = "";//详情

    @DBField
    private Date issueTime;//监测时间

    @DBField
    private Date checkTime;//检查时间

    @DBField
    private Integer isResolved = 0;//处理状态

    @DBField
    private Integer isDel = 0;//删除状态

    @DBField
    private Integer deptId;//所属部门编号

    @DBField
    private Integer workOrderStatus = 0;//工单状态

    @DBField
    private String customer1;//扩展字段1

    @DBField
    private String customer2;//扩展字段2

    @DBField
    private String customer3;//扩展字段3

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

    public Integer getDeptId() {
        return deptId;
    }

    public void setDeptId(Integer deptId) {
        this.deptId = deptId;
    }

    public Integer getWorkOrderStatus() {
        return workOrderStatus;
    }

    public void setWorkOrderStatus(Integer workOrderStatus) {
        this.workOrderStatus = workOrderStatus;
    }

    public Date getCheckTime() {
        return checkTime;
    }

    public void setCheckTime(Date checkTime) {
        this.checkTime = checkTime;
    }
}
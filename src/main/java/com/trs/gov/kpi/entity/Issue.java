package com.trs.gov.kpi.entity;

import lombok.Data;

import java.util.Date;

@Data
public class Issue extends IssueBase{

    private Integer typeId;

    private Integer subTypeId;

    private String subTypeName;

    private String detail;

    private Date issueTime;

    private Integer isResolved;

    private Integer isDel;

    private String customer1;

    private String customer2;

    private String customer3;
}
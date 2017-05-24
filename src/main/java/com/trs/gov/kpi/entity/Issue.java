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

    private Integer isResolved = 0;

    private Integer isDel = 0;

    private String customer1;

    private String customer2;

    private String customer3;
}
package com.trs.gov.kpi.entity;

import lombok.Data;

import java.util.Date;

@Data
public class Issue {
    private Integer id;

    private Integer siteId;

    private Integer typeId;

    private Integer subTypeId;

    private String detail;

    private Date issueTime;

    private Boolean isResolved;

    private Boolean isDel;

    private String customer1;

    private String customer2;

    private String customer3;
}
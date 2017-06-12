package com.trs.gov.kpi.entity;

import lombok.Data;

import java.util.Date;

/**
 * Created by ranwei on 2017/6/12.
 */
@Data
public class Report {

    private int id;

    private int siteId;

    private String title;

    private Date crTime;
}

package com.trs.gov.kpi.entity.responsedata;

import lombok.Data;

import java.util.Date;

/**
 * Created by ranwei on 2017/6/12.
 */
@Data
public class ReportResponse {

    private int id;

    private String siteName;

    private String title;

    private Date crTime;
}

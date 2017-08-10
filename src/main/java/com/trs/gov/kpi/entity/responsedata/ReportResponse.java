package com.trs.gov.kpi.entity.responsedata;

import lombok.Data;

import java.util.Date;

/**
 * 报表数据的返回模板
 * Created by ranwei on 2017/6/12.
 */
@Data
public class ReportResponse {

    private int id;

    private String siteName;

    private String title;

    private Date crTime;
}

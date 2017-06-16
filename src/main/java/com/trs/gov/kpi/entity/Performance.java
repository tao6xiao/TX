package com.trs.gov.kpi.entity;

import lombok.Data;

import java.util.Date;

/**
 * Created by ranwei on 2017/6/16.
 */
@Data
public class Performance {

    private Integer id;

    private Integer siteId;

    private Double index;//绩效指数

    private Date checkTime;
}

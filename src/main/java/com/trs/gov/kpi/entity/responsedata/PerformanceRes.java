package com.trs.gov.kpi.entity.responsedata;

import lombok.Data;

import java.util.Date;

/**
 * 绩效指数数据的返回模板
 * Created by ranwei on 2017/6/16.
 */
@Data
public class PerformanceRes {

    private Integer id;

    private Double index;//绩效指数

    private Date checkTime;
}

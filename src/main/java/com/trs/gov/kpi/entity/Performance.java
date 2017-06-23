package com.trs.gov.kpi.entity;

import lombok.Data;

import java.util.Date;

/**
 * 绩效指数
 * Created by ranwei on 2017/6/16.
 */
@Data
public class Performance {

    private Integer id;//绩效指数编号

    private Integer siteId;//站点编号

    private Double index;//绩效指数

    private Date checkTime;//检测时间
}

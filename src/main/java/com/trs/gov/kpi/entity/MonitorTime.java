package com.trs.gov.kpi.entity;

import lombok.Data;

import java.util.Date;

/**
 * Created by ranwei on 2017/7/31.
 */
@Data
public class MonitorTime {

    private Integer siteId;

    private Integer typeId;

    private Date startTime;

    private Date endTime;
}

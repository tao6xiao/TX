package com.trs.gov.kpi.entity;

import com.trs.gov.kpi.annotation.DBField;
import com.trs.gov.kpi.annotation.DBTable;
import lombok.Data;

import java.util.Date;

/**
 * Created by ranwei on 2017/7/31.
 */
@Data
@DBTable("monitortime")
public class MonitorTime {

    @DBField
    private Integer siteId;

    @DBField
    private Integer typeId;

    @DBField
    private Date startTime;

    @DBField
    private Date endTime;
}

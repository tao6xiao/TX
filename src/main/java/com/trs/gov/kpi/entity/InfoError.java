package com.trs.gov.kpi.entity;

import lombok.Data;

import java.util.Date;

/**
 *信息错误
 * Created by ranwei on 2017/5/27.
 */
@Data
public class InfoError {

    private String id;

    private Integer siteId;//站点编号

    private Integer subTypeId;//问题子类型

    private String snapshot;//快照

    private Date checkTime;//监测时间

    private String errorDetail;//错误详细信息

    private Integer workOrderStatus;//工单状态

    private Integer deptId;//部门编号
}

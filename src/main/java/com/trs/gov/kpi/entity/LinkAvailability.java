package com.trs.gov.kpi.entity;

import lombok.Data;

import java.util.Date;

/**
 * 链接可用性
 * Created by ranwei on 2017/5/27.
 */
@Data
public class LinkAvailability {

    private String id;//问题编号

    private Integer issueTypeId;//问题子类型编号

    private String invalidLink;//失效链接

    private String snapshot;//快照

    private Date checkTime;//监测时间

    private Integer deptId;//部门编号
}

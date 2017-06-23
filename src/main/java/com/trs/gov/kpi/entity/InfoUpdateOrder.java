package com.trs.gov.kpi.entity;

import lombok.Data;

import java.util.Date;

/**
 * 信息更新工单
 * Created by ranwei on 2017/6/7.
 */
@Data
public class InfoUpdateOrder {

    private String id;

    private Integer chnlId;

    private Integer siteId;

    private Integer typeId;

    private Integer subTypeId;

    private String detail;

    private Date issueTime;

    private Integer isResolved;

    private Integer isDel;

    private Integer workOrderStatus;//工单状态
}

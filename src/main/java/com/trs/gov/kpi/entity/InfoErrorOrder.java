package com.trs.gov.kpi.entity;

import lombok.Data;

import java.util.Date;

/**
 * 信息错误工单
 * Created by ranwei on 2017/6/8.
 */
@Data
public class InfoErrorOrder {

    private String id;

    private Integer chnlId;

    private Integer siteId;

    private Integer typeId;

    private Integer subTypeId;

    private String detail;

    private String errorDetail;

    private Date issueTime;

    private Integer isResolved;

    private Integer isDel;

    private Integer workOrderStatus;


}

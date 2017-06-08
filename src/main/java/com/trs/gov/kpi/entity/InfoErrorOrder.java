package com.trs.gov.kpi.entity;

import lombok.Data;

import java.util.Date;

/**
 * Created by ranwei on 2017/6/8.
 */
@Data
public class InfoErrorOrder {

    private String id;

    private Integer chnlId;

    private Integer siteId;

    private Integer subTypeId;

    private String detail;

    private String errorDetail;

    private Date issueTime;

    private Integer isResolved;

    private Integer isDel;

    private Integer workOrderStatus;


}

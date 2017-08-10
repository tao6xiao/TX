package com.trs.gov.kpi.entity.responsedata;

import lombok.Data;

import java.util.Date;

/**
 * 信息更新工单数据的返回模板
 * Created by ranwei on 2017/6/7.
 */
@Data
public class InfoUpdateOrderRes {

    private String id;

    private String chnlName;

    private String siteName;

    private String parentTypeName;

    private String issueTypeName;

    private String department;

    private String chnlUrl;

    private Date checkTime;

    private Integer solveStatus;

    private Integer isDeleted;

    private Integer workOrderStatus;
}

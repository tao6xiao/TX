package com.trs.gov.kpi.entity.responsedata;

import lombok.Data;

import java.util.Date;

/**
 * Created by ranwei on 2017/6/7.
 */
@Data
public class InfoUpdateOrderRes {

    private String id;

    private String chnlName;

    private String siteName;

    private String parentTypeName;

    private String issueTypeName;

    private String department = "";//TODO 等接口，暂时设为空串

    private String chnlUrl;

    private Date checkTime;

    private Integer solveStatus;

    private Integer isDeleted;

    private Integer workOrderStatus;
}

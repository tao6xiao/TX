package com.trs.gov.kpi.entity.responsedata;

import lombok.Data;

import java.util.Date;

/**
 * 信息错误工单数据的返回模板
 * Created by ranwei on 2017/6/8.
 */
@Data
public class InfoErrorOrderRes {

    private String id;

    private String chnlName;

    private String siteName;

    private String parentTypeName;

    private String issueTypeName;

    private String department;

    private String url;

    private String wrongContent;

    private Date checkTime;

    private Integer solveStatus;

    private Integer isDeleted;

    private Integer workOrderStatus;
}

package com.trs.gov.kpi.entity.responsedata;

import lombok.Data;

import java.util.Date;

/**
 * 信息错误数据的返回模板
 * Created by ran.wei on 2017/5/15.
 */
@Data
public class InfoErrorResponse {

    private String id;

    private String issueTypeName;

    private String snapshot;

    private Date checkTime;

    private String errorDetail;

    private String workOrderStatus;

    private String deptName;//部门名称

}

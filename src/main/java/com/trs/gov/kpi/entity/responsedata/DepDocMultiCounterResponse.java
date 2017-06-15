package com.trs.gov.kpi.entity.responsedata;

import lombok.Data;

/**
 * Created by linwei on 2017/6/15.
 */
@Data
public class DepDocMultiCounterResponse extends DocMultiCounterResponse{

    // 部门
    private Long departmentId;

    // 部门名称
    private String departmentName;

}

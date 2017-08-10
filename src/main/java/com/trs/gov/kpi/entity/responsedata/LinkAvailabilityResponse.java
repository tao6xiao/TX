package com.trs.gov.kpi.entity.responsedata;

import lombok.Data;

import java.util.Date;

/**
 * 链接可用性问题数据的返回模板
 * Created by rw103 on 2017/5/11.
 */
@Data
public class LinkAvailabilityResponse {

    private String id;

    private Integer siteId;

    private Integer issueTypeId;

    private String issueTypeName = "";

    private String invalidLink;

    private String snapshot;

    private Date checkTime;

    private String deptName;//部门名称

}

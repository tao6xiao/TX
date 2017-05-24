package com.trs.gov.kpi.entity;

import lombok.Data;

import java.util.Date;

/**
 * Created by ran.wei on 2017/5/15.
 */
@Data
public class InfoError {

    /**
     * 问题编号
     */
    private String id;

    /**
     * 站点编号
     */
    private Integer siteId;

    private Integer issueTypeId;

    private String issueTypeName;

    private String snapshot;

    private Date checkTime;
}

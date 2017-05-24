package com.trs.gov.kpi.entity;

import lombok.Data;

import java.util.Date;

/**
 * Created by rw103 on 2017/5/11.
 */
@Data
public class LinkAvailability {

    /**
     * 问题编号
     */
    private String id;

    /**
     * 站点编号
     */
    private Integer siteId;

    private Integer issueTypeId;

    private String issueTypeName = "";

    private String invalidLink;

    private String snapshot;

    private Date checkTime;

}

package com.trs.gov.kpi.entity;

import lombok.Data;

import java.util.Date;

/**
 * Created by ranwei on 2017/5/27.
 */
@Data
public class InfoError {

    private String id;

    private Integer siteId;

    private Integer subTypeId;

    private String snapshot;

    private Date checkTime;

    private String errorDetail;
}

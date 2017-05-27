package com.trs.gov.kpi.entity;

import lombok.Data;

import java.util.Date;

/**
 * Created by ranwei on 2017/5/27.
 */
@Data
public class LinkAvailability {

    private String id;

    private Integer issueTypeId;

    private String invalidLink;

    private String snapshot;

    private Date checkTime;
}

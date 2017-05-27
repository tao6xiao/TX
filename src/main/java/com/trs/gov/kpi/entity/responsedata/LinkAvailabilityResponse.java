package com.trs.gov.kpi.entity.responsedata;

import lombok.Data;

import java.util.Date;

/**
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

}

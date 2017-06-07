package com.trs.gov.kpi.entity.responsedata;

import lombok.Data;

import java.util.Date;

/**
 * Created by ran.wei on 2017/5/15.
 */
@Data
public class InfoErrorResponse {

    private String id;

    private String issueTypeName;

    private String snapshot;

    private Date checkTime;

    private String errorDetail;

}

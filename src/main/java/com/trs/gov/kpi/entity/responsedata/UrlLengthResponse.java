package com.trs.gov.kpi.entity.responsedata;

import lombok.Data;

import java.util.Date;

/**
 * Created by ranwei on 2017/6/6.
 */
@Data
public class UrlLengthResponse {

    private String id;

    private String chnlName;

    private String pageLink;

    private Long urlLength;

    private Long pageSpace;

    private Date checkTime;


}

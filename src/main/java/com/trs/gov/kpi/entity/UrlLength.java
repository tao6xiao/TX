package com.trs.gov.kpi.entity;

import lombok.Data;

import java.util.Date;

/**
 * Created by ranwei on 2017/6/6.
 */
@Data
public class UrlLength {

    private String id;

    private Integer chnlId;

    private String pageLink;

    private String length;

    private String pageSpace;

    private Date checkTime;
}

package com.trs.gov.kpi.entity;

import lombok.Data;

import java.util.Date;

/**
 * Created by ranwei on 2017/6/6.
 */
@Data
public class PageDepth {

    private String id;

    private Integer chnlId;

    private String pageLink;

    private Integer depth;

    private Integer space;

    private Date checkTime;
}

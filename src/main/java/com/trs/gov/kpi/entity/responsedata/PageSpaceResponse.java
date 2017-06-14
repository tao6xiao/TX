package com.trs.gov.kpi.entity.responsedata;

import lombok.Data;

import java.util.Date;

/**
 * Created by ranwei on 2017/6/6.
 */
@Data
public class PageSpaceResponse {

    private String id;

    private String chnlName;

    private String pageLink;

    private Integer replySpeed;

    private Integer pageSpace;

    private Date checkTime;
}

package com.trs.gov.kpi.entity.responsedata;

import lombok.Data;

import java.util.Date;

/**
 * Created by ranwei on 2017/6/6.
 */
@Data
public class RepeatCodeResponse {

    private String id;

    private String chnlName;

    private Integer repeatPlace;

    private String repeatDegree;

    private Date updateTime;

    private Date checkTime;
}

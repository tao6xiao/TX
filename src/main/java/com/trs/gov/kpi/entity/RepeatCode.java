package com.trs.gov.kpi.entity;

import lombok.Data;

import java.util.Date;

/**
 * Created by ranwei on 2017/6/6.
 */
@Data
public class RepeatCode extends PageIssue {

    private Integer repeatPlace;

    private String repeatDegree;

    private Date updateTime;//更新时间

}

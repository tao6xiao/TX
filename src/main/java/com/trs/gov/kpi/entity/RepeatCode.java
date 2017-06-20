package com.trs.gov.kpi.entity;

import lombok.Data;

import java.util.Date;

/**
 * Created by ranwei on 2017/6/6.
 */
@Data
public class RepeatCode extends PageIssue {

    private Integer typeId;//类型编号

    private Integer repeatPlace;

    private String repeatDegree;

    private Date updateTime;//更新时间

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

}

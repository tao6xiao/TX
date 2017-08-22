package com.trs.gov.kpi.entity;

import com.trs.gov.kpi.annotation.DBField;
import com.trs.gov.kpi.annotation.DBTable;
import lombok.Data;

import java.util.Date;

/**
 * Created by ranwei on 2017/6/6.
 */
@Data
@DBTable("webpage")
public class RepeatCode extends PageIssue {

    @DBField
    private Integer typeId;//类型编号

    @DBField
    private Integer repeatPlace;//冗余位置

    @DBField
    private String repeatDegree;//冗余程度

    @DBField
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

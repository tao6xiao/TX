package com.trs.gov.kpi.entity;

import com.trs.gov.kpi.annotation.DBField;
import com.trs.gov.kpi.annotation.DBTable;
import lombok.Data;

import java.util.Date;

/**
 * 过长URL页面
 * <p>
 * Created by ranwei on 2017/6/6.
 */
@Data
@DBTable("webpage")
public class UrlLength extends PageIssue {

    @DBField
    private Integer typeId;//类型编号

    @DBField("urlLength")
    private Long length;//URL长度

    @DBField("pageSpace")
    private Long space;//页面大小

    public UrlLength() {
    }

    public UrlLength(Integer typeId, String chnlName, String pageLink, Long length, Long space, Date checkTime) {
        this.typeId = typeId;
        this.chnlName = chnlName;
        this.pageLink = pageLink;
        this.length = length;
        this.space = space;
        this.checkTime = checkTime;
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}

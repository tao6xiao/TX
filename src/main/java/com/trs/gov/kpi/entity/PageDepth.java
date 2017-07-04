package com.trs.gov.kpi.entity;

import com.trs.gov.kpi.annotation.DBField;
import com.trs.gov.kpi.annotation.DBTable;
import lombok.Data;

import java.util.Date;

/**
 * 过深页面
 * <p>
 * Created by ranwei on 2017/6/6.
 */
@Data
@DBTable("webpage")
public class PageDepth extends PageIssue {

    @DBField
    private Integer typeId;//类型编号

    @DBField("replySpeed")
    private Integer depth;//页面深度

    @DBField("pageSpace")
    private Long space;//页面大小

    public PageDepth(Integer typeId, String chnlName, String pageLink, Integer depth, Long space, Date checkTime) {
        this.typeId = typeId;
        this.chnlName = chnlName;
        this.pageLink = pageLink;
        this.depth = depth;
        this.space = space;
        this.checkTime = checkTime;
    }

    public PageDepth() {
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

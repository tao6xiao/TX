package com.trs.gov.kpi.entity;

import com.trs.gov.kpi.annotation.DBField;
import com.trs.gov.kpi.annotation.DBTable;
import lombok.Data;

import java.util.Date;

/**
 * 页面深度
 * <p>
 * Created by ranwei on 2017/6/6.
 */
@Data
@DBTable("webpage")
public class PageDepth extends PageIssue {

    @DBField("pageDepth")
    private Integer depth;//页面深度

    @DBField("pageSpace")
    private Long space;//页面大小

    public PageDepth(Integer typeId, Integer chnlId, String pageLink, Integer depth, Long space, Date checkTime) {
        this.typeId = typeId;
        this.chnlId = chnlId;
        this.pageLink = pageLink;
        this.depth = depth;
        this.space = space;
        this.checkTime = checkTime;
    }

    public PageDepth() {
    }
}

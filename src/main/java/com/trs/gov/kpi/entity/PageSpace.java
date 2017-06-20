package com.trs.gov.kpi.entity;

import com.trs.gov.kpi.annotation.DBField;
import com.trs.gov.kpi.annotation.DBTable;
import com.trs.gov.kpi.constant.Types;
import lombok.Data;

import java.util.Date;

/**
 * Created by ranwei on 2017/6/6.
 */
@Data
@DBTable("webpage")
public class PageSpace extends PageIssue {

    @DBField
    private Integer typeId = Types.AnalysisType.OVERSIZE_PAGE.value;

    @DBField("pageSpeed")
    private Long speed;//响应速度

    @DBField("pageSpace")
    private Long space;//页面大小

    public PageSpace() {
    }

    public PageSpace(Integer chnlId, String pageLink, Long speed, Long space, Date checkTime) {
        this.chnlId = chnlId;
        this.pageLink = pageLink;
        this.speed = speed;
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

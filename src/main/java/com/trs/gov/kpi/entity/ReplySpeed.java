package com.trs.gov.kpi.entity;

import com.trs.gov.kpi.annotation.DBField;
import com.trs.gov.kpi.annotation.DBTable;
import lombok.Data;

import java.util.Date;

/**
 * 响应速度
 * <p>
 * Created by ranwei on 2017/6/6.
 */
@Data
@DBTable("webpage")
public class ReplySpeed extends PageIssue {

    @DBField("pageSpeed")
    private Long speed;

    @DBField("pageSpace")
    private Long space;

    public ReplySpeed(Integer typeId, Integer chnlId, String pageLink, Long speed, Long space, Date checkTime) {
        this.typeId = typeId;
        this.chnlId = chnlId;
        this.pageLink = pageLink;
        this.speed = speed;
        this.space = space;
        this.checkTime = checkTime;
    }

    public ReplySpeed() {
    }
}


package com.trs.gov.kpi.entity;

import com.trs.gov.kpi.annotation.DBField;
import com.trs.gov.kpi.annotation.DBTable;
import lombok.Data;

import java.util.Date;

/**
 *  响应速度
 *
 * Created by ranwei on 2017/6/6.
 */
@Data
@DBTable("webpage")
public class ReplySpeed {

    @DBField
    private String id;

    @DBField
    private Integer typeId;//类型编号

    @DBField
    private Integer siteId;//站点编号

    @DBField
    private Integer chnlId;//所在栏目编号

    @DBField
    private String pageLink;//网页链接

    @DBField("pageSpeed")
    private Long speed;

    @DBField("pageSpace")
    private Long space;

    @DBField
    private Date checkTime;//监测时间

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


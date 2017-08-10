package com.trs.gov.kpi.entity.responsedata;

import lombok.Data;

import java.util.Date;

/**
 * 响应速度数据的返回模板
 * Created by ranwei on 2017/6/6.
 */
@Data
public class ReplySpeedResponse {

    private String id;

    private String chnlName;

    private String pageLink;

    private Long replySpeed;

    private Long pageSpace;

    private Date checkTime;
}

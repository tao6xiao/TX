package com.trs.gov.kpi.entity.responsedata;

import lombok.Data;

import java.util.Date;

/**
 * 过深页面数据的返回模板
 * Created by ranwei on 2017/6/6.
 */
@Data
public class PageDepthResponse {

    private String id;

    private String chnlName;

    private String pageLink;

    private Integer pageDepth;

    private Long pageSpace;

    private Date checkTime;
}

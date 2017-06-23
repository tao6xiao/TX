package com.trs.gov.kpi.entity;

import lombok.Data;

import java.util.Date;

/**
 * 报表
 * Created by ranwei on 2017/6/12.
 */
@Data
public class Report {

    private int id;//报表编号

    private int siteId;//站点

    private String title;//标题

    private int typeId;//报表类型编号：1-->按时间节点生成 2-->按时间区间生成

    private Date reportTime;//报表对应时间

    private Date crTime;//报表的生成时间

    private String path;//存放的相对路径
}

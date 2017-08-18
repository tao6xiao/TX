package com.trs.gov.kpi.entity;

import com.trs.gov.kpi.annotation.DBField;
import com.trs.gov.kpi.annotation.DBTable;
import lombok.Data;

import java.util.Date;

/**
 * 报表
 * Created by ranwei on 2017/6/12.
 */
@Data
@DBTable("report")
public class Report {

    @DBField
    private int id;//报表编号

    @DBField
    private int siteId;//站点

    @DBField
    private String title;//标题

    @DBField("type")
    private int typeId;//报表类型编号：1-->按时间节点生成 2-->按时间区间生成

    @DBField
    private Date reportTime;//报表对应时间

    @DBField
    private Date crTime;//报表的生成时间

    @DBField
    private String path;//存放的相对路径
}

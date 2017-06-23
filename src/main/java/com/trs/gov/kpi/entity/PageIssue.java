package com.trs.gov.kpi.entity;

import com.trs.gov.kpi.annotation.DBField;
import com.trs.gov.kpi.annotation.DBTable;
import lombok.Data;

import java.util.Date;

/**
 * 访问分析-->响应速度、页面大小、页面深度、冗余代码、过长url 提取共有字段组成的基类
 * Created by ranwei on 2017/6/20.
 */
@Data
@DBTable("webpage")
public class PageIssue {

    @DBField(autoInc = true)
    String id;//问题编号

    @DBField
    Integer siteId;//站点编号

    @DBField
    Integer chnlId;//所在栏目编号

    @DBField
    String pageLink;//网页链接

    @DBField
    Date checkTime;//监测时间
}

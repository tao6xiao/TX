package com.trs.gov.kpi.entity;

import com.trs.gov.kpi.annotation.DBField;
import com.trs.gov.kpi.annotation.DBTable;
import lombok.Data;

import java.util.Date;

/**
 * Created by ranwei on 2017/6/20.
 */
@Data
@DBTable("webpage")
public class PageIssue {

    @DBField(autoInc = true)
    String id;

    @DBField
    Integer siteId;//站点编号

    @DBField
    Integer chnlId;//所在栏目编号

    @DBField
    String pageLink;//网页链接

    @DBField
    Date checkTime;//监测时间
}

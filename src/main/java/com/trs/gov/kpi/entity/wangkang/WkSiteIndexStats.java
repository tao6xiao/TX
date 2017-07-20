package com.trs.gov.kpi.entity.wangkang;

import com.trs.gov.kpi.annotation.DBField;
import com.trs.gov.kpi.annotation.DBTable;
import lombok.Data;

/**
 * 网站名和网站综合指标
 * Created by li.hao on 2017/7/20.
 */
@Data
@DBTable("Wksiteindexstats")
public class WkSiteIndexStats{

    @DBField
    private Integer siteId;

    @DBField
    private Integer checkId;

    @DBField
    private String siteName;

    @DBField
    private Integer updateContent;

    @DBField
    private Integer contentError;

    @DBField
    private Integer overSpeed;

    @DBField
    private Integer invalidLink;

}

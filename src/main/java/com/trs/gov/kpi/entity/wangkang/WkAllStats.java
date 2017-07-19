package com.trs.gov.kpi.entity.wangkang;

import com.trs.gov.kpi.annotation.DBField;
import com.trs.gov.kpi.annotation.DBTable;
import lombok.Data;

/**
 * 网站更新各项指标数量记录
 *
 * Created by li.hao on 2017/7/12.
 */
@Data
@DBTable("wkallstats")
public class WkAllStats {

    @DBField
    private Integer checkId;//检测编号

    @DBField
    private Integer siteId;//网站编号

    @DBField
    private Integer updateContent;//网站更新数

    @DBField
    private Integer avgSpeed;// 平均访问速度

    @DBField
    private Integer errorInfo;// 错别字，敏感词等个数

    @DBField
    private Integer invalidLink;// 失效链接个数

}

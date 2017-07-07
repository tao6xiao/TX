package com.trs.gov.kpi.entity.wangkang;

import com.trs.gov.kpi.annotation.DBField;
import com.trs.gov.kpi.annotation.DBTable;
import lombok.Data;

/**
 * Created by li.hao on 2017/7/7.
 */
@Data
@DBTable("wkscore")
public class WkSocre {

    @DBField
    private Integer id;

    @DBField
    private Integer siteId;//网站编号

    @DBField
    private String siteName;//网站名称

    @DBField
    private Data checkTime;//检测时间

    @DBField
    private Integer total;//综合评分

    @DBField
    private Integer invalidLink;//链接可用性分数

    @DBField
    private Integer contentError;//内容检测分数

    @DBField
    private Integer overSpeed;//访问速度分数

    @DBField
    private Integer updateContent;//网站更新分数
}

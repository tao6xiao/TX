package com.trs.gov.kpi.entity.wangkang;

import com.trs.gov.kpi.annotation.DBField;
import com.trs.gov.kpi.annotation.DBTable;
import lombok.Data;

import java.util.Date;

/**
 * 网站分数记录
 *
 * Created by li.hao on 2017/7/7.
 */
@Data
@DBTable("wkscore")
public class WkScore {

    @DBField(autoInc = true)
    private Integer id;

    @DBField
    private Integer checkId;

    @DBField
    private Integer siteId;//网站编号

    @DBField
    private Date checkTime;//检测时间

    @DBField
    private double total = 0;//综合评分

    @DBField
    private double invalidLink = 0;//链接可用性分数

    @DBField
    private double contentError = 0;//内容检测分数

    @DBField
    private double overSpeed = 0;//访问速度分数

    @DBField
    private double updateContent = 0;//网站更新分数
}

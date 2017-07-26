package com.trs.gov.kpi.entity.responsedata;

import lombok.Data;

import java.util.Date;

/**
 * Created by li.hao on 2017/7/12.
 */
@Data
public class WkOneSiteScoreResponse {

    private Integer checkId;//检查编号

    private double total;//综合评分

    private double invalidLink;//链接可用性分数

    private double contentError;//内容检测分数

    private double overSpeed;//访问速度分数

    private double updateContent;//网站更新分数

    private Date checkTime;//入库时间
}

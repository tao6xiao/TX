package com.trs.gov.kpi.entity.responsedata;

import lombok.Data;

/**
 * 稿件分类统计计数器
 * Created by linwei on 2017/6/15.
 */
@Data
public class DocTypeCounterResponse {

    // 稿件类型
    private Integer type;

    // 类型名称
    private String name;

    // 数量
    private Long count;

    public DocTypeCounterResponse(Integer type, Long count) {
        this.type = type;
        this.count = count;
    }

}

package com.trs.gov.kpi.entity.responsedata;

import lombok.Data;

/**
 * 统计数据的返回模板
 * Created by rw103 on 2017/5/12.
 */
@Data
public class Statistics {

    private int type;

    private String name;

    private int count;

}

package com.trs.gov.kpi.entity.responsedata;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 部门归纳统计返回对象
 * Created by he.lang on 2017/6/16.
 */
@Data
public class DeptInductionResponse {
    private String dept;

    private List<Statistics> data;

    public void addStatistics(Statistics stat) {
        if (data == null) {
            data = new ArrayList<>();
        }
        this.data.add(stat);
    }
}

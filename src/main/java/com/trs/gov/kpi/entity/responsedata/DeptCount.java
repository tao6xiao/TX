package com.trs.gov.kpi.entity.responsedata;

import lombok.Data;

/**
 * Created by he.lang on 2017/6/16.
 */
@Data
public class DeptCount {
    private String dept;

    private Integer value;

    public DeptCount(){

    }
    public DeptCount(String dept, Integer value){
        this.dept = dept;
        this.value = value;
    }
}

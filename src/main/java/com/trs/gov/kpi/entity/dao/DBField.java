package com.trs.gov.kpi.entity.dao;

import lombok.Data;

/**
 * Created by linwei on 2017/6/20.
 */
@Data
public class DBField {
    private String name;
    private Object value;

    public DBField(String fieldName, Object value) {
        this.name = fieldName;
        this.value = value;
    }
}

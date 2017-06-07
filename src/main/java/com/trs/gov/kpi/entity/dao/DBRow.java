package com.trs.gov.kpi.entity.dao;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by linwei on 2017/6/7.
 */
public class DBRow {

    @Getter
    private List<String> fields = new ArrayList<>();

    @Getter
    private List<Object> values = new ArrayList<>();

    /**
     * 添加一个字段
     *
     * @param fieldName 字段名
     * @param value 字段值
     */
    public void addField(String fieldName, Object value) {
        fields.add(fieldName);
        values.add(value);
    }
}

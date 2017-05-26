package com.trs.gov.kpi.entity.dao;

import com.trs.gov.kpi.utils.StringUtil;
import lombok.Getter;

/**
 * 用于数据查询的排序字段
 *
 * Created by linwei on 2017/5/22.
 */
public class SortDBField {

    @Getter
    private String fieldName;

    @Getter
    private boolean isAsc = true;

    public SortDBField(String fieldName){
        this(fieldName, true);
    }
    public SortDBField(String fieldName, boolean isAsc) {
        if (StringUtil.isEmpty(fieldName)) {
            throw new IllegalArgumentException("empty field");
        }
        this.fieldName = fieldName.trim();
        this.isAsc = isAsc;
    }
}

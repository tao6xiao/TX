package com.trs.gov.kpi.entity.dao;

/**
 * Created by linwei on 2017/5/22.
 */
public class SortDBField {

    private String fieldName;

    private boolean isAsc = true;

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public boolean isAsc() {
        return isAsc;
    }

    public void setAsc(boolean asc) {
        isAsc = asc;
    }
}

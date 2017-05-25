package com.trs.gov.kpi.entity.dao;

import com.trs.gov.kpi.entity.responsedata.Pager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by linwei on 2017/5/22.
 */
public class QueryFilter {

    private List<SortDBField> sortFields;
    private List<CondDBField> condFields;
    private List<String> fieldNames;
    private QueryFilterPager pager;

    public void addSortField(String filedName, boolean isAsc) {
        if (filedName == null || filedName.trim().isEmpty()) {
            return;
        }
        if (sortFields == null) {
            sortFields = new ArrayList<>();
        }

        SortDBField field = new SortDBField();
        field.setFieldName(filedName);
        field.setAsc(isAsc);
        sortFields.add(field);
    }

    public void addSortField(SortDBField sortField) {
        if (sortField == null) {
            return;
        }
        if (sortFields == null) {
            sortFields = new ArrayList<>();
        }
        sortFields.add(sortField);
    }

    public List<SortDBField> getSortFields() {
        return sortFields;
    }

    public CondDBField addCond(String fieldName, Object value) {
        if (fieldName == null || fieldName.trim().isEmpty() || value == null) {
            return null;
        }

        if (condFields == null) {
            condFields = new ArrayList<>();
        }
        CondDBField field = new CondDBField(fieldName, value);
        condFields.add(field);
        return field;
    }

    public void addCond(CondDBField field) {
        if (field == null) {
            return;
        }
        if (condFields == null) {
            condFields = new ArrayList<>();
        }
        condFields.add(field);
    }

    public List<CondDBField> getCondFields() {
        return condFields;
    }

    public void setPager(Pager pager) {
        if (pager != null) {
            this.pager = new QueryFilterPager();
            this.pager.setCount(pager.getPageSize());
            this.pager.setOffset((pager.getCurrPage()-1) * pager.getPageSize());
        }
    }

    public QueryFilterPager getPager() {
        return this.pager;
    }

    public void setFieldNames(List<String> nameList) {
        this.fieldNames = nameList;
    }

    public List<String> getFieldNames() {
        return this.fieldNames;
    }
}

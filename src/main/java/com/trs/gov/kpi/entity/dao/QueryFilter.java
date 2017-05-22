package com.trs.gov.kpi.entity.dao;

import com.trs.gov.kpi.entity.responsedata.Pager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by linwei on 2017/5/22.
 */
public class QueryFilter {

//    private List<QueryDBField> sortFields;
//    private List<QueryDBField> condFields;
    private List<String> sortFields;
    private List<CondDBField> condFields;
    private List<String> selectFields;
    private QueryFilterPager pager;

    public void addSortField(String filedName, boolean isAsc) {
        if (filedName == null || filedName.trim().isEmpty()) {
            return;
        }
        if (sortFields == null) {
            sortFields = new ArrayList<>();
        }
        String field = filedName;
//        QueryDBField field = new QueryDBField();
//        field.setName(filedName);
        if (isAsc) {
            field += " asc";
//            field.setSort("asc");
        } else {
//            field.setSort("desc");
            field += " desc";
        }

        sortFields.add(field);
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
        condFields.add(field);
    }

    public void addSelectField(String fieldName) {
        if (fieldName == null || fieldName.trim().isEmpty()) {
            return;
        }
        if (selectFields == null) {
            selectFields = new ArrayList<>();
        }

        selectFields.add(fieldName);
    }

    public List<CondDBField> getCondFields() {
        return condFields;

//        return condFields;
//
//        StringBuilder whereSql = new StringBuilder();
//        if (condFields != null) {
//            for (String cond : condFields) {
//                whereSql.append(cond);
//                whereSql.append(" AND ");
//            }
//            whereSql.setLength(whereSql.length() - " AND ".length());
//            whereSql = new StringBuilder(whereSql.toString());
//        }
//
//        if (sortFields != null) {
//            whereSql.append(" order by ");
//            for (String cond : sortFields) {
//                whereSql.append(cond);
//                whereSql.append(",");
//            }
//            whereSql.setLength(whereSql.length() - ",".length());
//        }
//        return whereSql.toString();
    }

    public String getSelectSql() {
        if (selectFields == null) {
            return "*";
        } else {
            StringBuilder selectSql = new StringBuilder();
            for (String field : selectFields) {
                selectSql.append(field);
                selectSql.append(",");
            }
            selectSql.setLength(selectSql.length()-1);
            return selectSql.toString();
        }
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
}

package com.trs.gov.kpi.entity.dao;

import com.trs.gov.kpi.entity.responsedata.Pager;
import com.trs.gov.kpi.utils.StringUtil;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * 通用查询条件类
 * Created by linwei on 2017/5/22.
 */
public class QueryFilter {

    // table里面的字段名集合
    @Getter
    private List<String> fieldNames;

    // where条件
    @Getter
    private List<CondDBField> condFields;

    // 排序字段
    @Getter
    private List<SortDBField> sortFields;

    // 分组字段
    @Getter
    private List<String> groupFields;

    // 分页
    @Getter
    private DBPager pager;

    /**
     * 添加一个排序字段，默认升序
     *
     * @param filedName
     */
    public void addSortField(String filedName) {
        addSortField(filedName, true);
    }

    /**
     * 添加一个排序字段
     *
     * @param filedName
     * @param isAsc
     */
    public void addSortField(String filedName, boolean isAsc) {
        if (StringUtil.isEmpty(filedName)) {
            return;
        }
        this.addSortField(new SortDBField(filedName.trim(), isAsc));
    }

    /**
     * 添加一个排序字段
     *
     * @param sortField
     *
     */
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

    /**
     * 添加或者的复合条件
     *
     * @param cond
     * @return
     */
    public CondDBField addOrConds(OrCondDBFields cond) {
        return addCond("OR_COMPLEX_FIELD", cond);
    }

    /**
     * 添加某个字段的查询条件
     *
     * @param fieldName
     * @param value
     * @return
     */
    public CondDBField addCond(String fieldName, Object value) {
        if (StringUtil.isEmpty(fieldName) || value == null) {
            return null;
        }
        return addCond(new CondDBField(fieldName, value));
    }

    /**
     * 添加一个查询条件
     *
     * @param field
     * @return
     */
    public CondDBField addCond(CondDBField field) {
        if (field == null) {
            return null;
        }
        if (condFields == null) {
            condFields = new ArrayList<>();
        }
        condFields.add(field);
        return field;
    }

    /**
     * 由外面的分页转换为数据库分页
     *
     * @param pager
     */
    public void setPager(Pager pager) {
        if (pager != null) {
            this.pager = new DBPager((pager.getCurrPage() - 1) * pager.getPageSize(), pager.getPageSize());
        }
    }

    /**
     * 设置分页
     * @param pager
     */
    public void setPager(DBPager pager) {
        this.pager = pager;
    }

    /**
     * 设置表中的字段
     *
     * @param nameList
     */
    private void setFieldNames(List<String> nameList) {
        if (nameList != null) {
            this.fieldNames = nameList;
        }
    }

    /**
     * 设置表查询用的表
     *
     * @param table
     */
    public void setTable(Table table) {
        if (table == null) {
            throw new IllegalArgumentException("empty table");
        }
        setFieldNames(table.getFields());
    }

    /**
     * 添加分组字段
     *
     * @param fieldName
     */
    public void addGroupField(String fieldName) {
        if (StringUtil.isEmpty(fieldName)) {
            return;
        }
        if (this.groupFields == null) {
            this.groupFields = new ArrayList<>();
        }
        this.groupFields.add(fieldName);
    }


}

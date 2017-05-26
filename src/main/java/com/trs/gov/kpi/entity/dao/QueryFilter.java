package com.trs.gov.kpi.entity.dao;

import com.trs.gov.kpi.entity.responsedata.Pager;
import com.trs.gov.kpi.utils.StringUtil;
import javafx.scene.control.Tab;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * 通用查询条件类
 * Created by linwei on 2017/5/22.
 */
public class QueryFilter {

    private static final String OR_COMPLEX_FIELD_NAME = "OR_COMPLEX_FIELD";

    @Getter
    private final Table table;

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

    public QueryFilter(Table table) {
        if (table == null) {
            throw new IllegalArgumentException("invalid table");
        }
        this.table = table;
        setFieldNames(table.getFields());
    }

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

        checkFieldName(sortField.getFieldName());

        if (sortFields == null) {
            sortFields = new ArrayList<>();
        }
        sortFields.add(sortField);
    }

    /**
     * 添加或者的复合条件
     *
     * @param cond
     * @return
     */
    public CondDBField addOrConds(OrCondDBFields cond) {
        return addCond(OR_COMPLEX_FIELD_NAME, cond);
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
     * 检查字段名是否合法
     *
     * @param fieldName
     */
    private void checkFieldName(String fieldName) {
        if (!fieldName.equals(OR_COMPLEX_FIELD_NAME) && !table.containsField(fieldName)) {
            throw new IllegalArgumentException("invalid field name " + fieldName + " of table " + table.getTableName());
        }
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

        checkFieldName(field.getFieldName());

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
     * 添加分组字段
     *
     * @param fieldName
     */
    public void addGroupField(String fieldName) {
        if (StringUtil.isEmpty(fieldName)) {
            return;
        }
        checkFieldName(fieldName);

        if (this.groupFields == null) {
            this.groupFields = new ArrayList<>();
        }
        this.groupFields.add(fieldName);
    }

}

package com.trs.gov.kpi.entity.dao;

import com.trs.gov.kpi.utils.StringUtil;

import java.util.Arrays;
import java.util.List;

/**
 * Created by linwei on 2017/5/26.
 */
public enum Table {

    ISSUE("issue", Arrays.asList("id", "siteId", "typeId", "subTypeId",
            "detail", "issueTime", "isResolved", "isDel", "customer1", "customer2", "customer3"));

    // 表名
    private final String tableName;
    // 表中字段
    private final List<String> fields;

    Table(String tableName, List<String> fieldNames) {
        this.tableName = tableName;
        this.fields = fieldNames;
    }

    /**
     * 获取表字段
     * @return
     */
    public List<String> getFields() {
        return this.fields;
    }

    /**
     * 根据表名获取表
     *
     * @param tableName
     * @return
     */
    public static Table valueOfTableName(String tableName) {
        if (StringUtil.isEmpty(tableName)) {
            return null;
        }

        Table[] tables = Table.values();
        for (Table table : tables) {
            if (table.name().equalsIgnoreCase(tableName)) {
                return table;
            }
        }
        return null;
    }

}

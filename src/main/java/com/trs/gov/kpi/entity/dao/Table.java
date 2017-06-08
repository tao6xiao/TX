package com.trs.gov.kpi.entity.dao;

import com.trs.gov.kpi.utils.StringUtil;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;

/**
 * Created by linwei on 2017/5/26.
 */
public enum Table {

    ISSUE("issue", Arrays.asList("id", "siteId", "typeId", "subTypeId",
            "detail", "issueTime", "isResolved", "isDel", "workOrderStatus", "customer1", "customer2", "customer3")),
    WEB_PAGE("webpage", Arrays.asList("id", "siteId", "typeId", "chnlId",
            "pageLink", "replySpeed", "pageSpace", "pageDepth", "repeatPlace", "repeatDegree", "updateTime", "urlLength", "checkTime", "isResolved", "isDel")),
    FREQ_SETUP("frequencysetup", Arrays.asList("id", "siteId", "presetFeqId", "chnlId", "setTime", "isOpen"));

    // 表名
    @Getter
    private final String tableName;

    // 表中字段
    @Getter
    private final List<String> fields;

    Table(String tableName, List<String> fieldNames) {
        this.tableName = tableName;
        this.fields = fieldNames;
    }

    /**
     * 表是否包含指定字段
     *
     * @param fieldName
     * @return
     */
    public boolean containsField(String fieldName) {
        if (StringUtil.isEmpty(fieldName)) {
            return false;
        }

        for (String field : fields) {
            if (field.equals(fieldName)) {
                return true;
            }
        }
        return false;
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
            if (table.getTableName().equalsIgnoreCase(tableName)) {
                return table;
            }
        }
        return null;
    }

}

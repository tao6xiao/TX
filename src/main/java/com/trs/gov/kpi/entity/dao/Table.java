package com.trs.gov.kpi.entity.dao;

import com.trs.gov.kpi.constant.Constants;
import com.trs.gov.kpi.utils.StringUtil;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;

/**
 * Created by linwei on 2017/5/26.
 */
public enum Table {

    ISSUE("issue", Arrays.asList(Constants.DB_FIELD_ID, Constants.DB_FIELD_SITE_ID, "typeId", "subTypeId",
            "detail", "issueTime", "checkTime", "isResolved", "isDel", "workOrderStatus", "deptId", "customer1", "customer2", "customer3")),
    WEB_PAGE("webpage", Arrays.asList(Constants.DB_FIELD_ID, Constants.DB_FIELD_SITE_ID, "typeId", Constants.DB_FIELD_CHNL_ID,
            "pageLink", "replySpeed", "pageSpace", "pageDepth", "repeatPlace", "repeatDegree", "updateTime", "urlLength", "checkTime", "isResolved", "isDel")),
    FREQ_SETUP("frequencysetup", Arrays.asList(Constants.DB_FIELD_ID, Constants.DB_FIELD_SITE_ID, "presetFeqId", Constants.DB_FIELD_CHNL_ID, "setTime", "isOpen")),
    REPORT("report", Arrays.asList("id", "siteId", "title", "reportTime", "crTime", "type", "path")),
    DUTY_DEPT("dutydept", Arrays.asList(Constants.DB_FIELD_CHNL_ID, "siteId", "deptId", "contain")),
    // TODO REVIEW LINWEI DO_li.hao 可以通过持久化对象，获取数据库字段，这样就更容易维护
    MONITOR_RECORD("monitorrecord",Arrays.asList(Constants.DB_FIELD_ID, Constants.DB_FIELD_SITE_ID, "taskId", "taskStatus", "result", "beginTime", "endTime"));


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

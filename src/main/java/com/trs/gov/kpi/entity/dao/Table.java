package com.trs.gov.kpi.entity.dao;

import com.trs.gov.kpi.entity.*;
import com.trs.gov.kpi.utils.DBUtil;
import com.trs.gov.kpi.utils.StringUtil;
import lombok.Getter;

import java.util.List;

/**
 * Created by linwei on 2017/5/26.
 */
public enum Table {

    ISSUE("issue", Issue.class),
    WEB_PAGE("webpage", PageIssue.class, UrlLength.class, PageDepth.class,
            PageSpace.class, ReplySpeed.class, RepeatCode.class),
    FREQ_SETUP("frequencysetup", FrequencySetup.class),
    DUTY_DEPT("dutydept", DutyDept.class),
    REPORT("report", Report.class),
    MONITOR_RECORD("monitorrecord", MonitorRecord.class),
    LINK_CONTENT_STATS("linkcontentstats", LinkContentStats.class),
    MONITOR_FREQUENCY("monitorfrequency", MonitorFrequency.class);

    // 表名
    @Getter
    private final String tableName;

    // 表中字段
    @Getter
    private final List<String> fields;

    Table(String tableName, Class<?> ...poClass) {
        this.tableName = tableName;
        this.fields = DBUtil.collectAllDBFieldNames(poClass);
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

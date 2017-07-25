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

    ISSUE("issue", Arrays.asList(Constants.DB_FIELD_ID, Constants.DB_FIELD_SITE_ID, Constants.DB_FIELD_TYPE_ID,Constants.DB_FIELD_CHECK_TIME, "subTypeId",
            "detail", "issueTime", "isResolved", "isDel", "workOrderStatus", "deptId", "customer1", "customer2", "customer3")),
    WEB_PAGE("webpage", Arrays.asList(Constants.DB_FIELD_ID, Constants.DB_FIELD_SITE_ID, Constants.DB_FIELD_TYPE_ID,Constants.DB_FIELD_CHECK_TIME, "chnlId","chnlName",
            "pageLink", "replySpeed", "pageSpace", "pageDepth", "repeatPlace", "repeatDegree", "updateTime", "urlLength", "isResolved", "isDel")),
    FREQ_SETUP("frequencysetup", Arrays.asList(Constants.DB_FIELD_ID, Constants.DB_FIELD_SITE_ID, "presetFeqId", "chnlId", "setTime", "isOpen")),
    REPORT("report", Arrays.asList("id", "siteId", "title", "reportTime", "crTime", "type", "path")),
    WK_SITEMANAGEMENT("wksite", Arrays.asList(Constants.DB_FIELD_SITE_ID, Constants.DB_FIELD_CHECK_TIME,"siteName",
            "siteIndexUrl", "deptAddress", "deptLatLng", "autoCheckType", "checkStatus", "isDel")),
    WK_ISSUE("wkissue", Arrays.asList(Constants.DB_FIELD_ID, Constants.DB_FIELD_SITE_ID,Constants.DB_FIELD_TYPE_ID,Constants.DB_FIELD_CHECK_TIME,"checkId",
            "subTypeId","chnlName","url","detailInfo","parentUrl","isResolved","resolvedTime","isDel","locationUrl")),
    WK_ALL_STATS("wkallstats", Arrays.asList(Constants.DB_FIELD_SITE_ID, Constants.DB_FIELD_CHECK_ID, "updateContent", "avgSpeed", "errorInfo", "invalidLink")),
    WK_ISSUE_COUNT("wkissuecount", Arrays.asList(Constants.DB_FIELD_SITE_ID, Constants.DB_FIELD_CHECK_ID, Constants.DB_FIELD_TYPE_ID, Constants.DB_FIELD_CHECK_TIME, "isResolved", "unResolved")),
    WK_EVERY_LINK("wkeverylink", Arrays.asList(Constants.DB_FIELD_ID, Constants.DB_FIELD_SITE_ID, Constants.DB_FIELD_CHECK_ID,Constants.DB_FIELD_CHECK_TIME, "url", "md5", "accessSpeed")),
    WK_CHECK_TIME("wkchecktime", Arrays.asList(Constants.DB_FIELD_SITE_ID, Constants.DB_FIELD_CHECK_ID, "beginTime", "endTime", "checkStatus")),
    WK_SCORE("wkscore", Arrays.asList(Constants.DB_FIELD_ID, Constants.DB_FIELD_SITE_ID, Constants.DB_FIELD_CHECK_ID,Constants.DB_FIELD_CHECK_TIME, "total", "invalidLink", "contentError", "overSpeed", "updateContent")),
    WK_SITE_INDEX_STATS("wksiteindexstats", Arrays.asList(Constants.DB_FIELD_SITE_ID, Constants.DB_FIELD_CHECK_ID, "siteName","updateContent", "overSpeed", "contentError", "invalidLink"));

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

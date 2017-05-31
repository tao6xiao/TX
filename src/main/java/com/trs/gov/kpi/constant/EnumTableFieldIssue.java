package com.trs.gov.kpi.constant;

import lombok.Getter;

/**
 * issue表字段枚举
 * Created by he.lang on 2017/5/31.
 */
public enum EnumTableFieldIssue {
    ID("id"),
    SITE_ID("siteId"),
    TYPE_ID("typeId"),
    SUBTYPE_ID("subTypeId"),
    DETAIL("detail"),
    ISSUE_TIME("issueTime"),
    IS_RESOLVED("isResolved"),
    IS_DEL("isDel"),
    CUSTOMER1("customer1"),
    CUSTOMER2("customer2"),
    CUSTOMER3("customer3");

    public final String fieldName;

    private EnumTableFieldIssue(String fieldName) {
        this.fieldName = fieldName;
    }
}

package com.trs.gov.kpi.constant;

/**
 * 信息预警子类型类型
 */
public enum InfoWarningType {

    UPDATE_WARNING(1,"栏目更新预警"),
    SELF_CHECK_WARNING(2,"信息自查预警");

    public final int value;

    public final String name;

    InfoWarningType(int type, String name) {
        this.value = type;
        this.name = name;
    }
}

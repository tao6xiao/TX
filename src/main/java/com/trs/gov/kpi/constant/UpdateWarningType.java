package com.trs.gov.kpi.constant;

/**
 * 信息预警子类型类型
 */
public enum UpdateWarningType {

    UPDATE_WARNING(511, "栏目更新预警"),
    SELF_CHECK_WARNING(512, "信息自查预警");

    public final int value;

    public final String name;

    private UpdateWarningType(int type, String name) {
        this.value = type;
        this.name = name;
    }
}

package com.trs.gov.kpi.constant;

/**
 * 互动回应预警子类型
 */
public enum RespondWarningType {

    RESPOND_WARNING(2,"咨询回应预警"),
    FEEDBACK_WARNING(3,"征集反馈预警");

    public final int value;

    public final String name;

    RespondWarningType(int type, String name) {
        this.value = type;
        this.name = name;
    }
}

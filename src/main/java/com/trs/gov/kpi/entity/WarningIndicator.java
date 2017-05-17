package com.trs.gov.kpi.entity;

/**
 * Created by ranwei on 2017/5/17.
 */
public enum WarningIndicator {

    UPDATE_WARNING(1,"栏目更新预警");

    public final int value;

    public final String name;

    WarningIndicator(int type, String name) {
        this.value = type;
        this.name = name;
    }
}

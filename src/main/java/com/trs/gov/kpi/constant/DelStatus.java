package com.trs.gov.kpi.constant;

/**
 * 枚举是否删除
 * Created by he.lang on 2017/5/19.
 */
public enum DelStatus {

    UN_DELETE(0, "未删除"),
    DELETED(1, "已删除");

    public final int value;

    public final String name;

    private DelStatus(int value, String name) {
        this.value = value;
        this.name = name;
    }
}

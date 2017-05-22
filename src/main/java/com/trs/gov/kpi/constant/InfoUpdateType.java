package com.trs.gov.kpi.constant;

/**
 * 信息更新的子类型
 */
public enum InfoUpdateType {

    UPDATE_NOT_INTIME(1,"更新不及时");

    public final int value;

    public final String name;

    private InfoUpdateType(int type, String name) {
        this.value = type;
        this.name = name;
    }
}

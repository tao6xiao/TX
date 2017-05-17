package com.trs.gov.kpi.entity;

/**
 * Created by ranwei on 2017/5/17.
 */
public enum InfoUpdateType {

    UPDATE_NOT_INTIME(1,"更新不及时");

    public final int value;

    public final String name;

    InfoUpdateType(int type, String name) {
        this.value = type;
        this.name = name;
    }
}

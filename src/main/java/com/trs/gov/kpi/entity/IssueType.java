package com.trs.gov.kpi.entity;

/**
 * Created by ranwei on 2017/5/17.
 */
public enum IssueType {

    INVALID_LINK(1,"失效链接"),UPDATE_NOT_INTIME(2,"更新不及时");

    public final int value;

    public final String name;

    IssueType(int type, String name) {
        this.value = type;
        this.name = name;
    }
}

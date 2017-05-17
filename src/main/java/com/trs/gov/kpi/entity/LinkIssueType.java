package com.trs.gov.kpi.entity;

/**
 * Created by ranwei on 2017/5/17.
 */
public enum LinkIssueType {

    INVALID_LINK(1,"失效链接"),INVALID_IMAGE(2,"失效图片"),LINK_TIME_OUT(3,"链接超时");

    public final int value;

    public final String name;

    LinkIssueType(int type, String name) {
        this.value = type;
        this.name = name;
    }
}

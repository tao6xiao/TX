package com.trs.gov.kpi.constant;

/**
 * 链接可用性的子类型
 */
public enum LinkIssueType {

    INVALID_LINK(1,"链接失效"),INVALID_IMAGE(2,"图片失效"),CONNECTION_TIME_OUT(3,"连接超时");

    public final int value;

    public final String name;

    LinkIssueType(int type, String name) {
        this.value = type;
        this.name = name;
    }
}

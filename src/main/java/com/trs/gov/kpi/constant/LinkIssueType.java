package com.trs.gov.kpi.constant;

import java.util.ArrayList;
import java.util.List;

/**
 * 链接可用性的子类型
 */
public enum LinkIssueType {

    INVALID_LINK(11, "链接失效"), INVALID_IMAGE(12, "图片失效"), CONNECTION_TIME_OUT(13, "连接超时");

    public final int value;

    public final String name;

    private LinkIssueType(int type, String name) {
        this.value = type;
        this.name = name;
    }

    public static List<LinkIssueType> findTypesByName(String name) {
        List<LinkIssueType> matchedType = new ArrayList<>();
        LinkIssueType[] types = LinkIssueType.values();
        for (LinkIssueType type : types) {
            if (type.name.indexOf(name) >= 0) {
                matchedType.add(type);
            }
        }
        return matchedType;
    }

    public static LinkIssueType valueOf(int value) {
        LinkIssueType[] types = LinkIssueType.values();
        for (LinkIssueType type : types) {
            if (type.value == value) {
                return type;
            }
        }
        return null;
    }
}

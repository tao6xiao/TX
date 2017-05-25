package com.trs.gov.kpi.constant;

import lombok.Getter;

/**
 * 枚举是否解决
 * Created by he.lang on 2017/5/19.
 */
public enum ResolveStatus {
    UN_RESOLVED(0, "未解决"),
    RESOLVED(1, "已处理"),
    IGNORED(2, "已忽略");

    public final int value;

    public final String name;

    ResolveStatus(int value, String name) {
        this.value = value;
        this.name = name;
    }
}

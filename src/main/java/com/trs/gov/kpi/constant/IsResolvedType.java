package com.trs.gov.kpi.constant;

import lombok.Getter;

/**
 * 枚举是否解决
 * Created by he.lang on 2017/5/19.
 */
public enum IsResolvedType {
    IS_NOT_RESOLVED(0, "未解决"),
    IS_RESOLVED(1, "已处理"),
    IS_IGNORED(2, "已忽略");

    @Getter
    private int code;

    @Getter
    private String name;

    private IsResolvedType(int code, String name) {
        this.code = code;
        this.name = name;
    }
}

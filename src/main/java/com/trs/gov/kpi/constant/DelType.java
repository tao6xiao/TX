package com.trs.gov.kpi.constant;

import lombok.Getter;

/**
 * 枚举是否删除
 * Created by he.lang on 2017/5/19.
 */
public enum DelType {

    IS_NOT_DEL(0, "未删除"),
    IS_DEL(1, "已删除");

    @Getter
    private int code;

    @Getter
    private String name;

    private DelType(int code, String name) {
        this.code = code;
        this.name = name;
    }
}

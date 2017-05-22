package com.trs.gov.kpi.constant;

import lombok.Getter;

/**
 *
 * 首页信息更新不及时有关栏目类型
 * Created by he.lang on 2017/5/22.
 */
public enum EnumIndexUpdateType {
    INVALID(-1, "不合法"),
    ALL(1,"所有栏目"),
    A_TYPE(2, "A类"),
    HOMEPAGE(3, "首页"),
    NULL_CHANNEL(4, "空栏目");
    @Getter
    private int code;

    @Getter
    private String name;

    private EnumIndexUpdateType(int code, String name){
        this.code = code;
        this.name = name;
    }

    public static EnumIndexUpdateType valueOf(int code){
        for (EnumIndexUpdateType type : EnumIndexUpdateType.values()) {
            if(type.getCode() == code){
                return type;
            }
        }
        return INVALID;
    }
}

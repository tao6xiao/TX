package com.trs.gov.kpi.constant;

/**
 * Created by linwei on 2017/5/24.
 */
public enum EnumCheckJobType {

    CHECK_HOME_PAGE(1, "homepage"),
    CHECK_LINK(2, "link"),
    CHECK_CONTENT(3, "content"),
    CHECK_INFO_UPDATE(4, "infoUpdate");

    public final int value ;
    public final String name;

    EnumCheckJobType(int value, String name) {
        this.value = value;
        this.name = name;
    }
}

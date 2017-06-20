package com.trs.gov.kpi.constant;

/**
 * Created by linwei on 2017/5/24.
 */
public enum EnumCheckJobType {

    CHECK_HOME_PAGE(1),
    CHECK_LINK(2),
    CHECK_CONTENT(3),
    CHECK_INFO_UPDATE(4),
    CALCULATE_PERFORMANCE(5),
    TIMENODE_REPORT_GENERATE(6),
    TIMEINTERVAL_REPORT_GENERATE(7);

    public final int value;

    EnumCheckJobType(int value) {
        this.value = value;
    }
}

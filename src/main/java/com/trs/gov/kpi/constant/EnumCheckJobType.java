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
    TIMEINTERVAL_REPORT_GENERATE(7),
    SERVICE_LINK(8);

    public final int value;

    EnumCheckJobType(int value) {
        this.value = value;
    }

    public static EnumCheckJobType valueOf(Integer id) {
        EnumCheckJobType[] all = EnumCheckJobType.values();
        for (EnumCheckJobType type : all) {
            if (type.value == id) {
                return type;
            }
        }
        return null;
    }

}

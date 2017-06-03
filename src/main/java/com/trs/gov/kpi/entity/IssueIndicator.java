package com.trs.gov.kpi.entity;

/**
 * Created by rw103 on 2017/5/12.
 */
public enum IssueIndicator {

    /**
     * 1-->已解决问题  10-19-->待解决问题   20-29-->预警
     */
    SOLVED(1, "已解决"),
    UN_SOLVED(10, "待解决"), UPDATE_NOT_INTIME(11, "更新不及时"),
    WARNING(20, "预警"), UPDATE_WARNING(21, "更新预警");

    public final int value;

    private final String name;

    IssueIndicator(int type, String name) {
        this.value = type;
        this.name = name;
    }

}

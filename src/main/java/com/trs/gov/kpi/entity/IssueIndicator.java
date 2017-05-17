package com.trs.gov.kpi.entity;

/**
 * Created by rw103 on 2017/5/12.
 */
public enum IssueIndicator {

    SOLVED(1, "已解决"),
    UN_SOLVED(10, "待解决"), UPDATE_NOT_INTIME(11, "更新不及时"),
    UPDATE_WARNING(20, "更新预警");

    public final int value;

    public final String name;

    IssueIndicator(int type, String name) {
        this.value = type;
        this.name = name;
    }

}

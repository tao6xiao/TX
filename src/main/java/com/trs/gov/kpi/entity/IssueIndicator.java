package com.trs.gov.kpi.entity;

import lombok.Getter;

/**
 * Created by rw103 on 2017/5/12.
 */
public enum IssueIndicator {

    /**
     * 1-->已解决问题  10-19-->待解决问题   20-29-->预警
     */
    SOLVED(1, "已解决"),SOLVED_ALL(2,"已解决问题和预警"),
    UN_SOLVED(11, "待解决"), UN_SOLVED_ISSUE(12, "待解决问题"),
    WARNING(21, "待解决预警");

    public final int value;

    @Getter
    private final String name;

    IssueIndicator(int type, String name) {
        this.value = type;
        this.name = name;
    }

}

package com.trs.gov.kpi.entity;

/**
 * Created by rw103 on 2017/5/12.
 */
public enum SolveStatus {

    UN_SOLVED(0, "待解决"),SOLVED(1,"已处理");


    private final int type;
    private final String name;

    SolveStatus(int type, String name) {
        this.type = type;
        this.name = name;
    }
}

package com.trs.gov.kpi.entity;

/**
 * Created by rw103 on 2017/5/12.
 */
public enum SolveStatus {

    UN_SOLVED(0, "待解决"),SOLVED(1,"已解决");

    public final int value;

    private final String name;

    SolveStatus(int type, String name) {
        this.value = type;
        this.name = name;
    }

}

package com.trs.gov.kpi.entity;

/**
 * Created by rw103 on 2017/5/12.
 */
public enum CountIndicator {

    UN_SOLVED(0, "待解决"),SOLVED(1,"已解决"),UPDATE_WARNING(2,"更新预警"),UPDATE_NOT_INTIME(3,"更新不及时");

    public final int value;

    public final String name;

    CountIndicator(int type, String name) {
        this.value = type;
        this.name = name;
    }

}

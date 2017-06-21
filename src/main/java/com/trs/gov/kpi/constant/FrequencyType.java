package com.trs.gov.kpi.constant;

import lombok.Getter;

/**
 * Created by he.lang on 2017/5/16.
 */
public enum FrequencyType {

    HOMEPAGE_AVAILABILITY(1, "首页可用性", FreqUnit.TIMES_PER_DAY),
    TOTAL_BROKEN_LINKS(2, "全站失效链接", FreqUnit.DAYS_PER_TIME),
    WRONG_INFORMATION(3, "信息错误", FreqUnit.DAYS_PER_TIME);

    @Getter
    private int typeId;//类型id

    @Getter
    private String name;//类型名

    @Getter
    private FreqUnit freqUnit;//粒度

    private FrequencyType(int typeId, String name, FreqUnit freqUnit) {
        this.typeId = typeId;
        this.name = name;
        this.freqUnit = freqUnit;
    }

    public static FrequencyType valueOf(int typeId) {
        for (FrequencyType frequencyType : FrequencyType.values()) {
            if (frequencyType.getTypeId() == typeId) {
                return frequencyType;
            }
        }
        return null;
    }

}

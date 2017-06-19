package com.trs.gov.kpi.constant;

import lombok.Getter;

/**
 * Created by he.lang on 2017/5/16.
 */
public enum FreqUnit {
    TIMES_PER_DAY(1, "次/天"),
    TIMES_PER_MONTH(1, "次/月"),
    DAYS_PER_TIME(2, "天/次");

    @Getter
    private int code;

    @Getter
    private String name;

    private FreqUnit(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public static FreqUnit getFreqUnitByCode(int code) {
        for (FreqUnit freqUnit : FreqUnit.values()) {
            if (freqUnit.getCode() == code) {
                return freqUnit;
            }
        }
        return null;
    }

}

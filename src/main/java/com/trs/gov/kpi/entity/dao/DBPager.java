package com.trs.gov.kpi.entity.dao;

import lombok.Getter;

/**
 * Created by linwei on 2017/5/22.
 */
public class DBPager {

    @Getter
    private final int offset;

    @Getter
    private final int count;

    public DBPager(int offset, int count) {
        if (offset < 0 || count < 0) {
            throw new IllegalArgumentException("offset or count must be equal or greater than 0");
        }
        this.offset = offset;
        this.count = count;
    }
}

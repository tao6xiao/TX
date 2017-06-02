package com.trs.gov.kpi.entity.responsedata;

import lombok.Getter;

import java.util.List;

/**
 * Created by rw103 on 2017/5/13.
 */
public class ApiPageData {

    @Getter
    private final Pager pager;

    @Getter
    private final List data;

    public ApiPageData(Pager pager, List data) {
        this.pager = pager;
        this.data = data;
    }
}

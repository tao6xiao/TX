package com.trs.gov.kpi.entity.responsedata;

import lombok.Getter;

import java.util.List;

/**
 * 返回列表数据的模板
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

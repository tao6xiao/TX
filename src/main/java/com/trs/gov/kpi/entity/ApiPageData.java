package com.trs.gov.kpi.entity;

import lombok.Data;

import java.util.List;

/**
 * Created by rw103 on 2017/5/13.
 */
@Data
public class ApiPageData {

    private Pager pager;

    private List data;
}

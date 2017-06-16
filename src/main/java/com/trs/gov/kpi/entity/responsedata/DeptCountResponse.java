package com.trs.gov.kpi.entity.responsedata;

import lombok.Data;

import java.util.List;

/**
 * Created by he.lang on 2017/6/16.
 */
@Data
public class DeptCountResponse {
    private int type;

    private String name;

    private List<DeptCount> count;
}

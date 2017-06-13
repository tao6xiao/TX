package com.trs.gov.kpi.entity.outerapi.bas;

import lombok.Data;

import java.util.List;

/**
 * Created by ranwei on 2017/6/13.
 */
@Data
public class BasPVResponse {

    private String beginDay;

    private String endDay;

    private List<PVDetail> records;

    private String result;

}

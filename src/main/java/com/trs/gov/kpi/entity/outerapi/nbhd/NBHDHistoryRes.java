package com.trs.gov.kpi.entity.outerapi.nbhd;

import lombok.Data;

import java.util.List;

/**
 * Created by ranwei on 2017/6/9.
 */
@Data
public class NBHDHistoryRes {

    private String message;

    private int statusCode;

    private List<NBHDHistory> datas;
}

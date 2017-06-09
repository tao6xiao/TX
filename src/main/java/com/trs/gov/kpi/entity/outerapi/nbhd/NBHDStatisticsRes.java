package com.trs.gov.kpi.entity.outerapi.nbhd;

import lombok.Data;

/**
 * Created by ranwei on 2017/6/9.
 */
@Data
public class NBHDStatisticsRes {

    private String message;

    private int statusCode;

    private NBHDCount datas;

}

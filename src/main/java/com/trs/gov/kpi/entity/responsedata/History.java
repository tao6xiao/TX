package com.trs.gov.kpi.entity.responsedata;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * Created by ranwei on 2017/6/9.
 */
@Data
public class History {

    private Date checkTime;

    private List data;
}

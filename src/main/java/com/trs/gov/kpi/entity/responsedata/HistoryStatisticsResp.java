package com.trs.gov.kpi.entity.responsedata;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 趋势图数据返回模板
 * Created by ranwei on 2017/6/9.
 */
@Data
public class HistoryStatisticsResp {

    private Date checkTime;

    private List data;

    public HistoryStatisticsResp(Date checkTime, List data){
        this.checkTime = checkTime;
        this.data = data;

    }
}

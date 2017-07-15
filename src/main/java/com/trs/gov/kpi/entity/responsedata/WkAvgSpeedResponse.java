package com.trs.gov.kpi.entity.responsedata;

import lombok.Data;

import java.util.Date;

/**
 * 返回前端的平均访问速度
 * Created by li.hao on 2017/7/12.
 */
@Data
public class WkAvgSpeedResponse {

    private Integer avgSpeed;//平均访问速度

    private Date checkTime;//入库时间

}

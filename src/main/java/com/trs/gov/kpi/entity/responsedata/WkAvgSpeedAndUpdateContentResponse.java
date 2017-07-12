package com.trs.gov.kpi.entity.responsedata;

import lombok.Data;

import java.util.Date;

/**
 * Created by li.hao on 2017/7/12.
 */
@Data
public class WkAvgSpeedAndUpdateContentResponse {

    private Integer avgSpeed;//平均访问速度

    private Integer updateContent;//网站更新数

    private Date checkTime;//入库时间

}

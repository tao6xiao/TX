package com.trs.gov.kpi.entity.responsedata;

import lombok.Data;

import java.util.Date;

/**
 * Created by li.hao on 2017/7/12.
 */
@Data
public class WkStatsCountResponse {

    private Date checkTime;//信息入库时间

    private Integer unhandleIssue;//已解决问题数

    private Integer handleIssue;//为解决问题数

}

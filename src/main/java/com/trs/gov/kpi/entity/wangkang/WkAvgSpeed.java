package com.trs.gov.kpi.entity.wangkang;

import com.trs.gov.kpi.annotation.DBField;
import com.trs.gov.kpi.annotation.DBTable;
import lombok.Data;

import java.util.Date;

/**
 * 平均速度记录
 *
 * Created by li.hao on 2017/7/12.
 */
@Data
public class WkAvgSpeed {

    private Integer checkId;//检测编号

    private Integer siteId;//网站编号

    private Integer avgSpeed;//平均访问速度

}

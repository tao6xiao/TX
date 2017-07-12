package com.trs.gov.kpi.entity.wangkang;

import com.trs.gov.kpi.annotation.DBField;
import com.trs.gov.kpi.annotation.DBTable;
import lombok.Data;

import java.util.Date;

/**
 * 记录检测时间
 *
 * Created by li.hao on 2017/7/12.
 */
@Data
@DBTable("wkchecktime")
public class WkCheckTime {

    @DBField
    private Integer checkId;//检查编号

    @DBField
    private Integer siteId;//网站编号

    @DBField
    private Date beginTime;//检测开始时间

    @DBField
    private Date endTime;//检测结束时间

}

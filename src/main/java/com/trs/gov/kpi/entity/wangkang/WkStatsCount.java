package com.trs.gov.kpi.entity.wangkang;

import com.trs.gov.kpi.annotation.DBField;
import com.trs.gov.kpi.annotation.DBTable;
import lombok.Data;

import java.util.Date;

/**
 * Created by li.hao on 2017/7/12.
 */
@Data
@DBTable("wkstatscount")
public class WkStatsCount {

    @DBField
    private Integer siteId;//网站编号

    @DBField
    private Integer checkId;//检测编号

    @DBField
    private Integer typeId;//问题类型编号

    @DBField
    private Date checkTime;//信息入库时间

    @DBField
    private Integer isResolved;//已解决问题数

    @DBField
    private Integer unResolved;//为解决问题数

}

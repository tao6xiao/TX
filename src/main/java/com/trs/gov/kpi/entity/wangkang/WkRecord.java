package com.trs.gov.kpi.entity.wangkang;

import com.trs.gov.kpi.annotation.DBField;
import com.trs.gov.kpi.annotation.DBTable;
import lombok.Data;

import java.util.Date;

/**
 * Created by li.hao on 2017/7/11.
 */
@Data
@DBTable("wkrecord")
public class WkRecord {

    @DBField
    private Integer id;

    @DBField
    private Integer checkId;//检测编号

    @DBField
    private Integer siteId;//网站编号

    @DBField
    private Integer typeId;//数据类型编号

    @DBField
    private Date checkTime;//信息入库时间

    @DBField
    private Integer avgSpeed;//平均访问速度

    @DBField
    private Integer updateContent;//网站更新数

}

package com.trs.gov.kpi.entity.wangkang;

import com.trs.gov.kpi.annotation.DBField;
import com.trs.gov.kpi.annotation.DBTable;
import lombok.Data;

import java.util.Date;

/**
 * 网站更新数量记录
 *
 * Created by li.hao on 2017/7/12.
 */
@Data
@DBTable("wkupdatecontent")
public class WkUpdateContent {

    @DBField
    private Integer id;

    @DBField
    private Integer checkId;//检测编号

    @DBField
    private Integer siteId;//网站编号

    @DBField
    private Date checkTime;//信息入库时间

    @DBField
    private Integer updateContent;//网站更新数

}

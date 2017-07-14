package com.trs.gov.kpi.entity.wangkang;

import com.trs.gov.kpi.annotation.DBField;
import com.trs.gov.kpi.annotation.DBTable;
import lombok.Data;

import java.util.Date;

/**
 * Created by li.hao on 2017/7/14.
 */
@Data
@DBTable("wklinktype")
public class WkLinkType {

    @DBField
    private Integer id;

    @DBField
    private Integer checkId;//检查编号

    @DBField
    private Integer siteId;//网站编号

    @DBField
    private Integer allLink;//链接总数

    @DBField
    private Integer webLink;//网页个数

    @DBField
    private Integer imageLink;//图片个数

    @DBField
    private Integer videoLink;//视频个数

    @DBField
    private Integer enclosuLink;//附件个数

    private Date checkTime;//入库时间

}

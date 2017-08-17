package com.trs.gov.kpi.entity;

import com.trs.gov.kpi.annotation.DBField;
import com.trs.gov.kpi.annotation.DBTable;
import lombok.Data;

import java.util.Date;

/**
 * 链接内容统计
 * Created by li.hao on 2017/8/11.
 */
@Data
@DBTable("linkcontentstats")
public class LinkContentStats {

    @DBField
    private Integer siteId;//站点编号

    @DBField
    private Integer typeId;//类型编号

    @DBField
    private String url;//当前链接

    @DBField
    private String md5;//MD5编码

    @DBField
    private Date checkTime;//检测入库时间

    @DBField
    private Integer infoErrorCount;//信息错误个数

}

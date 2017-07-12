package com.trs.gov.kpi.entity.wangkang;

import com.trs.gov.kpi.annotation.DBField;
import com.trs.gov.kpi.annotation.DBTable;
import lombok.Data;

import java.util.Date;

/**
 * 当前链接的信息记录(访问速度/文档内容)
 *
 * Created by li.hao on 2017/7/12.
 */
@Data
@DBTable("wkeverylink")
public class WkEveryLink {

    @DBField
    private Integer id;//

    @DBField
    private Integer checkId;//检测编号

    @DBField
    private Integer siteId;//网站编号

    @DBField
    private String url;//当前URl

    @DBField
    private String md5;//文档的MD5编码

    @DBField
    private Integer accessSpeed;//访问熟读

    @DBField
    private Date checkTime;//数据入库时间

}

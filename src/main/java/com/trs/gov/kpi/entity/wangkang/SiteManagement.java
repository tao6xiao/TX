package com.trs.gov.kpi.entity.wangkang;

import com.trs.gov.kpi.annotation.DBField;
import com.trs.gov.kpi.annotation.DBTable;
import lombok.Data;

import java.util.Date;

/**
 * 网康---网站管理
 *
 * Created by li.hao on 2017/7/5.
 */
@Data
@DBTable("wksite")
public class SiteManagement {

    @DBField
    private Integer id;

    @DBField
    private Integer siteId;//网站编号

    @DBField
    private String siteName;//网站名称

    @DBField
    private String siteIndexUrl;//网站URL

    @DBField
    private String deptAddress;//单位地址

    @DBField
    private String deptLatLng;//单位地址

    @DBField
    private Integer autoCheckType;//自动检测粒度

    @DBField
    private Date checkTime;//检查时间

    @DBField
    private Integer checkStatus;//检查完成状态

    @DBField
    private Integer isDel;//是否删除
}

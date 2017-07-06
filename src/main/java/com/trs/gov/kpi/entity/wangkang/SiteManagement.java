package com.trs.gov.kpi.entity.wangkang;

import lombok.Data;

import java.util.Date;

/**
 * 网康---网站管理
 *
 * Created by li.hao on 2017/7/5.
 */
@Data
public class SiteManagement {

    private Integer id;

    private Integer siteId;//网站编号

    private String siteName;//网站名称

    private String siteIndexUrl;//网站URL

    private Integer autoCheckType;//自动检测粒度

    private Date checkTime;//检查时间

    private Integer checkStatus;//检查完成状态

    private Integer isDel;//是否删除
}

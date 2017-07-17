package com.trs.gov.kpi.entity.responsedata;

import lombok.Data;

import java.util.Date;

/**
 * Created by li.hao on 2017/7/6.
 */
@Data
public class WkSiteManagementResponse {

    private Integer siteId;//网站编号

    private String siteName;//网站名称

    private String siteIndexUrl;//网站URL

    private String deptAddress;//网站所属单位地址

    private String deptLatLng;//网站所属单位的经纬度地址

    private Integer autoCheckType;//自动检测粒度

    private Date checkTime;//检查时间

    private Integer checkStatus;//检查完成状态

    private Integer total = 0; // 总分
}

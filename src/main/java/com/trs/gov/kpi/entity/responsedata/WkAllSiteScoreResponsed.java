package com.trs.gov.kpi.entity.responsedata;

import lombok.Data;

/**
 * Created by li.hao on 2017/7/10.
 */
@Data
public class WkAllSiteScoreResponsed {

    private Integer siteId;//网站编号

    private String siteName;//网站名称

    private Integer total;//总分
}

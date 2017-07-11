package com.trs.gov.kpi.entity.responsedata;

import lombok.Data;

/**
 * Created by li.hao on 2017/7/11.
 */
@Data
public class WkIndexLinkIssueResponse {

    private Integer siteId;//网站编号

    private String siteName;//网站名称

    private Integer invalidLinkCount;//可用链接数

    private Integer contentErrorCount;//内容错误数

    private Integer overSpeedCount;//平均访问速度

    private Integer updateContentCount;//网站更新数

}

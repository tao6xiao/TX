package com.trs.gov.kpi.entity.responsedata;

import lombok.Data;

/**
 * 首页可用性数据返回的模板
 */
@Data
public class IndexPage {

    private Boolean indexAvailable;

    private String monitorTime;

    private String indexUrl;
}

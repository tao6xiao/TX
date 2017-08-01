package com.trs.gov.kpi.entity.responsedata;

import lombok.Data;

/**
 * 首页可用性
 */
@Data
public class IndexPage {

    private Boolean indexAvailable;

    private String monitorTime;

    private String indexUrl;
}

package com.trs.gov.kpi.entity;

import lombok.Data;

import java.util.ArrayList;

/**
 * Created by HLoach on 2017/5/11.
 */
@Data
public class MonitorSiteDeal {
    private Integer siteId;

    private String departmentName;

    private String guarderName;

    private String guarderAccount;

    private String guarderPhone;

    private String indexUrl;

    private Integer[] siteIds;

}


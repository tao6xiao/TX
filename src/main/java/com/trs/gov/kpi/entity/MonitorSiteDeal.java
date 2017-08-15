package com.trs.gov.kpi.entity;

import lombok.Data;

/**
 * Created by HLoach on 2017/5/11.
 */
@Data
public class MonitorSiteDeal {
    private Integer siteId;

    private String departmentName;

    private Integer guarderId;

    private String guarderName;

    private String guarderAccount;

    private String guarderPhone;

    @Override
    public String toString() {
        return "{" +
                "siteId=" + siteId +
                ", departmentName='" + departmentName + '\'' +
                ", guarderId=" + guarderId +
                ", guarderName='" + guarderName + '\'' +
                ", guarderAccount='" + guarderAccount + '\'' +
                ", guarderPhone='" + guarderPhone + '\'' +
                '}';
    }
}


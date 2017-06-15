package com.trs.gov.kpi.entity.requestdata;

import lombok.Data;

/**
 * Created by ranwei on 2017/6/13.
 */
@Data
public class ReportRequestParam extends DateRequest {

    private Integer id;

    private Integer siteId;

    private String day;

}

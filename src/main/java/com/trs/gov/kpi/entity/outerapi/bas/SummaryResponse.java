package com.trs.gov.kpi.entity.outerapi.bas;

import lombok.Data;

import java.util.List;

/**
 * Created by ranwei on 2017/6/14.
 */
@Data
public class SummaryResponse {
    private List<SiteSummary> records;
}

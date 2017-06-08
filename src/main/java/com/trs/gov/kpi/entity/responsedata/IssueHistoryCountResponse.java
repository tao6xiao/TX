package com.trs.gov.kpi.entity.responsedata;

import lombok.Data;

import java.util.List;

/**
 * Created by he.lang on 2017/6/8.
 */
@Data
public class IssueHistoryCountResponse {

    private Integer type;

    private String name;

    private List<HistoryStatistics> data;
}

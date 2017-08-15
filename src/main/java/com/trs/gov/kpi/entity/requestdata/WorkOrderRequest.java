package com.trs.gov.kpi.entity.requestdata;

import lombok.Data;

import java.util.Arrays;

/**
 * Created by ranwei on 2017/6/7.
 */
@Data
public class WorkOrderRequest extends DateRequest {

    private String id;

    private Integer[] siteId;

    /**
     * 0表示未进入工单流程，1表示工单处理过程中，2表示工单处理完成，默认为0
     */
    private Integer workOrderStatus = 0;

    /**
     * 0表示待解决，1表示已处理，2表示已忽略，默认为0
     */
    private Integer solveStatus = 0;

    @Override
    public String toString() {
        return "{" +
                "id='" + id + '\'' +
                "siteId='" + Arrays.asList(siteId) + '\'' +
                "workOrderStatus='" + workOrderStatus + '\'' +
                "solveStatus='" + solveStatus + '\'' +
                "beginDateTime='" + getBeginDateTime() + '\'' +
                ", endDateTime='" + getEndDateTime() + '\'' +
                ", pageSize=" + getPageSize() +
                ", pageIndex=" + getPageIndex() +
                ", sortFields='" + getSortFields() + '\'' +
                ", granularity=" + getGranularity() +
                ", searchField='" + getSearchField() + '\'' +
                ", searchText='" + getSearchText() + '\'' +
                '}';
    }


}

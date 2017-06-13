package com.trs.gov.kpi.entity.requestdata;

import lombok.Data;

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
}

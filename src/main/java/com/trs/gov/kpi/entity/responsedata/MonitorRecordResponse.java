package com.trs.gov.kpi.entity.responsedata;

import lombok.Data;

import java.util.Date;

/**
 * Created by li.hao on 2017/8/8.
 */
@Data
public class MonitorRecordResponse {

    private Integer taskId;//任务编号

    private String  taskName;//任务类型名称

    private  String  taskStatusName;//任务状态名称

    private Integer result;//执行结果

    private Date beginDateTime;//开始时间

    private Date endDateTime;//结束时间
}

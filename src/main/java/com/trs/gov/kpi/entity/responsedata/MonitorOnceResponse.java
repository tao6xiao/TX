package com.trs.gov.kpi.entity.responsedata;

import lombok.Data;

import java.util.Date;

/**
 * 检测一次返回结果（手动检测）
 * Created by li.hao on 2017/8/17.
 */
@Data
public class MonitorOnceResponse {


    private Integer taskId;//任务类型编号

    private String taskStatusName;//任务状态

    private Date beginDateTime;//开始时间

    private Date endDateTime;//结束时间

    private Integer result;//执行结果

}

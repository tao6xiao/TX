package com.trs.gov.kpi.entity;

import com.trs.gov.kpi.annotation.DBField;
import com.trs.gov.kpi.annotation.DBTable;
import lombok.Data;

import java.util.Date;

/**
 * 站点监测记录
 *
 * Created by li.hao on 2017/8/4.
 */
@Data
@DBTable("monitorrecord")
public class MonitorRecord {

    @DBField("id")
    private Integer id;

    @DBField("siteId")
    private Integer siteId;//站点编号

    @DBField("taskId")
    private Integer taskId;//任务类型编号

    @DBField("taskStatus")
    private  Integer taskStatus;//任务状态

    @DBField("result")
    private String result;//执行结果

    @DBField("beginTime")
    private Date beginTime;//开始时间

    @DBField("endTime")
    private Date endTime;//结束时间

}

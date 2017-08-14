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

    // TODO REVIEW LINWEI DO_li.hao FIXED 如果数据库字段的名字和变量名一样，怎不需要在注解中指定名字
    @DBField
    private Integer id;

    @DBField
    private Integer siteId;//站点编号

    @DBField
    private Integer taskId;//任务类型编号

    @DBField
    private  Integer taskStatus;//任务状态

    @DBField
    private Integer result;//执行结果

    @DBField
    private Integer typeId;//监测类型（0：自动监测；1：手动监测）

    @DBField
    private Date beginTime;//开始时间

    @DBField
    private Date endTime;//结束时间

}

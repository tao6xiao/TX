package com.trs.gov.kpi.dao;

import com.trs.gov.kpi.entity.MonitorTime;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;

/**
 * Created by ranwei on 2017/7/31.
 */
@Mapper
public interface MonitorTimeMapper {

    /**
     * 查询监测任务的结束时间
     * @param siteId
     * @param typeId
     * @return
     */
    Date getMonitorTime(@Param("siteId") Integer siteId,@Param("typeId") Integer typeId);

    /**
     * 查询监测任务的执行时间
     * @param monitorTime
     */
    void insertMonitorTime(@Param("monitorTime") MonitorTime monitorTime);
}

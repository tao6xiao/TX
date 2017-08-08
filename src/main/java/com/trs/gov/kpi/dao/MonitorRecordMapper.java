package com.trs.gov.kpi.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;

/**
 * Created by li.hao on 2017/8/8.
 */
@Mapper
public interface MonitorRecordMapper {

    /**
     * 查询最近的监测任务的开始时间
     *
     * @param siteId
     * @param typeId
     * @return
     */
    Date getMonitorStartTime(@Param("siteId") Integer siteId, @Param("typeId") Integer typeId);

    /**
     * 查询最近的监测任务的结束时间
     *
     * @param siteId
     * @param typeId
     * @return
     */
    Date getMonitorEndTime(@Param("siteId") Integer siteId, @Param("typeId") Integer typeId);
}

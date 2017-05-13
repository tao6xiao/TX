package com.trs.gov.kpi.dao;

import com.trs.gov.kpi.entity.MonitorFrequency;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MonitorFrequencyMapper {
    int insert(MonitorFrequency record);

    int insertSelective(MonitorFrequency record);

    /**
     * 通过siteId查询MonitorFrequency的List
     * @param siteId
     * @return
     */
    List<MonitorFrequency> queryBySiteId(int siteId);

    /**
     * 插入监测频率设置的记录，是一个集合（当前为三条记录）
     * @param monitorFrequencyList
     * @return
     */
    int insertMonitorFrequencyList(@Param("monitorFrequencyList") List<MonitorFrequency> monitorFrequencyList);

    /**
     * 传入设置的对象，去更新数据库中对应siteId和typeId的记录
     * @param monitorFrequencyList
     * @return
     */
    int updateMonitorFrequencySetUp(@Param("monitorFrequencyList") List<MonitorFrequency> monitorFrequencyList);
}
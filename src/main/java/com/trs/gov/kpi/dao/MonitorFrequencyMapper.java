package com.trs.gov.kpi.dao;

import com.trs.gov.kpi.entity.MonitorFrequency;
import org.apache.ibatis.annotations.Mapper;

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
}
package com.trs.gov.kpi.dao;

import com.trs.gov.kpi.entity.MonitorFrequency;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MonitorFrequencyMapper {
    int insert(MonitorFrequency record);

    int insertSelective(MonitorFrequency record);
}
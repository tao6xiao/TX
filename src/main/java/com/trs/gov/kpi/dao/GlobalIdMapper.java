package com.trs.gov.kpi.dao;

import com.trs.gov.kpi.entity.GlobalId;
import com.trs.gov.kpi.entity.MonitorSite;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface GlobalIdMapper {
    int insert(GlobalId record);
}
package com.trs.gov.kpi.dao;

import com.trs.gov.kpi.entity.MonitorSite;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MonitorSiteMapper {
    int deleteByPrimaryKey(Integer siteId);

    int insert(MonitorSite record);

    int insertSelective(MonitorSite record);

    MonitorSite selectByPrimaryKey(Integer siteId);

    int updateByPrimaryKeySelective(MonitorSite record);

    int updateByPrimaryKey(MonitorSite record);

    List<MonitorSite> getAllMonitorSites();
}
package com.trs.gov.kpi.dao;

import com.trs.gov.kpi.entity.Performance;
import com.trs.gov.kpi.entity.requestdata.PageDataRequestParam;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by ranwei on 2017/6/16.
 */
@Mapper
public interface PerformanceMapper {

    Double getRecentPerformance(@Param("param") PageDataRequestParam param);

    List<Performance> getHistoryPerformance(@Param("param") PageDataRequestParam param);
}

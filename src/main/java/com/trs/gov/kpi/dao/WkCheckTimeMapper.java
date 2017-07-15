package com.trs.gov.kpi.dao;

import com.trs.gov.kpi.entity.dao.QueryFilter;
import com.trs.gov.kpi.entity.wangkang.WkAllStats;
import com.trs.gov.kpi.entity.wangkang.WkAvgSpeed;
import com.trs.gov.kpi.entity.wangkang.WkCheckTime;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by li.hao on 2017/7/12.
 */
@Mapper
public interface WkCheckTimeMapper {

    List<WkCheckTime> select(@Param("filter") QueryFilter filter);

    /**
     * 获取站点上一次检查的编号
     * @param siteId
     * @return
     */
    Integer getLastCheckId(@Param("siteId") Integer siteId, @Param("checkId") Integer checkId);

    /**
     * 获取站点上一次检查的编号
     * @param siteId
     * @return
     */
    Integer getMaxCheckId(@Param("siteId") Integer siteId);

}

package com.trs.gov.kpi.dao;

import com.trs.gov.kpi.entity.dao.QueryFilter;
import com.trs.gov.kpi.entity.wangkang.WkSiteIndexStats;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by li.hao on 2017/7/20.
 */
@Mapper
public interface WkSiteIndexStatsMapper {

    /**
     * 网站更新---查询所有已检查网站最近一次更新的记录
     *
     * @return
     */
    List<WkSiteIndexStats> select(@Param("filter") QueryFilter filter);

}

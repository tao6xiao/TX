package com.trs.gov.kpi.dao;

import com.trs.gov.kpi.entity.dao.QueryFilter;
import com.trs.gov.kpi.entity.wangkang.WkAvgSpeed;
import com.trs.gov.kpi.entity.wangkang.WkAllStats;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by li.hao on 2017/7/12.
 */
@Mapper
public interface WkAllStatsMapper {

    /**
     * 访问速度---查询网站平均访问速度历史记录
     *
     * @return
     */
    List<WkAvgSpeed> getAvgSpeedHistory();

    /**
     * 网站更新---查询网站每次更新数量的历史记录
     *
     * @return
     */
    List<WkAllStats> getUpdateContentHistory();

    /**
     * 网站更新---查询网站每次更新数量的历史记录
     *
     * @return
     */
    List<WkAllStats> select(@Param("filter") QueryFilter filter);

    /**
     * 获取站点上一次检查的编号
     * @param siteId
     * @return
     */
    Integer getLastCheckId(@Param("siteId") Integer siteId, @Param("checkId") Integer checkId);
}

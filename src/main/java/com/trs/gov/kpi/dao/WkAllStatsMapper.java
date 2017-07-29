package com.trs.gov.kpi.dao;

import com.trs.gov.kpi.entity.dao.QueryFilter;
import com.trs.gov.kpi.entity.wangkang.WkAllStats;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * Created by li.hao on 2017/7/12.
 */
@Mapper
public interface WkAllStatsMapper {

    /**
     * 网站更新---查询网站每次更新数量的历史记录
     *
     * @return
     */
    List<WkAllStats> select(@Param("filter") QueryFilter filter);

    /**
     * 查询一次统计结果
     * @param filter
     * @return
     */
    WkAllStats selectOnce(@Param("filter") QueryFilter filter);

    /**
     * 获取最近一周更新文章数
     * @param beginTime
     * @param endTime
     * @param siteId
     * @return
     */
    int getOneWeekUpdateCount(@Param("beginTime") Date beginTime, @Param("endTime") Date endTime, @Param("siteId") Integer siteId);

    /**
     * 获取最近一次文章更新的时间
     * @return
     */
    Date getLastTimeUpdateContent(Integer siteId);
}

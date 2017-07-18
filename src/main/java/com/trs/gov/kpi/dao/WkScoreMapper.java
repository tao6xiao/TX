package com.trs.gov.kpi.dao;

import com.trs.gov.kpi.entity.dao.QueryFilter;
import com.trs.gov.kpi.entity.wangkang.WkScore;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * Created by li.hao on 2017/7/12.
 */
@Mapper
public interface WkScoreMapper {

    /**
     * 网站更新---查询网站每次更新数量的历史记录
     *
     * @return
     */
    List<WkScore> select(@Param("filter") QueryFilter filter);

    int getOneWeekUpdateCount(@Param("checkTime") Date checkTime, @Param("siteId") Integer siteId);

}

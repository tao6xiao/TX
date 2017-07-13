package com.trs.gov.kpi.dao;

import com.trs.gov.kpi.entity.wangkang.WkStatsCount;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by li.hao on 2017/7/12.
 */
@Mapper
public interface WkStatsCountMapper {

    /**
     * 链接可用性---根据网站编号查询已解决和未解决问题总数
     *
     * @param siteId
     * @param typeId
     * @return
     */
    WkStatsCount getlinkAndContentStatsBySiteId(@Param("siteId") Integer siteId, @Param("typeId") Integer typeId);

    /**
     *  链接可用性---根据网站编号已解决和未解决问题总数历史记录
     *
     * @param siteId
     * @param typeId
     * @return
     */
    List<WkStatsCount> getlinkAndContentHistoryStatsBySiteId(@Param("siteId") Integer siteId, @Param("typeId") Integer typeId);

}

package com.trs.gov.kpi.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * Created by li.hao on 2017/7/13.
 */
@Mapper
public interface WkEveryLinkMapper {

    /**
     * 查询一次检测的平均速度
     *
     * @param siteId
     * @param checkId
     * @return
     */
    Integer selectOnceCheckAvgSpeed(@Param("siteId") Integer siteId, @Param("checkId") Integer checkId);

    /**
     * 查询一次检测的网站更新数
     *
     * @param siteId
     * @param checkId
     * @return
     */
    Integer selectOnceCheckUpdateContent(@Param("siteId") Integer siteId, @Param("checkId") Integer checkId);
}

package com.trs.gov.kpi.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * Created by li.hao on 2017/8/11.
 */
@Mapper
public interface LinkContentStatsMapper {

    /**
     * 获取当前检测的链接内容的md5编码
     *
     * @param siteId
     * @param typeId
     * @return
     */
    String selectThisTimeMD5(@Param("siteId") Integer siteId, @Param("typeId") Integer typeId, @Param("url") String url);

    /**
     * 获取上一次检测的链接内容的md5编码
     *
     * @param siteId
     * @param typeId
     * @return
     */
    String selectLastTimeMD5(@Param("siteId") Integer siteId, @Param("typeId") Integer typeId, @Param("url") String url);
}

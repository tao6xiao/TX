package com.trs.gov.kpi.dao;

import com.trs.gov.kpi.entity.LinkContentStats;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * Created by li.hao on 2017/8/11.
 */
@Mapper
public interface LinkContentStatsMapper {

    /**
     * 获取上一次检测内容
     * @param siteId
     * @param customer3
     * @return
     */
    LinkContentStats getLastLinkContentStats(@Param("siteId") int siteId, @Param("typeId")Integer typeId, @Param("customer3")String customer3);

}

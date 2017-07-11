package com.trs.gov.kpi.dao;

import org.apache.ibatis.annotations.Mapper;

/**
 * Created by li.hao on 2017/7/11.
 */
@Mapper
public interface WkRecordMapper {

    /**
     * 根据网站编号，检查编号，问题类型编号查询访问平均速度
     *
     * @param siteId
     * @param checkId
     * @param typeId
     * @return
     */
    Integer getWkRecordAvgSpeed(Integer siteId, Integer checkId, Integer typeId);

    /**
     * 根据网站编号，检查编号，问题类型编号查询网站更新数
     *
     * @param siteId
     * @param checkId
     * @param typeId
     * @return
     */
    Integer getWkRecordUpdateContent(Integer siteId, Integer checkId, Integer typeId);

}

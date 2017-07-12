package com.trs.gov.kpi.dao;

import com.trs.gov.kpi.entity.dao.DBRow;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

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
    Integer getWkRecordAvgSpeed(@Param("siteId") Integer siteId,@Param("checkId") Integer checkId,@Param("typeId") Integer typeId);

    /**
     * 根据网站编号，检查编号，问题类型编号查询网站更新数
     *
     * @param siteId
     * @param checkId
     * @param typeId
     * @return
     */
    Integer getWkRecordUpdateContent(@Param("siteId") Integer siteId,@Param("checkId") Integer checkId,@Param("typeId") Integer typeId);

    /**
     * 插入数据
     * @param row
     * @return
     */
    int insert(DBRow row);

}

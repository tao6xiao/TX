package com.trs.gov.kpi.dao;

import com.trs.gov.kpi.entity.dao.DBRow;
import com.trs.gov.kpi.entity.dao.DBUpdater;
import com.trs.gov.kpi.entity.dao.QueryFilter;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * Created by li.hao on 2017/6/14.
 */
@Mapper
public interface CommonMapper {

    /**
     * 插入一条记录
     * @param row
     */
    void insert(DBRow row);

    /**
     * 更新数据库数据
     * @param updater
     * @param filter
     */
    void update(@Param("updater") DBUpdater updater, @Param("filter") QueryFilter filter);

    /**
     *  统计记录数
     * @param filter
     * @return
     */
    int count(@Param("filter") QueryFilter filter);

}

package com.trs.gov.kpi.dao;

import com.trs.gov.kpi.entity.dao.DBRow;
import org.apache.ibatis.annotations.Mapper;

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

}

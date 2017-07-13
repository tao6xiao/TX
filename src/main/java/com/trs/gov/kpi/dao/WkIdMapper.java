package com.trs.gov.kpi.dao;

import com.trs.gov.kpi.entity.wangkang.WkId;
import org.apache.ibatis.annotations.Mapper;

/**
 * Created by li.hao on 2017/7/11.
 */
@Mapper
public interface WkIdMapper {

    /**
     * 新增编号
     * @param wkId
     */
    void incId(WkId wkId);

}

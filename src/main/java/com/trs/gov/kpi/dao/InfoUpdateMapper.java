package com.trs.gov.kpi.dao;

import com.trs.gov.kpi.entity.responsedata.InfoUpdateResponse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * Created by rw103 on 2017/5/13.
 */
@Mapper
public interface InfoUpdateMapper {

    /**
     * 插入预警或者问题
     *
     * @param typeId
     * @param infoUpdateResponse
     * @return
     */
    int insert(@Param("typeId") Integer typeId, @Param("infoUpdateResponse") InfoUpdateResponse infoUpdateResponse);

}

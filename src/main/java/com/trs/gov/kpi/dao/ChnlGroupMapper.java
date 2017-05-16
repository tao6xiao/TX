package com.trs.gov.kpi.dao;

import com.trs.gov.kpi.entity.ChnlGroup;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ChnlGroupMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ChnlGroup record);

    int insertSelective(ChnlGroup record);

    ChnlGroup selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ChnlGroup record);

    int updateByPrimaryKey(ChnlGroup record);
}
package com.trs.gov.kpi.dao;

import com.trs.gov.kpi.entity.DefaultUpdateFreq;

public interface DefaultUpdateFreqMapper {
    int deleteByPrimaryKey(Integer siteId);

    int insert(DefaultUpdateFreq record);

    int insertSelective(DefaultUpdateFreq record);

    DefaultUpdateFreq selectByPrimaryKey(Integer siteId);

    int updateByPrimaryKeySelective(DefaultUpdateFreq record);

    int updateByPrimaryKey(DefaultUpdateFreq record);
}
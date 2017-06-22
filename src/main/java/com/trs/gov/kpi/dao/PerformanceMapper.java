package com.trs.gov.kpi.dao;

import com.trs.gov.kpi.entity.Performance;
import com.trs.gov.kpi.entity.requestdata.PageDataRequestParam;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by ranwei on 2017/6/16.
 */
@Mapper
public interface PerformanceMapper {

    /**
     * 查询绩效指得分的历史记录
     *
     * @param param
     * @return
     */
    List<Performance> getHistoryPerformance(@Param("param") PageDataRequestParam param);


    /**
     * 插入绩效指数得分数据
     *
     * @param performance
     */
    void insert(@Param("performance") Performance performance);
}

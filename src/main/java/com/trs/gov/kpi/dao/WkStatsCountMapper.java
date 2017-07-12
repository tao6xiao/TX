package com.trs.gov.kpi.dao;

import com.trs.gov.kpi.entity.dao.QueryFilter;
import com.trs.gov.kpi.entity.wangkang.WkStatsCount;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * Created by li.hao on 2017/7/12.
 */
@Mapper
public interface WkStatsCountMapper {

    /**
     * 链接可用性---根据网站编号查询已解决和未解决问题总数
     *
     * @param queryFilter
     * @return
     */
    WkStatsCount getInvalidlinkStatsBySiteId(QueryFilter queryFilter);

    /**
     *  链接可用性---根据网站编号已解决和未解决问题总数历史记录
     *
     * @param queryFilter
     * @return
     */
    List<WkStatsCount> getInvalidlinkHistoryStatsBySiteId(QueryFilter queryFilter);

}

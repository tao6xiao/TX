package com.trs.gov.kpi.dao;

import com.trs.gov.kpi.entity.dao.QueryFilter;
import com.trs.gov.kpi.entity.wangkang.WkSocre;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * Created by li.hao on 2017/7/10.
 */
@Mapper
public interface WkAllSiteDetailMapper {

    /**
     * 查询所有网站总分
     *
     * @return
     */
    List<WkSocre> selectAllSiteScore();

    /**
     * 查询可用首页的总数
     *
     * @param filter
     * @return
     */
    Integer selectAllSiteCount(QueryFilter filter);

}

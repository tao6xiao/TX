package com.trs.gov.kpi.dao;

import com.trs.gov.kpi.entity.dao.QueryFilter;
import com.trs.gov.kpi.entity.wangkang.WkSocre;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * Created by li.hao on 2017/7/10.
 */
@Mapper
public interface WkSiteDetailMapper {

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
    Integer selectAllSiteScoreCount(QueryFilter filter);

    /**
     * 根据网站编号查询网站总分数 （获取最近一次检查记录）
     *
     * @param siteId
     * @return
     */
    WkSocre getOneSiteScoreBySiteId(Integer siteId);

    /**
     * 根据网站编号查询历史评分记录
     *
     * @param siteId
     * @return
     */
    List<WkSocre> getOneSiteScoreListBySiteId(Integer siteId);



}

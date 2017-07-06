package com.trs.gov.kpi.dao;

import com.trs.gov.kpi.entity.dao.QueryFilter;
import com.trs.gov.kpi.entity.wangkang.SiteManagement;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * Created by li.hao on 2017/7/5.
 */
@Mapper
public interface WkSiteManagementMapper {

    /**
     * 根据网站编号（siteId）获取网站信息
     *
     * @param siteId
     * @return
     */
    SiteManagement selectSiteManagementByStieId(Integer siteId);

    /**
     * 查询所有网站数量
     *
     * @return
     */
    Integer selectAllSiteCount(QueryFilter filter);

    /**
     * 查询所有网站信息
     *
     * @param filter
     * @return
     */
    List<SiteManagement> selectAllSiteList(QueryFilter filter);

    /**
     * 更新网站信息
     *
     * @param siteManagement
     * @return
     */
    int updateSiteManagement(SiteManagement siteManagement);

    /**
     * 删除网站（支持批量删除）
     *
     * @param siteIds
     */
    void deleteSiteBySiteIds(List<Integer> siteIds);

}

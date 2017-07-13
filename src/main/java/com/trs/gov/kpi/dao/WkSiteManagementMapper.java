package com.trs.gov.kpi.dao;

import com.trs.gov.kpi.entity.dao.QueryFilter;
import com.trs.gov.kpi.entity.outerapi.Site;
import com.trs.gov.kpi.entity.wangkang.SiteManagement;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

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
     * 根据网站名称查询网站个数
     *
     * @param siteName
     * @return
     */
    Integer getSiteCountBySiteName(String siteName);

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
     * 根据网站编号查询网站
     *
     * @param siteId
     * @return
     */
    SiteManagement getSiteBySiteId(Integer siteId);

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
    void deleteSiteBySiteIds(@Param("siteIds") List<Integer> siteIds);

    /**
     * 根据网站编号查询网站名称
     *
     * @param siteId
     * @return
     */
    String getSiteNameBySiteId(Integer siteId);

}

package com.trs.gov.kpi.dao;

import com.trs.gov.kpi.entity.wangkang.SiteManagement;

/**
 * Created by li.hao on 2017/7/5.
 */
public interface WkSiteManagementMapper {

    /**
     * 根据网站编号（siteId）获取网站信息
     *
     * @param siteId
     * @return
     */
    SiteManagement selectSiteManagementByStieId(Integer siteId);

    /**
     * 更新网站信息
     *
     * @param siteManagement
     * @return
     */
    int updateSiteManagement(SiteManagement siteManagement);

}

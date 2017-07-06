package com.trs.gov.kpi.service.wangkang;

import com.trs.gov.kpi.entity.wangkang.SiteManagement;

/**
 * Created by li.hao on 2017/7/5.
 */
public interface WkSiteManagementService {

    /**
     * 网康---网站管理（添加网站）
     *
     * @param siteManagement
     */
    String addWkSite(SiteManagement siteManagement);

    /**
     * 根据网站编号（siteId）查询网站信息
     *
     * @param siteId
     * @return
     */
    SiteManagement getSiteManagementBySiteId(Integer siteId);

    /**
     * 更新网站信息
     *
     * @param siteManagement
     * @return
     */
    int updateSiteManagement(SiteManagement siteManagement);

}

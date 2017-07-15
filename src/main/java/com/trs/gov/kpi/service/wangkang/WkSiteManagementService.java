package com.trs.gov.kpi.service.wangkang;

import com.trs.gov.kpi.constant.Types;
import com.trs.gov.kpi.entity.exception.RemoteException;
import com.trs.gov.kpi.entity.requestdata.WkAllSiteDetailRequest;
import com.trs.gov.kpi.entity.responsedata.ApiPageData;
import com.trs.gov.kpi.entity.wangkang.SiteManagement;

import java.util.List;

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
     * 根据名称查询网站数量
     * 
     * @param siteName
     * @return
     */
    Integer getSiteCountBySiteName(String siteName);

    /**
     *  查询所有站点（支持分页、排序和模糊查询）
     *
     * @param wkAllSiteDetail
     * @return
     */
    ApiPageData queryAllSite(WkAllSiteDetailRequest wkAllSiteDetail) throws RemoteException;

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

    /**
     * 根据网站编号查询网站名称
     *
     * @param siteId
     * @return
     */
    String getSiteNameBySiteId(Integer siteId);

    /**
     * 获取所有的站点
     * @return
     */
    List<SiteManagement> getAllSites();

    /**
     * 改变状态
     * @param status
     */
    void changeSiteStatus(Integer siteId, Types.WkCheckStatus status);

}

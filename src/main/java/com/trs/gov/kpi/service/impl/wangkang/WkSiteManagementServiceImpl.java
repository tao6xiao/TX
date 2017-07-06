package com.trs.gov.kpi.service.impl.wangkang;

import com.trs.gov.kpi.dao.CommonMapper;
import com.trs.gov.kpi.dao.WkSiteManagementMapper;
import com.trs.gov.kpi.entity.wangkang.SiteManagement;
import com.trs.gov.kpi.service.wangkang.WkSiteManagementService;
import com.trs.gov.kpi.utils.DBUtil;

import javax.annotation.Resource;

/**
 * Created by li.hao on 2017/7/5.
 */
public class WkSiteManagementServiceImpl implements WkSiteManagementService {

    @Resource
    private CommonMapper commonMapper;

    @Resource
    private WkSiteManagementMapper wkSiteManagementMapper;

    @Override
    public String  addWkSite(SiteManagement siteManagement) {
       commonMapper.insert(DBUtil.toRow(siteManagement));
        return null;
    }

    @Override
    public SiteManagement getSiteManagementBySiteId(Integer siteId) {

        return wkSiteManagementMapper.selectSiteManagementByStieId(siteId);
    }

    @Override
    public int updateSiteManagement(SiteManagement siteManagement) {

        return wkSiteManagementMapper.updateSiteManagement(siteManagement);
    }
}

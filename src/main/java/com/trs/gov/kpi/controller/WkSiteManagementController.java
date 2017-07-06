package com.trs.gov.kpi.controller;

import com.trs.gov.kpi.constant.Constants;
import com.trs.gov.kpi.entity.exception.BizException;
import com.trs.gov.kpi.entity.exception.RemoteException;
import com.trs.gov.kpi.entity.requestdata.WkSiteManagementRequest;
import com.trs.gov.kpi.entity.responsedata.ApiPageData;
import com.trs.gov.kpi.entity.wangkang.SiteManagement;
import com.trs.gov.kpi.service.wangkang.WkSiteManagementService;
import com.trs.gov.kpi.utils.ParamCheckUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Arrays;

/**
 * Created by li.hao on 2017/7/5.
 */
@Slf4j
@RestController
@RequestMapping("gov/wangkang/setting")
public class WkSiteManagementController {

    @Resource
    private WkSiteManagementService wkSiteManagementService;

    /**
     * 获取参数插入或者修改网站信息
     *
     * @param siteManagement
     * @return
     * @throws BizException
     */
    @RequestMapping(value = "/site", method = RequestMethod.POST)
    @ResponseBody
    public Object addWkSite(@ModelAttribute SiteManagement siteManagement) throws BizException {
        if (siteManagement.getSiteName() == null || siteManagement.getAutoCheckType() == null || siteManagement.getSiteIndexUrl() == null){
            log.error("Invalid parameter: 参数siteManagement对象中siteName、autoCheckType、SiteIndexUrl 三个属性中至少有一个存在null值");
            throw new BizException(Constants.INVALID_PARAMETER);
        }

        Integer siteId = siteManagement.getSiteId();
        SiteManagement siteManage = wkSiteManagementService.getSiteManagementBySiteId(siteId);
        if(siteManage != null){
            wkSiteManagementService.updateSiteManagement(siteManagement);
        }else{
            wkSiteManagementService.addWkSite(siteManagement);
        }
        return null;
    }

    /**
     * 分页查询所有站点的数据（附带模糊查询和排序功能）
     *
     * @param wkSiteRequest
     * @return
     * @throws BizException
     * @throws RemoteException
     */
    @RequestMapping(value = "/site/list", method = RequestMethod.GET)
    @ResponseBody
    public ApiPageData querySiteManagement(WkSiteManagementRequest wkSiteRequest) throws BizException, RemoteException {

        ParamCheckUtil.pagerCheck(wkSiteRequest.getPageIndex(), wkSiteRequest.getPageSize());
        return wkSiteManagementService.queryAllSite(wkSiteRequest);
    }

    /**
     * 删除网站（支持批量删除）
     *
     * @param siteIds
     * @return
     */
    @RequestMapping(value = "/site/delete", method = RequestMethod.DELETE)
    public String deleteSiteBySiteIds(Integer[] siteIds){
        wkSiteManagementService.deleteSiteBySiteIds(Arrays.asList(siteIds));
        return null;
    }

}

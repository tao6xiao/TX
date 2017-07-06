package com.trs.gov.kpi.controller;

import com.trs.gov.kpi.constant.Constants;
import com.trs.gov.kpi.entity.exception.BizException;
import com.trs.gov.kpi.entity.wangkang.SiteManagement;
import com.trs.gov.kpi.service.wangkang.WkSiteManagementService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

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
        if (siteManagement.getSiteName() == null || siteManagement.getAutoCheckType() == null || siteManagement.getCheckStatus() == null ||siteManagement.getSiteIndexUrl() == null){
            log.error("Invalid parameter: 参数siteManagement对象中siteName、autoCheckType、CheckStatus、SiteIndexUrl四个属性中至少有一个存在null值");
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

}

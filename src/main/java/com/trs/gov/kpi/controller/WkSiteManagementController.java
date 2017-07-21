package com.trs.gov.kpi.controller;

import com.trs.gov.kpi.constant.Constants;
import com.trs.gov.kpi.constant.Types;
import com.trs.gov.kpi.dao.CommonMapper;
import com.trs.gov.kpi.entity.dao.DBUpdater;
import com.trs.gov.kpi.entity.dao.QueryFilter;
import com.trs.gov.kpi.entity.dao.Table;
import com.trs.gov.kpi.entity.exception.BizException;
import com.trs.gov.kpi.entity.exception.RemoteException;
import com.trs.gov.kpi.entity.requestdata.WkAllSiteDetailRequest;
import com.trs.gov.kpi.entity.responsedata.ApiPageData;
import com.trs.gov.kpi.entity.wangkang.SiteManagement;
import com.trs.gov.kpi.service.SchedulerService;
import com.trs.gov.kpi.service.wangkang.WkSiteManagementService;
import com.trs.gov.kpi.utils.ParamCheckUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Date;

/**
 * 网康——网站管理
 * Created by li.hao on 2017/7/5.
 */
@Slf4j
@RestController
@RequestMapping("gov/wangkang/setting")
public class WkSiteManagementController {

    @Resource
    private WkSiteManagementService wkSiteManagementService;

    @Resource
    private SchedulerService schedulerService;

    @Resource
    private CommonMapper commonMapper;

    /**
     * 获取参数插入或者修改网站信息
     *
     * @param siteManagement
     * @return
     * @throws BizException
     */
    @RequestMapping(value = "/site/add", method = RequestMethod.POST)
    @ResponseBody
    public Object addWkSite(@ModelAttribute SiteManagement siteManagement) throws BizException {
        if (siteManagement.getSiteName() == null || siteManagement.getAutoCheckType() == null || siteManagement.getSiteIndexUrl() == null || siteManagement.getDeptAddress() == null || siteManagement.getDeptLatLng() == null){
            log.error("Invalid parameter: 参数siteManagement对象中siteName、autoCheckType、SiteIndexUrl、deptAddress、deptLatLng 五个属性中至少有一个存在null值");
            throw new BizException(Constants.INVALID_PARAMETER);
        }
        String siteName = siteManagement.getSiteName();
        String siteIndexUrl = siteManagement.getSiteIndexUrl();
        int unDel = 0;
        Integer nameCount = wkSiteManagementService.getSiteCountBySiteName(siteName , unDel);
        Integer urlCount = wkSiteManagementService.getSiteCountByUrl(siteIndexUrl, unDel);
        if (nameCount != 0 || urlCount != 0){
            log.error("Invalid parameter: 网站名或首页URL已存在！！！");
            throw new BizException(Constants.SITE_NAME_IS_EXIST);
        }else{
                siteManagement.setCheckTime(new Date());
                wkSiteManagementService.addWkSite(siteManagement);
            }
        return null;
    }

    /**
     * 修改网站信息
     *
     * @param siteManagement
     * @return
     * @throws BizException
     */
    @RequestMapping(value = "/site/update", method = RequestMethod.PUT)
    @ResponseBody
    public Object modifyWkSite(@ModelAttribute SiteManagement siteManagement) throws BizException {
        if (siteManagement.getSiteId() == null || siteManagement.getSiteName() == null || siteManagement.getAutoCheckType() == null || siteManagement.getSiteIndexUrl() == null || siteManagement.getDeptAddress() == null || siteManagement.getDeptLatLng() == null){
            log.error("Invalid parameter: 参数siteManagement对象中siteId, siteName、autoCheckType、SiteIndexUr、deptAddress、deptLatLng 五个属性中至少有一个存在null值");
            throw new BizException(Constants.INVALID_PARAMETER);
        }
        int isDel = 0;
        Integer siteId = siteManagement.getSiteId();
        SiteManagement siteManage = wkSiteManagementService.getSiteManagementBySiteId(siteId, isDel);
        if(siteManage != null){

            String siteName = siteManagement.getSiteName();
            Integer siteCount = wkSiteManagementService.getSiteCountBySiteName(siteName, isDel);
            if (siteCount > 1){
                log.error("Invalid parameter: 该网站名已存在！！！");
                throw new BizException(Constants.SITE_NAME_IS_EXIST);
            }

            siteManagement.setCheckTime(new Date());
            wkSiteManagementService.updateSiteManagement(siteManagement);

            if (siteManage.getAutoCheckType() != Types.WkAutoCheckType.CHECK_CLOSE.value) {
                change2WaitCheckStatus(siteId);
            }

        }else{
            throw new BizException("网站"+siteManagement.getSiteName()+"已不存在！");
        }
        return null;
    }

    /**
     * 分页查询所有站点的数据（附带模糊查询和排序功能）
     *
     * @param wkAllSiteDetail
     * @return
     * @throws BizException
     * @throws RemoteException
     */
    @RequestMapping(value = "/site/list", method = RequestMethod.GET)
    @ResponseBody
    public ApiPageData querySiteManagement(WkAllSiteDetailRequest wkAllSiteDetail) throws BizException, RemoteException {

        ParamCheckUtil.pagerCheck(wkAllSiteDetail.getPageIndex(), wkAllSiteDetail.getPageSize());
        return wkSiteManagementService.queryAllSite(wkAllSiteDetail);
    }

    /**
     * 删除网站（支持批量删除）
     *
     * @param siteIds
     * @return
     */
    @RequestMapping(value = "/site/delete", method = RequestMethod.DELETE)
    public String deleteSiteBySiteIds(Integer[] siteIds) throws BizException {

        if (siteIds == null || siteIds.length == 0) {
            throw new BizException(Constants.INVALID_PARAMETER);
        }

        wkSiteManagementService.deleteSiteBySiteIds(Arrays.asList(siteIds));
        return null;
    }

    /**
     * 重新检查
     *
     * @param siteId
     * @return
     */
    @RequestMapping(value = "/site/check", method = RequestMethod.PUT)
    public String deleteSiteBySiteIds(Integer siteId) throws BizException {
        if (siteId == null) {
            throw new BizException(Constants.INVALID_PARAMETER);
        }

        schedulerService.doCheckJobOnce(siteId);
        change2WaitCheckStatus(siteId);
        return null;
    }

    private void change2WaitCheckStatus(Integer siteId) throws BizException {
        if (siteId == null) {
            throw new BizException(Constants.INVALID_PARAMETER);
        }
        // 修改状态
        DBUpdater updater = new DBUpdater(Table.WK_SITEMANAGEMENT.getTableName());
        updater.addField("checkStatus", Types.WkCheckStatus.WAIT_CHECK.value);

        QueryFilter filter = new QueryFilter(Table.WK_SITEMANAGEMENT);
        filter.addCond(Constants.DB_FIELD_SITE_ID, siteId);
        filter.addCond("checkStatus", Types.WkCheckStatus.NOT_SUMBIT_CHECK.value);
        commonMapper.update(updater, filter);
    }

}

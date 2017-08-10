package com.trs.gov.kpi.controller;

import com.trs.gov.kpi.constant.Authority;
import com.trs.gov.kpi.constant.Constants;
import com.trs.gov.kpi.constant.OperationType;
import com.trs.gov.kpi.entity.exception.BizException;
import com.trs.gov.kpi.entity.exception.RemoteException;
import com.trs.gov.kpi.entity.requestdata.ChnlGroupChannelRequest;
import com.trs.gov.kpi.entity.requestdata.ChnlGroupChnlsAddRequest;
import com.trs.gov.kpi.entity.responsedata.ApiPageData;
import com.trs.gov.kpi.entity.responsedata.ChnlGroupChnlsResponse;
import com.trs.gov.kpi.entity.responsedata.ChnlGroupsResponse;
import com.trs.gov.kpi.entity.responsedata.Pager;
import com.trs.gov.kpi.service.ChnlGroupService;
import com.trs.gov.kpi.service.outer.AuthorityService;
import com.trs.gov.kpi.service.outer.SiteApiService;
import com.trs.gov.kpi.utils.LogUtil;
import com.trs.gov.kpi.utils.PageInfoDeal;
import com.trs.gov.kpi.utils.ParamCheckUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 栏目分类Controller
 * Created by he.lang on 2017/5/16.
 */
@Slf4j
@RestController
@RequestMapping("/gov/kpi/setting")
public class ChnlGroupController {
    @Resource
    ChnlGroupService chnlGroupService;

    @Resource
    private AuthorityService authorityService;

    @Resource
    SiteApiService siteApiService;

    /**
     * 获取栏目分类
     *
     * @return
     */
    @RequestMapping(value = "/chnlgroups", method = RequestMethod.GET)
    @ResponseBody
    public ChnlGroupsResponse[] getChnlGroups(@RequestParam("siteId") Integer siteId) throws RemoteException, BizException {
        String logDesc = "查询栏目分类" + LogUtil.paramsToLogString(Constants.DB_FIELD_SITE_ID, siteId);
        return LogUtil.controlleFunctionWrapper(() -> {
            authorityService.checkRight(Authority.KPIWEB_INDEXSETUP_SEARCH, siteId);
            return chnlGroupService.getChnlGroupsResponseDetailArray();
        }, OperationType.QUERY, logDesc, LogUtil.getSiteNameForLog(siteApiService, siteId));
    }

    /**
     * 分页查询对于siteId和groupId下面的数据记录
     *
     * @param siteId
     * @param groupId
     * @param pageSize
     * @param pageIndex
     * @return
     * @throws BizException
     */
    @RequestMapping(value = "/chnlgroup/chnls", method = RequestMethod.GET)
    @ResponseBody
    public ApiPageData getPageDataBySiteIdAndGroupId(@RequestParam("siteId") Integer siteId, @RequestParam Integer groupId, Integer pageSize, Integer pageIndex) throws BizException,
            RemoteException {
        String logDesc = "查询站点和根栏目分类下的数据" + LogUtil.paramsToLogString(Constants.DB_FIELD_SITE_ID, siteId);
        return LogUtil.controlleFunctionWrapper(() -> {
            if (siteId == null || groupId == null) {
                log.error("Invalid parameter:  参数siteId、groupId（分类编号）至少有一个存在null值");
                throw new BizException(Constants.INVALID_PARAMETER);
            }
            ParamCheckUtil.pagerCheck(pageIndex, pageSize);
            authorityService.checkRight(Authority.KPIWEB_INDEXSETUP_SEARCH, siteId);

            int itemCount = chnlGroupService.getItemCountBySiteIdAndGroupId(siteId, groupId);
            Pager pager = PageInfoDeal.buildResponsePager(pageIndex, pageSize, itemCount);
            List<ChnlGroupChnlsResponse> chnlGroupChnlsResponseList = chnlGroupService.getPageDataBySiteIdAndGroupId(siteId, groupId, pager.getCurrPage() - 1, pager.getPageSize());
            return new ApiPageData(pager, chnlGroupChnlsResponseList);
        }, OperationType.QUERY, logDesc, LogUtil.getSiteNameForLog(siteApiService, siteId));
    }

    /**
     * 在当前站点和根栏目下添加栏目
     *
     * @param chnlGroupChnlsAddRequest
     * @return
     * @throws BizException
     */
    @RequestMapping(value = "/chnlgroup/chnls", method = RequestMethod.POST)
    @ResponseBody
    public Object addChnlGroupChnls(@RequestBody ChnlGroupChnlsAddRequest chnlGroupChnlsAddRequest) throws BizException, RemoteException {
        String logDesc = "在当前站点和根栏目下添加栏目" + LogUtil.paramsToLogString("chnlGroupChnlsAddRequest", chnlGroupChnlsAddRequest);
        return LogUtil.controlleFunctionWrapper(() -> {
            if (chnlGroupChnlsAddRequest.getSiteId() == null || chnlGroupChnlsAddRequest.getGroupId() == null || chnlGroupChnlsAddRequest.getChnlIds() == null ||
                    chnlGroupChnlsAddRequest.getChnlIds().length == 0) {

                log.error("Invalid parameter:  参数对象chnlGroupChnlsAddRequest中siteId、groupId（分类编号）、chnlIds[]中至少有一个存在null值");
                throw new BizException(Constants.INVALID_PARAMETER);
            }
            authorityService.checkRight(Authority.KPIWEB_INDEXSETUP_ADDCHNLTOTYPE, chnlGroupChnlsAddRequest.getSiteId());

            chnlGroupService.addChnlGroupChnls(chnlGroupChnlsAddRequest);
            return null;
        }, OperationType.ADD, logDesc, LogUtil.getSiteNameForLog(siteApiService, chnlGroupChnlsAddRequest.getSiteId()));
    }

    /**
     * 在当前站点和根栏目下修改栏目
     *
     * @param chnlGroupChnlRequestDetail
     * @return
     * @throws BizException
     */
    @RequestMapping(value = "/chnlgroup/chnls", method = RequestMethod.PUT)
    @ResponseBody
    public Object updateChnlGroupChnls(@ModelAttribute ChnlGroupChannelRequest chnlGroupChnlRequestDetail) throws BizException, RemoteException {
        String logDesc = "在当前站点和根栏目下修改栏目" + LogUtil.paramsToLogString("chnlGroupChnlRequestDetail", chnlGroupChnlRequestDetail);
        return LogUtil.controlleFunctionWrapper(() -> {
            if (chnlGroupChnlRequestDetail.getSiteId() == null || chnlGroupChnlRequestDetail.getGroupId() == null || chnlGroupChnlRequestDetail.getId() == null ||
                    chnlGroupChnlRequestDetail
                            .getChnlId() == null) {
                log.error("Invalid parameter:  参数对象chnlGroupChnlRequestDetails中iteId、groupId（分类编号）、id（当前栏目设置对象编号）、chnlId中（栏目id）至少有一个存在null值");
                throw new BizException(Constants.INVALID_PARAMETER);
            }
            authorityService.checkRight(Authority.KPIWEB_INDEXSETUP_UPDATETYPEOFCHNL, chnlGroupChnlRequestDetail.getSiteId());

            chnlGroupService.updateBySiteIdAndId(chnlGroupChnlRequestDetail);
            return null;
        }, OperationType.UPDATE, logDesc, LogUtil.getSiteNameForLog(siteApiService, chnlGroupChnlRequestDetail.getSiteId()));
    }

    /**
     * 删除对应站点和id的栏目记录
     *
     * @param siteId
     * @param id
     * @return
     * @throws BizException
     */
    @RequestMapping(value = "/chnlgroup/chnls", method = RequestMethod.DELETE)
    @ResponseBody
    public Object deleteChnlGroupChnl(@RequestParam Integer siteId, @RequestParam Integer id) throws BizException, RemoteException {
        String logDesc = "删除对应站点和id的栏目记录" + LogUtil.paramsToLogString(Constants.DB_FIELD_SITE_ID, siteId, Constants.DB_FIELD_ID, id);
        return LogUtil.controlleFunctionWrapper(() -> {
            if (siteId == null || id == null) {
                log.error("Invalid parameter:  参数siteId、id（当前栏目设置对象编号）中至少有一个存在null值");
                throw new BizException(Constants.INVALID_PARAMETER);
            }
            authorityService.checkRight(Authority.KPIWEB_INDEXSETUP_DELCHNLFROMTYPE, siteId);

            chnlGroupService.deleteBySiteIdAndId(siteId, id);
            return null;
        }, OperationType.DELETE, logDesc, LogUtil.getSiteNameForLog(siteApiService, siteId));
    }
}

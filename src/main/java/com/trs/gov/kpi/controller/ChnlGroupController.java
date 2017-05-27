package com.trs.gov.kpi.controller;

import com.trs.gov.kpi.entity.exception.BizException;
import com.trs.gov.kpi.entity.requestdata.ChnlGroupChannelRequest;
import com.trs.gov.kpi.entity.requestdata.ChnlGroupChnlsAddRequest;
import com.trs.gov.kpi.entity.responsedata.ApiPageData;
import com.trs.gov.kpi.entity.responsedata.ChnlGroupChnlsResponse;
import com.trs.gov.kpi.entity.responsedata.ChnlGroupsResponse;
import com.trs.gov.kpi.service.ChnlGroupService;
import com.trs.gov.kpi.utils.PageInfoDeal;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 栏目分类Controller
 * Created by he.lang on 2017/5/16.
 */
@RestController
@RequestMapping("/gov/kpi/setting")
public class ChnlGroupController {
    @Resource
    ChnlGroupService chnlGroupService;

    /**
     * 获取栏目分类
     *
     * @return
     */
    @RequestMapping(value = "/chnlgroups", method = RequestMethod.GET)
    @ResponseBody
    public ChnlGroupsResponse[] getChnlGroups() {
        return chnlGroupService.getChnlGroupsResponseDetailArray();
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
    public ApiPageData getPageDataBySiteIdAndGroupId(@RequestParam("siteId") Integer siteId, @RequestParam Integer groupId, Integer pageSize, Integer pageIndex) throws BizException {
        if (siteId == null || groupId == null) {
            throw new BizException("参数不合法！");
        }

        if (pageIndex != null && pageIndex < 1) {
            throw new BizException("参数不合法！");
        }

        if (pageSize != null && pageSize < 1) {
            throw new BizException("参数不合法！");
        }

        int itemCount = chnlGroupService.getItemCountBySiteIdAndGroupId(siteId, groupId);
        ApiPageData apiPageData = PageInfoDeal.buildApiPageData(pageIndex, pageSize, itemCount);
        List<ChnlGroupChnlsResponse> chnlGroupChnlsResponseList = chnlGroupService.getPageDataBySiteIdAndGroupId(siteId, groupId, apiPageData.getPager().getCurrPage() - 1, apiPageData.getPager().getPageSize());
        apiPageData.setData(chnlGroupChnlsResponseList);
        return apiPageData;
    }

    /**
     * 在当前站点和根栏目下添加栏目
     * @param chnlGroupChnlsAddRequest
     * @return
     * @throws BizException
     */
    @RequestMapping(value = "/chnlgroup/chnls", method = RequestMethod.POST)
    @ResponseBody
    public Object addChnlGroupChnls(@RequestBody ChnlGroupChnlsAddRequest chnlGroupChnlsAddRequest) throws BizException {
        if (chnlGroupChnlsAddRequest.getSiteId() == null || chnlGroupChnlsAddRequest.getGroupId() == null || chnlGroupChnlsAddRequest.getChnlIds() == null || chnlGroupChnlsAddRequest.getChnlIds().length == 0) {
            throw new BizException("参数存在null值");
        }
        chnlGroupService.addChnlGroupChnls(chnlGroupChnlsAddRequest);
        return null;
    }

    /**
     * 在当前站点和根栏目下修改栏目
     * @param chnlGroupChnlRequestDetail
     * @return
     * @throws BizException
     */
    @RequestMapping(value = "/chnlgroup/chnls", method = RequestMethod.PUT)
    @ResponseBody
    public Object updateChnlGroupChnls(@ModelAttribute ChnlGroupChannelRequest chnlGroupChnlRequestDetail) throws BizException {
        if (chnlGroupChnlRequestDetail.getSiteId() == null || chnlGroupChnlRequestDetail.getGroupId() == null || chnlGroupChnlRequestDetail.getId() == null || chnlGroupChnlRequestDetail.getChnlId() == null) {
            throw new BizException("参数存在null值");
        }
        chnlGroupChnlRequestDetail.getId();
        chnlGroupService.updateBySiteIdAndId(chnlGroupChnlRequestDetail);
        return null;
    }

    /**
     * 删除对应站点和id的栏目记录
     * @param siteId
     * @param id
     * @return
     * @throws BizException
     */
    @RequestMapping(value = "/chnlgroup/chnls", method = RequestMethod.DELETE)
    @ResponseBody
    public Object deleteChnlGroupChnl(@RequestParam Integer siteId, @RequestParam Integer id) throws BizException {
        if (siteId == null || id == null) {
            throw new BizException("参数存在null值");
        }
        chnlGroupService.deleteBySiteIdAndId(siteId, id);
        return null;
    }
}

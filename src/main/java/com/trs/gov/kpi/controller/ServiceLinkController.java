package com.trs.gov.kpi.controller;

import com.trs.gov.kpi.constant.EnumChannelGroup;
import com.trs.gov.kpi.constant.IssueType;
import com.trs.gov.kpi.constant.LinkIssueType;
import com.trs.gov.kpi.dao.ChnlGroupMapper;
import com.trs.gov.kpi.entity.ChannelGroup;
import com.trs.gov.kpi.entity.LinkAvailability;
import com.trs.gov.kpi.entity.dao.QueryFilter;
import com.trs.gov.kpi.entity.exception.BizException;
import com.trs.gov.kpi.entity.exception.RemoteException;
import com.trs.gov.kpi.entity.requestdata.PageDataRequestParam;
import com.trs.gov.kpi.entity.responsedata.ApiPageData;
import com.trs.gov.kpi.entity.responsedata.ChnlGroupChnlsResponseDetail;
import com.trs.gov.kpi.service.ChnlGroupService;
import com.trs.gov.kpi.service.LinkAvailabilityService;
import com.trs.gov.kpi.service.helper.LinkAvailabilityServiceHelper;
import com.trs.gov.kpi.service.outer.SiteApiService;
import com.trs.gov.kpi.utils.InitQueryFiled;
import com.trs.gov.kpi.utils.PageInfoDeal;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.*;

/**
 * 服务实用中服务链接Controller
 * Created by he.lang on 2017/5/17.
 */
@RestController
@RequestMapping(value = "/gov/kpi/service/link/issue")
public class ServiceLinkController {
    @Resource
    private LinkAvailabilityService linkAvailabilityService;

    @Resource
    private ChnlGroupMapper chnlGroupMapper;

    @Resource
    private SiteApiService siteApiService;

    /**
     * 处理（批量和单个结合）
     * @param siteId
     * @param ids
     * @return
     */
    @RequestMapping(value = "/handle", method = RequestMethod.POST)
    @ResponseBody
    public String handIssuesByIds(int siteId, Integer[] ids) {
        linkAvailabilityService.handIssuesByIds(siteId, Arrays.asList(ids));
        return null;
    }

    /**
     * 忽略（批量和单个结合）
     * @param siteId
     * @param ids
     * @return
     */
    @RequestMapping(value = "/ignore", method = RequestMethod.POST)
    @ResponseBody
    public String ignoreIssuesByIds(int siteId, Integer[] ids) {
        linkAvailabilityService.ignoreIssuesByIds(siteId, Arrays.asList(ids));
        return null;
    }

    /**
     * 删除（批量和单个结合）
     * @param siteId
     * @param ids
     * @return
     */
    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    @ResponseBody
    public String delIssueByIds(int siteId, Integer[] ids) {
        linkAvailabilityService.delIssueByIds(siteId, Arrays.asList(ids));
        return null;
    }

    /**
     * 查询未解决问题列表
     * @param requestParam
     * @return
     * @throws BizException
     */
    @RequestMapping(value = "/unhandled", method = RequestMethod.GET)
    public ApiPageData getIssueList(@ModelAttribute PageDataRequestParam requestParam) throws BizException, RemoteException {

        if (requestParam.getSiteId() == null) {
            throw new BizException("站点编号为空");
        }

        QueryFilter filter = LinkAvailabilityServiceHelper.toFilter(requestParam);
        filter.addCond("typeId", Integer.valueOf(IssueType.AVAILABLE_ISSUE.getCode()));

        Set<Integer> ids = getChannelIdsOfHandleGuide(requestParam.getSiteId());
        if (!ids.isEmpty()) {
            filter.addCond("customer2", ids).setCollection(true);
        }

        int itemCount = linkAvailabilityService.getUnsolvedIssueCount(filter);
        ApiPageData apiPageData = PageInfoDeal.buildApiPageData(requestParam.getPageIndex(), requestParam.getPageSize(), itemCount);
        filter.setPager(apiPageData.getPager());
        List<LinkAvailability> linkAvailabilityList = linkAvailabilityService.getUnsolvedIssueList(filter);
        for (LinkAvailability link : linkAvailabilityList) {
            LinkIssueType type = LinkIssueType.valueOf(link.getIssueTypeId());
            if (type != null) {
                link.setIssueTypeName(type.name);
            }
        }
        apiPageData.setData(linkAvailabilityList);

        return apiPageData;
    }

    private Set<Integer> getChannelIdsOfHandleGuide(int siteId) throws RemoteException {
        List<ChannelGroup> channelGroupList = chnlGroupMapper.selectPageDataBySiteIdAndGroupId(
                siteId, EnumChannelGroup.HANDLE_GUIDE.getId(), 0, 10000);
        Set<Integer> chnlIds = new HashSet<>();
        if (!channelGroupList.isEmpty()) {
            for (ChannelGroup chnl : channelGroupList) {
                chnlIds.add(chnl.getChnlId());
                siteApiService.getAllChildChnlIds("", siteId, chnl.getChnlId(), chnlIds);
            }
        }
        return chnlIds;
    }

}

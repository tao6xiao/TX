package com.trs.gov.kpi.controller;

import com.trs.gov.kpi.constant.EnumChannelGroup;
import com.trs.gov.kpi.constant.Types;
import com.trs.gov.kpi.dao.ChnlGroupMapper;
import com.trs.gov.kpi.entity.ChannelGroup;
import com.trs.gov.kpi.entity.dao.QueryFilter;
import com.trs.gov.kpi.entity.exception.BizException;
import com.trs.gov.kpi.entity.exception.RemoteException;
import com.trs.gov.kpi.entity.requestdata.PageDataRequestParam;
import com.trs.gov.kpi.entity.responsedata.ApiPageData;
import com.trs.gov.kpi.entity.responsedata.LinkAvailabilityResponse;
import com.trs.gov.kpi.entity.responsedata.Pager;
import com.trs.gov.kpi.service.IssueService;
import com.trs.gov.kpi.service.LinkAvailabilityService;
import com.trs.gov.kpi.service.helper.QueryFilterHelper;
import com.trs.gov.kpi.service.outer.SiteApiService;
import com.trs.gov.kpi.utils.PageInfoDeal;
import com.trs.gov.kpi.utils.ParamCheckUtil;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    @Resource
    private IssueService issueService;

    /**
     * 处理（批量和单个结合）
     *
     * @param siteId
     * @param ids
     * @return
     */
    @RequestMapping(value = "/handle", method = RequestMethod.POST)
    @ResponseBody
    public String handIssuesByIds(int siteId, Integer[] ids) {
        issueService.handIssuesByIds(siteId, Arrays.asList(ids));
        return null;
    }

    /**
     * 忽略（批量和单个结合）
     *
     * @param siteId
     * @param ids
     * @return
     */
    @RequestMapping(value = "/ignore", method = RequestMethod.POST)
    @ResponseBody
    public String ignoreIssuesByIds(int siteId, Integer[] ids) {
        issueService.ignoreIssuesByIds(siteId, Arrays.asList(ids));
        return null;
    }

    /**
     * 删除（批量和单个结合）
     *
     * @param siteId
     * @param ids
     * @return
     */
    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    @ResponseBody
    public String delIssueByIds(int siteId, Integer[] ids) {
        issueService.delIssueByIds(siteId, Arrays.asList(ids));
        return null;
    }

    /**
     * 查询未解决问题列表
     *
     * @param requestParam
     * @return
     * @throws BizException
     */
    @RequestMapping(value = "/unhandled", method = RequestMethod.GET)
    public ApiPageData getIssueList(@ModelAttribute PageDataRequestParam requestParam) throws BizException, RemoteException {

        ParamCheckUtil.paramCheck(requestParam);

        QueryFilter filter = QueryFilterHelper.toFilter(requestParam, Types.IssueType.LINK_AVAILABLE_ISSUE);
        filter.addCond("typeId", Integer.valueOf(Types.IssueType.LINK_AVAILABLE_ISSUE.value));

        Set<Integer> ids = getChannelIdsOfHandleGuide(requestParam.getSiteId());
        if (!ids.isEmpty()) {
            filter.addCond("customer2", ids).setCollection(true);
        }

        int itemCount = linkAvailabilityService.getUnsolvedIssueCount(filter);
        Pager pager = PageInfoDeal.buildResponsePager(requestParam.getPageIndex(), requestParam.getPageSize(), itemCount);
        filter.setPager(pager);
        List<LinkAvailabilityResponse> linkAvailabilityResponseList = linkAvailabilityService.getUnsolvedIssueList(filter);
        for (LinkAvailabilityResponse link : linkAvailabilityResponseList) {
            if (link.getIssueTypeId() != null) {
                link.setIssueTypeName(Types.LinkAvailableIssueType.valueOf(link.getIssueTypeId()).getName());
            }
        }
        return new ApiPageData(pager, linkAvailabilityResponseList);
    }

    /**
     * 查询未解决问题总数
     *
     * @param requestParam
     * @return
     * @throws BizException
     */
    @RequestMapping(value = "/count", method = RequestMethod.GET)
    public Integer getIssueCount(@ModelAttribute PageDataRequestParam requestParam) throws BizException, RemoteException {

        if (requestParam.getSiteId() == null) {
            throw new BizException("参数不合法！");
        }

        QueryFilter filter = QueryFilterHelper.toFilter(requestParam);
        filter.addCond("typeId", Integer.valueOf(Types.IssueType.LINK_AVAILABLE_ISSUE.value));

        Set<Integer> ids = getChannelIdsOfHandleGuide(requestParam.getSiteId());
        if (!ids.isEmpty()) {
            filter.addCond("customer2", ids).setCollection(true);
        }

        return linkAvailabilityService.getUnsolvedIssueCount(filter);
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

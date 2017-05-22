package com.trs.gov.kpi.controller;

import com.trs.gov.kpi.constant.*;
import com.trs.gov.kpi.entity.Issue;
import com.trs.gov.kpi.entity.exception.BizException;
import com.trs.gov.kpi.entity.responsedata.ApiPageData;
import com.trs.gov.kpi.entity.responsedata.IssueIsNotResolvedResponseDetail;
import com.trs.gov.kpi.service.IssueService;
import com.trs.gov.kpi.utils.IssueDataUtil;
import com.trs.gov.kpi.utils.PageInfoDeal;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

/**
 * 综合实时监测：待解决问题Controller
 */
@RestController
@RequestMapping("/gov/kpi/issue")
public class IntegratedMonitorIssueController {

    @Resource
    private IssueService issueService;

    /**
     * 查询所有未解决问题列表
     *
     * @param pageSize
     * @param pageIndex
     * @param issue
     * @return
     * @throws BizException
     */
    @RequestMapping(value = "/unhandled", method = RequestMethod.GET)
    public ApiPageData getAllIssueList(Integer pageSize, Integer pageIndex, @ModelAttribute Issue issue) throws BizException {
        if (issue.getSiteId() == null) {
            throw new BizException("站点编号为空");
        }
        issue = IssueDataUtil.getIssueToGetPageData(issue, issueService, IsResolvedType.IS_NOT_RESOLVED.getCode(), IsDelType.IS_NOT_DEL.getCode());
        int itemCount = issueService.getAllIssueCount(issue);
        ApiPageData apiPageData = PageInfoDeal.buildApiPageData(pageIndex, pageSize, itemCount);
        List<IssueIsNotResolvedResponseDetail> linkAvailabilityList = issueService.getAllIssueList((apiPageData.getPager().getCurrPage() - 1)*apiPageData.getPager().getPageSize(), apiPageData.getPager().getPageSize(), issue);
        apiPageData.setData(linkAvailabilityList);
        return apiPageData;
    }

    /**
     * 处理对应siteId和id的待解决记录（批量和单个）
     *
     * @param siteId
     * @param ids
     * @return
     * @throws BizException
     */
    @RequestMapping(value = "/handle", method = RequestMethod.POST)
    @ResponseBody
    public Object handIssuesByIds(@RequestParam Integer siteId, @RequestParam Integer[] ids) throws BizException {
        if (siteId == null || ids == null || ids.length == 0) {
            throw new BizException("参数存在null值");
        }
        issueService.handIssuesByIds(siteId, Arrays.asList(ids));
        return null;
    }

    /**
     * 忽略对应siteId和id的待解决记录（批量和单个）
     * @param siteId
     * @param ids
     * @return
     * @throws BizException
     */
    @RequestMapping(value = "/ignore", method = RequestMethod.POST)
    @ResponseBody
    public Object ignoreIssuesByIds(@RequestParam Integer siteId, @RequestParam Integer[] ids) throws BizException {
        if (siteId == null || ids == null || ids.length == 0) {
            throw new BizException("参数存在null值");
        }
        issueService.ignoreIssuesByIds(siteId, Arrays.asList(ids));
        return null;
    }

    /**
     * 删除对应siteId和id的待解决记录（批量和单个）
     * @param siteId
     * @param ids
     * @return
     * @throws BizException
     */
    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    @ResponseBody
    public Object delIssueByIds(@RequestParam Integer siteId, @RequestParam Integer[] ids) throws BizException {
        if (siteId == null || ids == null || ids.length == 0) {
            throw new BizException("参数存在null值");
        }
        issueService.delIssueByIds(siteId, Arrays.asList(ids));
        return null;
    }

}

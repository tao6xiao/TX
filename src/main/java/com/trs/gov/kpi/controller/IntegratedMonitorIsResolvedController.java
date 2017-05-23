package com.trs.gov.kpi.controller;

import com.trs.gov.kpi.constant.IsResolvedType;
import com.trs.gov.kpi.entity.Issue;
import com.trs.gov.kpi.entity.exception.BizException;
import com.trs.gov.kpi.entity.responsedata.ApiPageData;
import com.trs.gov.kpi.entity.responsedata.IssueIsResolvedResponseDetail;
import com.trs.gov.kpi.service.IntegratedMonitorIsResolvedService;
import com.trs.gov.kpi.utils.IssueDataUtil;
import com.trs.gov.kpi.utils.PageInfoDeal;
import com.trs.gov.kpi.utils.ParamCheckUtil;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 综合监测：已解决Controller，主要是查询
 * Created by he.lang on 2017/5/19.
 */
@RestController
@RequestMapping("/gov/kpi/issuealert")
public class IntegratedMonitorIsResolvedController {

    @Resource
    IntegratedMonitorIsResolvedService integratedMonitorIsResolvedService;

    /**
     * 获取已解决的分页数据
     * @param pageIndex
     * @param pageSize
     * @param issue
     * @return
     * @throws BizException
     */
    @RequestMapping(value = "/handled", method = RequestMethod.GET)
    @ResponseBody
    public ApiPageData getPageDataIsResolved(Integer pageIndex, Integer pageSize, @ModelAttribute Issue issue) throws BizException {
        if (issue.getSiteId() == null) {
            throw new BizException("站点编号为空");
        }
        ParamCheckUtil.pagerCheck(pageIndex, pageSize);
        Integer isResolved = IsResolvedType.IS_RESOLVED.getCode();
        issue = IssueDataUtil.getIssueToGetPageData(issue, integratedMonitorIsResolvedService,isResolved,null);
        int itemCount = integratedMonitorIsResolvedService.getPageDataIsResolvedItemCount(issue);
        ApiPageData apiPageData = PageInfoDeal.buildApiPageData(pageIndex, pageSize, itemCount);
        List<IssueIsResolvedResponseDetail> issueIsResolvedResponseDetailList = integratedMonitorIsResolvedService.getPageDataIsResolvedList(apiPageData.getPager().getCurrPage()-1,apiPageData.getPager().getPageSize(),issue);
        apiPageData.setData(issueIsResolvedResponseDetailList);
        return apiPageData;
    }

    /**
     * 获取已忽略的分页数据
     * @param pageIndex
     * @param pageSize
     * @param issue
     * @return
     * @throws BizException
     */
    @RequestMapping(value = "/ignored", method = RequestMethod.GET)
    @ResponseBody
    public ApiPageData getPageDataIsIgnored(Integer pageIndex, Integer pageSize, @ModelAttribute Issue issue) throws BizException {
        if (issue.getSiteId() == null) {
            throw new BizException("站点编号为空");
        }
        ParamCheckUtil.pagerCheck(pageIndex, pageSize);
        Integer isIgnored = IsResolvedType.IS_IGNORED.getCode();
        issue = IssueDataUtil.getIssueToGetPageData(issue, integratedMonitorIsResolvedService,isIgnored,null);
        int itemCount = integratedMonitorIsResolvedService.getPageDataIsResolvedItemCount(issue);
        ApiPageData apiPageData = PageInfoDeal.buildApiPageData(pageIndex, pageSize, itemCount);
        List<IssueIsResolvedResponseDetail> issueIsResolvedResponseDetailList = integratedMonitorIsResolvedService.getPageDataIsResolvedList(apiPageData.getPager().getCurrPage()-1,apiPageData.getPager().getPageSize(),issue);
        apiPageData.setData(issueIsResolvedResponseDetailList);
        return apiPageData;
    }
}

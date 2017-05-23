package com.trs.gov.kpi.controller;

import com.trs.gov.kpi.constant.IsDelType;
import com.trs.gov.kpi.constant.IsResolvedType;
import com.trs.gov.kpi.entity.Issue;
import com.trs.gov.kpi.entity.exception.BizException;
import com.trs.gov.kpi.entity.responsedata.ApiPageData;
import com.trs.gov.kpi.entity.responsedata.IssueWarningResponseDetail;
import com.trs.gov.kpi.service.IntegratedMonitorWarningService;
import com.trs.gov.kpi.utils.IssueDataUtil;
import com.trs.gov.kpi.utils.PageInfoDeal;
import com.trs.gov.kpi.utils.ParamCheckUtil;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.text.ParseException;
import java.util.List;

/**
 * 综合实时监测：预警提醒Controller
 * Created by he.lang on 2017/5/18.
 */
@RestController
@RequestMapping("/gov/kpi/alert")
public class IntegratedMonitorWarningController {

    @Resource
    IntegratedMonitorWarningService integratedMonitorWarningService;

    /**
     * 查询预警提醒的分页数据（未处理、未忽略、未删除）
     * @param pageIndex
     * @param pageSize
     * @param issue
     * @return
     * @throws BizException
     */
    @RequestMapping(value = "/unhandled", method = RequestMethod.GET)
    @ResponseBody
    public ApiPageData getPageDataWaringList(Integer pageIndex, Integer pageSize, @ModelAttribute Issue issue) throws BizException, ParseException {
        if (issue.getSiteId() == null) {
            throw new BizException("站点编号为空");
        }
        ParamCheckUtil.pagerCheck(pageIndex, pageSize);
        issue = IssueDataUtil.getIssueToGetPageData(issue, integratedMonitorWarningService, IsResolvedType.IS_NOT_RESOLVED.getCode(), IsDelType.IS_NOT_DEL.getCode());
        int itemCount = integratedMonitorWarningService.getItemCount(issue);
        ApiPageData apiPageData = PageInfoDeal.buildApiPageData(pageIndex, pageSize, itemCount);
        List<IssueWarningResponseDetail> issueList = integratedMonitorWarningService.getPageDataWaringList(apiPageData.getPager().getCurrPage()-1,apiPageData.getPager().getPageSize(), issue);
        apiPageData.setData(issueList);
        return apiPageData;
    }

    /**
     * 处理对应siteId和id的预警记录（批量和单个）
     *
     * @param siteId
     * @param ids
     * @return
     * @throws BizException
     */
    @RequestMapping(value = "/handle", method = RequestMethod.POST)
    @ResponseBody
    public Object dealWithWarningBySiteIdAndId(@RequestParam Integer siteId, @RequestParam Integer[] ids) throws BizException {
        if (siteId == null || ids == null || ids.length == 0) {
            throw new BizException("参数存在null值");
        }
        integratedMonitorWarningService.dealWithWarningBySiteIdAndId(siteId, ids);
        return null;
    }

    /**
     * 忽略对应siteId和id的预警记录（批量和单个）
     * @param siteId
     * @param ids
     * @return
     * @throws BizException
     */
    @RequestMapping(value = "/ignore", method = RequestMethod.POST)
    @ResponseBody
    public Object ignoreWarningBySiteIdAndId(@RequestParam Integer siteId, @RequestParam Integer[] ids) throws BizException {
        if (siteId == null || ids == null || ids.length == 0) {
            throw new BizException("参数存在null值");
        }
        integratedMonitorWarningService.ignoreWarningBySiteIdAndId(siteId, ids);
        return null;
    }

    /**
     * 删除对应siteId和id的预警记录（批量和单个）
     * @param siteId
     * @param ids
     * @return
     * @throws BizException
     */
    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    @ResponseBody
    public Object deleteWarningBySiteIdAndId(@RequestParam Integer siteId, @RequestParam Integer[] ids) throws BizException {
        if (siteId == null || ids == null || ids.length == 0) {
            throw new BizException("参数存在null值");
        }
        integratedMonitorWarningService.deleteWarningBySiteIdAndId(siteId, ids);
        return null;
    }
}

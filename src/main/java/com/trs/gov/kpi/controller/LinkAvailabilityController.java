package com.trs.gov.kpi.controller;


import com.trs.gov.kpi.entity.IssueBase;
import com.trs.gov.kpi.entity.LinkAvailability;
import com.trs.gov.kpi.entity.exception.BizException;
import com.trs.gov.kpi.entity.responsedata.ApiPageData;
import com.trs.gov.kpi.entity.responsedata.HistoryStatistics;
import com.trs.gov.kpi.entity.responsedata.IndexPage;
import com.trs.gov.kpi.service.LinkAvailabilityService;
import com.trs.gov.kpi.utils.InitQueryFiled;
import com.trs.gov.kpi.utils.IssueCounter;
import com.trs.gov.kpi.utils.PageInfoDeal;
import com.trs.gov.kpi.utils.ParamCheckUtil;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;

/**
 * 链接可用性问题
 */
@RestController
@RequestMapping("/gov/kpi/available/issue")
public class LinkAvailabilityController {

    @Resource
    private LinkAvailabilityService linkAvailabilityService;


    /**
     * 查询待解决和已解决问题数量
     *
     * @param issueBase
     * @return
     */
    @RequestMapping(value = "/bytype/count", method = RequestMethod.GET)
    public List getIssueCount(IssueBase issueBase) throws BizException {
        ParamCheckUtil.paramCheck(issueBase);

        return IssueCounter.getIssueCount(linkAvailabilityService, issueBase);
    }

    /**
     * 查询待解决问题数量
     *
     * @param issueBase
     * @return
     * @throws BizException
     */
    @RequestMapping(value = "/unhandled/count", method = RequestMethod.GET)
    public int getUnhandledIssueCount(IssueBase issueBase) throws BizException {
        ParamCheckUtil.paramCheck(issueBase);

        return linkAvailabilityService.getUnhandledIssueCount(issueBase);
    }


    /**
     * 查询历史记录
     *
     * @param issueBase
     * @return
     */
    @RequestMapping(value = "/all/count/history", method = RequestMethod.GET)
    public List<HistoryStatistics> getIssueHistoryCount(@ModelAttribute IssueBase issueBase) throws BizException {
        if (issueBase.getBeginDateTime() == null || issueBase.getBeginDateTime().trim().isEmpty()) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            issueBase.setBeginDateTime(sdf.format(linkAvailabilityService.getEarliestIssueTime()));
        }
        ParamCheckUtil.paramCheck(issueBase);

        return linkAvailabilityService.getIssueHistoryCount(issueBase);
    }

    /**
     * 查询未解决问题列表
     *
     * @param pageIndex
     * @param pageSize
     * @param issueBase
     * @return
     * @throws BizException
     */
    @RequestMapping(value = "/unhandled", method = RequestMethod.GET)
    public ApiPageData getIssueList(Integer pageSize, Integer pageIndex, @ModelAttribute IssueBase issueBase) throws BizException {

        if (issueBase.getSearchText() != null && !issueBase.getSearchText().trim().isEmpty()) {
            List list = InitQueryFiled.init(issueBase.getSearchText(), linkAvailabilityService);
            issueBase.setIds(list);
        }
        ParamCheckUtil.paramCheck(issueBase);

        int itemCount = linkAvailabilityService.getUnhandledIssueCount(issueBase);
        ApiPageData apiPageData = PageInfoDeal.buildApiPageData(pageIndex, pageSize, itemCount);
        List<LinkAvailability> linkAvailabilityList = linkAvailabilityService.getIssueList((apiPageData.getPager().getCurrPage() - 1) * apiPageData.getPager().getPageSize(), apiPageData.getPager().getPageSize(), issueBase);
        apiPageData.setData(linkAvailabilityList);
        return apiPageData;
    }

    /**
     * 获取网站首页可用性
     *
     * @param issueBase
     * @return
     */
    @RequestMapping(value = "/indexpage/status", method = RequestMethod.GET)
    public Integer getIndexAvailability(@ModelAttribute IssueBase issueBase) throws BizException {

        ParamCheckUtil.paramCheck(issueBase);
        String indexUrl = linkAvailabilityService.getIndexUrl(issueBase);
        return linkAvailabilityService.getIndexAvailability(indexUrl, issueBase);
    }

    /**
     * 首页可用性校验显示
     *
     * @param issueBase
     * @return
     * @throws BizException
     */
    @RequestMapping(value = "/check/index", method = RequestMethod.GET)
    public IndexPage showIndexAvailability(@ModelAttribute IssueBase issueBase) throws BizException {
        ParamCheckUtil.paramCheck(issueBase);
        return linkAvailabilityService.showIndexAvailability(issueBase);
    }


    /**
     * 批量处理
     *
     * @param siteId
     * @param ids
     * @return
     */
    @RequestMapping(value = "/handle", method = RequestMethod.POST)
    public String handIssuesByIds(int siteId, Integer[] ids) {
        linkAvailabilityService.handIssuesByIds(siteId, Arrays.asList(ids));
        return null;
    }


    /**
     * 批量忽略
     *
     * @param siteId
     * @param ids
     * @return
     */
    @RequestMapping(value = "/ignore", method = RequestMethod.POST)
    public String ignoreIssuesByIds(int siteId, Integer[] ids) {
        linkAvailabilityService.ignoreIssuesByIds(siteId, Arrays.asList(ids));
        return null;
    }


    /**
     * 批量删除
     *
     * @param siteId
     * @param ids
     * @return
     */
    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    public String delIssueByIds(int siteId, Integer[] ids) {
        linkAvailabilityService.delIssueByIds(siteId, Arrays.asList(ids));
        return null;
    }
}

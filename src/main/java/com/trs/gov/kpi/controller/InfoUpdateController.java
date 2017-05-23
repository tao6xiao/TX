package com.trs.gov.kpi.controller;

import com.trs.gov.kpi.entity.InfoUpdate;
import com.trs.gov.kpi.entity.IssueBase;
import com.trs.gov.kpi.entity.exception.BizException;
import com.trs.gov.kpi.entity.exception.RemoteException;
import com.trs.gov.kpi.entity.responsedata.ApiPageData;
import com.trs.gov.kpi.entity.responsedata.Statistics;
import com.trs.gov.kpi.service.InfoUpdateService;
import com.trs.gov.kpi.utils.*;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;

/**
 * 信息更新问题
 */
@RestController
@RequestMapping("/gov/kpi/channel/issue")
public class InfoUpdateController {

    @Resource
    private InfoUpdateService infoUpdateService;

    /**
     * 查询已解决、预警和更新不及时的数量
     *
     * @param issueBase
     * @return
     */
    @RequestMapping(value = "/bytype/count", method = RequestMethod.GET)
    public List getIssueCount(IssueBase issueBase) throws BizException {
        ParamCheckUtil.paramCheck(issueBase);
        return IssueCounter.getIssueCount(infoUpdateService, issueBase);
    }

    /**
     * 查询历史记录
     *
     * @param issueBase
     * @return
     */
    @RequestMapping(value = "/all/count/history", method = RequestMethod.GET)
    public List getIssueHistoryCount(@ModelAttribute IssueBase issueBase) throws BizException {
        if (issueBase.getBeginDateTime() == null || issueBase.getBeginDateTime().trim().isEmpty()) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            issueBase.setBeginDateTime(sdf.format(infoUpdateService.getEarliestIssueTime()));
        }
        ParamCheckUtil.paramCheck(issueBase);
        return infoUpdateService.getIssueHistoryCount(issueBase);
    }

    /**
     * 查询待解决的问题列表
     *
     * @param pageIndex
     * @param pageSize
     * @param issueBase
     * @return
     * @throws BizException
     */
    @RequestMapping(value = "/unhandled", method = RequestMethod.GET)
    public ApiPageData getIssueList(Integer pageIndex, Integer pageSize, @ModelAttribute IssueBase issueBase) throws BizException {

        PagerCheckUtil.check(pageIndex, pageSize);
        if (issueBase.getSearchText() != null && !issueBase.getSearchText().trim().isEmpty()) {
            List list = InitQueryFiled.init(issueBase.getSearchText(), infoUpdateService);
            issueBase.setIds(list);
        }
        ParamCheckUtil.paramCheck(issueBase);
        int itemCount = infoUpdateService.getUnhandledIssueCount(issueBase);
        ApiPageData apiPageData = PageInfoDeal.buildApiPageData(pageIndex, pageSize, itemCount);
        List<InfoUpdate> infoUpdateList = infoUpdateService.getIssueList((apiPageData.getPager().getCurrPage() - 1) * apiPageData.getPager().getPageSize(), apiPageData.getPager().getPageSize(), issueBase);
        apiPageData.setData(infoUpdateList);
        return apiPageData;
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
        infoUpdateService.handIssuesByIds(siteId, Arrays.asList(ids));
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
        infoUpdateService.ignoreIssuesByIds(siteId, Arrays.asList(ids));
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
        infoUpdateService.delIssueByIds(siteId, Arrays.asList(ids));
        return null;
    }

    /**
     * 获取栏目信息更新不及时的统计信息
     *
     * @param siteId
     * @param beginDateTime
     * @param endDateTime
     * @return
     * @throws BizException
     * @throws ParseException
     * @throws RemoteException
     */
    @RequestMapping(value = "/bygroup/count", method = RequestMethod.GET)
    @ResponseBody
    public List<Statistics> getUpdateNotInTimeCountList(@RequestParam Integer siteId, String beginDateTime, String endDateTime) throws BizException, ParseException, RemoteException {
        if (siteId == null) {
            throw new BizException("站点编号存在null值");
        }
        List<Statistics> updateNotInTimeCountList = infoUpdateService.getUpdateNotInTimeCountList(siteId, beginDateTime, endDateTime);
        return updateNotInTimeCountList;
    }

    @RequestMapping(value = "/bygroup/all/count", method = RequestMethod.GET)
    @ResponseBody
    public Integer getUpdateNotInTimeAllCount(@RequestParam Integer siteId, String beginDateTime, String endDateTime) throws BizException, ParseException, RemoteException {
        if (siteId == null) {
            throw new BizException("站点编号存在null值");
        }
        Integer count = infoUpdateService.getAllUpdateNotInTime(siteId, beginDateTime, endDateTime);
        return count;
    }
}

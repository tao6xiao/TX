package com.trs.gov.kpi.controller;

import com.trs.gov.kpi.constant.InfoUpdateType;
import com.trs.gov.kpi.entity.HistoryDate;
import com.trs.gov.kpi.entity.InfoUpdate;
import com.trs.gov.kpi.entity.IssueBase;
import com.trs.gov.kpi.entity.exception.BizException;
import com.trs.gov.kpi.entity.exception.RemoteException;
import com.trs.gov.kpi.entity.responsedata.ApiPageData;
import com.trs.gov.kpi.entity.responsedata.HistoryStatistics;
import com.trs.gov.kpi.entity.responsedata.Statistics;
import com.trs.gov.kpi.service.InfoUpdateService;
import com.trs.gov.kpi.utils.*;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
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
    public List getIssueCount(IssueBase issueBase) {
        if (issueBase.getEndDateTime() != null && !issueBase.getEndDateTime().trim().isEmpty()) {
            issueBase.setEndDateTime(InitTime.initTime(issueBase.getEndDateTime()));//结束日期加一
        }
        if (issueBase.getSearchText() == null) {
            issueBase.setSearchText("");
        }
        if (issueBase.getSearchText() == null || issueBase.getSearchText().trim().isEmpty()) {
            List list = new ArrayList();
            Integer exception = 0;
            list.add(exception);
            issueBase.setIds(list);
        }
        return IssueCounter.getIssueCount(infoUpdateService, issueBase);
    }

    /**
     * 查询历史记录
     *
     * @param issueBase
     * @return
     */
    @RequestMapping(value = "/all/count/history", method = RequestMethod.GET)
    public List getIssueHistoryCount(@ModelAttribute IssueBase issueBase) {
        if (issueBase.getBeginDateTime() == null
                || issueBase.getBeginDateTime().trim().isEmpty()) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            issueBase.setBeginDateTime(sdf.format(infoUpdateService.getEarliestIssueTime()));
        }
        if (issueBase.getEndDateTime() == null
                || issueBase.getEndDateTime().trim().isEmpty()) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            issueBase.setEndDateTime(sdf.format(new Date()));
        }
        List<HistoryDate> dateList = DateSplitUtil.getHistoryDateList(issueBase.getBeginDateTime(), issueBase.getEndDateTime());
        List<HistoryStatistics> list = new ArrayList<>();
        for (HistoryDate date : dateList) {
            HistoryStatistics historyStatistics = new HistoryStatistics();
            issueBase.setBeginDateTime(date.getBeginDate());
            issueBase.setEndDateTime(date.getEndDate());
            historyStatistics.setValue(infoUpdateService.getIssueHistoryCount(issueBase));
            historyStatistics.setTime(date.getMonth());
            list.add(historyStatistics);
        }
        return list;
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
    public ApiPageData getIssueList(Integer pageSize, Integer pageIndex, @ModelAttribute IssueBase issueBase) throws BizException {

        if (issueBase.getSiteId() == null) {
            throw new BizException("站点编号为空");
        }
        if (issueBase.getSearchText() != null && !issueBase.getSearchText().trim().isEmpty()) {
            List list = InitQueryFiled.init(issueBase.getSearchText(), infoUpdateService);
            issueBase.setIds(list);
        }
        if (issueBase.getSearchText() == null || issueBase.getSearchText().trim().isEmpty()) {
            List<Integer> list = new ArrayList<>();
            Integer exception = 0;
            list.add(exception);
            issueBase.setIds(list);
        }
        if (issueBase.getSearchText() == null) {
            issueBase.setSearchText("");
        }
        int itemCount = infoUpdateService.getUnhandledIssueCount(issueBase);
        ApiPageData apiPageData = PageInfoDeal.buildApiPageData(pageIndex, pageSize, itemCount);
        List<InfoUpdate> infoUpdateList = infoUpdateService.getIssueList((apiPageData.getPager().getCurrPage() - 1) * apiPageData.getPager().getPageSize(), apiPageData.getPager().getPageSize(), issueBase);
        for (InfoUpdate info : infoUpdateList) {
            if (info.getIssueTypeId() == InfoUpdateType.UPDATE_NOT_INTIME.value) {
                info.setIssueTypeName(InfoUpdateType.UPDATE_NOT_INTIME.name);
            }
        }
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
        if(siteId == null){
            throw new BizException("站点编号存在null值");
        }
        List<Statistics> updateNotInTimeCountList = infoUpdateService.getUpdateNotInTimeCountList(siteId,beginDateTime,endDateTime);
        return  updateNotInTimeCountList;
    }
}

package com.trs.gov.kpi.controller;


import com.trs.gov.kpi.constant.InfoErrorType;
import com.trs.gov.kpi.entity.HistoryDate;
import com.trs.gov.kpi.entity.InfoError;
import com.trs.gov.kpi.entity.IssueBase;
import com.trs.gov.kpi.entity.exception.BizException;
import com.trs.gov.kpi.entity.responsedata.ApiPageData;
import com.trs.gov.kpi.entity.responsedata.HistoryStatistics;
import com.trs.gov.kpi.service.InfoErrorService;
import com.trs.gov.kpi.utils.*;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;


/**
 * 信息错误问题
 */
@RestController
@RequestMapping("/gov/kpi/content/issue")
public class InfoErrorController {

    @Resource
    private InfoErrorService infoErrorService;


    /**
     * 查询待解决和已解决问题数量
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
        return IssueCounter.getIssueCount(infoErrorService, issueBase);
    }

    /**
     * 查询历史记录
     *
     * @param infoError
     * @return
     */
    @RequestMapping(value = "/all/count/history", method = RequestMethod.GET)
    public List getIssueHistoryCount(@ModelAttribute InfoError infoError) {
        if (infoError.getBeginDateTime() == null || infoError.getBeginDateTime().trim().isEmpty()) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            infoError.setBeginDateTime(sdf.format(infoErrorService.getEarliestIssueTime()));
        }
        if (infoError.getEndDateTime() == null || infoError.getEndDateTime().trim().isEmpty()) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            infoError.setEndDateTime(sdf.format(new Date()));
        }
        List<HistoryDate> dateList = DateSplitUtil.getHistoryDateList(infoError.getBeginDateTime(), infoError.getEndDateTime());
        List<HistoryStatistics> list = new ArrayList<>();
        for (HistoryDate date : dateList) {
            HistoryStatistics historyStatistics = new HistoryStatistics();
            infoError.setBeginDateTime(date.getBeginDate());
            infoError.setEndDateTime(date.getEndDate());
            historyStatistics.setValue(infoErrorService.getIssueHistoryCount(infoError));
            historyStatistics.setTime(date.getMonth());
            list.add(historyStatistics);
        }
        return list;
    }

    /**
     * 查询待解决问题列表
     *
     * @param pageIndex
     * @param pageSize
     * @param infoError
     * @return
     * @throws BizException
     */
    @RequestMapping(value = "/unhandled", method = RequestMethod.GET)
    public ApiPageData getIssueList(Integer pageSize, Integer pageIndex, @ModelAttribute InfoError infoError) throws BizException {

        if (infoError.getSiteId() == null) {
            throw new BizException("站点编号为空");
        }
        if (infoError.getSearchText() != null && !infoError.getSearchText().trim().isEmpty()) {
            List list = InitQueryFiled.init(infoError.getSearchText(), infoErrorService);
            infoError.setIds(list);
        }
        if (infoError.getSearchText() == null || infoError.getSearchText().trim().isEmpty()) {
            List<Integer> list = new ArrayList<>();
            Integer exception = 0;
            list.add(exception);
            infoError.setIds(list);
        }
        if (infoError.getSearchText() == null) {
            infoError.setSearchText("");
        }
        int itemCount = infoErrorService.getUnhandledIssueCount(infoError);
        ApiPageData apiPageData = PageInfoDeal.buildApiPageData(pageIndex, pageSize, itemCount);
        List<InfoError> infoErrorList = infoErrorService.getIssueList((apiPageData.getPager().getCurrPage() - 1)*apiPageData.getPager().getPageSize(), apiPageData.getPager().getPageSize(), infoError);
        for (InfoError info : infoErrorList) {
            if (info.getIssueTypeId() == InfoErrorType.TYPOS.value) {
                info.setIssueTypeName(InfoErrorType.TYPOS.name);
            } else if (info.getIssueTypeId() == InfoErrorType.SENSITIVE_WORDS.value) {
                info.setIssueTypeName(InfoErrorType.SENSITIVE_WORDS.name);
            }
        }
        apiPageData.setData(infoErrorList);
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
        infoErrorService.handIssuesByIds(siteId, Arrays.asList(ids));
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
        infoErrorService.ignoreIssuesByIds(siteId, Arrays.asList(ids));
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
        infoErrorService.delIssueByIds(siteId, Arrays.asList(ids));
        return null;
    }

}

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
    public List getIssueCount(IssueBase issueBase) throws BizException{
        ParamCheckUtil.paramCheck(issueBase);
        return IssueCounter.getIssueCount(infoErrorService, issueBase);
    }

    /**
     * 查询历史记录
     *
     * @param issueBase
     * @return
     */
    @RequestMapping(value = "/all/count/history", method = RequestMethod.GET)
    public List getIssueHistoryCount(@ModelAttribute IssueBase issueBase) throws BizException{
        if (issueBase.getBeginDateTime() == null || issueBase.getBeginDateTime().trim().isEmpty()) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            issueBase.setBeginDateTime(sdf.format(infoErrorService.getEarliestIssueTime()));
        }
        ParamCheckUtil.paramCheck(issueBase);
        List<HistoryDate> dateList = DateSplitUtil.getHistoryDateList(issueBase.getBeginDateTime(), issueBase.getEndDateTime());
        List<HistoryStatistics> list = new ArrayList<>();
        for (HistoryDate date : dateList) {
            HistoryStatistics historyStatistics = new HistoryStatistics();
            issueBase.setBeginDateTime(date.getBeginDate());
            issueBase.setEndDateTime(date.getEndDate());
            historyStatistics.setValue(infoErrorService.getIssueHistoryCount(issueBase));
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
     * @param issueBase
     * @return
     * @throws BizException
     */
    @RequestMapping(value = "/unhandled", method = RequestMethod.GET)
    public ApiPageData getIssueList(Integer pageSize, Integer pageIndex, @ModelAttribute IssueBase issueBase) throws BizException {

        if (issueBase.getSearchText() != null && !issueBase.getSearchText().trim().isEmpty()) {
            List list = InitQueryFiled.init(issueBase.getSearchText(), infoErrorService);
            issueBase.setIds(list);
        }
        ParamCheckUtil.paramCheck(issueBase);
        int itemCount = infoErrorService.getUnhandledIssueCount(issueBase);
        ApiPageData apiPageData = PageInfoDeal.buildApiPageData(pageIndex, pageSize, itemCount);
        List<InfoError> infoErrorList = infoErrorService.getIssueList((apiPageData.getPager().getCurrPage() - 1)*apiPageData.getPager().getPageSize(), apiPageData.getPager().getPageSize(), issueBase);
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

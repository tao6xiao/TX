package com.trs.gov.kpi.controller;


import com.trs.gov.kpi.entity.HistoryDate;
import com.trs.gov.kpi.entity.InfoError;
import com.trs.gov.kpi.entity.exception.BizException;
import com.trs.gov.kpi.entity.responsedata.ApiPageData;
import com.trs.gov.kpi.entity.responsedata.HistoryCount;
import com.trs.gov.kpi.service.InfoErrorService;
import com.trs.gov.kpi.utils.DateSplitUtil;
import com.trs.gov.kpi.utils.IssueCounter;
import com.trs.gov.kpi.utils.PageInfoDeal;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * Created by ranwei on 2017/5/15.
 */
@RestController
@RequestMapping("/gov/kpi/content/issue")
public class InfoErrorController {

    @Resource
    private InfoErrorService infoErrorService;


    @RequestMapping(value = "/bytype/count", method = RequestMethod.GET)
    public List getIssueCount(int siteId) {

        return IssueCounter.getIssueCount(infoErrorService, siteId);
    }

    @RequestMapping(value = "/all/count/history", method = RequestMethod.GET)
    public List getIssueHistoryCount(@ModelAttribute InfoError infoError) {
        List<HistoryDate> dateList = DateSplitUtil.getHistoryDateList(infoError.getBeginDateTime(), infoError.getEndDateTime());
        List<HistoryCount> list = new ArrayList<>();
        for (HistoryDate date : dateList) {
            HistoryCount historyCount = new HistoryCount();
            infoError.setBeginDateTime(date.getBeginDate());
            infoError.setEndDateTime(date.getEndDate());
            historyCount.setValue(infoErrorService.getIssueHistoryCount(infoError));
            historyCount.setTime(date.getMonth());
            list.add(historyCount);
        }
        return list;
    }

    @RequestMapping(value = "/unhandled", method = RequestMethod.GET)
    public ApiPageData getIssueList(Integer currPage, Integer pageSize, @ModelAttribute InfoError infoError) throws BizException {

        if (infoError.getSiteId() == null) {
            throw new BizException("站点编号为空");
        }
        int itemCount = infoErrorService.getUnhandledIssueCount(infoError.getSiteId());
        ApiPageData apiPageData = PageInfoDeal.getApiPageData(currPage, pageSize, itemCount);
        List<InfoError> infoErrorList = infoErrorService.getIssueList(apiPageData.getPager().getCurrPage() - 1, apiPageData.getPager().getPageSize(), infoError);
        apiPageData.setData(infoErrorList);
        return apiPageData;
    }

    @RequestMapping(value = "/handle", method = RequestMethod.POST)
    public String handIssueById(int siteId, int id) {
        infoErrorService.handIssueById(siteId, id);
        return null;
    }

    @RequestMapping(value = "/handle/batch", method = RequestMethod.POST)
    public String handIssuesByIds(int siteId, Integer[] ids) {
        infoErrorService.handIssuesByIds(siteId, Arrays.asList(ids));
        return null;
    }

    @RequestMapping(value = "/ignore", method = RequestMethod.POST)
    public String ignoreIssueById(int siteId, int id) {
        infoErrorService.ignoreIssueById(siteId, id);
        return null;
    }

    @RequestMapping(value = "/ignore/batch", method = RequestMethod.POST)
    public String ignoreIssuesByIds(int siteId, Integer[] ids) {
        infoErrorService.ignoreIssuesByIds(siteId, Arrays.asList(ids));
        return null;
    }


    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public String delIssueByIds(int siteId, Integer[] ids) {
        infoErrorService.delIssueByIds(siteId, Arrays.asList(ids));
        return null;
    }

}

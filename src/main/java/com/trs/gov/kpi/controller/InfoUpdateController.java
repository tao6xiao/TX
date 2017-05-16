package com.trs.gov.kpi.controller;

import com.trs.gov.kpi.entity.HistoryDate;
import com.trs.gov.kpi.entity.InfoUpdate;
import com.trs.gov.kpi.entity.exception.BizException;
import com.trs.gov.kpi.entity.responsedata.ApiPageData;
import com.trs.gov.kpi.entity.responsedata.HistoryCount;
import com.trs.gov.kpi.service.InfoUpdateService;
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
 * Created by rw103 on 2017/5/13.
 */
@RestController
@RequestMapping("/gov/kpi/channel/issue")
public class InfoUpdateController {

    @Resource
    private InfoUpdateService infoUpdateService;

    @RequestMapping(value = "/bytype/count", method = RequestMethod.GET)
    public List getIssueCount(int siteId) {
        return IssueCounter.getIssueCount(infoUpdateService, siteId);
    }

    @RequestMapping(value = "/all/count/history", method = RequestMethod.GET)
    public List getIssueHistoryCount(@ModelAttribute InfoUpdate infoUpdate) {
        List<HistoryDate> dateList = DateSplitUtil.getHistoryDateList(infoUpdate.getBeginDateTime(), infoUpdate.getEndDateTime());
        List<HistoryCount> list = new ArrayList<>();
        for (HistoryDate date : dateList) {
            HistoryCount historyCount = new HistoryCount();
            infoUpdate.setBeginDateTime(date.getBeginDate());
            infoUpdate.setEndDateTime(date.getEndDate());
            historyCount.setValue(infoUpdateService.getIssueHistoryCount(infoUpdate));
            historyCount.setTime(date.getMonth());
            list.add(historyCount);
        }
        return list;
    }

    @RequestMapping(value = "/unhandled", method = RequestMethod.GET)
    public ApiPageData getIssueList(Integer currPage, Integer pageSize, @ModelAttribute InfoUpdate infoUpdate) throws BizException {

        if (infoUpdate.getSiteId() == null) {
            throw new BizException("站点编号为空");
        }
        int itemCount = infoUpdateService.getUnhandledIssueCount(infoUpdate.getSiteId());
        ApiPageData apiPageData = PageInfoDeal.getApiPageData(currPage, pageSize, itemCount);
        List<InfoUpdate> infoUpdateList = infoUpdateService.getIssueList(apiPageData.getPager().getCurrPage() - 1, apiPageData.getPager().getPageSize(), infoUpdate);
        apiPageData.setData(infoUpdateList);
        return apiPageData;
    }

    @RequestMapping(value = "/handle", method = RequestMethod.POST)
    public String handIssueById(int siteId, int id) {
        infoUpdateService.handIssueById(siteId, id);
        return null;
    }

    @RequestMapping(value = "/handle/batch", method = RequestMethod.POST)
    public String handIssuesByIds(int siteId, Integer[] ids) {
        infoUpdateService.handIssuesByIds(siteId, Arrays.asList(ids));
        return null;
    }

    @RequestMapping(value = "/ignore", method = RequestMethod.POST)
    public String ignoreIssueById(int siteId, int id) {
        infoUpdateService.ignoreIssueById(siteId, id);
        return null;
    }

    @RequestMapping(value = "/ignore/batch", method = RequestMethod.POST)
    public String ignoreIssuesByIds(int siteId, Integer[] ids) {
        infoUpdateService.ignoreIssuesByIds(siteId, Arrays.asList(ids));
        return null;
    }


    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public String delIssueByIds(int siteId, Integer[] ids) {
        infoUpdateService.delIssueByIds(siteId, Arrays.asList(ids));
        return null;
    }
}

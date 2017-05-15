package com.trs.gov.kpi.controller;

import com.trs.gov.kpi.entity.*;
import com.trs.gov.kpi.service.InfoUpdateService;
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

        IssueCount handledIssueCount = new IssueCount();
        handledIssueCount.setType(SolveStatus.SOLVED.value);
        handledIssueCount.setName(SolveStatus.SOLVED.name);
        handledIssueCount.setCount(infoUpdateService.getHandledIssueCount(siteId));

        IssueCount updateNotIntimeCount = new IssueCount();
        updateNotIntimeCount.setType(SolveStatus.UPDATE_NOT_INTIME.value);
        updateNotIntimeCount.setName(SolveStatus.UPDATE_NOT_INTIME.name);
        updateNotIntimeCount.setCount(infoUpdateService.getUpdateNotIntimeCount(siteId));

        IssueCount updateWarningCount = new IssueCount();
        updateWarningCount.setType(SolveStatus.UPDATE_WARNING.value);
        updateWarningCount.setName(SolveStatus.UPDATE_WARNING.name);
        updateWarningCount.setCount(infoUpdateService.getUpdateWarningCount(siteId));

        List list = new ArrayList();
        list.add(handledIssueCount);
        list.add(updateNotIntimeCount);
        list.add(updateWarningCount);

        return list;
    }


    @RequestMapping(value = "/unhandled", method = RequestMethod.GET)
    public ApiPageData getIssueList(int currPage, int pageSize, @ModelAttribute InfoUpdate infoUpdate) {
        List<InfoUpdate> list = infoUpdateService.getIssueList(currPage, pageSize, infoUpdate);
        Pager pager = new Pager();
        if(infoUpdate != null){
            pager.setCurrPage(currPage);
            pager.setPageSize(pageSize);
            int count = infoUpdateService.getUpdateNotIntimeCount(infoUpdate.getSiteId());
            pager.setItemCount(count);
            int pageCount = count%pageSize==0?count/pageSize:count/pageSize+1;
            pager.setPageCount(pageCount);
        }
        ApiPageData data = new ApiPageData();
        data.setData(list);
        data.setPager(pager);
        return data;
    }

    @RequestMapping(value = "/handle", method = RequestMethod.POST)
    public String handIssueById(int siteId, int id) {
        infoUpdateService.handIssueById(siteId, id);
        return "";
    }

    @RequestMapping(value = "/handle/batch",method = RequestMethod.POST)
    public String handIssuesByIds(int siteId, Integer[] ids) {
        infoUpdateService.handIssuesByIds(siteId, Arrays.asList(ids));
        return "";
    }

    @RequestMapping(value = "/ignore", method = RequestMethod.POST)
    public String ignoreIssueById(int siteId, int id) {
        infoUpdateService.ignoreIssueById(siteId, id);
        return "";
    }

    @RequestMapping(value = "/ignore/batch",method = RequestMethod.POST)
    public String ignoreIssuesByIds(int siteId, Integer[] ids) {
        infoUpdateService.ignoreIssuesByIds(siteId, Arrays.asList(ids));
        return "";
    }


    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public String delIssueByIds(int siteId, Integer[] ids) {
        infoUpdateService.delIssueByIds(siteId, Arrays.asList(ids));
        return "";
    }
}

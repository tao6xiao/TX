package com.trs.gov.kpi.controller;


import com.trs.gov.kpi.entity.InfoError;
import com.trs.gov.kpi.entity.responsedata.Pager;
import com.trs.gov.kpi.service.InfoErrorService;
import com.trs.gov.kpi.utils.IssueCounter;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
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

    @RequestMapping(value = "/unhandled", method = RequestMethod.GET)
    public List getIssueList(@ModelAttribute Pager pager, @ModelAttribute InfoError infoError) {
        return null;
    }

}

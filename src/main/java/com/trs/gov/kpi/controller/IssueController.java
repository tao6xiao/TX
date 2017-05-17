package com.trs.gov.kpi.controller;

import com.alibaba.fastjson.JSONObject;
import com.trs.gov.kpi.service.IssueService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * Created by wangxuan on 2017/5/17.
 */
@RestController
@RequestMapping("/gov/kpi/issuealert")
public class IssueController {

    @Resource
    private IssueService issueService;

    @GetMapping("/ignored")
    public JSONObject getIgnoredIssues(@RequestParam("siteId") Integer siteId,
                                       @RequestParam(value = "pageSize", required = false, defaultValue = "20") Integer pageSize,
                                       @RequestParam(value = "currPage", required = false, defaultValue = "1") Integer currPage) {

        return issueService.getIgnoredIssues(siteId, currPage, pageSize);

    }
}

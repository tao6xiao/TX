package com.trs.gov.kpi.controller;

import com.trs.gov.kpi.constant.Constants;
import com.trs.gov.kpi.entity.exception.BizException;
import com.trs.gov.kpi.entity.requestdata.PageDataRequestParam;
import com.trs.gov.kpi.entity.responsedata.ApiPageData;
import com.trs.gov.kpi.service.IssueService;
import com.trs.gov.kpi.utils.ParamCheckUtil;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Arrays;

/**
 * 综合实时监测：待解决问题Controller
 */
@RestController
@RequestMapping("/gov/kpi/issue")
public class IntegratedMonitorIssueController extends IssueHandler{

    @Resource
    private IssueService issueService;

    /**
     * 查询所有未解决问题列表
     *
     * @param param
     * @return
     * @throws BizException
     */
    @RequestMapping(value = "/unhandled", method = RequestMethod.GET)
    public ApiPageData getAllIssueList(@ModelAttribute PageDataRequestParam param) throws BizException {
        ParamCheckUtil.paramCheck(param);
        return issueService.get(param);
    }

}

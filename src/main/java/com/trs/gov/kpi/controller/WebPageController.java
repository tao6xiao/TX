package com.trs.gov.kpi.controller;

import com.trs.gov.kpi.entity.exception.BizException;
import com.trs.gov.kpi.entity.requestdata.PageDataRequestParam;
import com.trs.gov.kpi.entity.responsedata.ApiPageData;
import com.trs.gov.kpi.service.WebPageService;
import com.trs.gov.kpi.utils.ParamCheckUtil;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * Created by ranwei on 2017/6/6.
 */
@RestController
@RequestMapping("/gov/kpi/analysis/improve")
public class WebPageController {

    @Resource
    private WebPageService webPageService;

    @RequestMapping(value = "/speed",method = RequestMethod.GET)
    public ApiPageData selectReplySpeed(@ModelAttribute PageDataRequestParam param) throws BizException {
        ParamCheckUtil.paramCheck(param);
        return webPageService.selectReplySpeed(param);
    }
}

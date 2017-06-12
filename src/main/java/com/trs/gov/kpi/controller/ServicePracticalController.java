package com.trs.gov.kpi.controller;

import com.trs.gov.kpi.entity.exception.BizException;
import com.trs.gov.kpi.entity.outerapi.sp.SGPageDataRes;
import com.trs.gov.kpi.entity.outerapi.sp.SPHistoryStatistics;
import com.trs.gov.kpi.entity.outerapi.sp.SPStatistics;
import com.trs.gov.kpi.entity.requestdata.PageDataRequestParam;
import com.trs.gov.kpi.service.outer.SPService;
import com.trs.gov.kpi.utils.ParamCheckUtil;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by ranwei on 2017/6/12.
 */
@RestController
@RequestMapping(value = "/gov/kpi/service")
public class ServicePracticalController {

    @Resource
    private SPService spService;

    @RequestMapping(value = "/issue/bytype/count", method = RequestMethod.GET)
    public SPStatistics getSGCount(PageDataRequestParam param) throws BizException {
        ParamCheckUtil.paramCheck(param);
        return spService.getSGCount(param);
    }

    @RequestMapping(value = "/issue/all/count/history", method = RequestMethod.GET)
    public List<SPHistoryStatistics> getSPHistoryCount(PageDataRequestParam param) throws BizException {
        ParamCheckUtil.paramCheck(param);
        return spService.getSPHistoryCount(param);
    }

    @RequestMapping(value = "/guide/issue/unhandled", method = RequestMethod.GET)
    public SGPageDataRes getSGList(@ModelAttribute PageDataRequestParam param) throws BizException {
        ParamCheckUtil.paramCheck(param);
        return spService.getSGList(param);
    }


}

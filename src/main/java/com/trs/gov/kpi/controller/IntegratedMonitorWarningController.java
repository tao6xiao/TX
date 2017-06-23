package com.trs.gov.kpi.controller;

import com.trs.gov.kpi.constant.Constants;
import com.trs.gov.kpi.entity.exception.BizException;
import com.trs.gov.kpi.entity.requestdata.PageDataRequestParam;
import com.trs.gov.kpi.entity.responsedata.ApiPageData;
import com.trs.gov.kpi.service.IntegratedMonitorWarningService;
import com.trs.gov.kpi.utils.ParamCheckUtil;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.text.ParseException;

/**
 * 综合实时监测：预警提醒Controller
 * Created by he.lang on 2017/5/18.
 */
@RestController
@RequestMapping("/gov/kpi/alert")
public class IntegratedMonitorWarningController extends IssueHandler{

    @Resource
    IntegratedMonitorWarningService integratedMonitorWarningService;

    /**
     * 查询预警提醒的分页数据（未处理、未忽略、未删除）
     *
     * @param param
     * @return
     * @throws BizException
     */
    @RequestMapping(value = "/unhandled", method = RequestMethod.GET)
    @ResponseBody
    public ApiPageData getPageDataWaringList(@ModelAttribute PageDataRequestParam param) throws BizException, ParseException {
        ParamCheckUtil.paramCheck(param);
        return integratedMonitorWarningService.get(param);
    }
}

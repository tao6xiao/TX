package com.trs.gov.kpi.controller;

import com.trs.gov.kpi.constant.OperationType;
import com.trs.gov.kpi.entity.exception.BizException;
import com.trs.gov.kpi.entity.requestdata.ReportRequestParam;
import com.trs.gov.kpi.entity.responsedata.ApiPageData;
import com.trs.gov.kpi.service.MonitorRecordService;
import com.trs.gov.kpi.service.outer.SiteApiService;
import com.trs.gov.kpi.utils.LogUtil;
import com.trs.gov.kpi.utils.ParamCheckUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 日志监测记录
 * Created by li.hao on 2017/8/8.
 */
@Slf4j
@RestController
@RequestMapping(value = "/gov/kpi/monitor/record")
public class MonitorRecordController {

    @Resource
    private MonitorRecordService monitorRecordService;

    @Resource
    private SiteApiService siteApiService;


    /**
     * 查询日志监测（支持按照时间查询、任务名和任务状态模糊查询）
     * @param param
     * @return
     * @throws BizException
     */
    @RequestMapping(value = "/select", method = RequestMethod.GET)
    public ApiPageData selectMonitorRecord(ReportRequestParam param) throws BizException {

        ParamCheckUtil.pagerCheck(param.getPageIndex(),param.getPageSize());
        ParamCheckUtil.checkDayTime(param.getBeginDateTime());
        ParamCheckUtil.checkDayTime(param.getEndDateTime());
        try {
            ApiPageData apiPageData = monitorRecordService.selectMonitorRecordList(param);
            return apiPageData;
        }catch (Exception e){
            LogUtil.addOperationLog(OperationType.QUERY, LogUtil.buildFailOperationLogDesc("查询日志监测记录"), LogUtil.getSiteNameForLog(siteApiService, param.getSiteId()));
            throw e;
        }
    }

}

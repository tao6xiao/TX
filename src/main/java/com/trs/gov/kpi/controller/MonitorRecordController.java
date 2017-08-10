package com.trs.gov.kpi.controller;

import com.trs.gov.kpi.constant.Authority;
import com.trs.gov.kpi.constant.Constants;
import com.trs.gov.kpi.constant.OperationType;
import com.trs.gov.kpi.entity.exception.BizException;
import com.trs.gov.kpi.entity.exception.RemoteException;
import com.trs.gov.kpi.entity.requestdata.PageDataRequestParam;
import com.trs.gov.kpi.entity.responsedata.ApiPageData;
import com.trs.gov.kpi.service.MonitorRecordService;
import com.trs.gov.kpi.service.outer.AuthorityService;
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

    @Resource
    private AuthorityService authorityService;


    /**
     * 查询日志监测（支持按照时间查询、任务名和任务状态模糊查询）
     *
     * @param param
     * @return
     * @throws BizException
     */
    @RequestMapping(value = "/select", method = RequestMethod.GET)
    public ApiPageData selectMonitorRecord(PageDataRequestParam param) throws BizException, RemoteException {
        String logDesc = "查询日志监测记录" + LogUtil.paramsToLogString(Constants.PARAM, param);
        return LogUtil.controlleFunctionWrapper(() -> {
            // TODO REVIEW LINWEI DO_li.hao 记得加日志成功和性能日志， 同时有一个 paramCheck 方法，为什么不用呢？ @see ParamCheckUtil#paramCheck
            ParamCheckUtil.paramCheck(param);
            authorityService.checkRight(Authority.KPIWEB_MONITORRECORD_SEARCH, param.getSiteId());
            ApiPageData apiPageData = monitorRecordService.selectMonitorRecordList(param);
            return apiPageData;
        }, OperationType.QUERY, logDesc, LogUtil.getSiteNameForLog(siteApiService, param.getSiteId()));
    }

}

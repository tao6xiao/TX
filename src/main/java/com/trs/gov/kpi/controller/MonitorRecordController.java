package com.trs.gov.kpi.controller;

import com.trs.gov.kpi.constant.*;
import com.trs.gov.kpi.entity.MonitorRecord;
import com.trs.gov.kpi.entity.exception.BizException;
import com.trs.gov.kpi.entity.exception.RemoteException;
import com.trs.gov.kpi.entity.requestdata.PageDataRequestParam;
import com.trs.gov.kpi.entity.responsedata.ApiPageData;
import com.trs.gov.kpi.entity.responsedata.MonitorOnceResponse;
import com.trs.gov.kpi.service.MonitorRecordService;
import com.trs.gov.kpi.service.SchedulerService;
import com.trs.gov.kpi.service.outer.AuthorityService;
import com.trs.gov.kpi.service.outer.SiteApiService;
import com.trs.gov.kpi.utils.LogUtil;
import com.trs.gov.kpi.utils.ParamCheckUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * 日志监测记录
 * Created by li.hao on 2017/8/8.
 */
@Slf4j
@RestController
@RequestMapping(value = "/gov/kpi/monitor")
public class MonitorRecordController {

    @Resource
    private MonitorRecordService monitorRecordService;

    @Resource
    private SiteApiService siteApiService;

    @Resource
    private AuthorityService authorityService;

    @Resource
    SchedulerService schedulerService;


    /**
     * 查询日志监测（支持按照时间查询、任务名和任务状态模糊查询）
     *
     * @param param
     * @return
     * @throws BizException
     */
    @RequestMapping(value = "/record/select", method = RequestMethod.GET)
    public ApiPageData selectMonitorRecordList(PageDataRequestParam param) throws BizException, RemoteException {
        String logDesc = "查询日志监测记录" + LogUtil.paramsToLogString(Constants.PARAM, param);
        return LogUtil.controlleFunctionWrapper(() -> {
            ParamCheckUtil.paramCheck(param);
            authorityService.checkRight(Authority.KPIWEB_MONITORRECORD_SEARCH, param.getSiteId());
            return monitorRecordService.selectMonitorRecordList(param);
        }, OperationType.QUERY, logDesc, LogUtil.getSiteNameForLog(siteApiService, param.getSiteId()));
    }

    /**
     * 手动监测——执行检测任务
     *
     * @param siteId
     * @return
     */
    @RequestMapping(value = "/manual/check", method = RequestMethod.GET)
    @ResponseBody
    public List<MonitorOnceResponse> manualMonitoring(Integer siteId, Integer checkJobValue) throws BizException, RemoteException {
        String logDesc = "网站手动监测——开始执行" + LogUtil.paramsToLogString(Constants.DB_FIELD_SITE_ID, siteId, "checkJobValue", checkJobValue);
        return LogUtil.controlleFunctionWrapper(() -> {
            if (siteId == null || checkJobValue == null) {
                log.error("Invalid parameter: 参数siteId或者checkJobTypeValue存在null值");
                throw new BizException(Constants.INVALID_PARAMETER);
            }
            authorityService.checkRight(Authority.KPIWEB_MANUALMONITOR_CHECK, siteId);
            //如果当前任务为正在检查或是等待检测，再次请求的时候就不在执行检测,直接返回当前结果
            List<MonitorRecord> newestMonitorRecordList = monitorRecordService.selectNewestMonitorRecord(siteId, checkJobValue);
            if ((!newestMonitorRecordList.isEmpty() && newestMonitorRecordList.get(0).getTaskStatus() == Status.MonitorStatusType.DOING_CHECK.value)
                    || (!newestMonitorRecordList.isEmpty() && newestMonitorRecordList.get(0).getTaskStatus() == Status.MonitorStatusType.WAIT_CHECK.value)){
                return monitorRecordService.getMonitorOnceResponse(newestMonitorRecordList);
            }else {
                //插入手动检测开始记录
                monitorRecordService.insertBeginManualMonitorRecord(siteId, checkJobValue, Status.MonitorType.MANUAL_MONITOR.value, new Date());
                schedulerService.doCheckJobOnce(siteId, EnumCheckJobType.valueOf(checkJobValue));
                List<MonitorRecord> monitorRecordListAfterCheck = monitorRecordService.selectNewestMonitorRecord(siteId, checkJobValue);
                return monitorRecordService.getMonitorOnceResponse(monitorRecordListAfterCheck);
            }
        }, OperationType.MONITOR, logDesc, LogUtil.getSiteNameForLog(siteApiService, siteId));
    }


    /**
     * 手动检测——查询监测结果
     * @param siteId
     * @param checkJobValues
     * @return
     * @throws RemoteException
     * @throws BizException
     */
    @RequestMapping(value = "/manual/result", method = RequestMethod.GET)
    @ResponseBody
    public List<MonitorOnceResponse> selectMonitorResultOnce(Integer siteId, Integer[] checkJobValues) throws RemoteException, BizException {
        String logDesc = "网站手动监测——结果查询" + LogUtil.paramsToLogString(Constants.DB_FIELD_SITE_ID, siteId, "checkJobValues", checkJobValues);
        return LogUtil.controlleFunctionWrapper(() -> {
            if (siteId == null || checkJobValues == null) {
                log.error("Invalid parameter: 参数siteId或者checkJobTypeValues存在null值");
                throw new BizException(Constants.INVALID_PARAMETER);
            }
            authorityService.checkRight(Authority.KPIWEB_MANUALMONITOR_SEARCH, siteId);
            return monitorRecordService.selectMonitorResulrOnce(siteId, Arrays.asList(checkJobValues));
        }, OperationType.QUERY, logDesc, LogUtil.getSiteNameForLog(siteApiService, siteId));
    }
}

package com.trs.gov.kpi.controller;

import com.trs.gov.kpi.constant.Authority;
import com.trs.gov.kpi.constant.Constants;
import com.trs.gov.kpi.constant.FrequencyType;
import com.trs.gov.kpi.constant.OperationType;
import com.trs.gov.kpi.entity.MonitorFrequency;
import com.trs.gov.kpi.entity.exception.BizException;
import com.trs.gov.kpi.entity.exception.RemoteException;
import com.trs.gov.kpi.entity.requestdata.MonitorFrequencyFreq;
import com.trs.gov.kpi.entity.requestdata.MonitorFrequencySetUp;
import com.trs.gov.kpi.entity.responsedata.MonitorFrequencyResponse;
import com.trs.gov.kpi.ids.ContextHelper;
import com.trs.gov.kpi.service.MonitorFrequencyService;
import com.trs.gov.kpi.service.MonitorSiteService;
import com.trs.gov.kpi.service.outer.AuthorityService;
import com.trs.gov.kpi.service.outer.SiteApiService;
import com.trs.gov.kpi.utils.TRSLogUserUtil;
import com.trs.mlf.simplelog.SimpleLogServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by helang on 2017/5/12.
 * 监测频率Controller
 */
@Slf4j
@RestController
@RequestMapping(value = "/gov/kpi/setting")
public class MonitorFrequencyController {
    @Resource
    MonitorFrequencyService monitorFrequencyService;

    @Resource
    MonitorSiteService monitorSiteService;

    @Resource
    private AuthorityService authorityService;

    @Resource
    SiteApiService siteApiService;

    /**
     * 通过siteId获取当前站点监测频率的设置记录集合
     *
     * @param siteId
     * @return
     * @throws BizException
     */
    @RequestMapping(value = "/checkfreq", method = RequestMethod.GET)
    @ResponseBody
    public List<MonitorFrequencyResponse> queryBySiteId(@RequestParam Integer siteId) throws BizException, RemoteException {
        if (!authorityService.hasRight(ContextHelper.getLoginUser().getUserName(), siteId, null, Authority.KPIWEB_MONITORSETUP_SEARCH) && !authorityService.hasRight(ContextHelper
                .getLoginUser().getUserName(), null, null, Authority.KPIWEB_MONITORSETUP_SEARCH)) {
            throw new BizException(Authority.NO_AUTHORITY);
        }
        if (siteId == null) {
            throw new BizException(Constants.INVALID_PARAMETER);
        }
        SimpleLogServer.getInstance(TRSLogUserUtil.getLogUser()).operation(OperationType.QUERY, "查询当前站点的监测频率", siteApiService.getSiteById(siteId, "").getSiteName()).info();
        return monitorFrequencyService.queryBySiteId(siteId);
    }

    /**
     * 通过传入的对象，判断插入记录还是修改记录，然后进行操作
     *
     * @param freqSetUp
     * @return
     * @throws BizException
     */
    @RequestMapping(value = "/monitorfrequency", method = RequestMethod.POST)
    @ResponseBody
    public Object save(@RequestBody MonitorFrequencySetUp freqSetUp) throws BizException, RemoteException {
        if (!authorityService.hasRight(ContextHelper.getLoginUser().getUserName(), freqSetUp.getSiteId(), null, Authority.KPIWEB_MONITORSETUP_SAVE) && !authorityService.hasRight
                (ContextHelper.getLoginUser().getUserName(), null, null, Authority.KPIWEB_MONITORSETUP_SAVE)) {
            throw new BizException(Authority.NO_AUTHORITY);
        }
        MonitorFrequencyFreq[] freqs = freqSetUp.getFreqs();
        if (freqSetUp.getSiteId() == null || freqs == null || freqs.length == 0) {
            throw new BizException(Constants.INVALID_PARAMETER);
        }
        for (int i = 0; i < freqs.length; i++) {
            if (freqs[i].getId() == null || freqs[i].getValue() == null) {
                log.error("Invalid parameter: 参数freqs[]中id（类型编号）存在null值");
                throw new BizException(Constants.INVALID_PARAMETER);
            }

            if (FrequencyType.valueOf(freqs[i].getId()) == null) {
                log.error("Invalid parameter: 参数freqs[]中id（类型编号）无对应的频率设置类型");
                throw new BizException(Constants.INVALID_PARAMETER);
            }
        }
        if (freqs.length < FrequencyType.values().length) {
            log.error("Invalid parameter: 添加频率设置时，缺少某些频率类型的数据");
            throw new BizException(Constants.INVALID_PARAMETER);
        }

        int siteId = freqSetUp.getSiteId();
        if (monitorSiteService.getMonitorSiteBySiteId(siteId) == null) {
            log.error("Invalid parameter: 当前站点" + siteId + "不是监测站点");
            throw new BizException("请先进行监测站点模块相关设置");
        }
        List<MonitorFrequency> monitorFrequencyList = monitorFrequencyService.checkSiteIdAndTypeAreBothExitsOrNot(siteId);
        if (monitorFrequencyList == null || monitorFrequencyList.isEmpty()) {//siteId和typeId同时不存在，插入记录
            monitorFrequencyService.addMonitorFrequencySetUp(freqSetUp);
            SimpleLogServer.getInstance(TRSLogUserUtil.getLogUser()).operation(OperationType.ADD, "添加监测频率", siteApiService.getSiteById(siteId, "").getSiteName()).info();
        } else {//siteId和typeId同时存在，修改对应站点的监测频率记录
            monitorFrequencyService.updateMonitorFrequencySetUp(freqSetUp);
            SimpleLogServer.getInstance(TRSLogUserUtil.getLogUser()).operation(OperationType.UPDATE, "修改监测频率", siteApiService.getSiteById(siteId, "").getSiteName()).info();
        }
        return null;
    }
}

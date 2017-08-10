package com.trs.gov.kpi.controller;

import com.trs.gov.kpi.constant.Authority;
import com.trs.gov.kpi.constant.Constants;
import com.trs.gov.kpi.constant.OperationType;
import com.trs.gov.kpi.entity.DefaultUpdateFreq;
import com.trs.gov.kpi.entity.exception.BizException;
import com.trs.gov.kpi.entity.exception.RemoteException;
import com.trs.gov.kpi.service.DefaultUpdateFreqService;
import com.trs.gov.kpi.service.outer.AuthorityService;
import com.trs.gov.kpi.service.outer.SiteApiService;
import com.trs.gov.kpi.utils.LogUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.text.ParseException;

/**
 * 按需更新的自查提醒Controller
 * Created by he.lang on 2017/5/15.
 */
@Slf4j
@RestController
@RequestMapping("/gov/kpi/setting")
public class DefaultUpdateFreqController {

    public static final String DEFAULT_UPDATE_FREQ_NAME = "defaultUpdateFreq";

    @Resource
    DefaultUpdateFreqService defaultUpdateFreqService;

    @Resource
    private AuthorityService authorityService;

    @Resource
    SiteApiService siteApiService;

    /**
     * 通过siteId查询对应自查提醒记录
     *
     * @param siteId
     * @return
     * @throws BizException
     */
    @RequestMapping(value = "/defaultupdatefreq", method = RequestMethod.GET)
    @ResponseBody
    public Integer getDefaultUpdateFreqBySiteId(@RequestParam Integer siteId) throws BizException, RemoteException {
        String logDesc = "通过siteId查询对应自查提醒记录" + LogUtil.paramsToLogString(Constants.DB_FIELD_SITE_ID, siteId);
        return LogUtil.ControlleFunctionWrapper(() -> {
            if (siteId == null) {
                log.error("Invalid parameter:  参数siteId存在null值");
                throw new BizException(Constants.INVALID_PARAMETER);
            }
            authorityService.checkRight(Authority.KPIWEB_INDEXSETUP_SEARCH, siteId);

            DefaultUpdateFreq defaultUpdateFreq = defaultUpdateFreqService.getDefaultUpdateFreqBySiteId(siteId);
            Integer value = null;
            if (defaultUpdateFreq != null) {
                value = defaultUpdateFreq.getValue();
            }
            return value;
        }, OperationType.QUERY, logDesc, LogUtil.getSiteNameForLog(siteApiService, siteId));
    }

    /**
     * 插入或者修改自查提醒记录
     *
     * @param defaultUpdateFreq
     * @return
     * @throws BizException
     */
    @RequestMapping(value = "/defaultupdatefreq", method = RequestMethod.PUT)
    @ResponseBody
    public Object save(@ModelAttribute DefaultUpdateFreq defaultUpdateFreq) throws BizException, RemoteException {
        String logDesc = "插入/修改自查提醒记录" + LogUtil.paramsToLogString(DEFAULT_UPDATE_FREQ_NAME, defaultUpdateFreq);
        return LogUtil.ControlleFunctionWrapper(() -> {
            if (defaultUpdateFreq.getSiteId() == null || defaultUpdateFreq.getValue() == null) {
                log.error("Invalid parameter:  参数siteId、value（自查提醒周期值）中至少一个存在null值");
                throw new BizException(Constants.INVALID_PARAMETER);
            }
            authorityService.checkRight(Authority.KPIWEB_INDEXSETUP_UPDATEDEMANDFREQ, defaultUpdateFreq.getSiteId());
            int siteId = defaultUpdateFreq.getSiteId();
            DefaultUpdateFreq defaultUpdateFreqCheck = defaultUpdateFreqService.getDefaultUpdateFreqBySiteId(siteId);
            if (defaultUpdateFreqCheck == null) {//不存在对应siteId的自查提醒记录，需要新增记录
                addDefaultUpdateFreq(defaultUpdateFreq);
            } else {//存在当前siteId对应自查提醒记录，修改记录
                updateDefaultUpdateFreq(defaultUpdateFreq);
            }
            return null;
        }, OperationType.ADD + "," + OperationType.UPDATE, logDesc, LogUtil.getSiteNameForLog(siteApiService, defaultUpdateFreq.getSiteId()));
    }

    private Integer updateDefaultUpdateFreq(DefaultUpdateFreq defaultUpdateFreq) throws RemoteException, BizException {
        String logDesc = "修改自查提醒记录" + LogUtil.paramsToLogString(DEFAULT_UPDATE_FREQ_NAME, defaultUpdateFreq);
        return LogUtil.ControlleFunctionWrapper(() -> {
            defaultUpdateFreqService.updateDefaultUpdateFreq(defaultUpdateFreq);
            return null;
        }, OperationType.UPDATE, logDesc, LogUtil.getSiteNameForLog(siteApiService, defaultUpdateFreq.getSiteId()));
    }

    private Integer addDefaultUpdateFreq(DefaultUpdateFreq defaultUpdateFreq) throws RemoteException, BizException {
        String logDesc = "插入自查提醒记录" + LogUtil.paramsToLogString(DEFAULT_UPDATE_FREQ_NAME, defaultUpdateFreq);
        return LogUtil.ControlleFunctionWrapper(() -> {
            defaultUpdateFreqService.addDefaultUpdateFreq(defaultUpdateFreq);
            return null;
        }, OperationType.ADD, logDesc, LogUtil.getSiteNameForLog(siteApiService, defaultUpdateFreq.getSiteId()));
    }

}

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
import java.util.Date;

/**
 * 按需更新的自查提醒Controller
 * Created by he.lang on 2017/5/15.
 */
@Slf4j
@RestController
@RequestMapping("/gov/kpi/setting")
public class DefaultUpdateFreqController {
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
        Date startTime = new Date();
        if (siteId == null) {
            log.error("Invalid parameter:  参数siteId存在null值");
            throw new BizException(Constants.INVALID_PARAMETER);
        }
        String logDesc = "通过siteId查询对应自查提醒记录";

        authorityService.checkRight(Authority.KPIWEB_INDEXSETUP_SEARCH, siteId);
        try {
            DefaultUpdateFreq defaultUpdateFreq = defaultUpdateFreqService.getDefaultUpdateFreqBySiteId(siteId);
            Integer value = null;
            if (defaultUpdateFreq != null) {
                value = defaultUpdateFreq.getValue();
            }
            Date endTime = new Date();
            LogUtil.addOperationLog(OperationType.QUERY, logDesc, LogUtil.getSiteNameForLog(siteApiService, siteId));
            LogUtil.addElapseLog(OperationType.QUERY, LogUtil.buildElapseLogDesc(siteApiService, siteId, logDesc), endTime.getTime() - startTime.getTime());
            return value;
        } catch (Exception e) {
            LogUtil.addOperationLog(OperationType.QUERY, LogUtil.buildFailOperationLogDesc(logDesc), LogUtil.getSiteNameForLog(siteApiService, siteId));
            throw e;
        }
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
    public Object save(@ModelAttribute DefaultUpdateFreq defaultUpdateFreq) throws BizException, ParseException, RemoteException {
        if (defaultUpdateFreq.getSiteId() == null || defaultUpdateFreq.getValue() == null) {
            log.error("Invalid parameter:  参数siteId、value（自查提醒周期值）中至少一个存在null值");
            throw new BizException(Constants.INVALID_PARAMETER);
        }
        authorityService.checkRight(Authority.KPIWEB_INDEXSETUP_UPDATEDEMANDFREQ, defaultUpdateFreq.getSiteId());
        int siteId = defaultUpdateFreq.getSiteId();
        DefaultUpdateFreq defaultUpdateFreqCheck = null;
        try {
            defaultUpdateFreqCheck = defaultUpdateFreqService.getDefaultUpdateFreqBySiteId(siteId);
        } catch (Exception e) {
            LogUtil.addOperationLog(OperationType.QUERY, LogUtil.buildFailOperationLogDesc("查询自查提醒记录"), LogUtil.getSiteNameForLog(siteApiService, siteId));
            throw e;
        }
        if (defaultUpdateFreqCheck == null) {//不存在对应siteId的自查提醒记录，需要新增记录
            try {
                defaultUpdateFreqService.addDefaultUpdateFreq(defaultUpdateFreq);
                LogUtil.addOperationLog(OperationType.ADD, "插入自查提醒记录", LogUtil.getSiteNameForLog(siteApiService, siteId));
            } catch (Exception e) {
                LogUtil.addOperationLog(OperationType.ADD, LogUtil.buildFailOperationLogDesc("插入自查提醒记录"), LogUtil.getSiteNameForLog(siteApiService, siteId));
                throw e;
            }

        } else {//存在当前siteId对应自查提醒记录，修改记录
            try {
                defaultUpdateFreqService.updateDefaultUpdateFreq(defaultUpdateFreq);
                LogUtil.addOperationLog(OperationType.UPDATE, "修改对应自查提醒记录", LogUtil.getSiteNameForLog(siteApiService, siteId));
            } catch (Exception e) {
                LogUtil.addOperationLog(OperationType.ADD, LogUtil.buildFailOperationLogDesc("修改对应自查提醒记录"), LogUtil.getSiteNameForLog(siteApiService, siteId));
                throw e;
            }
        }
        return null;
    }
}

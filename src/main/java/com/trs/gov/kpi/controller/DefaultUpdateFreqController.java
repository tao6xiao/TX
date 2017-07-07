package com.trs.gov.kpi.controller;

import com.trs.gov.kpi.constant.Authority;
import com.trs.gov.kpi.constant.Constants;
import com.trs.gov.kpi.entity.DefaultUpdateFreq;
import com.trs.gov.kpi.entity.exception.BizException;
import com.trs.gov.kpi.entity.exception.RemoteException;
import com.trs.gov.kpi.service.DefaultUpdateFreqService;
import com.trs.gov.kpi.service.outer.AuthorityService;
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
    @Resource
    DefaultUpdateFreqService defaultUpdateFreqService;

    @Resource
    private AuthorityService authorityService;

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
        if (!authorityService.hasRight(siteId, null, Authority.KPIWEB_INDEXSETUP_SEARCH)) {
            throw new BizException(Authority.NO_AUTHORITY);
        }
        if (siteId == null) {
            log.error("Invalid parameter:  参数siteId存在null值");
            throw new BizException(Constants.INVALID_PARAMETER);
        }
        DefaultUpdateFreq defaultUpdateFreq = defaultUpdateFreqService.getDefaultUpdateFreqBySiteId(siteId);
        Integer value = null;
        if (defaultUpdateFreq != null) {
            value = defaultUpdateFreq.getValue();
        }
        return value;
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
        if (!authorityService.hasRight(defaultUpdateFreq.getSiteId(), null, Authority.KPIWEB_INDEXSETUP_UPDATEDEMANDFREQ)) {
            throw new BizException(Authority.NO_AUTHORITY);
        }
        if (defaultUpdateFreq.getSiteId() == null || defaultUpdateFreq.getValue() == null) {
            log.error("Invalid parameter:  参数siteId、value（自查提醒周期值）中至少一个存在null值");
            throw new BizException(Constants.INVALID_PARAMETER);
        }
        int siteId = defaultUpdateFreq.getSiteId();
        DefaultUpdateFreq defaultUpdateFreqCheck = defaultUpdateFreqService.getDefaultUpdateFreqBySiteId(siteId);
        if (defaultUpdateFreqCheck == null) {//不存在对应siteId的自查提醒记录，需要新增记录
            defaultUpdateFreqService.addDefaultUpdateFreq(defaultUpdateFreq);
        } else {//存在当前siteId对应自查提醒记录，修改记录
            defaultUpdateFreqService.updateDefaultUpdateFreq(defaultUpdateFreq);
        }
        return null;
    }
}

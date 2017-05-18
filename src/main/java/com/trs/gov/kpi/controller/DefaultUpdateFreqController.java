package com.trs.gov.kpi.controller;

import com.trs.gov.kpi.entity.DefaultUpdateFreq;
import com.trs.gov.kpi.entity.exception.BizException;
import com.trs.gov.kpi.service.DefaultUpdateFreqService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 按需更新的自查提醒Controller
 * Created by he.lang on 2017/5/15.
 */
@RestController
@RequestMapping("/gov/kpi/setting")
public class DefaultUpdateFreqController {
    @Resource
    DefaultUpdateFreqService defaultUpdateFreqService;

    /**
     * 通过siteId查询对应自查提醒记录
     * @param siteId
     * @return
     * @throws BizException
     */
    @RequestMapping(value = "/defaultupdatefreq", method = RequestMethod.GET)
    @ResponseBody
    public Integer getDefaultUpdateFreqBySiteId(@RequestParam Integer siteId) throws BizException {
        if(siteId == null){
            throw new BizException("传入的参数存在null值");
        }
        DefaultUpdateFreq defaultUpdateFreq = defaultUpdateFreqService.getDefaultUpdateFreqBySiteId(siteId);
        Integer value = null;
        if(defaultUpdateFreq != null) {
            value  = defaultUpdateFreq.getValue();
        }
        return value;
    }

    /**
     * 插入或者修改自查提醒记录
     * @param defaultUpdateFreq
     * @return
     * @throws BizException
     */
    @RequestMapping(value = "/defaultupdatefreq", method = RequestMethod.PUT)
    @ResponseBody
    public Object save(@ModelAttribute DefaultUpdateFreq defaultUpdateFreq) throws BizException {
        if(defaultUpdateFreq.getSiteId() == null || defaultUpdateFreq.getValue() == null){
            throw new BizException("传入的参数存在null值");
        }
        int siteId = defaultUpdateFreq.getSiteId();
        DefaultUpdateFreq defaultUpdateFreqCheck = defaultUpdateFreqService.getDefaultUpdateFreqBySiteId(siteId);
        int num = 0;
        if(defaultUpdateFreqCheck == null){//不存在对应siteId的自查提醒记录，需要新增记录
            num = defaultUpdateFreqService.addDefaultUpdateFreq(defaultUpdateFreq);
        }else{//存在当前siteId对应自查提醒记录，修改记录
            num = defaultUpdateFreqService.updateDefaultUpdateFreq(defaultUpdateFreq);
        }
        return null;
    }
}

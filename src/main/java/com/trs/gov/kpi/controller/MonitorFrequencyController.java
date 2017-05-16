package com.trs.gov.kpi.controller;

import com.trs.gov.kpi.constant.FrequencyType;
import com.trs.gov.kpi.entity.MonitorFrequency;
import com.trs.gov.kpi.entity.responsedata.MonitorFrequencyDeal;
import com.trs.gov.kpi.entity.requestdata.MonitorFrequencyFreq;
import com.trs.gov.kpi.entity.requestdata.MonitorFrequencySetUp;
import com.trs.gov.kpi.entity.exception.BizException;
import com.trs.gov.kpi.service.MonitorFrequencyService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by helang on 2017/5/12.
 * 监测频率Controller
 */
@RestController
@RequestMapping(value = "/gov/kpi/setting")
public class MonitorFrequencyController {
    @Resource
    MonitorFrequencyService monitorFrequencyService;

    /**
     * 通过siteId获取当前站点监测频率的设置记录集合
     *
     * @param siteId
     * @return
     * @throws BizException
     */
    @RequestMapping(value = "/checkfreq", method = RequestMethod.GET)
    @ResponseBody
    public List<MonitorFrequencyDeal> queryBySiteId(@RequestParam Integer siteId) throws BizException {
        if (siteId == null) {
            throw new BizException("参数存在null值");
        }
        List<MonitorFrequencyDeal> monitorFrequencyDealList = monitorFrequencyService.queryBySiteId(siteId);
        return monitorFrequencyDealList;
    }

    /**
     * 通过传入的对象，判断插入记录还是修改记录，然后进行操作
     *
     * @param monitorFrequencySetUp
     * @return
     * @throws BizException
     */
    @RequestMapping(value = "/monitorfrequency", method = RequestMethod.POST)
    @ResponseBody
    public Object save(@RequestBody MonitorFrequencySetUp monitorFrequencySetUp) throws BizException {
        MonitorFrequencyFreq[] freqs = monitorFrequencySetUp.getFreqs();
        if (monitorFrequencySetUp.getSiteId() == null || freqs.length == 0 || freqs == null) {
            throw new BizException("参数存在null值");
        }
        for (int i = 0; i < freqs.length; i++) {
            if (freqs[i].getId() == null) {
                throw new BizException("参数siteId存在null值");
            }
        }
        int siteId = monitorFrequencySetUp.getSiteId();
        List<MonitorFrequency> monitorFrequencyList = monitorFrequencyService.checkSiteIdAndTypeAreBothExitsOrNot(siteId);
        int num = 0;
        if (monitorFrequencyList == null || monitorFrequencyList.size() == 0) {//siteId和typeId同时不存在，插入记录
            if(freqs.length < FrequencyType.values().length){
                throw new BizException("添加频率设置时，缺少某些频率类型的数据");
            }
            for (int i = 0; i < freqs.length; i++) {
                if (freqs[i].getValue() == null) {
                    throw new BizException("参数value存在null值");
                }
            }
            num = monitorFrequencyService.addMonitorFrequencySetUp(monitorFrequencySetUp);
        } else {//siteId和typeId同时存在，修改对应站点的监测频率记录
            num = monitorFrequencyService.updateMonitorFrequencySetUp(monitorFrequencySetUp);
        }
        return null;
    }
}

package com.trs.gov.kpi.controller;

import com.trs.gov.kpi.constant.Constants;
import com.trs.gov.kpi.constant.FrequencyType;
import com.trs.gov.kpi.entity.MonitorFrequency;
import com.trs.gov.kpi.entity.exception.BizException;
import com.trs.gov.kpi.entity.requestdata.MonitorFrequencyFreq;
import com.trs.gov.kpi.entity.requestdata.MonitorFrequencySetUp;
import com.trs.gov.kpi.entity.responsedata.MonitorFrequencyResponse;
import com.trs.gov.kpi.service.MonitorFrequencyService;
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

    /**
     * 通过siteId获取当前站点监测频率的设置记录集合
     *
     * @param siteId
     * @return
     * @throws BizException
     */
    @RequestMapping(value = "/checkfreq", method = RequestMethod.GET)
    @ResponseBody
    public List<MonitorFrequencyResponse> queryBySiteId(@RequestParam Integer siteId) throws BizException {
        if (siteId == null) {
            throw new BizException(Constants.INVALID_PARAMETER);
        }
        List<MonitorFrequencyResponse> monitorFrequencyResponseList = monitorFrequencyService.queryBySiteId(siteId);
        return monitorFrequencyResponseList;
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
    public Object save(@RequestBody MonitorFrequencySetUp freqSetUp) throws BizException {
        MonitorFrequencyFreq[] freqs = freqSetUp.getFreqs();
        if (freqSetUp.getSiteId() == null || freqs == null || freqs.length == 0) {
            throw new BizException(Constants.INVALID_PARAMETER);
        }
        for (int i = 0; i < freqs.length; i++) {
            if (freqs[i].getId() == null || freqs[i].getValue() == null) {
                log.error("Invalid parameter: 参数freqs[]中id（类型编号）存在null值");
                throw new BizException(Constants.INVALID_PARAMETER);
            }

            if(FrequencyType.valueOf(freqs[i].getId()) == null){
                log.error("Invalid parameter: 参数freqs[]中id（类型编号）无对应的频率设置类型");
                throw new BizException(Constants.INVALID_PARAMETER);
            }
        }
        if (freqs.length < FrequencyType.values().length) {
            log.error("Invalid parameter: 添加频率设置时，缺少某些频率类型的数据");
            throw new BizException(Constants.INVALID_PARAMETER);
        }

        int siteId = freqSetUp.getSiteId();
        List<MonitorFrequency> monitorFrequencyList = monitorFrequencyService.checkSiteIdAndTypeAreBothExitsOrNot(siteId);
        if (monitorFrequencyList == null || monitorFrequencyList.size() == 0) {//siteId和typeId同时不存在，插入记录
            monitorFrequencyService.addMonitorFrequencySetUp(freqSetUp);
        } else {//siteId和typeId同时存在，修改对应站点的监测频率记录
            monitorFrequencyService.updateMonitorFrequencySetUp(freqSetUp);
        }
        return null;
    }
}

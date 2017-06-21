package com.trs.gov.kpi.service.impl;

import com.trs.gov.kpi.constant.EnumCheckJobType;
import com.trs.gov.kpi.constant.FrequencyType;
import com.trs.gov.kpi.dao.MonitorFrequencyMapper;
import com.trs.gov.kpi.entity.MonitorFrequency;
import com.trs.gov.kpi.entity.exception.BizException;
import com.trs.gov.kpi.entity.requestdata.MonitorFrequencyFreq;
import com.trs.gov.kpi.entity.requestdata.MonitorFrequencySetUp;
import com.trs.gov.kpi.entity.responsedata.MonitorFrequencyResponse;
import com.trs.gov.kpi.service.MonitorFrequencyService;
import com.trs.gov.kpi.service.MonitorSiteService;
import com.trs.gov.kpi.service.SchedulerService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by helang on 2017/5/12.
 */
@Service
public class MonitorFrequencyServiceImpl implements MonitorFrequencyService {

    @Resource
    MonitorSiteService monitorSiteService;

    @Resource
    MonitorFrequencyMapper monitorFrequencyMapper;

    @Resource
    SchedulerService schedulerService;

    @Override
    public List<MonitorFrequencyResponse> queryBySiteId(int siteId) {
        List<MonitorFrequency> monitorFrequencyList = monitorFrequencyMapper.queryBySiteId(siteId);
        List<MonitorFrequencyResponse> monitorFrequencyResponseList = new ArrayList<>();
        if (monitorFrequencyList != null) {
            for (MonitorFrequency monitorFrequency : monitorFrequencyList) {
                MonitorFrequencyResponse monitorFrequencyResponse =
                        getMonitorFrequencyDealFromMonitorFrequency(monitorFrequency);
                monitorFrequencyResponseList.add(monitorFrequencyResponse);
            }
        }
        return monitorFrequencyResponseList;
    }

    @Override
    public int addMonitorFrequencySetUp(MonitorFrequencySetUp monitorFrequencySetUp) throws BizException {
        List<MonitorFrequency> monitorFrequencyList = addFrequencySetUpToList(monitorFrequencySetUp);
        int num = monitorFrequencyMapper.insertMonitorFrequencyList(monitorFrequencyList);
        updateMonitorScheduler(monitorFrequencyList);
        return num;
    }

    @Override
    public List<MonitorFrequency> checkSiteIdAndTypeAreBothExitsOrNot(int siteId) {
        return monitorFrequencyMapper.queryBySiteId(siteId);
    }

    @Override
    public int updateMonitorFrequencySetUp(MonitorFrequencySetUp monitorFrequencySetUp) throws BizException {
        List<MonitorFrequency> monitorFrequencyList = addFrequencySetUpToList(monitorFrequencySetUp);
        int num = monitorFrequencyMapper.updateMonitorFrequencySetUp(monitorFrequencyList);
        updateMonitorScheduler(monitorFrequencyList);
        return num;
    }

    private List<MonitorFrequency> addFrequencySetUpToList(MonitorFrequencySetUp monitorFrequencySetUp) {
        int siteId = monitorFrequencySetUp.getSiteId();
        MonitorFrequencyFreq[] freqs = monitorFrequencySetUp.getFreqs();
        List<MonitorFrequency> monitorFrequencyList = new ArrayList<>();//为何创建ArrayList
        for (int i = 0; i < freqs.length; i++) {
            MonitorFrequencyFreq monitorFrequencyFreq = freqs[i];
            MonitorFrequency monitorFrequency = new MonitorFrequency();
            monitorFrequency.setSiteId(siteId);
            monitorFrequency.setTypeId(monitorFrequencyFreq.getId());
            Short value = monitorFrequencyFreq.getValue();
            if (value != null) {
                monitorFrequency.setValue(value);
                monitorFrequencyList.add(monitorFrequency);
            }
        }
        return monitorFrequencyList;
    }

    private MonitorFrequencyResponse getMonitorFrequencyDealFromMonitorFrequency(MonitorFrequency monitorFrequency) {
        MonitorFrequencyResponse monitorFrequencyResponse = new MonitorFrequencyResponse();
        monitorFrequencyResponse.setId(monitorFrequency.getTypeId());
        monitorFrequencyResponse.setValue(monitorFrequency.getValue());
        int typeId = monitorFrequency.getTypeId();
        FrequencyType frequencyType = FrequencyType.valueOf(typeId);
        monitorFrequencyResponse.setName(frequencyType.getName());
        monitorFrequencyResponse.setFreqUnit(frequencyType.getFreqUnit().getCode());
        return monitorFrequencyResponse;
    }

    /**
     * 更新检测任务
     *
     * @param monitorFrequencyList
     */
    private void updateMonitorScheduler(List<MonitorFrequency> monitorFrequencyList) throws BizException {

        if (monitorFrequencyList == null || monitorFrequencyList.isEmpty()) {
            return;
        }

        for (MonitorFrequency monitorFrequency : monitorFrequencyList) {
            if (monitorFrequency.getTypeId() == FrequencyType.TOTAL_BROKEN_LINKS.getTypeId()) {
                schedulerService.removeCheckJob(monitorFrequency.getSiteId(), EnumCheckJobType
                        .CHECK_LINK);
                schedulerService.addCheckJob(monitorFrequency.getSiteId(), EnumCheckJobType
                        .CHECK_LINK);
            } else if (monitorFrequency.getTypeId() == FrequencyType.HOMEPAGE_AVAILABILITY
                    .getTypeId()) {
                schedulerService.removeCheckJob(monitorFrequency.getSiteId(), EnumCheckJobType
                        .CHECK_HOME_PAGE);
                schedulerService.addCheckJob(monitorFrequency.getSiteId(), EnumCheckJobType
                        .CHECK_HOME_PAGE);
            } else if (monitorFrequency.getTypeId() == FrequencyType.WRONG_INFORMATION.getTypeId
                    ()) {
                schedulerService.removeCheckJob(monitorFrequency.getSiteId(), EnumCheckJobType
                        .CHECK_CONTENT);
                schedulerService.addCheckJob(monitorFrequency.getSiteId(), EnumCheckJobType
                        .CHECK_CONTENT);
            }
        }
    }
}

package com.trs.gov.kpi.service.impl;

import com.trs.gov.kpi.constant.FrequencyType;
import com.trs.gov.kpi.dao.MonitorFrequencyMapper;
import com.trs.gov.kpi.entity.MonitorFrequency;
import com.trs.gov.kpi.entity.MonitorSite;
import com.trs.gov.kpi.entity.responsedata.MonitorFrequencyDeal;
import com.trs.gov.kpi.entity.requestdata.MonitorFrequencyFreq;
import com.trs.gov.kpi.entity.requestdata.MonitorFrequencySetUp;
import com.trs.gov.kpi.service.MonitorFrequencyService;
import com.trs.gov.kpi.service.MonitorSiteService;
import com.trs.gov.kpi.service.SchedulerService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public List<MonitorFrequencyDeal> queryBySiteId(int siteId) {
        List<MonitorFrequency> monitorFrequencyList = monitorFrequencyMapper.queryBySiteId(siteId);
        List<MonitorFrequencyDeal> monitorFrequencyDealList = new ArrayList<>();
        if (monitorFrequencyList != null) {
//            Map<Integer, MonitorFrequencyType> monitorFrequencyTypeAllMap = MonitorFrequencyTypeModel.getTypes();//获取全部监测类型
            for (MonitorFrequency monitorFrequency : monitorFrequencyList) {
                MonitorFrequencyDeal monitorFrequencyDeal = getMonitorFrequencyDealFromMonitorFrequency(monitorFrequency);
                monitorFrequencyDealList.add(monitorFrequencyDeal);
            }
        }
        return monitorFrequencyDealList;
    }

    @Override
    public int addMonitorFrequencySetUp(MonitorFrequencySetUp monitorFrequencySetUp) {
        List<MonitorFrequency> monitorFrequencyList = addFrequencySetUpToList(monitorFrequencySetUp);
        int num = monitorFrequencyMapper.insertMonitorFrequencyList(monitorFrequencyList);
        updateMonitorScheduler(monitorFrequencyList);
        return num;
    }

    @Override
    public List<MonitorFrequency> checkSiteIdAndTypeAreBothExitsOrNot(int siteId) {
        List<MonitorFrequency> monitorFrequencyList = monitorFrequencyMapper.queryBySiteId(siteId);
        return monitorFrequencyList;
    }

    @Override
    public int updateMonitorFrequencySetUp(MonitorFrequencySetUp monitorFrequencySetUp) {
        List<MonitorFrequency> monitorFrequencyList = addFrequencySetUpToList(monitorFrequencySetUp);
        int num = monitorFrequencyMapper.updateMonitorFrequencySetUp(monitorFrequencyList);
//        updateMonitorScheduler(monitorFrequencyList);
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

    private MonitorFrequencyDeal getMonitorFrequencyDealFromMonitorFrequency(MonitorFrequency monitorFrequency) {
        MonitorFrequencyDeal monitorFrequencyDeal = new MonitorFrequencyDeal();
        monitorFrequencyDeal.setId(monitorFrequency.getTypeId());
        monitorFrequencyDeal.setValue(monitorFrequency.getValue());
        int typeId = monitorFrequency.getTypeId();
        FrequencyType frequencyType = FrequencyType.getFrequencyTypeByTypeId(typeId);
        monitorFrequencyDeal.setName(frequencyType.getName());
        monitorFrequencyDeal.setFreqUnit(frequencyType.getFreqUnit().getCode());
        return monitorFrequencyDeal;
    }

    private void updateMonitorScheduler(List<MonitorFrequency> monitorFrequencyList) {

        for(MonitorFrequency monitorFrequency: monitorFrequencyList) {

            MonitorSite monitorSite = monitorSiteService.getMonitorSiteBySiteId(monitorFrequency.getSiteId());
            schedulerService.registerScheduler(
                    monitorSite.getIndexUrl(),
                    monitorFrequency.getSiteId(),
                    FrequencyType.getFrequencyTypeByTypeId(monitorFrequency.getTypeId()),
                    FrequencyType.getFrequencyTypeByTypeId(monitorFrequency.getTypeId()).getFreqUnit(),
                    monitorFrequency.getValue().intValue()
            );
        }
    }
}

package com.trs.gov.kpi.service.impl;

import com.trs.gov.kpi.dao.MonitorFrequencyMapper;
import com.trs.gov.kpi.entity.MonitorFrequency;
import com.trs.gov.kpi.entity.MonitorFrequencyDeal;
import com.trs.gov.kpi.model.MonitorFrequencyType;
import com.trs.gov.kpi.model.MonitorFrequencyTypeModel;
import com.trs.gov.kpi.service.MonitorFrequencyService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by helang on 2017/5/12.
 */
@Service
public class MonitorFrequencyServiceImpl implements MonitorFrequencyService{

    @Resource
    MonitorFrequencyMapper monitorFrequencyMapper;

    @Override
    public List<MonitorFrequencyDeal> queryBySiteId(int siteId) {
        List<MonitorFrequency> monitorFrequencyList = monitorFrequencyMapper.queryBySiteId(siteId);
        List<MonitorFrequencyDeal> monitorFrequencyDealList = new ArrayList<>();
        if(monitorFrequencyList != null) {
            HashMap<Integer, MonitorFrequencyType> monitorFrequencyTypeAllMap = MonitorFrequencyTypeModel.getTypes();//获取全部监测类型
            for (MonitorFrequency monitorFrequency : monitorFrequencyList) {
                MonitorFrequencyDeal monitorFrequencyDeal = getMonitorFrequencyDealFromMonitorFrequency(monitorFrequency,monitorFrequencyTypeAllMap);
                monitorFrequencyDealList.add(monitorFrequencyDeal);
            }
        }
        return monitorFrequencyDealList;
    }

    private MonitorFrequencyDeal getMonitorFrequencyDealFromMonitorFrequency(MonitorFrequency monitorFrequency,HashMap<Integer, MonitorFrequencyType> monitorFrequencyTypeAllMap) {
        MonitorFrequencyDeal monitorFrequencyDeal = new MonitorFrequencyDeal();
        monitorFrequencyDeal.setId(monitorFrequency.getTypeId());
        monitorFrequencyDeal.setValue(monitorFrequency.getValue());
        int typeId = monitorFrequency.getTypeId();
        MonitorFrequencyType monitorFrequencyType = monitorFrequencyTypeAllMap.get(typeId);
        if(monitorFrequencyType != null){
            monitorFrequencyDeal.setName(monitorFrequencyType.getName());
            monitorFrequencyDeal.setFreqUnit(monitorFrequencyType.getFreqUnit());
        }
        return monitorFrequencyDeal;
    }
}

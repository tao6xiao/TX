package com.trs.gov.kpi.service.impl;

import com.trs.gov.kpi.dao.MonitorTimeMapper;
import com.trs.gov.kpi.entity.MonitorTime;
import com.trs.gov.kpi.service.MonitorTimeService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

/**
 * Created by ranwei on 2017/7/31.
 */
@Service
public class MonitorTimeServiceImpl implements MonitorTimeService {

    @Resource
    private MonitorTimeMapper monitorTimeMapper;

    @Override
    public Date getMonitorTime(Integer siteId, Integer typeId) {
        return monitorTimeMapper.getMonitorTime(siteId,typeId);
    }

    @Override
    public void insertMonitorTime(MonitorTime monitorTime) {

    }
}

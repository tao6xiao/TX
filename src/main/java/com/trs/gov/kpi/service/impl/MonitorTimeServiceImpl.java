package com.trs.gov.kpi.service.impl;

import com.trs.gov.kpi.dao.CommonMapper;
import com.trs.gov.kpi.dao.MonitorTimeMapper;
import com.trs.gov.kpi.entity.MonitorTime;
import com.trs.gov.kpi.service.MonitorTimeService;
import com.trs.gov.kpi.utils.DBUtil;
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

    @Resource
    private CommonMapper commonMapper;

    @Override
    public Date getMonitorStartTime(Integer siteId, Integer typeId) {
        return monitorTimeMapper.getMonitorStartTime(siteId, typeId);
    }

    @Override
    public Date getMonitorEndTime(Integer siteId, Integer typeId) {
        return monitorTimeMapper.getMonitorEndTime(siteId, typeId);
    }

    @Override
    public void insertMonitorTime(MonitorTime monitorTime) {
        commonMapper.insert(DBUtil.toRow(monitorTime));
    }
}

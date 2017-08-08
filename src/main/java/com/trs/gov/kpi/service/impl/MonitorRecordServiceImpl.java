package com.trs.gov.kpi.service.impl;

import com.trs.gov.kpi.dao.CommonMapper;
import com.trs.gov.kpi.dao.MonitorRecordMapper;
import com.trs.gov.kpi.entity.MonitorRecord;
import com.trs.gov.kpi.service.MonitorRecordService;
import com.trs.gov.kpi.utils.DBUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

/**
 * Created by li.hao on 2017/8/4.
 */
@Service
public class MonitorRecordServiceImpl implements MonitorRecordService {

    @Resource
    private CommonMapper commonMapper;

    @Resource
    private MonitorRecordMapper monitorRecordMapper;

    @Override
    public void insertMonitorRecord(MonitorRecord monitorRecord) {

        commonMapper.insert(DBUtil.toRow(monitorRecord));
    }

    @Override
    public Date getMonitorStartTime(Integer siteId, Integer typeId) {
        return monitorRecordMapper.getMonitorStartTime(siteId, typeId);
    }

    @Override
    public Date getMonitorEndTime(Integer siteId, Integer typeId) {
        return monitorRecordMapper.getMonitorEndTime(siteId, typeId);
    }
}

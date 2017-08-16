package com.trs.gov.kpi.service.impl;

import com.trs.gov.kpi.constant.MonitorRecordTableField;
import com.trs.gov.kpi.constant.Status;
import com.trs.gov.kpi.constant.Types;
import com.trs.gov.kpi.dao.CommonMapper;
import com.trs.gov.kpi.dao.MonitorRecordMapper;
import com.trs.gov.kpi.entity.MonitorRecord;
import com.trs.gov.kpi.entity.dao.QueryFilter;
import com.trs.gov.kpi.entity.dao.Table;
import com.trs.gov.kpi.entity.exception.BizException;
import com.trs.gov.kpi.entity.requestdata.PageDataRequestParam;
import com.trs.gov.kpi.entity.responsedata.ApiPageData;
import com.trs.gov.kpi.entity.responsedata.MonitorRecordResponse;
import com.trs.gov.kpi.entity.responsedata.Pager;
import com.trs.gov.kpi.service.MonitorRecordService;
import com.trs.gov.kpi.service.helper.QueryFilterHelper;
import com.trs.gov.kpi.utils.DBUtil;
import com.trs.gov.kpi.utils.PageInfoDeal;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
    public Date getLastMonitorEndTime(Integer siteId, Integer taskId) {

        QueryFilter filter = new QueryFilter(Table.MONITOR_RECORD);
        filter.addCond(MonitorRecordTableField.SITE_ID, siteId);
        filter.addCond(MonitorRecordTableField.TASK_STATUS, Status.MonitorStatusType.CHECK_DONE.value);
        filter.addCond(MonitorRecordTableField.TASK_ID, taskId);

        return monitorRecordMapper.getLastMonitorEndTime(filter);
    }

    @Override
    public Integer getResultByLastEndTime(Integer siteId, Integer taskId, Date endTime) {
        QueryFilter filter = new QueryFilter(Table.MONITOR_RECORD);
        filter.addCond(MonitorRecordTableField.SITE_ID, siteId);
        filter.addCond(MonitorRecordTableField.END_TIME, endTime);
        filter.addCond(MonitorRecordTableField.TASK_ID, taskId);

        return monitorRecordMapper.getResultByLastEndTime(filter);
    }

    @Override
    public ApiPageData selectMonitorRecordList(PageDataRequestParam param) throws BizException {

        QueryFilter filter = QueryFilterHelper.toMonitorRecordFilter(param);
        if (param.getBeginDateTime() != null) {
            filter.addCond(MonitorRecordTableField.BEGIN_TIME, param.getBeginDateTime()).setRangeBegin(true);
        }
        if (param.getEndDateTime() != null) {
            filter.addCond(MonitorRecordTableField.BEGIN_TIME, param.getEndDateTime()).setRangeEnd(true);
        }
        int itemCount = commonMapper.count(filter);
        Pager pager = PageInfoDeal.buildResponsePager(param.getPageIndex(), param.getPageSize(), itemCount);
        filter.setPager(pager);

        List<MonitorRecord> monitorRecordList = monitorRecordMapper.selectMonitorRecordList(filter);
        List<MonitorRecordResponse> monitorRecordResponseList = new ArrayList<>();
        for (MonitorRecord monitorRecord : monitorRecordList) {
            MonitorRecordResponse monitorRecordResponse = toMonitorRecordResponse(monitorRecord);
            monitorRecordResponseList.add(monitorRecordResponse);
        }
        return new ApiPageData(pager, monitorRecordResponseList);
    }

    @NotNull
    private MonitorRecordResponse toMonitorRecordResponse(MonitorRecord monitorRecord) {
        MonitorRecordResponse monitorRecordResponse = new MonitorRecordResponse();
        monitorRecordResponse.setTaskId(monitorRecord.getTaskId());
        monitorRecordResponse.setTaskName(Types.MonitorRecordNameType.valueOf(monitorRecord.getTaskId()).getName());
        monitorRecordResponse.setTaskStatusName(Status.MonitorStatusType.valueOf(monitorRecord.getTaskStatus()).getName());
        monitorRecordResponse.setBeginDateTime(monitorRecord.getBeginTime());
        monitorRecordResponse.setEndDateTime(monitorRecord.getEndTime());
        //如果任务状态为检测失败就不返回检测结果
        if (monitorRecord.getTaskStatus() != Status.MonitorStatusType.CHECK_ERROR.value) {
            monitorRecordResponse.setResult(monitorRecord.getResult());
        }
        return monitorRecordResponse;
    }
}

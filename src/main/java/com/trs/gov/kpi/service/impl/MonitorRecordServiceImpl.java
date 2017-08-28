package com.trs.gov.kpi.service.impl;

import com.trs.gov.kpi.constant.MonitorRecordTableField;
import com.trs.gov.kpi.constant.Status;
import com.trs.gov.kpi.constant.Types;
import com.trs.gov.kpi.dao.CommonMapper;
import com.trs.gov.kpi.dao.MonitorRecordMapper;
import com.trs.gov.kpi.entity.MonitorRecord;
import com.trs.gov.kpi.entity.dao.DBUpdater;
import com.trs.gov.kpi.entity.dao.QueryFilter;
import com.trs.gov.kpi.entity.dao.Table;
import com.trs.gov.kpi.entity.exception.BizException;
import com.trs.gov.kpi.entity.requestdata.PageDataRequestParam;
import com.trs.gov.kpi.entity.responsedata.ApiPageData;
import com.trs.gov.kpi.entity.responsedata.MonitorOnceResponse;
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
    public void insertBeginManualMonitorRecord(Integer siteId, Integer taskId, Integer typeId, Date beginTime) {
        MonitorRecord monitorRecord = new MonitorRecord();
        monitorRecord.setSiteId(siteId);
        monitorRecord.setTaskId(taskId);
        monitorRecord.setTypeId(typeId);
        monitorRecord.setBeginTime(beginTime);
        monitorRecord.setTaskStatus(Status.MonitorStatusType.WAIT_CHECK.value);
        commonMapper.insert(DBUtil.toRow(monitorRecord));
    }

    @Override
    public Date getLastManualMonitorBeginTime(QueryFilter filter) {
        return monitorRecordMapper.getLastManualMonitorBeginTime(filter);
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
        filter.addCond(MonitorRecordTableField.TASK_STATUS, Status.MonitorStatusType.CHECK_DONE.value);
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

        filter.addSortField(MonitorRecordTableField.BEGIN_TIME, false);
        List<MonitorRecord> monitorRecordList = monitorRecordMapper.selectMonitorRecordList(filter);
        List<MonitorRecordResponse> monitorRecordResponseList = new ArrayList<>();
        for (MonitorRecord monitorRecord : monitorRecordList) {
            MonitorRecordResponse monitorRecordResponse = toMonitorRecordResponse(monitorRecord);
            monitorRecordResponseList.add(monitorRecordResponse);
        }
        return new ApiPageData(pager, monitorRecordResponseList);
    }

    @Override
    public List<MonitorOnceResponse> selectMonitorResulrOnce(Integer siteId, List<Integer> checkJobValues) {
        List<MonitorOnceResponse> monitorOnceResponseList = new ArrayList<>();
        for (int i=0; i < checkJobValues.size(); i++){
            List<MonitorRecord> monitorRecordList = monitorRecordMapper.selectNewestMonitorRecord(siteId, checkJobValues.get(i));
            MonitorOnceResponse monitorOnceResponse = new MonitorOnceResponse();

            if(monitorRecordList.isEmpty()){
                monitorOnceResponse.setTaskStatusName(Status.MonitorStatusType.NO_CHECK.getName());
                monitorOnceResponse.setTaskId(checkJobValues.get(i));
                monitorOnceResponseList.add(monitorOnceResponse);
            }else {
                monitorOnceResponse.setTaskId(monitorRecordList.get(0).getTaskId());
                monitorOnceResponse.setTaskStatusName(Status.MonitorStatusType.valueOf(monitorRecordList.get(0).getTaskStatus()).getName());
                monitorOnceResponse.setBeginDateTime(monitorRecordList.get(0).getBeginTime());
                monitorOnceResponse.setEndDateTime(monitorRecordList.get(0).getEndTime());
                if (monitorRecordList.get(0).getTaskStatus() != Status.MonitorStatusType.CHECK_ERROR.value) {
                    monitorOnceResponse.setResult(monitorRecordList.get(0).getResult());
                }
                monitorOnceResponseList.add(monitorOnceResponse);
            }
        }
        return monitorOnceResponseList;
    }

    @Override
    public List<MonitorRecord> selectNewestMonitorRecord(Integer siteId, Integer taskId) {
       return monitorRecordMapper.selectNewestMonitorRecord(siteId, taskId);
    }

    @Override
    public List<MonitorOnceResponse> getMonitorOnceResponse(List<MonitorRecord> monitorRecordList) {

        List<MonitorOnceResponse> monitorOnceResponseList = new ArrayList<>();
        MonitorOnceResponse monitorOnceResponse = new MonitorOnceResponse();
        monitorOnceResponse.setTaskStatusName(Status.MonitorStatusType.valueOf(monitorRecordList.get(0).getTaskStatus()).getName());
        monitorOnceResponse.setTaskId(monitorRecordList.get(0).getTaskId());
        monitorOnceResponse.setBeginDateTime(monitorRecordList.get(0).getBeginTime());
        monitorOnceResponse.setEndDateTime(monitorRecordList.get(0).getEndTime());
        if (monitorRecordList.get(0).getTaskStatus() == Status.MonitorStatusType.CHECK_DONE.value) {
            monitorOnceResponse.setResult(monitorRecordList.get(0).getResult());
        }
        monitorOnceResponseList.add(monitorOnceResponse);
        return monitorOnceResponseList;
    }

    @Override
    public void updateLastServerAbnormalShutdownTaskMonitorState(Integer siteId, Integer taskId) {
        List<MonitorRecord> newestMonitorRecordList = monitorRecordMapper.selectNewestMonitorRecord(siteId, taskId);

        if((!newestMonitorRecordList.isEmpty() && newestMonitorRecordList.get(0).getTaskStatus() == Status.MonitorStatusType.DOING_CHECK.value)
                || (!newestMonitorRecordList.isEmpty() && newestMonitorRecordList.get(0).getTaskStatus() == Status.MonitorStatusType.WAIT_CHECK.value)){
            QueryFilter filter = new QueryFilter(Table.MONITOR_RECORD);
            filter.addCond(MonitorRecordTableField.SITE_ID, siteId);
            filter.addCond(MonitorRecordTableField.TASK_ID, taskId);
            filter.addCond(MonitorRecordTableField.BEGIN_TIME, newestMonitorRecordList.get(0).getBeginTime());

            DBUpdater updater = new DBUpdater(Table.MONITOR_RECORD.getTableName());
            updater.addField(MonitorRecordTableField.END_TIME, new Date());
            updater.addField(MonitorRecordTableField.TASK_STATUS,Status.MonitorStatusType.CHECK_ERROR.value);
            commonMapper.update(updater,filter);
        }

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

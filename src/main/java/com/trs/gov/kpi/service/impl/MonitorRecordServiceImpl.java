package com.trs.gov.kpi.service.impl;

import com.trs.gov.kpi.constant.MonitorRecordTableField;
import com.trs.gov.kpi.constant.Status;
import com.trs.gov.kpi.constant.Types;
import com.trs.gov.kpi.dao.CommonMapper;
import com.trs.gov.kpi.dao.MonitorRecordMapper;
import com.trs.gov.kpi.entity.MonitorRecord;
import com.trs.gov.kpi.entity.dao.QueryFilter;
import com.trs.gov.kpi.entity.requestdata.PageDataRequestParam;
import com.trs.gov.kpi.entity.responsedata.ApiPageData;
import com.trs.gov.kpi.entity.responsedata.MonitorRecordResponse;
import com.trs.gov.kpi.entity.responsedata.Pager;
import com.trs.gov.kpi.service.MonitorRecordService;
import com.trs.gov.kpi.service.helper.QueryFilterHelper;
import com.trs.gov.kpi.utils.DBUtil;
import com.trs.gov.kpi.utils.PageInfoDeal;
import lombok.NonNull;
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
    public Date getMonitorStartTime(@NonNull Integer siteId, @NonNull Integer taskId) {
        return monitorRecordMapper.getMonitorBeginTime(siteId, taskId);
    }

    @Override
    public Date getMonitorEndTime(@NonNull Integer siteId, @NonNull Integer taskId) {
        return monitorRecordMapper.getMonitorEndTime(siteId, taskId);
    }

    @Override
    public ApiPageData selectMonitorRecordList(PageDataRequestParam param) {

        QueryFilter filter = QueryFilterHelper.toMonitorRecordFilter(param);
        filter.addCond(MonitorRecordTableField.BEGIN_TIME, param.getBeginDateTime()).setRangeBegin(true);
        filter.addCond(MonitorRecordTableField.END_TIME, param.getEndDateTime()).setRangeEnd(true);

        int itemCount = monitorRecordMapper.selectMonitorRecordCount(filter);
        Pager pager = PageInfoDeal.buildResponsePager(param.getPageIndex(), param.getPageSize(), itemCount);
        filter.setPager(pager);

        List<MonitorRecord> monitorRecordList = monitorRecordMapper.selectMonitorRecordList(filter);
        List<MonitorRecordResponse> monitorRecordResponseList = new ArrayList<>();
        for (MonitorRecord monitorRecord : monitorRecordList) {
            MonitorRecordResponse monitorRecordResponse = new MonitorRecordResponse();
            monitorRecordResponse.setTaskName(Types.MonitorRecordNameType.valueOf(monitorRecord.getTaskId()).getName());
            monitorRecordResponse.setTaskStatusName(Status.MonitorStatusType.valueOf(monitorRecord.getTaskStatus()).getName());
            monitorRecordResponse.setBeginTime(monitorRecord.getBeginTime());
            monitorRecordResponse.setEndTime(monitorRecord.getEndTime());
            monitorRecordResponse.setResult(monitorRecord.getResult());

            monitorRecordResponseList.add(monitorRecordResponse);
        }
        return new ApiPageData(pager, monitorRecordResponseList);
    }
}

package com.trs.gov.kpi.service.impl;

import com.trs.gov.kpi.constant.*;
import com.trs.gov.kpi.dao.IssueMapper;
import com.trs.gov.kpi.entity.HistoryDate;
import com.trs.gov.kpi.entity.dao.QueryFilter;
import com.trs.gov.kpi.entity.dao.Table;
import com.trs.gov.kpi.entity.requestdata.IssueCountByTypeRequest;
import com.trs.gov.kpi.entity.requestdata.IssueCountRequest;
import com.trs.gov.kpi.entity.responsedata.*;
import com.trs.gov.kpi.service.InfoErrorService;
import com.trs.gov.kpi.service.IssueCountService;
import com.trs.gov.kpi.service.helper.QueryFilterHelper;
import com.trs.gov.kpi.utils.DateUtil;
import com.trs.gov.kpi.utils.InitTime;
import com.trs.gov.kpi.utils.StringUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * Created by he.lang on 2017/6/7.
 */
@Service
public class IssueCountServiceImpl implements IssueCountService {
    private static final String COUNT = "count";

    @Resource
    IssueMapper issueMapper;

    @Resource
    InfoErrorService infoErrorService;

    @Override
    public List<Statistics> countSort(IssueCountRequest request) {
        List<Statistics> list = new ArrayList<>();
        Integer[] siteIds = StringUtil.stringToIntegerArray(request.getSiteIds());
        //待解决预警
        int waringCount = 0;
        for (int i = 0; i < siteIds.length; i++) {
            QueryFilter filter = QueryFilterHelper.toFilter(request);
            filter.addCond(IssueTableField.SITE_ID, siteIds[i]);
            filter.addCond(IssueTableField.IS_DEL, Status.Delete.UN_DELETE.value);
            filter.addCond(IssueTableField.IS_RESOLVED, Status.Resolve.UN_RESOLVED.value);
            filter.addCond(IssueTableField.TYPE_ID, Constants.WARNING_BEGIN_ID).setRangeBegin(true);
            filter.addCond(IssueTableField.TYPE_ID, Constants.WARNING_END_ID).setRangeEnd(true);
            int count = issueMapper.count(filter);
            waringCount = waringCount + count;
        }
        Statistics statistics = getByTypeAndCount(IssueIndicator.WARNING, waringCount);
        list.add(statistics);

        //待解决问题
        int issueCount = 0;
        for (int i = 0; i < siteIds.length; i++) {
            QueryFilter filter = QueryFilterHelper.toFilter(request);
            filter.addCond(IssueTableField.SITE_ID, siteIds[i]);
            filter.addCond(IssueTableField.IS_DEL, Status.Delete.UN_DELETE.value);
            filter.addCond(IssueTableField.IS_RESOLVED, Status.Resolve.UN_RESOLVED.value);
            filter.addCond(IssueTableField.TYPE_ID, Constants.ISSUE_BEGIN_ID).setRangeBegin(true);
            filter.addCond(IssueTableField.TYPE_ID, Constants.ISSUE_END_ID).setRangeEnd(true);
            int count = issueMapper.count(filter);
            issueCount = issueCount + count;
        }
        statistics = getByTypeAndCount(IssueIndicator.UN_SOLVED_ISSUE, issueCount);
        list.add(statistics);

        //待解决问题和预警
        statistics = getByTypeAndCount(IssueIndicator.UN_SOLVED_ALL, issueCount + waringCount);
        list.add(statistics);

        //已解决问题和预警
        int resolvedCount = 0;
        for (int i = 0; i < siteIds.length; i++) {
            QueryFilter filter = QueryFilterHelper.toFilter(request);
            filter.addCond(IssueTableField.SITE_ID, siteIds[i]);
            filter.addCond(IssueTableField.IS_DEL, Status.Delete.UN_DELETE.value);
            filter.addCond(IssueTableField.IS_RESOLVED, Status.Resolve.RESOLVED.value);
            int count = issueMapper.count(filter);
            resolvedCount = resolvedCount + count;
        }
        statistics = getByTypeAndCount(IssueIndicator.SOLVED_ALL, resolvedCount);
        list.add(statistics);
        return list;
    }

    @Override
    public History historyCountSort(IssueCountRequest request) {
        Integer[] siteIds = StringUtil.stringToIntegerArray(request.getSiteIds());
        request.setBeginDateTime(InitTime.initBeginDateTime(request.getBeginDateTime(), issueMapper.getEarliestIssueTime()));
        request.setEndDateTime(InitTime.initEndDateTime(request.getEndDateTime()));
        List<HistoryDate> dateList = DateUtil.splitDateByMonth(request.getBeginDateTime(), request.getEndDateTime());

        List<IssueHistoryCountResponse> historyResponseList = new ArrayList<>();

        //待解决问题
        IssueHistoryCountResponse historyResponse = buildHistoryResponse(IssueIndicator.UN_SOLVED_ISSUE, dateList, siteIds);
        historyResponseList.add(historyResponse);

        //待解决预警
        historyResponse = buildHistoryResponse(IssueIndicator.WARNING, dateList, siteIds);
        historyResponseList.add(historyResponse);

        //待解决问题和预警
        historyResponse = buildHistoryResponse(IssueIndicator.UN_SOLVED_ALL, dateList, siteIds);
        historyResponseList.add(historyResponse);

        return new History(new Date(), historyResponseList);
    }

    @Override
    public List<DeptCountResponse> deptCountSort(IssueCountRequest request) {
        Integer[] siteIds = StringUtil.stringToIntegerArray(request.getSiteIds());
        request.setBeginDateTime(InitTime.initBeginDateTime(request.getBeginDateTime(), issueMapper.getEarliestIssueTime()));
        request.setEndDateTime(InitTime.initEndDateTime(request.getEndDateTime()));
        List<DeptCountResponse> deptCountResponses = new ArrayList<>();
        //待解决问题
        DeptCountResponse responseIssue = getResponse(siteIds, IssueIndicator.UN_SOLVED_ISSUE, request);
        deptCountResponses.add(responseIssue);

        //待解决预警
        DeptCountResponse responseWarning = getResponse(siteIds, IssueIndicator.WARNING, request);
        deptCountResponses.add(responseWarning);

        //已解决问题和预警
        DeptCountResponse responseAll = getResponse(siteIds, IssueIndicator.SOLVED_ALL, request);
        deptCountResponses.add(responseAll);

        return deptCountResponses;
    }

    @Override
    public List<DeptCount> getDeptCountByType(IssueCountByTypeRequest request) {
        Integer[] siteIds = StringUtil.stringToIntegerArray(request.getSiteIds());
        request.setBeginDateTime(InitTime.initBeginDateTime(request.getBeginDateTime(), issueMapper.getEarliestIssueTime()));
        request.setEndDateTime(InitTime.initEndDateTime(request.getEndDateTime()));
        return getDepCountByType(siteIds, request);
    }
	
	@Override
    public DeptInductionResponse[] deptInductionSort(IssueCountRequest request) {
        Map<Integer, DeptInductionResponse> result = new HashMap<>();

        Integer[] siteIds = StringUtil.stringToIntegerArray(request.getSiteIds());
        request.setBeginDateTime(InitTime.initBeginDateTime(request.getBeginDateTime(), issueMapper.getEarliestIssueTime()));
        request.setEndDateTime(InitTime.initEndDateTime(request.getEndDateTime()));

        getInductionResponse(siteIds, IssueIndicator.UN_SOLVED_ISSUE, request, result);
        getInductionResponse(siteIds, IssueIndicator.WARNING, request, result);
        getInductionResponse(siteIds, IssueIndicator.SOLVED_ALL, request, result);

        DeptInductionResponse[] inductionArray = new DeptInductionResponse[result.values().size()];
        inductionArray = result.values().toArray(inductionArray);
        for (int i = 0; i < inductionArray.length; i++){
            buildInductionResponse(inductionArray[i]);
        }
        return inductionArray;
    }

    private void buildInductionResponse(DeptInductionResponse response) {
        int countIssue = 0;
        int countWarning = 0;
        int countAll = 0;
        if(response.getData().size() < 3){
            for (Statistics stat : response.getData()) {
                if(stat.getType() == IssueIndicator.UN_SOLVED_ISSUE.value){
                    countIssue++;
                }else if(stat.getType() == IssueIndicator.WARNING.value){
                    countWarning++;
                }else if(stat.getType() == IssueIndicator.SOLVED_ALL.value){
                    countAll++;
                }
            }
            Statistics stat2 = null;
            if(countIssue == 0){
                stat2 = new Statistics();
                stat2.setType(IssueIndicator.UN_SOLVED_ISSUE.value);
                stat2.setName(IssueIndicator.UN_SOLVED_ISSUE.getName());
                stat2.setCount(0);
                response.addStatistics(stat2);
            }
            if (countWarning == 0){
                stat2 = new Statistics();
                stat2.setType(IssueIndicator.WARNING.value);
                stat2.setName(IssueIndicator.WARNING.getName());
                stat2.setCount(0);
                response.addStatistics(stat2);
            }
            if (countAll == 0){
                stat2 = new Statistics();
                stat2.setType(IssueIndicator.SOLVED_ALL.value);
                stat2.setName(IssueIndicator.SOLVED_ALL.getName());
                stat2.setCount(0);
                response.addStatistics(stat2);
            }

        }
    }

    private void getInductionResponse(Integer[] siteIds, IssueIndicator type, IssueCountRequest request, Map<Integer, DeptInductionResponse> result) {
        QueryFilter filter = new QueryFilter(Table.ISSUE);
        if(request.getBeginDateTime() != null){
            filter.addCond(IssueTableField.ISSUE_TIME, request.getBeginDateTime()).setRangeBegin(true);
        }
        filter.addCond(IssueTableField.ISSUE_TIME, request.getEndDateTime()).setRangeEnd(true);
        filter.addCond(IssueTableField.IS_DEL, Status.Delete.UN_DELETE.value);
        filter.addGroupField(IssueTableField.DEPT_ID);
        buildFilter(type, filter);
        filter.addCond(IssueTableField.SITE_ID, Arrays.asList(siteIds));
        List<Map<String, Object>> mapList = issueMapper.getDeptIdMap(filter);
        for (Map<String, Object> map : mapList) {

            if (map.get(IssueTableField.DEPT_ID) != null) {
                Integer deptId = (Integer) map.get(IssueTableField.DEPT_ID);
                DeptInductionResponse induction = result.get(deptId);
                if (induction == null) {
                    induction = new DeptInductionResponse();
                    // TODO: 2017/6/19 get dept by deptId from editor center
                    induction.setDept(deptId.toString());
                    result.put(deptId, induction);
                }

                Statistics statistics = new Statistics();
                statistics.setType(type.value);
                statistics.setName(type.getName());
                statistics.setCount(((Long) map.get(COUNT)).intValue());
                induction.addStatistics(statistics);
            }
        }

    }

    private DeptCountResponse getResponse(Integer[] siteIds, IssueIndicator type, IssueCountRequest request) {
        DeptCountResponse countResponse = new DeptCountResponse();
        countResponse.setType(type.value);
        countResponse.setName(type.getName());
        QueryFilter filter = new QueryFilter(Table.ISSUE);
        if(request.getBeginDateTime() != null){
            filter.addCond(IssueTableField.ISSUE_TIME, request.getBeginDateTime()).setRangeBegin(true);
        }
        filter.addCond(IssueTableField.ISSUE_TIME, request.getEndDateTime()).setRangeEnd(true);
        filter.addCond(IssueTableField.IS_DEL, Status.Delete.UN_DELETE.value);
        filter.addGroupField(IssueTableField.DEPT_ID);
        buildFilter(type, filter);
        filter.addCond(IssueTableField.SITE_ID, Arrays.asList(siteIds));
        List<DeptCount> deptCountList = new ArrayList<>();
        List<Map<String, Object>> mapList = issueMapper.getDeptIdMap(filter);
        for (Map<String, Object> map : mapList) {
            DeptCount deptCount;
            if (map.get(IssueTableField.DEPT_ID) == null || "".equals(map.get(IssueTableField.DEPT_ID))) {
                deptCount = new DeptCount(Constants.DEPT_NULL, ((Long) map.get(COUNT)).intValue());
            }else {
                // TODO: 2017/6/19 get dept by deptId from editor center
                deptCount = new DeptCount(map.get(IssueTableField.DEPT_ID).toString(), ((Long) map.get(COUNT)).intValue());
            }
            deptCountList.add(deptCount);
        }
        countResponse.setCount(deptCountList);
        return countResponse;
    }

    private void buildFilter(IssueIndicator type, QueryFilter filter) {
        if (type == IssueIndicator.UN_SOLVED_ISSUE || type == IssueIndicator.WARNING) {
            filter.addCond(IssueTableField.IS_RESOLVED, Status.Resolve.UN_RESOLVED.value);
            if (type == IssueIndicator.UN_SOLVED_ISSUE) {
                filter.addCond(IssueTableField.TYPE_ID, Constants.ISSUE_BEGIN_ID).setRangeBegin(true);
                filter.addCond(IssueTableField.TYPE_ID, Constants.ISSUE_END_ID).setRangeEnd(true);
            } else {
                filter.addCond(IssueTableField.TYPE_ID, Constants.WARNING_BEGIN_ID).setRangeBegin(true);
                filter.addCond(IssueTableField.TYPE_ID, Constants.WARNING_END_ID).setRangeEnd(true);
            }
        } else if (type == IssueIndicator.SOLVED_ALL) {
            filter.addCond(IssueTableField.IS_RESOLVED, Status.Resolve.RESOLVED.value);
        }
    }

    private List<DeptCount> getDepCountByType(Integer[] siteIds, IssueCountByTypeRequest request) {
        QueryFilter filter = new QueryFilter(Table.ISSUE);

        if (siteIds != null) {
            filter.addCond(IssueTableField.SITE_ID, Arrays.asList(siteIds));
        }

        if(request.getBeginDateTime() != null){
            filter.addCond(IssueTableField.ISSUE_TIME, request.getBeginDateTime()).setRangeBegin(true);
        }
        filter.addCond(IssueTableField.ISSUE_TIME, request.getEndDateTime()).setRangeEnd(true);
        filter.addCond(IssueTableField.IS_DEL, Status.Delete.UN_DELETE.value);
        filter.addGroupField(IssueTableField.DEPT_ID);

        switch (request.getTypeId()) {
            case IssueCountByTypeRequest.TYPE_SITE_AVALIABLE:
                filter.addCond(IssueTableField.TYPE_ID, Types.IssueType.LINK_AVAILABLE_ISSUE.value);
                break;
            case IssueCountByTypeRequest.TYPE_INFO_UPDATE:
                filter.addCond(IssueTableField.TYPE_ID, Types.IssueType.INFO_UPDATE_ISSUE.value);
                break;
            case IssueCountByTypeRequest.TYPE_INFO_ERROR:
                filter.addCond(IssueTableField.TYPE_ID, Types.IssueType.INFO_ERROR_ISSUE.value);
                break;
            // TODO 在线服务和互动问题等待数据联调
            default:
                filter.addCond(IssueTableField.TYPE_ID, -1);

        }
        filter.addCond(IssueTableField.IS_RESOLVED, Status.Resolve.UN_RESOLVED.value);
        List<Map<String, Object>> depIssueCountList = issueMapper.getDepIssueCount(filter);
        List<DeptCount> result = new ArrayList<>();
        for (Map<String, Object> countMap : depIssueCountList) {
            Integer depId = (Integer)countMap.get(IssueTableField.DEPT_ID);
            Long count = (Long)countMap.get(COUNT);
            result.add(new DeptCount(String.valueOf(depId), count.intValue()));
        }
        return result;
    }
    private IssueHistoryCountResponse buildHistoryResponse(IssueIndicator type, List<HistoryDate> dateList, Integer[] siteIds) {
        IssueHistoryCountResponse historyResponse = new IssueHistoryCountResponse();
        historyResponse.setType(type.value);
        historyResponse.setName(type.getName());
        List<HistoryStatistics> list = new ArrayList<>();
        for (HistoryDate date : dateList) {
            HistoryStatistics historyStatistics = new HistoryStatistics();
            int count = 0;
            for (int i = 0; i < siteIds.length; i++) {
                QueryFilter filter = new QueryFilter(Table.ISSUE);
                filter.addCond(IssueTableField.SITE_ID, siteIds[i]);
                filter.addCond(IssueTableField.ISSUE_TIME, date.getBeginDate()).setRangeBegin(true);
                filter.addCond(IssueTableField.ISSUE_TIME, date.getEndDate()).setRangeEnd(true);
                filter.addCond(IssueTableField.IS_DEL, Status.Delete.UN_DELETE.value);
                filter.addCond(IssueTableField.IS_RESOLVED, Status.Resolve.UN_RESOLVED.value);
                if (type == IssueIndicator.UN_SOLVED_ISSUE) {
                    filter.addCond(IssueTableField.TYPE_ID, Constants.ISSUE_BEGIN_ID).setRangeBegin(true);
                    filter.addCond(IssueTableField.TYPE_ID, Constants.ISSUE_END_ID).setRangeEnd(true);
                } else if (type == IssueIndicator.WARNING) {
                    filter.addCond(IssueTableField.TYPE_ID, Constants.WARNING_BEGIN_ID).setRangeBegin(true);
                    filter.addCond(IssueTableField.TYPE_ID, Constants.WARNING_END_ID).setRangeEnd(true);
                }
                count = count + issueMapper.count(filter);
            }
            historyStatistics.setValue(count);
            historyStatistics.setTime(date.getMonth());
            list.add(historyStatistics);
        }
        historyResponse.setData(list);
        return historyResponse;
    }

    private Statistics getByTypeAndCount(IssueIndicator type, int count) {
        Statistics statistics = new Statistics();
        statistics.setType(type.value);
        statistics.setName(type.getName());
        statistics.setCount(count);
        return statistics;
    }
}

package com.trs.gov.kpi.service.helper;

import com.trs.gov.kpi.constant.*;
import com.trs.gov.kpi.dao.FrequencyPresetMapper;
import com.trs.gov.kpi.entity.FrequencyPreset;
import com.trs.gov.kpi.entity.dao.CondDBField;
import com.trs.gov.kpi.entity.dao.OrCondDBFields;
import com.trs.gov.kpi.entity.dao.QueryFilter;
import com.trs.gov.kpi.entity.dao.Table;
import com.trs.gov.kpi.entity.exception.BizException;
import com.trs.gov.kpi.entity.exception.RemoteException;
import com.trs.gov.kpi.entity.requestdata.*;
import com.trs.gov.kpi.service.outer.DeptApiService;
import com.trs.gov.kpi.service.outer.SiteApiService;
import com.trs.gov.kpi.utils.DateUtil;
import com.trs.gov.kpi.utils.SpringContextUtil;
import com.trs.gov.kpi.utils.StringUtil;
import lombok.extern.log4j.Log4j;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

/**
 * Created by linwei on 2017/5/22.
 */
@Log4j
public class QueryFilterHelper {

    public static final String FREQ_SETUP_TABLE_FIELD_CHNL_ID = "chnlId";
    public static final String FREQ_SETUP_TABLE_FIELD_PRESET_FEQ_ID = "presetFeqId";

    private QueryFilterHelper() {

    }

    /**
     * 把参数转换为查询filter
     *
     * @param param
     * @return
     */
    public static QueryFilter toFilter(PageDataRequestParam param, Types.IssueType... issueType) throws RemoteException {
        QueryFilter filter = new QueryFilter(Table.ISSUE);
        filter.addCond(IssueTableField.SITE_ID, param.getSiteId());

        initTime(param, IssueTableField.ISSUE_TIME, filter);

        if (param.getSearchText() != null) {
            if (param.getSearchField() != null && param.getSearchField().equalsIgnoreCase("id")) {
                filter.addCond(IssueTableField.ID, '%' + param.getSearchText() + "%").setLike(true);
            } else if (param.getSearchField() != null && param.getSearchField().equalsIgnoreCase("issueType")) {
                CondDBField field = buildIssueTypeCond(param.getSearchText(), issueType);
                initSubTypeId(field, filter);
            } else if (param.getSearchField() != null && param.getSearchField().equalsIgnoreCase("deptName")) {
                DeptApiService deptApiService = (DeptApiService) SpringContextUtil.getBean(DeptApiService.class);
                List<Integer> deptIds = deptApiService.queryDeptsByName("", param.getSearchText());
                if (deptIds != null && !deptIds.isEmpty()) {
                    filter.addCond(IssueTableField.DEPT_ID, deptIds);
                } else {
                    filter.addCond(IssueTableField.DEPT_ID, -1);
                }
            } else if (param.getSearchField() == null) {
                CondDBField idField = new CondDBField(IssueTableField.ID, '%' + param.getSearchText() + "%");
                idField.setLike(true);

                CondDBField deptIdsField = buildDeptIdsField(param);

                CondDBField issueTypefield = buildIssueTypeCond(param.getSearchText(), issueType);
                initAll(filter, idField, issueTypefield, deptIdsField);

            }
        }

        // sort field
        if (!StringUtil.isEmpty(param.getSortFields())) {
            String[] sortFields = param.getSortFields().trim().split(";");
            addSort(filter, sortFields);
        }

        return filter;
    }

    private static CondDBField buildDeptIdsField(PageDataRequestParam param) throws RemoteException {
        CondDBField deptIdsField = null;
        DeptApiService deptApiService = (DeptApiService) SpringContextUtil.getBean(DeptApiService.class);
        List<Integer> deptIds = deptApiService.queryDeptsByName("", param.getSearchText());
        if (deptIds != null && !deptIds.isEmpty()) {
            deptIdsField = new CondDBField(IssueTableField.DEPT_ID, deptIds);
        }
        return deptIdsField;
    }

    private static void initSubTypeId(CondDBField field, QueryFilter filter) {
        if (field != null) {
            filter.addCond(field);
        } else {
            filter.addCond(new CondDBField(IssueTableField.SUBTYPE_ID, Types.IssueType.INVALID.value));
        }
    }

    private static void initAll(QueryFilter filter, CondDBField idField, CondDBField issueTypefield, CondDBField deptIdsField) {
        OrCondDBFields orFields = new OrCondDBFields();
        orFields.addCond(idField);
        orFields.addCond(deptIdsField);
        if (issueTypefield != null) {
            orFields.addCond(issueTypefield);
        }
        filter.addOrConds(orFields);
    }

    private static void initTime(DateRequest param, String timeField, QueryFilter filter) {
        if (param.getBeginDateTime() != null) {
            filter.addCond(timeField, param.getBeginDateTime()).setRangeBegin(true);
        }

        if (param.getEndDateTime() != null) {
            filter.addCond(timeField, param.getEndDateTime()).setRangeEnd(true);
        }
    }


    private static void addSort(QueryFilter filter, String[] sortFields) {
        for (String sortField : sortFields) {
            String[] nameAndDirection = sortField.split(",");
            if (nameAndDirection.length == 2 && !nameAndDirection[0].trim().isEmpty()) {
                if (nameAndDirection[1].trim().equalsIgnoreCase("asc")) {
                    filter.addSortField(nameAndDirection[0], true);
                } else if (nameAndDirection[1].trim().equalsIgnoreCase("desc")) {
                    filter.addSortField(nameAndDirection[0], false);
                }
            }
        }
    }


    /**
     * 把参数转换为工单所需的查询filter
     *
     * @param request
     * @return
     */
    public static QueryFilter toFilter(WorkOrderRequest request, DeptApiService deptApiService) throws RemoteException {
        QueryFilter filter = new QueryFilter(Table.ISSUE);

        if (request.getSiteId() != null) {
            filter.addCond(IssueTableField.SITE_ID, Arrays.asList(request.getSiteId()));
        }
        initTime(request, IssueTableField.ISSUE_TIME, filter);

        if (request.getSearchText() != null) {
            addWorkOrderSearchCond(filter, request, deptApiService);
        }

        // sort field
        if (!StringUtil.isEmpty(request.getSortFields())) {
            String[] sortFields = request.getSortFields().trim().split(";");
            addSort(filter, sortFields);
        }

        return filter;
    }

    private static void addWorkOrderSearchCond(QueryFilter filter, WorkOrderRequest request, DeptApiService deptApiService) throws RemoteException {
        if (request.getSearchField() != null && request.getSearchField().equalsIgnoreCase("id")) {
            filter.addCond(IssueTableField.ID, '%' + request.getSearchText() + "%").setLike(true);
        } else if (request.getSearchField() != null && request.getSearchField().equalsIgnoreCase("department")) {
            List<Integer> deptIds = deptApiService.queryDeptsByName("", request.getSearchText());
            if (deptIds != null && !deptIds.isEmpty()) {
                filter.addCond(IssueTableField.DEPT_ID, deptIds);
            } else {
                filter.addCond(IssueTableField.DEPT_ID, -1);
            }
        } else if (request.getSearchField() == null) {
            OrCondDBFields orFields = new OrCondDBFields();
            orFields.addCond(IssueTableField.ID, '%' + request.getSearchText() + "%").setLike(true);

            List<Integer> deptIds = deptApiService.queryDeptsByName("", request.getSearchText());
            if (deptIds != null && !deptIds.isEmpty()) {
                orFields.addCond(IssueTableField.DEPT_ID, deptIds);
            }

            filter.addOrConds(orFields);
        }
    }

    private static void addWebPageSearchCond(QueryFilter filter, PageDataRequestParam param, SiteApiService siteApiService) throws RemoteException {
        if (param.getSearchField() != null && param.getSearchField().equalsIgnoreCase("id")) {
            filter.addCond(WebpageTableField.ID, '%' + param.getSearchText() + "%").setLike(true);
        } else if (param.getSearchField() != null && param.getSearchField().equalsIgnoreCase(Constants.CHNL_NAME)) {

            List<Integer> chnlIds = siteApiService.findChnlIds("", param.getSiteId(), param.getSearchText());
            if (!chnlIds.isEmpty()) {
                filter.addCond(WebpageTableField.CHNL_ID, chnlIds);
            } else {
                // 找不到符合条件的记录，构造一个不成立的条件
                filter.addCond(WebpageTableField.CHNL_ID, -1);
            }
        } else if (param.getSearchField() == null) {
            OrCondDBFields orFields = new OrCondDBFields();
            orFields.addCond(WebpageTableField.ID, '%' + param.getSearchText() + "%").setLike(true);

            List<Integer> chnlIds = siteApiService.findChnlIds("", param.getSiteId(), param.getSearchText());
            if (chnlIds != null && !chnlIds.isEmpty()) {
                orFields.addCond(WebpageTableField.CHNL_ID, chnlIds);
            }

            filter.addOrConds(orFields);
        }
    }

    private static CondDBField buildIssueTypeCond(String issueName, Types.IssueType... issueType) {
        List<Integer> matchedTypes = new ArrayList<>();

        for (int i = 0; i < issueType.length; i++) {

            switch (issueType[i]) {
                case LINK_AVAILABLE_ISSUE:
                    matchedTypes.addAll(Types.LinkAvailableIssueType.findByName(issueName));
                    break;
                case INFO_ERROR_ISSUE:
                    matchedTypes.addAll(Types.InfoErrorIssueType.findByName(issueName));
                    break;
                case INFO_UPDATE_ISSUE:
                    matchedTypes.addAll(Types.InfoUpdateIssueType.findByName(issueName));
                    break;
                case INFO_UPDATE_WARNING:
                    matchedTypes.addAll(Types.InfoUpdateWarningType.findByName(issueName));
                    break;
                case RESPOND_WARNING:
                    matchedTypes.addAll(Types.RespondWarningType.findByName(issueName));
                    break;
                case SERVICE_LINK_AVAILABLE:
                    matchedTypes.addAll(Types.ServiceLinkIssueType.findByName(issueName));
                    break;
                default:
            }
        }

        CondDBField field = null;
        if (!matchedTypes.isEmpty()) {
            if (matchedTypes.size() == 1) {
                field = new CondDBField(IssueTableField.SUBTYPE_ID, matchedTypes.get(0));
            } else {
                field = new CondDBField(IssueTableField.SUBTYPE_ID, matchedTypes);
                field.setCollection(true);
            }
        }
        return field;
    }

    /**
     * 把参数转换为网页查询的filter
     *
     * @param param
     * @return
     */
    public static QueryFilter toPageFilter(PageDataRequestParam param, SiteApiService siteApiService) throws RemoteException {
        QueryFilter filter = new QueryFilter(Table.WEB_PAGE);

        filter.addCond(IssueTableField.SITE_ID, param.getSiteId());
        initTime(param, WebpageTableField.CHECK_TIME, filter);

        if (param.getSearchText() != null) {
            addWebPageSearchCond(filter, param, siteApiService);
        }

        // sort field
        if (!StringUtil.isEmpty(param.getSortFields())) {
            String[] sortFields = param.getSortFields().trim().split(";");
            addSort(filter, sortFields);
        }

        return filter;
    }

    /**
     * 统计分析中的问题统计的filter处理
     *
     * @param request
     * @return
     */
    public static QueryFilter toFilter(IssueCountRequest request) {
        QueryFilter filter = new QueryFilter(Table.ISSUE);
        if (request.getBeginDateTime() != null) {
            filter.addCond(IssueTableField.ISSUE_TIME, request.getBeginDateTime()).setRangeBegin(true);
        }

        if (request.getEndDateTime() != null) {
            filter.addCond(IssueTableField.ISSUE_TIME, request.getEndDateTime()).setRangeEnd(true);
        }
        return filter;
    }


    /**
     * 栏目更新频率设置的filter处理
     *
     * @param request
     * @return
     */
    public static QueryFilter toFilter(FrequencySetupSelectRequest request, SiteApiService siteService, FrequencyPresetMapper frequencyPresetMapper) throws RemoteException {
        QueryFilter filter = new QueryFilter(Table.FREQ_SETUP);
        filter.addCond("siteId", request.getSiteId());

        if (request.getSearchText() == null) {
            return filter;
        }

        if (StringUtil.isEmpty(request.getSearchField())) {
            OrCondDBFields orCond = new OrCondDBFields();
            orCond.addCond(FREQ_SETUP_TABLE_FIELD_CHNL_ID, "%" + request.getSearchText() + "%").setLike(true);

            List<Integer> chnlIds = siteService.findChnlIds("", request.getSiteId(), request.getSearchText());
            if (!chnlIds.isEmpty()) {
                orCond.addCond(FREQ_SETUP_TABLE_FIELD_CHNL_ID, chnlIds);
            }

            List<Integer> ids = getFrequencyPresetIds(frequencyPresetMapper, request.getSiteId(), request.getSearchText());
            if (!ids.isEmpty()) {
                orCond.addCond(FREQ_SETUP_TABLE_FIELD_PRESET_FEQ_ID, ids);
            }

            filter.addOrConds(orCond);
        } else if (request.getSearchField().equalsIgnoreCase("chnlId")) {
            filter.addCond(FREQ_SETUP_TABLE_FIELD_CHNL_ID, "%" + request.getSearchText() + "%").setLike(true);
        } else if (request.getSearchField().equalsIgnoreCase(Constants.CHNL_NAME)) {
            List<Integer> chnlIds = siteService.findChnlIds("", request.getSiteId(), request.getSearchText());
            if (!chnlIds.isEmpty()) {
                filter.addCond(FREQ_SETUP_TABLE_FIELD_CHNL_ID, chnlIds);
            } else {
                // 找不到符合条件的记录，构造一个不成立的条件
                filter.addCond(FREQ_SETUP_TABLE_FIELD_CHNL_ID, -1);
            }
        } else if (request.getSearchField().equalsIgnoreCase("updateFreq")) {
            List<Integer> ids = getFrequencyPresetIds(frequencyPresetMapper, request.getSiteId(), request.getSearchText());
            if (!ids.isEmpty()) {
                filter.addCond(FREQ_SETUP_TABLE_FIELD_PRESET_FEQ_ID, ids);
            } else {
                // 找不到符合条件的记录，构造一个不成立的条件
                filter.addCond(FREQ_SETUP_TABLE_FIELD_PRESET_FEQ_ID, -1);
            }
        }

        return filter;
    }

    private static List<Integer> getFrequencyPresetIds(FrequencyPresetMapper frequencyPresetMapper, Integer siteId, String presetValue) {
        List<FrequencyPreset> presetList = frequencyPresetMapper.selectBySiteIdAndUpdateFreq(siteId, presetValue);
        ArrayList<Integer> ids = new ArrayList<>();
        if (presetList != null && !presetList.isEmpty()) {
            for (FrequencyPreset preset : presetList) {
                ids.add(preset.getId());
            }
        }
        return ids;
    }

    public static QueryFilter toReportFilter(ReportRequestParam param, boolean isTimeNode) throws BizException {
        QueryFilter filter = new QueryFilter(Table.REPORT);
        if (param.getSiteId() != null) {
            filter.addCond(ReportTableField.SITE_ID, param.getSiteId());
        }
        if (isTimeNode) {
            if (!StringUtil.isEmpty(param.getDay())) {
                filter.addCond(ReportTableField.REPORT_TIME, param.getDay()).setRangeBegin(true);
                String endTime = null;
                try {
                    endTime = initEndTime(param.getDay());
                } catch (ParseException e) {
                    log.error(Constants.INVALID_PARAMETER + param.getDay(), e);
                    throw new BizException(Constants.INVALID_PARAMETER, e);
                }
                filter.addCond(ReportTableField.REPORT_TIME, endTime).setRangeEnd(true);
            }
        } else {
            if (!StringUtil.isEmpty(param.getBeginDateTime())) {
                filter.addCond(ReportTableField.REPORT_TIME, param.getBeginDateTime()).setRangeBegin(true);
            }
            if (!StringUtil.isEmpty(param.getEndDateTime())) {
                String endTime = null;
                try {
                    endTime = initEndTime(param.getEndDateTime());
                } catch (ParseException e) {
                    log.error(Constants.INVALID_PARAMETER + param.getEndDateTime(), e);
                    throw new BizException(Constants.INVALID_PARAMETER, e);
                }
                filter.addCond(ReportTableField.REPORT_TIME, endTime).setRangeEnd(true);
            }
        }

        return filter;
    }

    private static String initEndTime(String day) throws ParseException {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        calendar.setTime(format.parse(day));
        calendar.add(Calendar.DAY_OF_MONTH, 1);//加一天，解决结束日期当天数据查不到的情况
        return DateUtil.toString(calendar.getTime());
    }

    public static QueryFilter toSetDeptFilter(PageDataRequestParam param, SiteApiService siteApiService, DeptApiService deptApiService) throws RemoteException {
        QueryFilter filter = new QueryFilter(Table.DUTY_DEPT);
        filter.addCond(DutyDeptTableField.SITE_ID, param.getSiteId());

        if (param.getSearchText() == null) {
            return filter;
        }

        if (StringUtil.isEmpty(param.getSearchField())) {
            filter.addOrConds(bulidByDeptParam(param, siteApiService, deptApiService));
        } else if (param.getSearchField() != null && param.getSearchField().equalsIgnoreCase(Constants.CHNL_NAME)) {
            List<Integer> chnlIds = siteApiService.findChnlIds("", param.getSiteId(), param.getSearchText());
            if (chnlIds != null && !chnlIds.isEmpty()) {
                filter.addCond(DutyDeptTableField.CHNL_ID, chnlIds);
            } else {
                // 找不到符合条件的记录，构造一个不成立的条件
                filter.addCond(DutyDeptTableField.CHNL_ID, -1);
            }
        } else if (param.getSearchField() != null && param.getSearchField().equalsIgnoreCase("deptName")) {
            List<Integer> deptIds = deptApiService.queryDeptsByName("", param.getSearchText());
            if (deptIds != null && !deptIds.isEmpty()) {
                filter.addCond(DutyDeptTableField.DEPT_ID, deptIds);
            } else {
                // 找不到符合条件的记录，构造一个不成立的条件
                filter.addCond(DutyDeptTableField.DEPT_ID, -1);
            }
        }
        return filter;
    }

    private static OrCondDBFields bulidByDeptParam(PageDataRequestParam param, SiteApiService siteApiService, DeptApiService deptApiService) throws RemoteException {
        OrCondDBFields orCondDBFields = new OrCondDBFields();
        List<Integer> chnlIds = siteApiService.findChnlIds("", param.getSiteId(), param.getSearchText());
        if (chnlIds != null && !chnlIds.isEmpty()) {
            orCondDBFields.addCond(DutyDeptTableField.CHNL_ID, chnlIds);
        } else {
            // 找不到符合条件的记录，构造一个不成立的条件
            orCondDBFields.addCond(DutyDeptTableField.CHNL_ID, -1);
        }
        List<Integer> deptIds = deptApiService.queryDeptsByName("", param.getSearchText());
        if (deptIds != null && !deptIds.isEmpty()) {
            orCondDBFields.addCond(DutyDeptTableField.DEPT_ID, deptIds);
        } else {
            // 找不到符合条件的记录，构造一个不成立的条件
            orCondDBFields.addCond(DutyDeptTableField.DEPT_ID, -1);
        }
        return orCondDBFields;
    }

    /**
     * 日志监测filter
     * @param param
     * @return
     * @throws RemoteException
     */
    public static QueryFilter toMonitorRecordFilter(PageDataRequestParam param) throws BizException {
        QueryFilter filter = new QueryFilter(Table.MONITOR_RECORD);
        filter.addCond(MonitorRecordTableField.SITE_ID, param.getSiteId());

        if (param.getSearchText() != null) {
            if (param.getSearchField() != null && param.getSearchField().equalsIgnoreCase("taskName")) {
                taskNameCond(param, filter);

            }else if(param.getSearchField() != null && param.getSearchField().equalsIgnoreCase("taskStatusName")){
                taskStatusNameCond(param, filter);
            } else if (StringUtil.isEmpty(param.getSearchField())) {
                filter.addOrConds(bulidByMonitorRecordParam(param.getSearchText()));
            } else{
                log.error("Invalid parameter: 检索类型错误，目前支持（任务名称（taskName），任务状态名称（taskStatusName）");
                throw new BizException(Constants.INVALID_PARAMETER);
            }
        }

        // sort field
        if (!StringUtil.isEmpty(param.getSortFields())) {
            String[] sortFields = param.getSortFields().trim().split(";");
            addSort(filter, sortFields);
        }
        return filter;
    }

    private static void taskStatusNameCond(PageDataRequestParam param, QueryFilter filter) {
        List<Integer> statusList = Status.MonitorStatusType.getStatusByStatusName(param.getSearchText());
        if(statusList.isEmpty()){// 找不到符合条件的记录，构造一个不成立的条件
            filter.addCond(MonitorRecordTableField.TASK_STATUS, Status.MonitorStatusType.INVALID.value);
        }else{
            filter.addCond(MonitorRecordTableField.TASK_STATUS, statusList);
        }
    }

    private static void taskNameCond(PageDataRequestParam param, QueryFilter filter) {
        List<Integer> taskIds = Types.MonitorRecordNameType.getTaskIdsByTaskName(param.getSearchText());
        if(taskIds.isEmpty()){// 找不到符合条件的记录，构造一个不成立的条件
            filter.addCond(MonitorRecordTableField.TASK_ID, Types.MonitorRecordNameType.INVALID.value);
        }else{
            filter.addCond(MonitorRecordTableField.TASK_ID, taskIds);
        }
    }

    /**
     * 日志监测——全部查询条件拼接
     * @param searchText
     * @return
     */
    private static OrCondDBFields bulidByMonitorRecordParam(String searchText){
        OrCondDBFields orCondDBFields = new OrCondDBFields();
        List<Integer> taskIds = Types.MonitorRecordNameType.getTaskIdsByTaskName(searchText);
        if(taskIds.isEmpty()){// 找不到符合条件的记录，构造一个不成立的条件
            orCondDBFields.addCond(MonitorRecordTableField.TASK_ID, Types.MonitorRecordNameType.INVALID.value);
        }else{
            orCondDBFields.addCond(MonitorRecordTableField.TASK_ID, taskIds);
        }
        List<Integer> statusList = Status.MonitorStatusType.getStatusByStatusName(searchText);
        if(statusList.isEmpty()){// 找不到符合条件的记录，构造一个不成立的条件
            orCondDBFields.addCond(MonitorRecordTableField.TASK_STATUS, Status.MonitorStatusType.INVALID.value);
        }else{
            orCondDBFields.addCond(MonitorRecordTableField.TASK_STATUS, statusList);
        }
        return orCondDBFields;
    }

}

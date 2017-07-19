package com.trs.gov.kpi.service.helper;

import com.trs.gov.kpi.constant.*;
import com.trs.gov.kpi.dao.FrequencyPresetMapper;
import com.trs.gov.kpi.entity.FrequencyPreset;
import com.trs.gov.kpi.entity.dao.CondDBField;
import com.trs.gov.kpi.entity.dao.OrCondDBFields;
import com.trs.gov.kpi.entity.dao.QueryFilter;
import com.trs.gov.kpi.entity.dao.Table;
import com.trs.gov.kpi.entity.exception.RemoteException;
import com.trs.gov.kpi.entity.requestdata.*;
import com.trs.gov.kpi.service.outer.DeptApiService;
import com.trs.gov.kpi.service.outer.SiteApiService;
import com.trs.gov.kpi.service.wangkang.WkSiteManagementService;
import com.trs.gov.kpi.utils.DateUtil;
import com.trs.gov.kpi.utils.StringUtil;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

/**
 * Created by linwei on 2017/5/22.
 */
public class QueryFilterHelper {

    @Resource
    private WkSiteManagementService wkSiteManagementService;

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
    public static QueryFilter toFilter(PageDataRequestParam param, Types.IssueType... issueType) {
        QueryFilter filter = new QueryFilter(Table.ISSUE);
        filter.addCond(IssueTableField.SITE_ID, param.getSiteId());

        initTime(param, IssueTableField.ISSUE_TIME, filter);

        if (param.getSearchText() != null) {
            if (param.getSearchField() != null && param.getSearchField().equalsIgnoreCase("id")) {
                filter.addCond(IssueTableField.ID, '%' + param.getSearchText() + "%").setLike(true);
            } else if (param.getSearchField() != null && param.getSearchField().equalsIgnoreCase("issueType")) {
                CondDBField field = buildIssueTypeCond(param.getSearchText(), issueType);
                initSubTypeId(field, filter);
            } else if (param.getSearchField() == null) {
                CondDBField idField = new CondDBField(IssueTableField.ID, '%' + param.getSearchText() + "%");
                idField.setLike(true);

                CondDBField issueTypefield = buildIssueTypeCond(param.getSearchText(), issueType);
                initAll(filter, idField, issueTypefield);
            }
        }

        // sort field
        if (!StringUtil.isEmpty(param.getSortFields())) {
            String[] sortFields = param.getSortFields().trim().split(";");
            addSort(filter, sortFields);
        }

        return filter;
    }

    private static void initSubTypeId(CondDBField field, QueryFilter filter) {
        if (field != null) {
            filter.addCond(field);
        } else {
            filter.addCond(new CondDBField(IssueTableField.SUBTYPE_ID, Types.IssueType.INVALID.value));
        }
    }

    private static void initAll(QueryFilter filter, CondDBField idField, CondDBField issueTypefield) {
        if (issueTypefield != null) {
            OrCondDBFields orFields = new OrCondDBFields();
            orFields.addCond(idField);
            orFields.addCond(issueTypefield);
            filter.addOrConds(orFields);
        } else {
            filter.addCond(idField);
        }
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
        } else if (param.getSearchField() != null && param.getSearchField().equalsIgnoreCase("chnlName")) {

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
        } else if (request.getSearchField().equalsIgnoreCase("chnlName")) {
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

    public static QueryFilter toReportFilter(ReportRequestParam param, boolean isTimeNode) throws ParseException {
        QueryFilter filter = new QueryFilter(Table.REPORT);
        if (param.getSiteId() != null) {
            filter.addCond(ReportTableField.SITE_ID, param.getSiteId());
        }
        if (isTimeNode) {
            if (!StringUtil.isEmpty(param.getDay())) {
                filter.addCond(ReportTableField.REPORT_TIME, param.getDay()).setRangeBegin(true);
                String endTime = initEndTime(param.getDay());
                filter.addCond(ReportTableField.REPORT_TIME, endTime).setRangeEnd(true);
            }
        } else {
            if (!StringUtil.isEmpty(param.getBeginDateTime())) {
                filter.addCond(ReportTableField.REPORT_TIME, param.getBeginDateTime()).setRangeBegin(true);
            }
            if (!StringUtil.isEmpty(param.getEndDateTime())) {
                String endTime = initEndTime(param.getEndDateTime());
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

        /**
         *  网康查询的filter处理
         *
         * @param wkAllSiteDetail
         * @return
         */
    public static QueryFilter toWkFilter(WkAllSiteDetailRequest wkAllSiteDetail){
        QueryFilter filter = new QueryFilter(Table.WK_SITEMANAGEMENT);

        if ("siteName".equalsIgnoreCase(wkAllSiteDetail.getSearchField())){
            filter.addCond("siteName", "%" + wkAllSiteDetail.getSearchText() + "%").setLike(true);
        }
        if ("checkStatus".equalsIgnoreCase(wkAllSiteDetail.getSearchField())){
            switch (wkAllSiteDetail.getSearchText()){
                case("0"):
                    filter.addCond("checkStatus",Types.WkCheckStatus.NOT_SUMBIT_CHECK.value);
                    break;
                case("1"):
                    filter.addCond("checkStatus",Types.WkCheckStatus.WAIT_CHECK.value);
                    break;
                case("2"):
                    filter.addCond("checkStatus",Types.WkCheckStatus.CONDUCT_CHECK.value);
                    break;
                case("3"):
                    filter.addCond("checkStatus",Types.WkCheckStatus.DONE_CHECK.value);
                    break;
                default:
            }

        }

        // sort field
        if (!StringUtil.isEmpty(wkAllSiteDetail.getSortFields())) {
            String[] sortFields = wkAllSiteDetail.getSortFields().trim().split(";");
            addSort(filter, sortFields);
        }

        return filter;
    }


    /**
     * 把参数转换为查询 wkissue 的 filter
     *
     * @param param
     * @return
     */
    public static QueryFilter toWkIssueFilter(PageDataRequestParam param, Types.WkSiteCheckType checkType ) {
        QueryFilter filter = new QueryFilter(Table.WK_ISSUE);
        filter.addCond(IssueTableField.SITE_ID, param.getSiteId());

        if (param.getSearchText() != null) {
            if (param.getSearchField() != null && param.getSearchField().equalsIgnoreCase("chnlName")) {
                filter.addCond(WkIssueTableField.CHNL_NAME, '%' + param.getSearchText() + "%").setLike(true);
            } else if (param.getSearchField() != null && param.getSearchField().equalsIgnoreCase("issueType")) {
                if (checkType == Types.WkSiteCheckType.INVALID_LINK) {
                    CondDBField field = buildWkInvalidLinkTypeCond(param.getSearchText());
                    initInvalidLinkSubTypeId(field, filter);
                } else if (checkType == Types.WkSiteCheckType.CONTENT_ERROR) {
                    CondDBField field = buildWkErrorWordsTypeCond(param.getSearchText());
                    initWkErrorWordsSubTypeId(field, filter);
                }
            } else if (param.getSearchField() != null && param.getSearchField().equalsIgnoreCase("errorInfo")) {
                filter.addCond(WkIssueTableField.URL, '%' + param.getSearchText() + "%").setLike(true);
            } else if (param.getSearchField() == null) {
                CondDBField chnlNameField = new CondDBField(WkIssueTableField.CHNL_NAME, '%' + param.getSearchText() + "%");
                chnlNameField.setLike(true);

                CondDBField errorInfoField = new CondDBField(WkIssueTableField.URL, '%' + param.getSearchText() + "%");
                errorInfoField.setLike(true);

                CondDBField subTypeField  = null;
                if (checkType == Types.WkSiteCheckType.INVALID_LINK) {
                    subTypeField = buildWkInvalidLinkTypeCond(param.getSearchText());
                } else if (checkType == Types.WkSiteCheckType.CONTENT_ERROR) {
                    subTypeField = buildWkErrorWordsTypeCond(param.getSearchText());
                }

                addOrField(filter, chnlNameField, errorInfoField, subTypeField);
            }
        }

        // sort field
        if (!StringUtil.isEmpty(param.getSortFields())) {
            String[] sortFields = param.getSortFields().trim().split(";");
            addWkIssueSort(filter, sortFields);
        }

        return filter;
    }

    private static CondDBField buildWkInvalidLinkTypeCond(String typeName) {
        final List<Integer> typeIds = Types.WkLinkIssueType.findByName(typeName);
        if (typeIds.isEmpty()) {
            return null;
        } else {
            return new CondDBField(WkIssueTableField.SUBTYPE_ID, typeIds);
        }
    }

    private static void initInvalidLinkSubTypeId(CondDBField field, QueryFilter filter) {
        if (field != null) {
            filter.addCond(field);
        } else {
            filter.addCond(new CondDBField(WkIssueTableField.SUBTYPE_ID, Types.WkLinkIssueType.INVALID.value));
        }
    }

    private static CondDBField buildWkErrorWordsTypeCond(String typeName) {
        final List<Integer> typeIds = Types.InfoErrorIssueType.findByName(typeName);
        if (typeIds.isEmpty()) {
            return null;
        } else {
            return new CondDBField(WkIssueTableField.SUBTYPE_ID, typeIds);
        }
    }

    private static void initWkErrorWordsSubTypeId(CondDBField field, QueryFilter filter) {
        if (field != null) {
            filter.addCond(field);
        } else {
            filter.addCond(new CondDBField(WkIssueTableField.SUBTYPE_ID, Types.InfoErrorIssueType.INVALID.value));
        }
    }

    private static void addOrField(QueryFilter filter, CondDBField... fields) {
        OrCondDBFields orFields = new OrCondDBFields();
        int validFieldCount = 0;
        for (CondDBField field : fields) {
            if (field != null) {
                validFieldCount++;
                orFields.addCond(field);
            }
        }

        if (validFieldCount > 0) {
            filter.addOrConds(orFields);
        }
    }

    private static void addWkIssueSort(QueryFilter filter, String[] sortFields) {
        for (String sortField : sortFields) {
            String[] nameAndDirection = sortField.split(",");
            if (nameAndDirection.length == 2 && !nameAndDirection[0].trim().isEmpty()) {

                String sortFieldName = "";
                if (nameAndDirection[0].equals("issueId")) {
                    sortFieldName =  WkIssueTableField.ID;
                } else if (nameAndDirection[0].equals("chnlName")) {
                    sortFieldName = WkIssueTableField.CHNL_NAME;
                } else if (nameAndDirection[0].equals("issueType")) {
                    sortFieldName = WkIssueTableField.SUBTYPE_ID;
                } else if (nameAndDirection[0].equals("errorInfo")) {
                    sortFieldName = WkIssueTableField.URL;
                } else {
                    continue;
                }

                if (nameAndDirection[1].trim().equalsIgnoreCase("asc")) {
                    filter.addSortField(sortFieldName, true);
                } else if (nameAndDirection[1].trim().equalsIgnoreCase("desc")) {
                    filter.addSortField(sortFieldName, false);
                }
            }
        }
    }

}

package com.trs.gov.kpi.service.helper;

import com.trs.gov.kpi.constant.IssueTableField;
import com.trs.gov.kpi.constant.Types;
import com.trs.gov.kpi.entity.dao.CondDBField;
import com.trs.gov.kpi.entity.dao.OrCondDBFields;
import com.trs.gov.kpi.entity.dao.QueryFilter;
import com.trs.gov.kpi.entity.dao.Table;
import com.trs.gov.kpi.entity.requestdata.PageDataRequestParam;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by linwei on 2017/5/22.
 */
public class QueryFilterHelper {

    /**
     * 把参数转换为查询filter
     *
     * @param param
     * @return
     */
    public static QueryFilter toFilter(PageDataRequestParam param, Types.IssueType... issueType) {
        QueryFilter filter = new QueryFilter(Table.ISSUE);
        filter.addCond("siteId", param.getSiteId());
        if (param.getBeginDateTime() != null) {
            filter.addCond("issueTime", param.getBeginDateTime()).setRangeBegin(true);
        }

        if (param.getEndDateTime() != null) {
            filter.addCond("issueTime", param.getEndDateTime()).setRangeEnd(true);
        }

        if (param.getSearchText() != null) {
            if (param.getSearchField() != null && param.getSearchField().equalsIgnoreCase("id")) {
                filter.addCond("id", '%' + param.getSearchText() + "%").setLike(true);
            } else if (param.getSearchField() != null && param.getSearchField().equalsIgnoreCase("issueType")) {
                CondDBField field = buildIssueTypeCond(param.getSearchText(), issueType);
                if (field != null) {
                    filter.addCond(field);
                } else {
                    filter.addCond(new CondDBField(IssueTableField.SUBTYPE_ID, Types.IssueType.INVALID.value));
                }
            } else if (param.getSearchField() == null) {
                CondDBField idField = new CondDBField("id", '%' + param.getSearchText() + "%");
                idField.setLike(true);

                CondDBField issueTypefield = buildIssueTypeCond(param.getSearchText(), issueType);
                if (issueTypefield != null) {
                    OrCondDBFields orFields = new OrCondDBFields();
                    orFields.addCond(idField);
                    orFields.addCond(issueTypefield);
                    filter.addOrConds(orFields);
                } else {
                    filter.addCond(idField);
                }
            }
        }

        // sort field
        if (param.getSortFields() != null && !param.getSortFields().trim().isEmpty()) {
            String[] sortFields = param.getSortFields().trim().split(";");
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

        return filter;
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
            }
        }

        CondDBField field = null;
        if (!matchedTypes.isEmpty()) {
            if (matchedTypes.size() == 1) {
                field = new CondDBField("subTypeId", matchedTypes.get(0));
            } else {
                field = new CondDBField("subTypeId", matchedTypes);
                field.setCollection(true);
            }
        }
        return field;
    }
}

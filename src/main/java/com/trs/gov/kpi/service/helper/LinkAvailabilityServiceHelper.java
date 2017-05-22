package com.trs.gov.kpi.service.helper;

import com.trs.gov.kpi.constant.LinkIssueType;
import com.trs.gov.kpi.entity.dao.CondDBField;
import com.trs.gov.kpi.entity.dao.OrCondDBFields;
import com.trs.gov.kpi.entity.dao.QueryFilter;
import com.trs.gov.kpi.entity.dao.SortDBField;
import com.trs.gov.kpi.entity.requestdata.PageDataRequestParam;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by linwei on 2017/5/22.
 */
public class LinkAvailabilityServiceHelper {

    /**
     * 把参数转换为查询filter
     *
     * @param param
     * @return
     */
    public static  QueryFilter toFilter(PageDataRequestParam param) {
        QueryFilter filter = new QueryFilter();
        filter.addCond("siteId", param.getSiteId());
        if (param.getBeginDateTime() != null) {
            filter.addCond("issueTime", param.getBeginDateTime()).setBeginTime(true);
        }

        if (param.getEndDateTime() != null) {
            filter.addCond("issueTime", param.getEndDateTime()).setEndTime(true);
        }

        if (param.getSearchText() != null) {
            if (param.getSearchField() != null && param.getSearchField().equalsIgnoreCase("id")) {
                filter.addCond("id", '%'+ param.getSearchText() + "%").setLike(true);
            } else if (param.getSearchField() != null && param.getSearchField().equalsIgnoreCase("issueType")) {
                CondDBField field = buildIssueTypeCond(param.getSearchText());
                if (field != null) {
                    filter.addCond(field);
                }
            } else if (param.getSearchField() == null) {
                CondDBField idField = new CondDBField("id", '%'+ param.getSearchText() + "%");
                idField.setLike(true);

                CondDBField issueTypefield = buildIssueTypeCond(param.getSearchText());
                if (issueTypefield != null) {
                    OrCondDBFields orFields = new OrCondDBFields();
                    orFields.addCondDBField(idField);
                    orFields.addCondDBField(issueTypefield);
                    filter.addCond("OR_COMPLEX_FIELD", orFields);
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
                        SortDBField dbField = new SortDBField();
                        dbField.setFieldName(nameAndDirection[0].trim());
                        dbField.setAsc(true);
                        filter.addSortField(dbField);
                    } else if (nameAndDirection[1].trim().equalsIgnoreCase("desc")) {
                        SortDBField dbField = new SortDBField();
                        dbField.setFieldName(nameAndDirection[0].trim());
                        dbField.setAsc(false);
                        filter.addSortField(dbField);
                    }
                }
            }
        }

        return filter;
    }

    private static CondDBField buildIssueTypeCond(String issueName) {
        List<LinkIssueType> matchedTypes = LinkIssueType.findTypesByName(issueName);
        CondDBField field = null;
        if (!matchedTypes.isEmpty()) {
            List<Integer> ids = new ArrayList<>();
            for (LinkIssueType type : matchedTypes) {
                ids.add(type.value);
            }

            if (ids.size() == 1) {
                field = new CondDBField("subTypeId", ids.get(0));
            } else {
                field = new CondDBField("subTypeId", ids);
                field.setCollection(true);
            }
        }
        return field;
    }
}

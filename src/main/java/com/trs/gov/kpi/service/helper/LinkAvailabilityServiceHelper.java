package com.trs.gov.kpi.service.helper;

import com.trs.gov.kpi.constant.LinkIssueType;
import com.trs.gov.kpi.entity.dao.QueryFilter;
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
            filter.addCond("issueTime", param.getBeginDateTime()).setEndTime(true);
        }

        if (param.getSearchText() != null && param.getSearchField() != null) {
            if (param.getSearchField().equalsIgnoreCase("id")) {
                filter.addCond("id", '%'+ param.getSearchText() + "%").setLike(true);
            } else if (param.getSearchField().equalsIgnoreCase("issueType")) {
                List<LinkIssueType> matchedTypes = LinkIssueType.findTypesByName(param.getSearchText());
                if (!matchedTypes.isEmpty()) {
                    if (matchedTypes.size() == 1) {
                        filter.addCond("subTypeId", matchedTypes.get(0).value);
                    } else {
                        List<Integer> ids = new ArrayList<>();
                        for (LinkIssueType type : matchedTypes) {
                            ids.add(type.value);
                        }
                        filter.addCond("subTypeId", ids).setCollection(true);
                    }
                }
            } else {
                // TODO LINWEI 所有字段检索的情况
            }
        }
//
//        if (param.getSortFields() != null) {
//            String[] sortFields = param.getSortFields().split(";");
//            for (String field : sortFields) {
//                filter.addCond(field.replace(",", " "));
//            }
//        }
        return filter;
    }
}

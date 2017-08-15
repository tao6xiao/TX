package com.trs.gov.kpi.entity.requestdata;

/**
 * Created by he.lang on 2017/6/8.
 */
public class IssueCountRequest extends DateRequest {

    private String siteIds;

    public String getSiteIds() {
        return siteIds;
    }

    public void setSiteIds(String siteIds) {
        this.siteIds = siteIds;
    }

    @Override
    public String toString() {
        return "{" +
                "siteIds='" + siteIds + '\'' +
                "beginDateTime='" + getBeginDateTime() + '\'' +
                ", endDateTime='" + getEndDateTime() + '\'' +
                ", pageSize=" + getPageSize() +
                ", pageIndex=" + getPageIndex() +
                ", sortFields='" + getSortFields() + '\'' +
                ", granularity=" + getGranularity() +
                ", searchField='" + getSearchField() + '\'' +
                ", searchText='" + getSearchText() + '\'' +
                '}';
    }

}

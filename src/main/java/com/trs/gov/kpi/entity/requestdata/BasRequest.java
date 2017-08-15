package com.trs.gov.kpi.entity.requestdata;

import lombok.Data;

import java.util.Date;

/**
 * Created by ranwei on 2017/6/13.
 */
@Data
public class BasRequest extends DateRequest {

    private Integer siteId;

    private Date dateTime;

    @Override
    public String toString() {
        return "{" +
                "siteId='" + siteId + '\'' +
                "dateTime='" + dateTime + '\'' +
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

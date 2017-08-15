package com.trs.gov.kpi.entity.outerapi.nbhd;

import com.trs.gov.kpi.entity.requestdata.DateRequest;
import lombok.Data;

/**
 * Created by ranwei on 2017/6/9.
 */
@Data
public class NBHDRequestParam extends DateRequest {

    private int siteId;

    private Integer solveStatus = 0;//0-->待解决

    @Override
    public String toString() {
        return "{" +
                "siteId='" + siteId + '\'' +
                "solveStatus='" + solveStatus + '\'' +
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

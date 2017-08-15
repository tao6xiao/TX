package com.trs.gov.kpi.entity.requestdata;

import lombok.Data;

/**
 * Created by yuanyuan on 2017/6/18.
 */
@Data
public class IssueCountByTypeRequest extends IssueCountRequest {

    // 网站可用性
    public static final int TYPE_SITE_AVALIABLE = 1;

    // 信息更新
    public static final int TYPE_INFO_UPDATE = 2;

    // 信息错误
    public static final int TYPE_INFO_ERROR = 3;

    //服务链接可用性
    public static final int TYPE_SERVICE_LINK_AVAILABLE = 4;

    private int typeId;

    @Override
    public String toString() {
        return "{" +
                "typeId='" + typeId + '\'' +
                "siteIds='" + getSiteIds() + '\'' +
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

package com.trs.gov.kpi.entity.requestdata;

/**
 * Created by he.lang on 2017/6/8.
 */
public class IssueCountRequest extends DateRequest {

    private String siteIds;

    /**
     * 查询历史记录的粒度，1-->天  2-->周  3-->月  4-->年
     */
    private Integer granularity;

    public String getSiteIds() {
        return siteIds;
    }

    public void setSiteIds(String siteIds) {
        this.siteIds = siteIds;
    }

    public Integer getGranularity() {
        return granularity;
    }

    public void setGranularity(Integer granularity) {
        this.granularity = granularity;
    }

}

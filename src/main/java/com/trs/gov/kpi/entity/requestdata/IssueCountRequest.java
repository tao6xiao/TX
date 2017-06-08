package com.trs.gov.kpi.entity.requestdata;

/**
 * Created by he.lang on 2017/6/8.
 */
public class IssueCountRequest{
    private String siteIds;

    /**
     * 查询历史记录的粒度，1-->天  2-->周  3-->月  4-->年
     */
    private Integer granularity;

    private String beginDateTime;

    private String endDateTime;

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

    public String getBeginDateTime() {
        return beginDateTime;
    }

    public void setBeginDateTime(String beginDateTime) {
        if (beginDateTime != null && beginDateTime.trim().isEmpty()) {
            return;
        }
        this.beginDateTime = beginDateTime;
    }

    public String getEndDateTime() {
        return endDateTime;
    }

    public void setEndDateTime(String endDateTime) {
        if (endDateTime != null && endDateTime.trim().isEmpty()) {
            return;
        }
        this.endDateTime = endDateTime;
    }
}

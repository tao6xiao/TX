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

}
